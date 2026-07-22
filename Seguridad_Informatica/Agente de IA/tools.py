"""
tools.py
========
Herramientas seguras que el agente puede usar. Cada herramienta:

  * Valida su entrada antes de actuar.
  * Usa subprocess.run() con una LISTA de argumentos (nunca shell=True).
  * Aplica un timeout configurable.
  * Limita el tamaño de la salida.
  * Registra la actividad mediante el logger de auditoría.

La herramienta principal es `run_command`, que ejecuta comandos de la
lista blanca SOLO después de validarlos en security.py y de obtener
confirmación humana (la confirmación se solicita en app.py).
"""

from __future__ import annotations

import subprocess
from pathlib import Path
from typing import List

from config import settings
from security import SecurityError, validate_command


def _truncate(text: str) -> str:
    """Recorta la salida al máximo permitido y avisa si se truncó."""
    limit = settings.max_output_chars
    if len(text) <= limit:
        return text
    return text[:limit] + f"\n... [salida truncada a {limit} caracteres]"


def run_command(raw_input: str) -> str:
    """Valida y ejecuta un comando de la lista blanca.

    Devuelve la salida combinada (stdout + stderr) ya truncada.
    Lanza SecurityError si el comando no es válido. Cualquier fallo de
    ejecución se devuelve como texto legible (no se relanza) para que el
    agente pueda explicarlo al estudiante.
    """
    # 1. Validacion de seguridad (lista blanca, operadores, rutas).
    program, args = validate_command(raw_input)

    # 2. Ejecucion segura: lista de argumentos, sin shell, con timeout.
    try:
        completed = subprocess.run(
            [program, *args],
            capture_output=True,
            text=True,
            timeout=settings.command_timeout,
            cwd=str(settings.workspace_dir),  # se ejecuta dentro de workspace
            check=False,
        )
    except subprocess.TimeoutExpired:
        return (
            f"[ERROR] El comando '{program}' supero el tiempo maximo de "
            f"{settings.command_timeout} s y fue cancelado."
        )
    except FileNotFoundError:
        return f"[ERROR] El programa '{program}' no esta instalado en el sistema."
    except OSError as exc:
        return f"[ERROR] No se pudo ejecutar '{program}': {exc}"

    # 3. Combinar y truncar la salida.
    salida = completed.stdout or ""
    error = completed.stderr or ""
    combinada = salida
    if error:
        combinada += ("\n[stderr]\n" + error) if combinada else error

    if not combinada.strip():
        combinada = f"[ok] El comando '{program}' termino con codigo {completed.returncode} sin salida."

    return _truncate(combinada)


def read_workspace_file(filename: str) -> str:
    """Lee un archivo de texto DENTRO de workspace/ de forma segura.

    Es una herramienta de conveniencia equivalente a `cat` restringido.
    """
    target = (settings.workspace_dir / filename).resolve()
    if target != settings.workspace_dir and settings.workspace_dir not in target.parents:
        raise SecurityError(
            f"'{filename}' esta fuera de workspace. Acceso denegado."
        )
    if not target.is_file():
        return f"[ERROR] El archivo '{filename}' no existe en workspace/."
    try:
        contenido = target.read_text(encoding="utf-8", errors="replace")
    except OSError as exc:
        return f"[ERROR] No se pudo leer '{filename}': {exc}"
    return _truncate(contenido)


def list_workspace() -> str:
    """Lista los archivos disponibles en workspace/."""
    entradas: List[str] = []
    for p in sorted(settings.workspace_dir.iterdir()):
        marca = "/" if p.is_dir() else ""
        entradas.append(f"{p.name}{marca}")
    if not entradas:
        return "workspace/ esta vacio."
    return "Contenido de workspace/:\n" + "\n".join(entradas)
