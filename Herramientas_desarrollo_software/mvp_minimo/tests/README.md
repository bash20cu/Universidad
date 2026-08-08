# Guía de pruebas para la exposición de TutorIA

Esta guía permite levantar TutorIA, ejecutar las pruebas automatizadas y
demostrar los flujos principales del sistema con Selenium Grid.

## Ubicación

```text
/Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo/tests
```

El proyecto debe ejecutarse desde la carpeta `mvp_minimo`.

## Requisitos previos

- Docker Desktop iniciado.
- Contenedor `selenium-chromium` disponible.
- Entorno virtual `.venv` creado.
- Puertos `4444` y `5050` disponibles.

Si el contenedor ya existe pero está detenido:

```bash
docker start selenium-chromium
```

Comprobar Selenium:

```bash
curl http://localhost:4444/status
```

La respuesta debe indicar que el servicio está listo.

## Visualización del navegador durante las pruebas

Selenium ofrece dos interfaces útiles:

- Panel de Selenium Grid, sesiones y estado del navegador:

  ```text
  http://localhost:4444/ui
  ```

- Vista visual del navegador mediante noVNC, para observar cómo se ejecuta el
  test paso a paso:

  ```text
  http://localhost:7900
  ```

La contraseña habitual de noVNC en las imágenes oficiales de Selenium es:

```text
secret
```

Comprobar los puertos publicados por el contenedor:

```bash
docker port selenium-chromium
```

La salida esperada incluye:

```text
4444/tcp -> 0.0.0.0:4444
7900/tcp -> 0.0.0.0:7900
```

El puerto `4444` permite supervisar Grid; el puerto `7900` permite ver el
navegador remoto mientras se ejecutan las pruebas.

## Ejecución para la exposición

### Terminal 1: Selenium Grid

```bash
cd /Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo
docker start selenium-chromium
curl http://localhost:4444/status
```

### Terminal 2: aplicación TutorIA

```bash
cd /Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo

APP_HOST=0.0.0.0 \
APP_PORT=5050 \
DATABASE_URL=sqlite:////private/tmp/tutoria_selenium.db \
AI_PRIMARY_PROVIDER=foundation \
FM_COMMAND=/usr/bin/false \
AUTO_OPEN_BROWSER=0 \
.venv/bin/python run.py
```

Abrir en el navegador:

```text
http://localhost:5050
```

### Terminal 3: pruebas automáticas

```bash
cd /Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo

SELENIUM_REMOTE_URL=http://localhost:4444/wd/hub \
SELENIUM_BASE_URL=http://host.docker.internal:5050 \
DATABASE_URL=sqlite:////private/tmp/tutoria_selenium.db \
RUN_SELENIUM=1 \
.venv/bin/python -m pytest tests/test_selenium_ui.py -vv -s --tb=short
```

Resultado esperado:

```text
9 passed
```

Las pruebas generan capturas y HTML en:

```text
docs/evidencias/selenium_2026-08-07/
```

## Pruebas backend

Para demostrar las pruebas de integración y funcionalidad del servidor:

```bash
cd /Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo
.venv/bin/python -m pytest tests/test_app.py -vv -s --tb=short
```

Para ejecutar todas las pruebas backend y ver cada caso individualmente:

```bash
.venv/bin/python -m pytest -vv -s --tb=short
```

Para ejecutar toda la suite incluyendo Selenium Grid, con la aplicación y el
contenedor Selenium ya iniciados:

```bash
SELENIUM_REMOTE_URL=http://localhost:4444/wd/hub \
SELENIUM_BASE_URL=http://host.docker.internal:5050 \
DATABASE_URL=sqlite:////private/tmp/tutoria_selenium.db \
RUN_SELENIUM=1 \
.venv/bin/python -m pytest -vv -s --tb=short
```

Durante la exposición, la opción recomendada es `-vv -s --tb=short`: muestra el
nombre de cada prueba, conserva los mensajes de consola y presenta los errores
de forma breve.

Resultado esperado de la suite completa sin Selenium:

```text
36 passed, 1 skipped
```

La prueba omitida corresponde a Selenium cuando no se ejecuta con
`RUN_SELENIUM=1`.

Con Selenium Grid activo, la regresión completa esperada es:

```text
45 passed
```

La suite UI ampliada cubre también validaciones incompletas, CRUD de contenidos,
filtros, banco de preguntas, permisos por rol, auditoría, estados de error del
chat, TOTP inválido, registros duplicados y viewport móvil.

La segunda ronda backend agrega casos de fallback NVIDIA → Foundation Models,
validación de historial del chat, escape XSS, duplicidad de usuarios y protección
contra eliminación de contenidos con recomendaciones asociadas. También verifica
errores SSE estructurados como `Load failed` y eventos de uso sin `choices` antes
de que lleguen al navegador.

## Recorrido recomendado durante la presentación

1. Mostrar la página pública y abrir **Ayuda**.
2. Explicar los tres roles: estudiante, docente y administrador.
3. Registrar o iniciar sesión con una cuenta de prueba.
4. Mostrar la configuración y verificación TOTP.
5. Como estudiante, completar el perfil académico.
6. Responder la evaluación diagnóstica.
7. Mostrar el progreso y el estado pendiente de clasificación.
8. Como docente, mostrar estudiantes, preguntas y contenidos.
9. Clasificar una evaluación con IA controlada.
10. Mostrar recomendaciones y reportes.
11. Como administrador, mostrar usuarios y bitácora.
12. Mostrar TutorIA/chat y explicar el proveedor configurado.

## Clasificación de las pruebas

| Archivo | Tipo | Cobertura |
|---|---|---|
| `test_app.py` | Unitaria | Normalización de mensajes y reglas aisladas. |
| `test_app.py` | Integración | Flask, SQLite, proveedores simulados, TOTP y streaming. |
| `test_app.py` | Funcional | Login, roles, estudiantes, evaluaciones, IA, recomendaciones y reportes. |
| `test_selenium_ui.py` | Funcional end-to-end | Navegación real en Chrome remoto, formularios, TOTP y evidencias visuales. |

## Problemas frecuentes

### Selenium no está disponible

```bash
docker ps
curl http://localhost:4444/status
```

Si el contenedor no aparece:

```bash
docker start selenium-chromium
```

### La aplicación no abre desde Docker

Confirmar que se inició con:

```text
APP_HOST=0.0.0.0
```

Y que Selenium utiliza:

```text
SELENIUM_BASE_URL=http://host.docker.internal:5050
```

### El puerto 5050 está ocupado

Detener la instancia anterior con `Ctrl+C` o iniciar la aplicación en otro
puerto, cambiando `APP_PORT` y `SELENIUM_BASE_URL` de forma consistente.

### Pylance muestra imports no resueltos

Seleccionar como intérprete de VS Code:

```text
mvp_minimo/.venv/bin/python
```

Para revisar el tipado desde consola:

```bash
.venv/bin/pyright --project pyrightconfig.json
```

## Cierre de la demostración

Detener la aplicación con `Ctrl+C` en la Terminal 2 y detener Selenium:

```bash
docker stop selenium-chromium
```
