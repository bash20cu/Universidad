# Estado Del MVP - TutorIA

## Resumen Ejecutivo

El MVP ya tiene una base técnica funcional: aplicación Flask modular, autenticación, roles, segundo factor preparado, bitácora, CRUD de estudiantes, chat con proveedor IA y pruebas iniciales. El trabajo restante se concentra en completar los módulos educativos: contenidos, evaluación diagnóstica, clasificación, recomendaciones y reportes.

## Matriz De Alcance

| ID | Requisito | Estado | Evidencia | Falta para completar |
|---|---|---|---|---|
| MVP-01 | Gestión de usuarios | Parcial | Modelo `User`, seed y login. | CRUD básico para administrador. |
| MVP-02 | Roles administrador/docente/estudiante | Hecho | `User.role` y decorador `roles_required`. | Ampliar pruebas al agregar nuevas rutas. |
| MVP-03 | Autenticación usuario/contraseña | Hecho | Flask-Login y hash de contraseña. | Endurecer mensajes y documentación final. |
| MVP-04 | Segundo factor por correo con Resend | Parcial | Servicio 2FA y Resend preparado. | Probar con API key real. |
| MVP-05 | Bitácora de transacciones | Hecho parcial | `AuditLog` y eventos de auth/estudiantes. | Registrar eventos de contenidos, evaluación y reportes. |
| MVP-06 | Gestión de estudiantes | Hecho | CRUD visual de estudiantes. | Añadir relaciones con evaluaciones reales. |
| MVP-07 | Gestión de contenidos educativos | Hecho | Modelo, seed, CRUD visual, permisos y bitácora. | Integrar contenidos con recomendaciones después de evaluación. |
| MVP-08 | Evaluación diagnóstica | Hecho inicial | Flujo `/diagnostics` registra estudiante, preguntas y respuestas. | Conectar clasificación con IA y recomendaciones. |
| MVP-09 | Envío de respuestas a IA | Hecho inicial | Servicio de diagnóstico usa proveedor IA abstracto. | Validar con Foundation Models real en demo. |
| MVP-10 | Clasificación básico/intermedio/avanzado | Hecho inicial | Contrato JSON validado y persistido en evaluación/estudiante. | Probar con modelo real y ajustar prompt si hace falta. |
| MVP-11 | Recomendación de contenidos | Pendiente | Contenidos tienen nivel. | Crear servicio de recomendación. |
| MVP-12 | Reportes básicos | Parcial | Dashboard con conteos. | Reportes por estudiante, evaluaciones y contenidos recomendados. |
| MVP-13 | README | Hecho | `mvp_minimo/README.md`. | Actualizar conforme avancen módulos. |
| MVP-14 | `.env.example` | Hecho | Variables documentadas. | Confirmar valores finales de Resend/FM. |
| MVP-15 | Pruebas iniciales | Hecho | `10 passed`. | Agregar pruebas por módulo nuevo. |

## Estado Funcional Actual

- El usuario puede iniciar sesión con 2FA.
- El administrador y docente pueden gestionar estudiantes.
- El administrador puede eliminar estudiantes sin evaluaciones.
- El chat IA puede consultar proveedor Foundation Models por streaming.
- El dashboard muestra métricas generales.
- El administrador y docente pueden gestionar contenidos educativos.
- El administrador y docente pueden registrar evaluaciones diagnósticas con respuestas.
- La base de datos se inicializa con usuarios, contenidos y preguntas semilla.

## Brecha Principal

El MVP ya registra datos educativos, respuestas diagnósticas y clasificación IA estructurada. La brecha principal ahora es usar el nivel clasificado para recomendar contenidos y reportar progreso.
