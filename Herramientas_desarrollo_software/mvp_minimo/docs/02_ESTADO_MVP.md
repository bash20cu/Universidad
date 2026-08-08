# Estado del MVP TutorIA

**Fecha de corte:** 2026-08-07  
**Resultado general:** funcional para demostración académica controlada; la decisión NVIDIA + Foundation Models queda adoptada y la ronda UI con Selenium ya cubre los flujos principales de acceso, roles y evaluación estudiantil.

## Resumen ejecutivo

El código implementa los módulos centrales descritos en el informe: usuarios y roles, autenticación con contraseña y TOTP, estudiantes, contenidos, preguntas diagnósticas, evaluaciones respondidas por docentes o estudiantes, clasificación IA, recomendaciones, reportes, portal privado, bitácora y ayuda pública. La suite de backend tiene 30 pruebas aprobadas y la suite UI agrega 3 escenarios Selenium aprobados contra Chrome remoto.

El proyecto no está listo para declararse terminado porque aún falta validar proveedores IA reales, Windows y los módulos de recomendaciones/reportes desde navegador. La documentación de `docs/` ya incluye la ronda Selenium y los documentos derivados reflejan la decisión NVIDIA + Foundation Models. Ollama queda fuera del MVP actual.

## Matriz de estado

| Área | Evidencia en código | Estado | Falta para cerrar |
|---|---|---|---|
| Aplicación Flask modular | `app/routes`, `app/services`, `app/models`, `app/forms`, templates y estáticos. | **Completado** | Mantener separación de responsabilidades. |
| Usuarios y roles | Rutas de usuarios, roles administrador/docente/estudiante, pruebas de autorización y capturas Selenium. | **Completado** | Añadir validación de teclado y responsive. |
| Contraseñas y sesiones | Hash de contraseña, Flask-Login y cierre de sesión. | **Completado** | Documentar configuración segura para producción. |
| Segundo factor | TOTP, QR, hash de códigos de desafío, expiración, tres intentos y un solo uso. | **Completado en MVP** | Códigos de recuperación quedan como mejora futura. |
| Estudiantes | Alta, edición, eliminación administrativa, perfil privado, progreso y capturas Selenium. | **Completado** | Añadir matriz requisito-prueba. |
| Contenidos | CRUD, niveles, temas, competencias, tipo, URL y estado. | **Completado** | Ampliar banco y tipos de material como mejora futura. |
| Preguntas diagnósticas | Alta/edición, activación y formulario de evaluación. | **Completado** | Documentar catálogo inicial y criterio pedagógico. |
| Evaluación | Persistencia de respuestas y estados; docentes y estudiantes pueden responder según su flujo, con evidencia Selenium del estudiante. | **Completado** | Añadir clasificación IA real desde navegador. |
| Clasificación IA | Prompt, parseo JSON, niveles cerrados, regla de evidencia mínima, proveedor/modelo persistidos. | **Completado con proveedor simulado probado** | Validar una integración real reproducible y documentar límites. |
| Recomendaciones | Selección determinista de contenidos existentes, límite de cinco y motivo trazable. | **Completado** | Añadir evidencia y más contenidos de demostración. |
| Reportes | Reporte general e individual de progreso. | **Completado** | Exportación PDF queda fuera del MVP y está pendiente como mejora. |
| Bitácora | Eventos de seguridad y operación con usuario, entidad, detalle e IP. | **Completado** | Documentar retención, revisión y protección del log. |
| Chat SSE | Proxy protegido, streaming, metadatos de uso y estado del proveedor. | **Completado** | Probar el flujo contra proveedores reales y guardar evidencia. |
| Abstracción IA | `ChatProvider`, `ProviderStatus`, inyección en `create_app`, ciclo de vida local y proveedor remoto NVIDIA. | **Completado** | Mantener ADR, minuta e informe sincronizados. |
| Foundation Models | Cliente hacia `fm serve`, salud, arranque bajo demanda y apagado solo si la app es propietaria. | **Implementado para el entorno objetivo** | Confirmar ejecución en el Mac de demostración y documentar versiones. |
| NVIDIA NIM | Proveedor remoto configurable por `.env`, streaming, respuesta completa y fallback a Foundation Models. | **Implementado** | Confirmar autorización académica, costo, privacidad y evidencia de la clave fuera del repositorio. |
| Ollama | Alternativa histórica mencionada en documentación técnica. | **Fuera del MVP** | No implementarlo en esta entrega; mantenerlo solo como evolución futura claramente etiquetada. |
| Windows | Lanzador y guía con NVIDIA como proveedor remoto. | **Preparado, no validado en esta revisión** | Ejecutar una prueba real en Windows y agregar evidencia. |
| Pruebas automatizadas | `tests/test_app.py` con 30 pruebas y `tests/test_selenium_ui.py` con 3 escenarios UI aprobados. | **Completado para el alcance probado** | Agregar pruebas de errores de proveedor real y responsive. |
| Evidencias de demostración | 15 PNG y 15 HTML de Selenium para inicio, ayuda, TOTP, estudiante, docente y administrador. | **Completado para el alcance cubierto** | Completar IA real, recomendaciones, reportes y Windows. |
| Documentación operativa | README y arquitectura en la raíz del MVP. | **Parcial** | Sincronizar con ADR/minuta/documentación técnica y mantener `docs/` actualizado. |

## Pendientes priorizados

### P0 - Resolver antes de presentar el proyecto como cerrado

- Confirmar en el equipo de demostración la disponibilidad de Foundation Models, el comando `fm`, la versión de macOS y el modelo `system`.
- Ejecutar una demostración reproducible de clasificación IA, recomendaciones y reportes desde navegador.

### P1 - Cerrar para una entrega académica sólida

- Documentar la configuración real de `.env` sin incluir secretos.
- Ejecutar y registrar una prueba manual con NVIDIA y otra con Foundation Models, identificando proveedor, modelo, modo de acceso, ubicación de procesamiento y resultado.
- Validar el lanzador Windows en un equipo Windows.
- Revisar README y arquitectura para mantenerlos sincronizados con la decisión de proveedores y el flujo estudiantil.
- Documentar la limitación de SQLite y del servidor de desarrollo frente a una futura versión productiva.

### P2 - Mejoras posteriores al MVP

- Códigos de recuperación para TOTP.
- Exportación de reportes a PDF.
- Banco de preguntas y materiales más amplio.
- Métricas históricas de progreso.
- PostgreSQL y servidor WSGI.

## Criterio de aceptación final

El MVP podrá marcarse como cerrado cuando la decisión de proveedores esté aprobada, las evidencias cubran los flujos principales, la documentación no prometa capacidades inexistentes y se conserve una ejecución reproducible de las pruebas.
