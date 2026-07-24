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
| 2026-07-11 | Miguel / Codex | Implementación | Se implementa clasificación IA estructurada para evaluaciones diagnósticas: contrato JSON, validación, persistencia de nivel y actualización del estudiante. | `app/services/diagnostic.py`, botón `Clasificar con IA` y pytest. | Implementar recomendaciones por nivel. |
| 2026-07-11 | Miguel / Codex | Mejora UI | Se implementa renderer Markdown seguro en el chat para títulos, negritas, listas, código inline y bloques de código con HTML escapado. | `app/static/js/chat.js`, `app/static/css/chat.css`, `chat.html` versionado. | Validar con respuesta real del modelo FM. |
| 2026-07-11 | Miguel / Codex | Mejora UI | Se agrega tarjeta visible de ejecución local en el chat: App Flask, endpoint del modelo, invocación `fm serve` y estado del proceso. | Chat `/chat`, rail técnico y QA visual. | Validar con Foundation Models disponible en demo. |
| 2026-07-23 | Miguel / Codex | Implementación | Se integran recomendaciones trazables por nivel y área de interés, reportes generales/individuales y administración básica de usuarios. | Rutas `/recommendations`, `/reports`, `/users`, modelo `ContentRecommendation` y plantillas nuevas. | Ejecutar validación funcional y preparar evidencias. |
| 2026-07-23 | Miguel / Codex | Calidad y documentación | Se amplía la suite a 21 pruebas aprobadas, se refresca la interfaz con tono colegial y se crea la arquitectura explicativa en español. | `pytest: 21 passed`, `mvp_minimo/ARQUITECTURA_TUTORIA.md`, `docs/06_TAREAS_IMPLEMENTACION_MVP.md`. | Validar Foundation Models/Resend reales y completar informe académico. |
| 2026-07-23 | Miguel / Codex | Herramienta de ejecución | Se agrega centro de control PySide6 para encender, observar y apagar Foundation Models y Flask, con estados, PID, puertos y logs. | `desktop_launcher.py`, `runtime_manager.py`, `Iniciar_TutorIA_Desktop.command`. Prueba real: ambos servicios encendidos y apagados correctamente. | Revisar visualmente el panel y preparar captura para la demo. |
| 2026-07-23 | Miguel / Codex | Integración IA | Se agrega NVIDIA NIM como proveedor principal mediante `NVIDIA_API_KEY`, con fallback automático a Foundation Models y carga segura desde `.env`. | `NVIDIAProvider`, `FallbackChatProvider`, `.env.example` sin secretos y `python-dotenv`. | Revocar claves expuestas, generar nuevas y validar una llamada NVIDIA real. |

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
| Alta | Clasificación IA estructurada | Miguel | Completado |
| Media | Recomendaciones de contenidos | Miguel / Roberto | Completado inicial |
| Media | Reportes básicos | Roberto | Completado inicial |
| Media | Gestión simple de usuarios | Miguel | Completado inicial |
| Media | Prueba real con Resend | Miguel | Pendiente |
| Alta | Actualizar informe Word con arquitectura y pruebas | Miguel / Roberto | Pendiente |
