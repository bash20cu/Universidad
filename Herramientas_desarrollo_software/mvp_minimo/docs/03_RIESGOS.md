# Riesgos y asuntos abiertos - TutorIA MVP

**Fecha de corte:** 2026-08-05

| ID | Riesgo / asunto | Impacto | Probabilidad | Estado | Mitigación o siguiente acción |
|---|---|---:|---:|---|---|
| R-01 | Los documentos derivados todavía pueden conservar el orden histórico de proveedores. | Alto | Media | **En corrección** | Actualizar ADR, minuta, documentación técnica e informe a NVIDIA principal + Foundation Models fallback. |
| R-02 | Ollama no existe en el código del MVP. | Bajo | Baja | **Aceptado fuera del MVP** | Mantenerlo únicamente como alternativa futura, sin presentarlo como respaldo activo. |
| R-03 | La afirmación de IA privada/local depende de la configuración: NVIDIA es remoto y Foundation Models es local al dispositivo. | Alto | Alta | **Abierto** | Mostrar siempre proveedor, modelo, `access_mode` y `processing_location`; no llamar local al flujo NVIDIA. |
| R-04 | Foundation Models depende de hardware Apple Intelligence, versión del sistema, Xcode y disponibilidad de `fm`. | Alto | Media | **Parcial** | Ejecutar checklist en el equipo de exposición y conservar salida de `fm available` y versión del sistema. |
| R-05 | Las evidencias de `docs/` no cubren el flujo académico completo. | Medio | Alta | **Abierto** | Capturar registro/TOTP, roles, CRUD, diagnóstico, clasificación, recomendaciones, reportes y bitácora. |
| R-06 | La suite requiere abrir puertos locales para el servidor simulado; entornos aislados pueden reportar errores de permisos aunque el código sea correcto. | Medio | Media | **Conocido** | Ejecutar con permisos de red local o sustituir el fixture por un mock HTTP sin socket. Registrar el entorno de ejecución. |
| R-07 | NVIDIA requiere API key, conectividad y decisión sobre uso de un servicio remoto/potencialmente sujeto a límites o costos. | Alto | Media | **Abierto** | Confirmar autorización del curso, política de uso, presupuesto y que ninguna clave aparezca en Git, capturas o informe. |
| R-08 | El flujo productivo no tiene exportación PDF, códigos de recuperación ni métricas históricas. | Bajo para MVP | Alta | **Aceptado como mejora** | Mantener fuera del alcance actual y documentarlo como evolución futura. |
| R-09 | SQLite y el servidor de desarrollo no son la configuración de producción. | Medio | Alta | **Aceptado como mejora** | Documentar como limitación del MVP; migrar a PostgreSQL/WSGI en una versión posterior. |
| R-10 | El banco inicial de preguntas y contenidos puede ser insuficiente para una demostración pedagógica representativa. | Medio | Media | **Abierto** | Cargar un conjunto mínimo fijo de casos y conservarlo como datos de demostración. |
| R-11 | El flujo de evaluación estudiantil requiere perfil completado y preguntas activas; sin evidencia manual puede parecer incompleto durante la presentación. | Medio | Media | **Abierto** | Capturar una ejecución del estudiante, verificar la persistencia de respuestas y relacionarla con RF-12 y las pruebas automatizadas. |

## Riesgos cerrados o controlados

- **Códigos 2FA expuestos en persistencia:** controlado; los códigos de desafío se almacenan como hash y tienen expiración, intentos y uso único.
- **Apagado accidental de un servidor IA externo:** controlado por pruebas; el proveedor distingue procesos administrados por la aplicación de servidores externos.
- **Respuesta IA con nivel inválido:** controlado; el parser valida niveles y la prueba confirma que una respuesta inválida no actualiza la evaluación.
- **Recomendaciones inventadas por la IA:** controlado en el flujo actual; la selección consulta contenidos existentes y guarda el motivo.
