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
