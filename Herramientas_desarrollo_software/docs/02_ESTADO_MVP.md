# Estado Del MVP - TutorIA

## Resumen Ejecutivo

El MVP ya tiene una base técnica funcional y los módulos principales integrados: aplicación Flask modular, autenticación, roles, segundo factor TOTP, bitácora, CRUD de estudiantes, contenidos, diagnóstico, clasificación IA, recomendaciones, reportes y administración básica de usuarios.

## Matriz De Alcance

| ID | Requisito | Estado | Evidencia | Falta para completar |
|---|---|---|---|---|
| MVP-01 | Gestión de usuarios | Hecho | Modelo `User`, autorregistro estudiantil, login, 2FA y CRUD administrativo. | Validar flujo completo de demo. |
| MVP-02 | Roles administrador/docente/estudiante | Hecho | `User.role` y decorador `roles_required`. | Ampliar pruebas al agregar nuevas rutas. |
| MVP-03 | Autenticación usuario/contraseña | Hecho | Flask-Login y hash de contraseña. | Endurecer mensajes y documentación final. |
| MVP-04 | Segundo factor TOTP | Hecho | TOTP gratuito con QR queda integrado para autenticadores compatibles; el código inicial se muestra en consola durante la demo. | Agregar códigos de recuperación. |
| MVP-05 | Bitácora de transacciones | Hecho | `AuditLog` registra acciones y el administrador dispone de visor web protegido. | Revisar evidencias finales. |
| MVP-06 | Gestión de estudiantes | Hecho | CRUD docente/admin y perfil académico vinculado a cuenta estudiantil. | Añadir relaciones con evaluaciones reales. |
| MVP-07 | Gestión de contenidos educativos | Hecho | Modelo, seed, CRUD visual, permisos, tipo de material, enlace, estado y bitácora. | Integrar más formatos multimedia en una versión futura. |
| MVP-08 | Evaluación diagnóstica | Hecho | Flujo `/diagnostics` registra estudiante, preguntas y respuestas; banco de preguntas administrable. | Ampliar banco con más materias. |
| MVP-09 | Envío de respuestas a IA | Hecho | Servicio de diagnóstico usa proveedor IA abstracto y fue validado con NVIDIA y Foundation Models reales. | Revisar resultados con docentes. |
| MVP-10 | Clasificación básico/intermedio/avanzado | Hecho | Contrato JSON validado y persistido junto con proveedor, modelo y fecha. | Ajustar prompt con más casos académicos. |
| MVP-11 | Recomendación de contenidos | Hecho inicial | Servicio explicable, persistencia y vista por estudiante. | Validar con más combinaciones de temas. |
| MVP-12 | Reportes básicos | Hecho | Reporte general e individual con indicadores, progreso porcentual, distribución por nivel y recomendaciones. | Preparar capturas y evidencia. |
| MVP-13 | README | Hecho | `mvp_minimo/README.md`. | Actualizar conforme avancen módulos. |
| MVP-14 | `.env.example` | Hecho | Variables documentadas. | Confirmar valores finales de Resend/FM. |
| MVP-15 | Pruebas iniciales | Hecho | `27 passed`, incluyendo registro con TOTP, usuarios, recomendaciones y reportes. | Ejecutar evidencia funcional en equipo compatible. |

## Estado Funcional Actual

- El visitante puede crear una cuenta estudiantil con correo único, contraseña cifrada y activar TOTP directamente durante el registro.
- El usuario puede activar TOTP mediante un código QR e iniciar sesión con su autenticador.
- El administrador y docente pueden gestionar estudiantes.
- El administrador puede eliminar estudiantes sin evaluaciones.
- El chat IA puede consultar proveedor Foundation Models por streaming.
- El dashboard administrativo muestra métricas generales y el estudiante recibe un panel privado de progreso.
- El administrador y docente pueden gestionar contenidos educativos.
- El administrador y docente pueden registrar evaluaciones diagnósticas con respuestas y administrar el banco de preguntas.
- El chat renderiza Markdown básico de forma segura para mejorar legibilidad de respuestas IA.
- El chat muestra evidencia visible de ejecución local: Flask, endpoint del modelo e invocación `fm serve`.
- La base de datos se inicializa con usuarios, contenidos y preguntas semilla; las nuevas cuentas quedan registradas en la bitácora.
- El personal autorizado puede consultar rutas de aprendizaje y reportes de progreso.
- El estudiante puede completar su perfil y consultar únicamente sus propias evaluaciones y recomendaciones.
- El administrador puede consultar la bitácora desde `/users/audit`.
- El administrador puede crear y editar usuarios sin guardar contraseñas en texto plano.

## Brecha Principal

El flujo funcional principal ya está integrado. Los pendientes restantes son códigos de recuperación, más capturas protegidas de la demo, exportación futura y entrega formal del informe/presentación.
