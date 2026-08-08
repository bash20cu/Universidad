#!/bin/zsh
# Archivo: Iniciar_TutorIA_Desktop.command
# Propósito: Inicia la aplicación de escritorio de la demo TutorIA.
# Responsabilidades: Ubica el proyecto, activa el entorno virtual y ejecuta el lanzador PySide6.
# Dependencias: zsh, .venv y desktop_launcher.py.
# Entradas y salidas: Usa la ruta del script; abre la interfaz de control local.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.

SCRIPT_DIR="${0:A:h}"
cd "$SCRIPT_DIR"
exec .venv/bin/python desktop_launcher.py
