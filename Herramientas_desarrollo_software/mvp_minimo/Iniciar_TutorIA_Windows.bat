@echo off
setlocal

REM Lanzador de TutorIA para Windows usando NVIDIA como proveedor principal.
cd /d "%~dp0"

where py >nul 2>nul
if errorlevel 1 (
    echo No se encontro Python Launcher ^(py^).
    echo Instala Python 3.11 o superior desde https://www.python.org/downloads/windows/
    pause
    exit /b 1
)

if not exist ".venv\Scripts\python.exe" (
    echo Creando entorno virtual de Windows...
    py -3 -m venv .venv
    if errorlevel 1 goto :error
)

echo Instalando o actualizando dependencias...
".venv\Scripts\python.exe" -m pip install -r requirements.txt
if errorlevel 1 goto :error

if not exist ".env" (
    copy /Y ".env.windows.example" ".env" >nul
    echo Se creo .env desde .env.windows.example.
    echo Abre .env y agrega NVIDIA_API_KEY antes de continuar.
    notepad .env
)

set APP_HOST=127.0.0.1
set APP_PORT=5050
set AI_PRIMARY_PROVIDER=nvidia
set APP_ACCESS_MODE=remote
set AI_PROCESSING_LOCATION=remote
set AUTO_OPEN_BROWSER=0

echo.
echo TutorIA iniciando en http://127.0.0.1:5050
echo Foundation Models se omite en Windows; NVIDIA es el proveedor principal.
echo Presiona Ctrl+C para detener la aplicacion.
echo.
".venv\Scripts\python.exe" run.py
exit /b %errorlevel%

:error
echo.
echo No se pudo preparar TutorIA. Revisa el mensaje anterior.
pause
exit /b 1
