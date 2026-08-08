# Pruebas UI con Selenium Grid

**Fecha de ejecución:** 2026-08-07  
**Entorno:** TutorIA en Flask sobre `0.0.0.0:5050`, SQLite temporal en `/private/tmp`, Chrome remoto en `selenium/standalone-chromium:latest`.  
**Resultado de la última ejecución:** 9 escenarios Selenium aprobados; 46 capturas PNG y 46 HTML generados, además del archivo de cuentas de prueba sin secretos.

## Herramientas utilizadas

- Selenium Python `4.27.1`.
- Selenium Grid remoto en `http://localhost:4444/wd/hub`.
- Chrome/Chromium remoto dentro del contenedor Docker.
- `pytest` para ejecutar la suite.
- `pyotp` para generar códigos TOTP de las cuentas efímeras de prueba.
- SQLite aislado; no se utiliza la base de datos de demostración ni una clave NVIDIA.

## Clasificación por tipo de prueba

La suite queda organizada por el nivel que valida cada archivo:

| Archivo | Tipo principal | Qué valida |
|---|---|---|
| `tests/test_app.py` | Unitaria, integración y funcional | Reglas aisladas, conexión Flask-proveedor-base de datos y recorridos de autenticación, roles, estudiantes, evaluaciones, IA, recomendaciones y reportes. |
| `tests/test_selenium_ui.py` | Funcional end-to-end | Uso real de la interfaz en Chrome remoto, navegación por rol, formularios, TOTP, evaluación estudiantil, CRUD, autorización, errores del chat, responsive y evidencias visuales. |

Dentro de `test_app.py`, la prueba de normalización de mensajes está marcada
como unitaria; las pruebas del servidor/proveedor simulado están marcadas como
integración; y los flujos de usuario restantes están marcados como funcionales.
Una misma prueba puede cubrir más de una capa, pero la clasificación indica su
objetivo principal para la evaluación del curso.

## Preparación reproducible

1. Iniciar el contenedor `selenium-chromium` y verificar `http://localhost:4444/status`.
2. Iniciar TutorIA con una base temporal y escucha accesible desde Docker:

```bash
APP_HOST=0.0.0.0 APP_PORT=5050 \
DATABASE_URL=sqlite:////private/tmp/tutoria_selenium.db \
AI_PRIMARY_PROVIDER=foundation FM_COMMAND=/usr/bin/false \
AUTO_OPEN_BROWSER=0 .venv/bin/python run.py
```

3. Ejecutar las pruebas apuntando el navegador remoto al host:

```bash
SELENIUM_REMOTE_URL=http://localhost:4444/wd/hub \
SELENIUM_BASE_URL=http://host.docker.internal:5050 \
DATABASE_URL=sqlite:////private/tmp/tutoria_selenium.db \
RUN_SELENIUM=1 \
.venv/bin/python -m pytest tests/test_selenium_ui.py -q
```

El fixture reinicia únicamente la base temporal, crea cuentas de administrador,
docente y estudiante con secretos TOTP efímeros, y siembra preguntas/contenidos.
Las claves se generan en tiempo de prueba y no se guardan en el repositorio.

## Escenarios cubiertos

| Escenario | Evidencia principal | Resultado |
|---|---|---|
| Inicio público y centro de ayuda por rol | `01_inicio_publico.png`, `02_ayuda_roles.png` | Aprobado |
| Registro estudiantil | `03_registro_formulario.png` | Aprobado |
| Configuración inicial TOTP | `04_configuracion_qr_totp.png`, `05_aula_inicial.png` | Aprobado; evidencia sanitizada |
| Perfil académico | `06_perfil_estudiante.png` | Aprobado |
| Respuesta de evaluación diagnóstica | `07_evaluacion_respuestas.png` | Aprobado |
| Persistencia y progreso pendiente de IA | `08_progreso_evaluacion_enviada.png` | Aprobado |
| Credenciales inválidas | `09_login_invalido.png` | Aprobado |
| Acceso docente y gestión de estudiantes | `10_docente_*.png`, `11_panel_docente_estudiantes.png` | Aprobado |
| Acceso administrador y gestión de usuarios | `12_admin_*.png`, `13_panel_admin_usuarios.png` | Aprobado |
| TutorIA/chat y estado del proveedor | `14_tutor_ia_chat.png` | Aprobado visualmente; el proveedor de esta ronda fue controlado |
| Biblioteca académica | `15_biblioteca_contenidos.png` | Aprobado |
| Banco y listado de diagnósticos | `16_banco_diagnosticos.png` | Aprobado |
| Recomendaciones | `17_recomendaciones.png` | Aprobado |
| Reportes de progreso | `18_reportes.png` | Aprobado; se recomienda revisar etiquetas estrechas en la barra de distribución |
| Bitácora administrativa | `19_bitacora_auditoria.png` | Aprobado |
| Validación, alta, edición y filtro de contenidos | `21_contenido_validacion_incompleta.png` a `24_filtro_contenidos.png` | Aprobado |
| Validación y alta de pregunta diagnóstica | `26_pregunta_validacion_incompleta.png`, `27_pregunta_creada.png` | Aprobado |
| Rechazo de evaluación incompleta | `28_evaluacion_validacion_incompleta.png` | Aprobado |
| Alta de usuario y auditoría | `30_usuario_validacion_incompleta.png` a `32_auditoria_usuario_creado.png` | Aprobado |
| Restricción de permisos de estudiante | `34_acceso_estudiante_denegado.png` | Aprobado |
| Interacción de chat y estado de error/proveedor | `35_sugerencia_chat.png`, `36_chat_respuesta_o_error.png` | Aprobado |
| Validación responsive móvil | `37_ayuda_movil.png`, `38_login_movil.png` | Aprobado |
| TOTP inválido y registro duplicado | `39_totp_invalido.png`, `40_registro_duplicado.png` | Aprobado |

Cada escenario también guarda un HTML para inspección estructural. Las capturas
de QR y TOTP ocultan la imagen, el secreto manual y el código temporal; los
artefactos no deben utilizarse para recuperar credenciales.

## Resultado y observaciones

- La navegación pública, el flujo de registro y la activación TOTP funcionan en
  navegador real remoto.
- El estudiante puede completar el perfil, contestar las tres preguntas activas
  y consultar una evaluación con estado **Pendiente**.
- El docente visualiza el listado de estudiantes; el administrador visualiza
  usuarios y roles.
- No se observaron desbordamientos visuales en los escenarios revisados.
- La página larga de evaluación exige desplazamiento para enviar el formulario;
  la prueba automatizada lo controla explícitamente. Es una oportunidad de UX
  para agregar un botón flotante o un indicador de avance en futuras mejoras.
- Se observa un `404` de `favicon.ico` durante la navegación; no afecta la
  funcionalidad, pero puede corregirse agregando un favicon local.

## Cobertura que queda para una tercera ronda

- Clasificación real con NVIDIA y fallback real con Foundation Models.
- Chat SSE exitoso con un proveedor controlado conectado al proceso web, recomendaciones y reportes con datos producidos desde la UI.
- Errores de proveedor, indisponibilidad de red y recuperación de sesión.
- Revisión responsive en viewport móvil y navegación completa con teclado.
- Prueba real del lanzador Windows.

## Segunda ronda QA UI - 2026-08-08

- La suite pasó de 3 a 9 escenarios funcionales end-to-end.
- Se agregaron validaciones de formularios, CRUD y filtros de contenidos, banco de preguntas, evaluación incompleta, permisos por rol, auditoría, interacción/error del chat, TOTP inválido, registros duplicados y viewport móvil.
- La ejecución reproducida terminó con **9 passed** y produjo 46 PNG más 46 HTML.
