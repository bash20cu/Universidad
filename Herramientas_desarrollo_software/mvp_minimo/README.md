# TutorIA MVP

MVP modular con Flask, SQLite, SQLAlchemy, autenticación, roles, 2FA, bitácora
y un proveedor de IA intercambiable. Foundation Models se inicia bajo demanda
y transmite respuestas mediante Server-Sent Events.

## Requisitos

- Mac con Apple Silicon y Apple Intelligence habilitado.
- macOS 27 para utilizar el comando `fm`.
- Modelo `system` disponible: `fm available`.
- Python 3.10 o posterior.

## Ejecución

La forma más sencilla es utilizar el lanzador:

```bash
./run.sh
```

El script crea `.venv` e instala las dependencias solamente cuando hace falta.
Después inicia Flask; `run.py` comprueba `fm serve`, lo despierta si está
detenido, abre el navegador automáticamente y lo cierra al recibir `Ctrl+C`.

También se puede abrir `Iniciar_TutorIA.command` con doble clic desde Finder.
La aplicación utilizará `http://127.0.0.1:5050` para evitar el puerto 5000, que
macOS puede reservar para AirPlay Receiver.

Si el puerto 5050 está ocupado:

```bash
APP_PORT=5051 ./run.sh
```

Para iniciar sin abrir el navegador:

```bash
AUTO_OPEN_BROWSER=0 ./run.sh
```

La ejecución manual equivalente es:

```bash
cd /Volumes/DATA/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
python run.py
```

Abrir `http://127.0.0.1:5050`.

La app usa `/usr/bin/fm serve --host 127.0.0.1 --port 1976`. Si ya existe un
servidor saludable en ese puerto, lo reutiliza. Si lo inicia ella misma, intenta
cerrarlo cuando termina el proceso Flask. El cierre está cubierto por `Ctrl+C`,
`SIGTERM`, un bloque `finally` y `atexit`. Un cierre forzado con `SIGKILL` o una
pérdida de energía no permite ejecutar limpieza en ningún programa.

## Arquitectura abstracta

Flask depende del contrato `ChatProvider`, no de Foundation Models. Un proveedor
debe implementar:

- `status()`;
- `ensure_ready()`;
- `stream_chat(messages)`;
- `shutdown()`.

`FoundationModelsProvider` es la implementación actual. La misma aplicación
puede recibir posteriormente `OllamaProvider`, un proveedor institucional o un
servicio remoto sin cambiar rutas, vistas ni lógica de conversación.

La ubicación del usuario, el modo de acceso y la ubicación del procesamiento son
variables independientes:

```dotenv
APP_ACCESS_MODE=local
AI_PROCESSING_LOCATION=device
```

Variantes previstas:

- usuario y modelo en el mismo dispositivo;
- usuario en la red local y modelo en un servidor del centro educativo;
- usuario remoto y proveedor institucional;
- proveedor local de respaldo cuando un proveedor remoto no esté disponible.

## Datos iniciales

Al iniciar, SQLite se crea en `instance/tutoria.db` con usuarios de demostración:

- `admin` / `Administrador123!`
- `docente` / `Docente123!`
- `estudiante` / `Estudiante123!`

En desarrollo, el código 2FA aparece en la consola. Para correo real, configura
`TWO_FACTOR_DELIVERY=resend`, `RESEND_API_KEY` y `RESEND_FROM_EMAIL`.

## Estructura

- `app/models`: persistencia SQLAlchemy.
- `app/routes`: autenticación, panel y chat.
- `app/services`: 2FA, correo, bitácora y conversación.
- `app/forms`: formularios validados y protegidos con CSRF.
- `app/templates` y `app/static`: interfaz Jinja2, chat y Bootstrap 5.3.8
  versionado localmente en `app/static/vendor/bootstrap/5.3.8`.

## Endpoints

- `GET /`: inicio.
- `GET|POST /auth/login`: primer factor.
- `GET|POST /auth/verify`: segundo factor.
- `GET /dashboard`: panel protegido.
- `GET /students`: listado para administrador y docente.
- `GET|POST /students/new`: registro de estudiantes.
- `GET|POST /students/<id>/edit`: actualización de estudiantes.
- `POST /students/<id>/delete`: eliminación exclusiva del administrador.
- `GET /chat`: interfaz del tutor protegida.
- `GET /chat/api/status`: disponibilidad del modelo.
- `POST /chat/api/provider/wake`: prepara el proveedor.
- `POST /chat/api/chat`: proxy SSE protegido hacia el proveedor.

## Pruebas

```bash
pytest
```

Las pruebas utilizan un servidor FM simulado y no consumen el modelo real.
También validan autenticación, 2FA, autorización por roles, CRUD de estudiantes
y registros de bitácora.

## Solución de problemas

- **Doble clic bloqueado por macOS:** clic derecho sobre
  `Iniciar_TutorIA.command`, seleccionar **Abrir** y confirmar una vez.
- **Puerto ocupado:** ejecutar `APP_PORT=5051 ./run.sh`.
- **Entorno movido o eliminado:** `run.sh` vuelve a crear `.venv`
  automáticamente cuando no es válido.
- **FM no disponible:** ejecutar `fm available` y confirmar que indique
  `System model available`.
- **La ventana parece detenida:** es normal; Flask permanece en primer plano.
  El navegador se abre automáticamente y `Ctrl+C` detiene TutorIA y el servidor
  FM administrado.
