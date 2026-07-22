"""
security.py
===========
Núcleo de seguridad del agente. Aquí viven los controles que impiden que
el modelo de lenguaje (o un usuario) ejecute acciones peligrosas:

  * Lista blanca de comandos permitidos.
  * Detección de operadores de shell prohibidos (; && || | ` $() etc.).
  * Validación de que las rutas se mantengan dentro de `workspace/`.
  * Validación específica por comando (p. ej. `cat` solo en workspace).

NINGÚN comando se ejecuta directamente aquí: este módulo solo *decide*
si una solicitud es aceptable. La ejecución real ocurre en tools.py con
subprocess.run() y argumentos separados (sin shell).
"""

from __future__ import annotations

import re
import shlex
from pathlib import Path
from typing import List, Tuple

from config import settings


# ---------------------------------------------------------------------------
# 1. Lista blanca de comandos
# ---------------------------------------------------------------------------
# Solo comandos informativos / defensivos. Cada entrada documenta el
# riesgo evaluado. NO se incluyen comandos ofensivos, destructivos ni de
# explotación.
ALLOWED_COMMANDS = {
    "pwd": "Muestra el directorio actual. Riesgo: ninguno.",
    "whoami": "Muestra el usuario actual. Riesgo: ninguno.",
    "date": "Muestra fecha y hora. Riesgo: ninguno.",
    "uname": "Informacion del kernel. Riesgo: divulgacion menor de version.",
    "hostname": "Nombre del host. Riesgo: divulgacion menor.",
    "ip": "Configuracion de red (solo lectura). Riesgo: divulgacion de red.",
    "ss": "Sockets/conexiones (solo lectura). Riesgo: divulgacion de red.",
    "df": "Espacio en disco. Riesgo: ninguno.",
    "free": "Memoria disponible. Riesgo: ninguno.",
    "ps": "Procesos en ejecucion. Riesgo: divulgacion de procesos.",
    "ls": "Lista archivos (restringido a workspace). Riesgo: bajo.",
    "cat": "Muestra archivos (SOLO dentro de workspace). Riesgo: bajo.",
    "sha256sum": "Hash SHA-256 (SOLO dentro de workspace). Riesgo: ninguno.",
    "file": "Tipo de archivo (SOLO dentro de workspace). Riesgo: ninguno.",
    "stat": "Metadatos de archivo (SOLO dentro de workspace). Riesgo: bajo.",
}

# Comandos cuyos argumentos de ruta DEBEN permanecer dentro de workspace.
PATH_RESTRICTED_COMMANDS = {"cat", "sha256sum", "file", "stat", "ls"}


# ---------------------------------------------------------------------------
# 2. Operadores de shell prohibidos
# ---------------------------------------------------------------------------
# Se rechaza cualquier solicitud que contenga estos patrones, porque
# habilitan encadenamiento, tuberías, redirecciones o sustitucion de
# comandos. Aunque NO usamos shell=True, los bloqueamos por defensa en
# profundidad y para dar mensajes claros al estudiante.
FORBIDDEN_PATTERNS = [
    (";", "secuencia de comandos ';'"),
    ("&&", "operador AND '&&'"),
    ("||", "operador OR '||'"),
    ("|", "tuberia '|'"),
    ("&", "ejecucion en segundo plano '&'"),
    (">", "redireccion de salida '>'"),
    ("<", "redireccion de entrada '<'"),
    ("`", "sustitucion de comandos con backticks"),
    ("$(", "sustitucion de comandos '$()'"),
    ("\n", "salto de linea"),
    ("\\", "barra invertida"),
]


class SecurityError(Exception):
    """Se lanza cuando una solicitud viola una regla de seguridad."""


def check_forbidden_operators(raw_input: str) -> None:
    """Rechaza la entrada si contiene operadores de shell peligrosos."""
    for token, description in FORBIDDEN_PATTERNS:
        if token in raw_input:
            raise SecurityError(
                f"Entrada rechazada: contiene {description}. "
                "No se permiten operadores de shell."
            )


def _is_within_workspace(candidate: Path) -> bool:
    """True si `candidate` está dentro de workspace/ (resolviendo symlinks)."""
    try:
        resolved = (settings.workspace_dir / candidate).resolve()
    except (OSError, RuntimeError):
        return False
    # Python 3.9+: Path.is_relative_to
    return resolved == settings.workspace_dir or settings.workspace_dir in resolved.parents


def validate_command(raw_input: str) -> Tuple[str, List[str]]:
    """Valida una solicitud de comando y devuelve (programa, argumentos).

    Pasos:
      1. Rechaza operadores de shell prohibidos.
      2. Divide de forma segura con shlex (sin invocar shell).
      3. Verifica que el programa esté en la lista blanca.
      4. Para comandos restringidos por ruta, confirma que toda ruta
         apunte dentro de workspace/.

    Lanza SecurityError si algo no cumple las reglas.
    """
    raw_input = raw_input.strip()
    if not raw_input:
        raise SecurityError("Entrada vacia.")

    # Paso 1: operadores prohibidos.
    check_forbidden_operators(raw_input)

    # Paso 2: tokenizacion segura.
    try:
        tokens = shlex.split(raw_input)
    except ValueError as exc:
        raise SecurityError(f"No se pudo interpretar la entrada: {exc}")

    if not tokens:
        raise SecurityError("No se encontro ningun comando.")

    program, args = tokens[0], tokens[1:]

    # Paso 3: lista blanca.
    if program not in ALLOWED_COMMANDS:
        raise SecurityError(
            f"Comando '{program}' no autorizado. "
            f"Permitidos: {', '.join(sorted(ALLOWED_COMMANDS))}."
        )

    # Paso 4: restriccion de rutas.
    if program in PATH_RESTRICTED_COMMANDS:
        for arg in args:
            # Se ignoran las banderas (p. ej. -l, -a, --human-readable).
            if arg.startswith("-"):
                continue
            if not _is_within_workspace(Path(arg)):
                raise SecurityError(
                    f"La ruta '{arg}' esta fuera del directorio workspace. "
                    "Solo se permite acceder a archivos dentro de workspace/."
                )

    return program, args


def describe_whitelist() -> str:
    """Devuelve una descripción legible de la lista blanca (para ayuda)."""
    lines = ["Comandos autorizados:"]
    for name, risk in sorted(ALLOWED_COMMANDS.items()):
        lines.append(f"  - {name}: {risk}")
    return "\n".join(lines)
