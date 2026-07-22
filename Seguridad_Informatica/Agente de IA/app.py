"""
app.py
======
Punto de entrada del agente: interfaz de línea de comandos (CLI).

Flujo de cada turno:
  1. La persona escribe una instrucción en lenguaje natural.
  2. El agente (modelo) responde con una explicación y, opcionalmente, un
     COMANDO propuesto de la lista blanca.
  3. Si hay un comando, app.py PIDE CONFIRMACIÓN HUMANA explícita.
  4. Solo si la persona confirma, se valida de nuevo y se ejecuta con
     tools.run_command(). Todo queda registrado en logs/.

Comandos especiales de la CLI:
  /salir   -> termina la sesión.
  /ayuda   -> muestra la lista blanca.
  /limpiar -> borra el historial de la conversación.
"""

from __future__ import annotations

import logging
import re
import sys
from datetime import datetime
from getpass import getuser

from agent import Agent
from config import settings
from security import SecurityError, describe_whitelist
from tools import run_command


# ---------------------------------------------------------------------------
# Configuración del logger de auditoría
# ---------------------------------------------------------------------------
def _build_logger() -> logging.Logger:
    logger = logging.getLogger("agente_lab")
    logger.setLevel(logging.INFO)
    if logger.handlers:  # evita duplicar handlers si se reimporta
        return logger
    log_file = settings.logs_dir / "auditoria.log"
    handler = logging.FileHandler(log_file, encoding="utf-8")
    fmt = logging.Formatter("%(asctime)s | %(levelname)s | %(message)s")
    handler.setFormatter(fmt)
    logger.addHandler(handler)
    return logger


logger = _build_logger()


# Extrae la línea "COMANDO: ..." de la respuesta del modelo, si existe.
_COMANDO_RE = re.compile(r"^\s*COMANDO:\s*(.+)\s*$", re.IGNORECASE | re.MULTILINE)


def _extraer_comando(respuesta_modelo: str) -> str | None:
    match = _COMANDO_RE.search(respuesta_modelo)
    if not match:
        return None
    comando = match.group(1).strip()
    # Algunos modelos formatean el comando como `whoami`. Se retiran solo
    # los acentos externos; cualquier acento interno sigue siendo rechazado
    # por la validacion de seguridad.
    if len(comando) >= 2 and comando.startswith("`") and comando.endswith("`"):
        comando = comando[1:-1].strip()
    return comando or None


def _confirmar(comando: str) -> bool:
    """Solicita confirmación humana explícita. Solo 's' o 'si' aceptan."""
    print(f"\n[CONFIRMACION REQUERIDA] El agente desea ejecutar:\n    {comando}")
    try:
        respuesta = input("¿Autoriza la ejecucion? [s/N]: ").strip().lower()
    except (EOFError, KeyboardInterrupt):
        print("\n[CANCELADO] Ejecucion cancelada por el usuario.")
        return False
    return respuesta in ("s", "si", "sí")


def main() -> int:
    # Valida la configuración; si falla, no arranca.
    try:
        settings.validate()
    except RuntimeError as exc:
        print(f"[ERROR DE CONFIGURACION] {exc}")
        return 1

    usuario = getuser()
    agente = Agent()

    print("=" * 60)
    print(" Agente de IA de laboratorio (uso academico y defensivo) ")
    print("=" * 60)
    print(f"Backend del modelo: {settings.model_backend}")
    print(f"Directorio de trabajo: {settings.workspace_dir}")
    print("Escriba /ayuda para ver los comandos permitidos, /salir para terminar.\n")
    logger.info("SESION_INICIADA | usuario=%s | backend=%s", usuario, settings.model_backend)

    while True:
        try:
            entrada = input("usuario> ").strip()
        except (EOFError, KeyboardInterrupt):
            print("\nSaliendo...")
            break

        if not entrada:
            continue
        if entrada == "/salir":
            print("Sesion finalizada.")
            break
        if entrada == "/ayuda":
            print(describe_whitelist())
            continue
        if entrada == "/limpiar":
            agente.reset()
            print("[ok] Historial borrado.")
            continue

        # 1. Consultar al modelo.
        respuesta = agente.ask(entrada)
        print(f"\nagente> {respuesta}\n")
        logger.info("SOLICITUD | usuario=%s | texto=%r", usuario, entrada)

        # 2. ¿Propuso un comando?
        comando = _extraer_comando(respuesta)
        if not comando:
            continue

        # 3. Confirmación humana.
        if not _confirmar(comando):
            print("[CANCELADO] No se ejecuto el comando.")
            logger.info("EJECUCION_CANCELADA | usuario=%s | comando=%r", usuario, comando)
            continue

        # 4. Validar de nuevo y ejecutar.
        try:
            resultado = run_command(comando)
            print(f"\n[RESULTADO]\n{resultado}\n")
            logger.info(
                "EJECUCION_OK | usuario=%s | comando=%r | bytes_salida=%d",
                usuario, comando, len(resultado),
            )
        except SecurityError as exc:
            print(f"[RECHAZADO POR SEGURIDAD] {exc}")
            logger.warning(
                "EJECUCION_RECHAZADA | usuario=%s | comando=%r | motivo=%s",
                usuario, comando, exc,
            )

    logger.info("SESION_FINALIZADA | usuario=%s", usuario)
    return 0


if __name__ == "__main__":
    sys.exit(main())
