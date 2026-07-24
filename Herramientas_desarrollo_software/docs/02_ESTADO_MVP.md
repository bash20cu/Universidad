# Estado Del MVP - TutorIA

## Resumen Ejecutivo

El MVP ya tiene una base técnica funcional y los módulos principales integrados: aplicación Flask modular, autenticación, roles, segundo factor preparado, bitácora, CRUD de estudiantes, contenidos, diagnóstico, clasificación IA, recomendaciones, reportes y administración básica de usuarios.

## Matriz De Alcance

| ID | Requisito | Estado | Evidencia | Falta para completar |
|---|---|---|---|---|
| MVP-01 | Gestión de usuarios | Hecho inicial | Modelo `User`, seed, login y CRUD básico para administrador. | Validar flujo completo de demo. |
| MVP-02 | Roles administrador/docente/estudiante | Hecho | `User.role` y decorador `roles_required`. | Ampliar pruebas al agregar nuevas rutas. |
| MVP-03 | Autenticación usuario/contraseña | Hecho | Flask-Login y hash de contraseña. | Endurecer mensajes y documentación final. |
| MVP-04 | Segundo factor por correo con Resend | Parcial | Servicio 2FA y Resend preparado. | Probar con API key real. |
| MVP-05 | Bitácora de transacciones | Hecho | `AuditLog` registra auth, CRUD, evaluación, recomendaciones y reportes. | Revisar evidencias finales. |
| MVP-06 | Gestión de estudiantes | Hecho | CRUD visual de estudiantes. | Añadir relaciones con evaluaciones reales. |
| MVP-07 | Gestión de contenidos educativos | Hecho | Modelo, seed, CRUD visual, permisos y bitácora. | Integrar contenidos con recomendaciones después de evaluación. |
| MVP-08 | Evaluación diagnóstica | Hecho inicial | Flujo `/diagnostics` registra estudiante, preguntas y respuestas. | Conectar clasificación con IA y recomendaciones. |
| MVP-09 | Envío de respuestas a IA | Hecho inicial | Servicio de diagnóstico usa proveedor IA abstracto. | Validar con Foundation Models real en demo. |
| MVP-10 | Clasificación básico/intermedio/avanzado | Hecho inicial | Contrato JSON validado y persistido en evaluación/estudiante. | Probar con modelo real y ajustar prompt si hace falta. |
| MVP-11 | Recomendación de contenidos | Hecho inicial | Servicio explicable, persistencia y vista por estudiante. | Validar con más combinaciones de temas. |
| MVP-12 | Reportes básicos | Hecho inicial | Reporte general e individual con nivel, evaluaciones y recomendaciones. | Preparar capturas y evidencia. |
| MVP-13 | README | Hecho | `mvp_minimo/README.md`. | Actualizar conforme avancen módulos. |
| MVP-14 | `.env.example` | Hecho | Variables documentadas. | Confirmar valores finales de Resend/FM. |
| MVP-15 | Pruebas iniciales | Hecho | `21 passed`, incluyendo usuarios, recomendaciones y reportes. | Ejecutar evidencia funcional en equipo compatible. |

## Estado Funcional Actual

- El usuario puede iniciar sesión con 2FA.
- El administrador y docente pueden gestionar estudiantes.
- El administrador puede eliminar estudiantes sin evaluaciones.
- El chat IA puede consultar proveedor Foundation Models por streaming.
- El dashboard muestra métricas generales.
- El administrador y docente pueden gestionar contenidos educativos.
- El administrador y docente pueden registrar evaluaciones diagnósticas con respuestas.
- El chat renderiza Markdown básico de forma segura para mejorar legibilidad de respuestas IA.
- El chat muestra evidencia visible de ejecución local: Flask, endpoint del modelo e invocación `fm serve`.
- La base de datos se inicializa con usuarios, contenidos y preguntas semilla.
- El personal autorizado puede consultar rutas de aprendizaje y reportes de progreso.
- El administrador puede crear y editar usuarios sin guardar contraseñas en texto plano.

## Brecha Principal

El flujo funcional principal ya está integrado. Los pendientes restantes son validación real de servicios, revisión visual final, evidencias, UML e informe académico.
