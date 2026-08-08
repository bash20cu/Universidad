# Archivo: run.py
# Propósito: Es el punto de entrada del servidor Flask.
# Responsabilidades: Inicializa esquema y datos, registra cierres ordenados, prepara IA y ejecuta la aplicación web.
# Dependencias: Flask, SQLAlchemy, ai_provider y app.
# Entradas y salidas: Lee variables de entorno; inicia un servidor HTTP y libera recursos al terminar.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Punto de entrada para inicializar la base y ejecutar Flask."""

from __future__ import annotations

import atexit
import os
import signal

from app import create_app, ensure_schema_compatibility, seed_database
from ai_provider import AIProviderError, ChatProvider
from app.extensions import db


def install_shutdown_handlers(provider: ChatProvider) -> None:
    """Registra cierre ordenado del proveedor ante Ctrl+C o SIGTERM."""

    def shutdown_handler(signum, _frame):
        """Libera procesos IA antes de terminar el proceso web."""

        provider.shutdown()
        raise SystemExit(128 + signum)

    signal.signal(signal.SIGTERM, shutdown_handler)
    signal.signal(signal.SIGINT, shutdown_handler)


app = create_app()


if __name__ == "__main__":
    ai_provider: ChatProvider = app.extensions["ai_provider"]
    with app.app_context():
        db.create_all()
        ensure_schema_compatibility()
        seed_database()
    atexit.register(ai_provider.shutdown)
    install_shutdown_handlers(ai_provider)
    try:
        ai_provider.ensure_ready()
    except AIProviderError as error:
        print(f"Advertencia: {error}")

    try:
        app.run(
            host=os.getenv("APP_HOST", "127.0.0.1"),
            port=int(os.getenv("APP_PORT", "5050")),
            debug=os.getenv("FLASK_DEBUG", "0") == "1",
            use_reloader=False,
            threaded=True,
        )
    finally:
        ai_provider.shutdown()
