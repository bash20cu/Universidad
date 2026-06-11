# TutorIA FM MVP

MVP de chat con Flask y un proveedor de IA intercambiable. La implementación
actual utiliza Apple Foundation Models: comprueba `fm serve`, lo inicia cuando
hace falta y transmite la respuesta al navegador mediante Server-Sent Events.

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
Después inicia Flask; `app.py` comprueba `fm serve`, lo despierta si está
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
python app.py
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

## Endpoints

- `GET /`: interfaz de chat.
- `GET /api/status`: disponibilidad del servidor y modelo.
- `POST /api/provider/wake`: prepara el proveedor configurado.
- `POST /api/chat`: proxy SSE hacia `/v1/chat/completions`.

## Pruebas

```bash
pytest
```

Las pruebas utilizan un servidor FM simulado y no consumen el modelo real.

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
