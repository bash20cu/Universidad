#!/bin/zsh
# Archivo: run.sh
# Propósito: Prepara el entorno virtual y ejecuta TutorIA.
# Responsabilidades: Resuelve Python, carga variables de entorno y delega el arranque al servidor Flask.
# Dependencias: zsh, .venv y run.py.
# Entradas y salidas: Lee PYTHON_BIN y variables del entorno; inicia el proceso web.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.

set -euo pipefail

SCRIPT_DIR="${0:A:h}"
cd "$SCRIPT_DIR"

PYTHON_BIN="${PYTHON_BIN:-python3}"
VENV_DIR="${VENV_DIR:-.venv}"
APP_HOST="${APP_HOST:-127.0.0.1}"
APP_PORT="${APP_PORT:-5050}"
FM_HOST="${FM_HOST:-127.0.0.1}"
FM_PORT="${FM_PORT:-1976}"
APP_URL="http://${APP_HOST}:${APP_PORT}"
AUTO_OPEN_BROWSER="${AUTO_OPEN_BROWSER:-1}"

export APP_HOST APP_PORT FM_HOST FM_PORT

if ! command -v "$PYTHON_BIN" >/dev/null 2>&1; then
  echo "Error: no se encontró Python (${PYTHON_BIN})."
  echo "Instala Python 3.10 o superior o define PYTHON_BIN."
  exit 1
fi

VENV_VALID=1
if [[ ! -x "$VENV_DIR/bin/python" ]]; then
  VENV_VALID=0
elif ! "$VENV_DIR/bin/python" -c "import sys; raise SystemExit(0 if sys.prefix != sys.base_prefix else 1)" >/dev/null 2>&1; then
  VENV_VALID=0
fi

if [[ "$VENV_VALID" -eq 0 ]]; then
  echo "Creando entorno virtual..."
  /bin/rm -rf "$VENV_DIR"
  "$PYTHON_BIN" -m venv "$VENV_DIR"
fi

if ! "$VENV_DIR/bin/python" -c "import flask_login, flask_sqlalchemy, flask_wtf" >/dev/null 2>&1; then
  echo "Instalando dependencias..."
  "$VENV_DIR/bin/python" -m pip install -r requirements.txt
fi

if [[ ! -x "${FM_COMMAND:-/usr/bin/fm}" ]]; then
  echo "Error: no se encontró el comando fm en ${FM_COMMAND:-/usr/bin/fm}."
  exit 1
fi

if /usr/bin/nc -z "$APP_HOST" "$APP_PORT" >/dev/null 2>&1; then
  echo "Error: el puerto $APP_PORT ya está ocupado."
  echo "Puedes usar otro puerto así: APP_PORT=5051 ./run.sh"
  exit 1
fi

echo "Iniciando TutorIA..."
echo "Web: ${APP_URL}"
echo "Foundation Models: http://${FM_HOST}:${FM_PORT}"
echo "Usa Ctrl+C para detener ambos servicios."

if [[ "$AUTO_OPEN_BROWSER" == "1" ]]; then
  (
    for _ in {1..80}; do
      if /usr/bin/curl -fsS --max-time 1 "${APP_URL}/chat/api/status" >/dev/null 2>&1; then
        /usr/bin/open "$APP_URL"
        exit 0
      fi
      /bin/sleep 0.25
    done
    echo "Aviso: TutorIA no respondió a tiempo en ${APP_URL}."
  ) &
fi

exec "$VENV_DIR/bin/python" run.py
