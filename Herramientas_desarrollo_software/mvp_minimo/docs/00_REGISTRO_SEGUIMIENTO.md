# Registro de seguimiento - TutorIA MVP

**Fecha de revisión:** 2026-08-05  
**Alcance:** revisión de `mvp_minimo`, inventario de `docs/`, pruebas y contraste con la documentación técnica del proyecto.

## 2026-08-05 - Revisión integral del MVP

### Actividades realizadas

- Se leyó `AGENTS.md` del proyecto y se confirmó que el trabajo debe concentrarse en `mvp_minimo`.
- Se inventariaron todos los archivos existentes en `docs/`.
- Se revisaron `README.md`, `ARQUITECTURA_TUTORIA.md`, las rutas Flask, servicios, modelos, proveedor IA, lanzadores y pruebas.
- Se contrastó el estado del código con `ADR_001_Arquitectura_Abstracta_Proveedores_IA.md`, `Documentacion_Tecnica_API_IA_TutorIA.md`, `Minuta_Decision_IA_FoundationModels_vs_Ollama.md` y el informe final.
- Se inspeccionó visualmente la captura `docs/evidencias/00_inicio.png`.
- Se ejecutó la suite completa con el entorno virtual del proyecto fuera del aislamiento de red local.

### Resultado verificable

- **30 pruebas automatizadas aprobadas.**
- La suite cubre autenticación, TOTP, autorización por roles, CRUD de estudiantes y contenidos, diagnóstico, clasificación con proveedor simulado, recomendaciones, reportes, auditoría y administración de usuarios.
- La ejecución dentro del aislamiento produjo errores de permisos al abrir servidores HTTP locales simulados; no fueron fallos funcionales del código. La ejecución autorizada fuera del aislamiento terminó con `27 passed in 13.10s`.

### Estado de documentación al inicio

Antes de esta revisión, `docs/` contenía únicamente:

- `evidencias/00_inicio.png`.
- `evidencias/00_inicio_snapshot.yml`.
- `evidencias/01_login_totp_snapshot.yml`.

No existían el registro de seguimiento, el estado del MVP ni el registro de riesgos exigidos por `AGENTS.md`.

### Decisiones y pendientes abiertos

1. Registrar el estado funcional y las evidencias faltantes en `02_ESTADO_MVP.md`.
2. Registrar riesgos y contradicciones de arquitectura en `03_RIESGOS.md`.
3. Completar evidencias de demostración del flujo académico y de los roles.
4. Resolver con el equipo/profesor el proveedor principal: Foundation Models, NVIDIA u Ollama.
5. Implementar o retirar formalmente la referencia a Ollama. No debe presentarse como respaldo implementado mientras no exista `OllamaProvider`, configuración, pruebas y evidencia.
6. Alinear README, ADR, minuta, documentación técnica e informe final con la implementación aprobada.

## Criterio de cierre

Un pendiente se marcará como cerrado cuando exista código verificable, prueba automatizada o evidencia manual reproducible y la documentación principal coincida con ese comportamiento.

## 2026-08-08 - Revisión de documentación del código

- Se revisaron los módulos propios de la aplicación, proveedores IA, lanzadores y pruebas.
- Se confirmó que las clases y funciones de la aplicación cuentan con docstrings en español.
- Se documentaron explícitamente los proveedores, la seguridad TOTP, la trazabilidad de evaluaciones, la compatibilidad de esquema y el aislamiento de pruebas.
- Se agregó `docs/05_DOCUMENTACION_CODIGO.md` como guía de mantenimiento y explicación académica del código.
- Se agregaron comentarios breves en la selección de proveedores, migración compatible y persistencia de respuestas diagnósticas.

## 2026-08-05 - Decisión de proveedores y centro de ayuda

- Se adopta NVIDIA NIM como proveedor principal y Foundation Models como fallback local en macOS.
- Se deja Ollama fuera del MVP activo y se conserva únicamente como alternativa futura.
- Se agregó la página pública `/help`, enlazada desde la navegación, con instrucciones para estudiantes, docentes y administradores.
- La ayuda incluye registro, TOTP, flujo diagnóstico, recomendaciones, Tutor IA y solución de problemas.
- Se actualizó el ADR, la minuta de decisión, la documentación técnica y el informe final DOCX/PDF.
- Se agregó la prueba `test_help_page_is_public_and_explains_roles`.
- La suite completa terminó con `28 passed`.

## 2026-08-05 - Evaluación respondida por estudiantes

- Se agregó el flujo privado `/student/diagnostic/new`.
- El estudiante debe tener perfil académico y responde todas las preguntas diagnósticas activas.
- Las respuestas se guardan como `DiagnosticEvaluation` y `DiagnosticAnswer` asociadas al propio estudiante.
- La evaluación queda `pendiente_ia`; la clasificación continúa bajo responsabilidad del docente o administrador.
- Se agregaron accesos desde la navegación, el aula del estudiante y la pantalla de progreso.
- Se agregaron pruebas de envío completo y rechazo de respuestas incompletas.

## 2026-08-07 - Ronda QA UI con Selenium Grid

- Se incorporó `selenium==4.27.1` a las dependencias del MVP.
- Se agregó `tests/test_selenium_ui.py` con tres escenarios UI remotos.
- Se validaron inicio, ayuda, registro, activación TOTP, perfil, evaluación estudiantil, progreso, login inválido, panel docente y panel administrador.
- Se generaron 21 capturas PNG y sus 21 HTML correspondientes en `docs/evidencias/selenium_2026-08-07/`.
- Se sanitizaron QR, secretos manuales y códigos TOTP antes de persistir evidencias.
- Resultado de la ejecución UI: **3 passed**.
- Se documentaron límites de cobertura: CRUD de contenidos/preguntas, IA real, chat SSE, recomendaciones, reportes, responsive móvil y Windows quedan para una segunda ronda.
- Regresión combinada con `RUN_SELENIUM=1`: **33 passed** (30 backend + 3 UI).

## 2026-08-08 - Cabeceras de documentación en código

- Se revisaron los 67 archivos propios de código del MVP: Python, JavaScript, CSS, plantillas Jinja y lanzadores.
- Se añadieron cabeceras breves con archivo, propósito, responsabilidades, dependencias y entradas/salidas.
- Se conservaron los shebangs, directivas de Windows y herencia de plantillas; no se modificó la lógica.
- Se excluyeron `.venv`, Bootstrap, evidencias generadas y archivos externos.
- Verificación: sintaxis Python y shell correctas; **30 pruebas aprobadas y 1 omitida** por requerir Selenium Grid.

## 2026-08-08 - Autoría y copyright en cabeceras

- Se actualizaron las cabeceras de los 67 archivos de código propios para incluir la autoría del equipo.
- Se agregó la línea de copyright académico 2026 con los nombres disponibles en el informe final.
- No se inventaron autores ni fechas de creación; se mantuvo únicamente el año académico disponible.
- No se duplicaron cabeceras ni se alteró la lógica del sistema.

## 2026-08-08 - Clasificación de tipos de prueba

- Se marcaron en `tests/test_app.py` las pruebas unitarias, de integración y funcionales.
- Se marcó `tests/test_selenium_ui.py` como suite funcional end-to-end.
- Se agregó la matriz de clasificación a `docs/04_PRUEBAS_UI_SELENIUM.md` para facilitar la exposición y evaluación docente.

## 2026-08-08 - Guía operativa para la exposición

- Se agregó `tests/README.md` con comandos de Selenium, TutorIA, pruebas backend y suite completa.
- La guía incluye recorrido recomendado, clasificación de pruebas y solución de problemas frecuentes.
- Se documentaron el panel Grid `:4444/ui` y la visualización noVNC `:7900` para observar el navegador durante Selenium.

## 2026-08-08 - Fallback ante errores SSE de NVIDIA

- Se detectó que NVIDIA podía devolver `Load failed` dentro de un evento SSE con HTTP 200.
- El fallback no se activaba porque no existía una excepción HTTP/Python que lo disparara.
- `FallbackChatProvider` ahora identifica errores SSE estructurados antes de entregar contenido y cambia a Foundation Models.
- Se agregó una prueba específica para este caso; la suite backend quedó en **35 passed**.

## 2026-08-08 - Robustez del parser de streaming

- Se corrigió `_content_from_chunk()` para ignorar eventos sin elementos `choices`, como bloques de uso enviados por proveedores compatibles con OpenAI.
- Se agregó una prueba con un evento de métricas sin `choices` seguido de contenido válido.
- La suite backend quedó en **36 passed** y dejó de producir el `IndexError: list index out of range` observado en consola.

## 2026-08-08 - Ampliación de pruebas Selenium

- Se amplió `tests/test_selenium_ui.py` de 3 a 9 escenarios funcionales end-to-end.
- Se agregaron validaciones de formularios, CRUD y filtros de contenidos, banco de preguntas, evaluación incompleta, permisos por rol, auditoría, interacción/error del chat, TOTP inválido, registros duplicados y viewport móvil.
- La ejecución contra Selenium Grid terminó con **9 passed**.
- Se generaron 46 capturas PNG y 46 HTML sanitizados en `docs/evidencias/selenium_2026-08-07/`.

## 2026-08-08 - Segunda ronda backend de seguridad y resiliencia

- Se agregaron pruebas del fallback entre NVIDIA y Foundation Models mediante proveedores controlados.
- Se validaron roles de chat inválidos, mensajes vacíos, duplicidad de usuarios y escape de contenido con patrón XSS.
- Se comprobó que un contenido con recomendaciones asociadas no puede eliminarse.
- La suite backend pasó de 30 a **34 pruebas aprobadas**.

## 2026-08-08 - Estabilización de ejecución completa

- Se revisó una ejecución combinada donde Selenium reportaba 1 fallo intermitente en el flujo docente.
- La causa era que el helper TOTP esperaba cualquier encabezado `h1` y podía continuar desde la pantalla de verificación sin confirmar el acceso.
- Se ajustó el helper para generar el código después de la captura y esperar explícitamente la URL `/dashboard`.
- La ejecución completa con nombres visibles terminó con **43 passed**.

## 2026-08-08 - Guía breve para exposición

- Se agregó `docs/06_GUIA_EXPOSICION_ARQUITECTURA_Y_PRUEBAS.md`.
- El documento resume arquitectura, componentes, roles, seguridad, estrategia de pruebas, resultados, comandos de ejecución y respuestas para preguntas frecuentes del profesor.
