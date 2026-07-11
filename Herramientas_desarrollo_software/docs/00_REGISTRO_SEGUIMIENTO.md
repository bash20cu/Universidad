# Registro Y Seguimiento - TutorIA

Bitácora viva para registrar avances, decisiones, evidencia y próximos pasos del proyecto TutorIA.

## Datos Del Proyecto

| Campo | Valor |
|---|---|
| Proyecto | TutorIA |
| Equipo | Equipo 1 |
| Integrantes | Miguel Alejandro Fernández Arteaga, Roberto José Rojas García |
| Tema | Herramienta de tutor inteligente adaptativo mediante IA local |
| Stack | Python, Flask, Jinja2, Bootstrap 5, SQLite, SQLAlchemy, Flask-Login, Resend, Foundation Models/Ollama |
| Ruta principal | `/Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo` |

## Bitácora

| Fecha | Responsable | Tipo | Descripción | Evidencia | Próximo paso |
|---|---|---|---|---|---|
| 2026-07-11 | Miguel / Codex | Documentación | Se crea estructura inicial de documentación viva, seguimiento, riesgos, estado MVP y reglas para agentes. | Carpeta `docs` y `AGENTS.md`. | Implementar CRUD de contenidos educativos. |
| 2026-07-11 | Miguel / Codex | Validación | Se confirma que el MVP actual tiene autenticación, roles, 2FA preparado, bitácora, CRUD de estudiantes, chat IA y pruebas iniciales. | `mvp_minimo`, README y pruebas existentes. | Completar módulos pendientes del alcance MVP. |
| 2026-07-11 | Miguel / Codex | Implementación | Se implementa CRUD de contenidos educativos con permisos, filtros, formularios, bitácora y pruebas. | Rutas `/contents`, templates `contents`, CSS/JS y pytest. | Implementar evaluación diagnóstica. |
| 2026-07-11 | Miguel / Codex | Implementación | Se implementa evaluación diagnóstica inicial sin IA: selección de estudiante, preguntas activas, guardado de respuestas, detalle y bitácora. | Rutas `/diagnostics`, templates `diagnostics`, CSS y pytest. | Implementar clasificación con IA estructurada. |

## Decisiones Vigentes

| ID | Fecha | Decisión | Justificación | Estado |
|---|---|---|---|---|
| DEC-001 | 2026-06-10 | Usar arquitectura abstracta para proveedores IA. | Evita acoplar TutorIA a Foundation Models u Ollama. | Aceptada |
| DEC-002 | 2026-07-11 | Mantener SQLite para desarrollo local. | Reduce complejidad del MVP y facilita demostración universitaria. | Vigente |
| DEC-003 | 2026-07-11 | Versionar Bootstrap localmente. | Evita dependencia de CDN en la demostración. | Vigente |
| DEC-004 | 2026-07-11 | Documentar avances en Markdown antes de cerrar cada bloque. | Mejora trazabilidad para informe y defensa del proyecto. | Vigente |

## Pendientes Inmediatos

| Prioridad | Actividad | Responsable sugerido | Estado |
|---|---|---|---|
| Alta | CRUD de contenidos educativos | Miguel | Completado |
| Alta | Evaluación diagnóstica | Miguel / Roberto | Completado inicial |
| Alta | Clasificación IA estructurada | Miguel | Próximo |
| Media | Recomendaciones de contenidos | Miguel / Roberto | Pendiente |
| Media | Reportes básicos | Roberto | Pendiente |
| Media | Gestión simple de usuarios | Miguel | Pendiente |
| Media | Prueba real con Resend | Miguel | Pendiente |
| Alta | Actualizar informe Word con arquitectura y pruebas | Miguel / Roberto | Pendiente |
