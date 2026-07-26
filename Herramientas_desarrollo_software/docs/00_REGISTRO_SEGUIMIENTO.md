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
| 2026-07-23 | Miguel / Codex | Usuarios y calidad | Se agrega autorregistro público de cuentas estudiantiles con contraseña cifrada, correo único, rol seguro y evento de bitácora. La suite queda en 22 pruebas. | `/auth/register`, `RegistrationForm`, `auth/register.html`, `pytest: 22 passed`. | Validar registro y 2FA con correo real durante la demo. |
| 2026-07-23 | Miguel / Codex | Calidad de código | Se documentan clases, funciones y métodos de producción con docstrings y comentarios académicos en español; la revisión AST confirma cobertura completa. | Módulos Python de `mvp_minimo`; `22 passed`. | Mantener la documentación al agregar nuevas clases o funciones. |
| 2026-07-23 | Miguel / Codex | Expansión funcional | Se implementa portal privado del estudiante, vínculo `User`-`Student`, edición de perfil, consulta de progreso y visor administrativo de bitácora. | Blueprint `student_portal`, `/users/audit`, templates `student` y `audit`; `pytest: 24 passed`. | Administrar preguntas, enriquecer contenidos y preparar migración de esquema. |
| 2026-07-23 | Miguel / Codex | Seguridad | Se integra segundo factor TOTP gratuito con secreto por usuario, QR de aprovisionamiento, activación desde el perfil, validación en login y migración compatible para bases SQLite existentes. Se elimina el envío de códigos por correo del flujo de autenticación. | `/auth/totp/setup`, `/auth/verify-totp`, `pyotp`, `qrcode[pil]`; `pytest: 26 passed`. | Agregar códigos de recuperación. |
| 2026-07-23 | Miguel / Codex | Registro y seguridad | El autorregistro ahora crea el secreto TOTP y muestra el QR inmediatamente; la cuenta no entra al panel hasta confirmar el código de Google Authenticator. | `/auth/register/2fa`; `pytest: 27 passed`. | Agregar códigos de recuperación. |
| 2026-07-26 | Miguel / Codex | Entrega académica | Se reemplaza el UML Mermaid exportado con una versión PDF maquetada de cinco páginas, sin páginas vacías ni cortes de diagramas, y se incorpora como Anexo A al informe final DOCX/PDF. | `docs/08_UML_TUTOR_IA.pdf`, `Informe_Proyecto_TutorIA_APA7.docx`, `Informe_Proyecto_TutorIA_APA7.pdf`; revisión visual con render. | Revisar capturas y personalizar la defensa final. |
| 2026-07-26 | Miguel / Codex | Compatibilidad | Se agrega lanzador `.bat`, configuración `.env.windows.example` y guía para ejecutar el MVP en Windows con NVIDIA; Foundation Models queda documentado como componente exclusivo de macOS. | `mvp_minimo/Iniciar_TutorIA_Windows.bat`, `mvp_minimo/README_WINDOWS.md`. | Probar en un equipo Windows real si se dispone de uno. |

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
| Media | Gestión de usuarios y autorregistro | Miguel | Completado inicial |
| Alta | Portal privado del estudiante y bitácora visible | Miguel | Completado |
| Media | Prueba real con Resend | Miguel | Pendiente |
| Media | Códigos de recuperación para TOTP | Miguel | Pendiente |
| Alta | Actualizar informe Word con arquitectura y pruebas | Miguel / Roberto | Pendiente |
