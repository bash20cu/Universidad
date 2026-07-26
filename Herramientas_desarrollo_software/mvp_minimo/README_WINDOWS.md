# Ejecutar TutorIA en Windows

## Compatibilidad

En Windows funcionan la aplicación Flask, SQLite, SQLAlchemy, Bootstrap local,
TOTP con Google Authenticator, NVIDIA NIM, reportes y todos los módulos web.
Foundation Models y el control de `fm serve` son componentes específicos de
macOS, por lo que Windows utiliza NVIDIA como proveedor principal.

## Requisitos

1. Windows 10 u 11.
2. Python 3.11 o superior, con la opción **Add Python to PATH** activa.
3. Una clave de NVIDIA NIM.
4. Google Authenticator, Authy o Microsoft Authenticator para TOTP.

## Instalación rápida

1. Copia la carpeta `mvp_minimo` al equipo Windows.
2. Haz doble clic en `Iniciar_TutorIA_Windows.bat`.
3. Si es la primera ejecución, se creará `.venv` y se instalarán las dependencias.
4. Cuando se abra `.env`, reemplaza `NVIDIA_API_KEY` por tu clave real.
5. Ejecuta nuevamente `Iniciar_TutorIA_Windows.bat`.
6. Abre `http://127.0.0.1:5050`.

## Configuración de IA

El archivo `.env.windows.example` deja estas decisiones explícitas:

- `AI_PRIMARY_PROVIDER=nvidia`.
- `APP_ACCESS_MODE=remote`.
- `AI_PROCESSING_LOCATION=remote`.
- `FM_COMMAND=fm-no-disponible-en-windows` para evitar intentar iniciar `fm`.

La clave nunca debe subirse a Git ni incluirse en capturas del informe.

## Base de datos y TOTP

La base SQLite se crea localmente en `instance/tutoria.db` y la aplicación aplica
las columnas nuevas automáticamente. El registro crea el secreto TOTP y muestra
el QR inmediatamente; no se envía correo.

## Solución de problemas

- **Puerto ocupado:** cambia `APP_PORT=5051` en `.env` y abre
  `http://127.0.0.1:5051`.
- **NVIDIA no responde:** revisa la clave, el modelo y la conectividad a
  `https://integrate.api.nvidia.com/v1`.
- **Error de compilación de paquetes:** actualiza Python y ejecuta nuevamente el
  lanzador.
- **PySide6:** el centro de control actual está preparado para macOS y `fm
  serve`; en Windows se recomienda el lanzador web hasta adaptar el controlador
  de procesos a Windows.
