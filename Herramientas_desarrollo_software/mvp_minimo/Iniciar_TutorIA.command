#!/bin/zsh
# Archivo: Iniciar_TutorIA.command
# Propósito: Inicia TutorIA desde macOS.
# Responsabilidades: Ubica el directorio del script y delega el arranque al lanzador web.
# Dependencias: zsh y run.sh.
# Entradas y salidas: Usa la ubicación del script; inicia el servidor TutorIA.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.

SCRIPT_DIR="${0:A:h}"
cd "$SCRIPT_DIR"

./run.sh
STATUS=$?

echo
if [[ $STATUS -eq 0 || $STATUS -eq 130 ]]; then
  echo "TutorIA se detuvo correctamente."
else
  echo "TutorIA no pudo iniciar. Revisa el mensaje anterior."
fi
echo "Presiona Enter para cerrar esta ventana."
read -r

exit $STATUS
