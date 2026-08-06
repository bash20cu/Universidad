# Inventario de documentación y evidencias

**Fecha de corte:** 2026-08-05

## Archivos existentes en `docs/`

| Archivo | Qué demuestra | Estado |
|---|---|---|
| `evidencias/00_inicio.png` | Vista visual de la pantalla inicial de TutorIA con acceso y creación de cuenta. | Disponible; evidencia visual revisada. |
| `evidencias/00_inicio_snapshot.yml` | Árbol accesible de la pantalla inicial: marca, formulario de usuario/contraseña, botón de continuar y enlace de registro. | Disponible; coincide con la pantalla inicial. |
| `evidencias/01_login_totp_snapshot.yml` | Pantalla del segundo factor TOTP con campo de código de seis dígitos y botón de verificación. | Disponible; evidencia estructural, sin captura PNG asociada. |

## Lo que actualmente no está documentado en `docs/`

- Instalación y ejecución reproducible en macOS.
- Ejecución en Windows con NVIDIA.
- Registro de usuarios y activación TOTP mediante QR.
- Inicio de sesión completo: contraseña, TOTP y cierre de sesión.
- Separación de permisos entre administrador, docente y estudiante.
- CRUD de estudiantes y contenidos.
- Gestión del banco de preguntas.
- Creación de una evaluación con todas sus respuestas.
- Clasificación diagnóstica con IA y validación del resultado.
- Recomendaciones trazables según nivel y área de interés.
- Reportes generales e individuales.
- Consulta de bitácora por el administrador.
- Estado del proveedor, ubicación de procesamiento, modo de acceso y fallback.
- Evidencia de que un proceso externo de IA no se apaga por error.
- Evidencia de pruebas automatizadas y su resultado.
- Evidencia de la página pública de ayuda y su navegación por rol.
- Evidencia de un estudiante respondiendo una evaluación diagnóstica.
- Comparación repetible entre proveedores.
- Evidencia de configuración sin secretos expuestos.

## Observaciones sobre las evidencias existentes

- El snapshot `00_inicio_snapshot.yml` termina sin salto de línea visible antes del separador; conviene regenerarlo con una herramienta de captura para mantenerlo legible.
- El snapshot de TOTP demuestra la interfaz, pero no el resultado exitoso ni el rechazo de códigos inválidos, expirados o reutilizados.
- La captura inicial no prueba el flujo posterior ni la adaptación por rol.
- No se encontraron documentos Markdown de seguimiento, estado o riesgos dentro de `docs/` antes de esta revisión.

## Evidencias mínimas recomendadas para cerrar la entrega

1. Captura o snapshot por cada flujo principal: registro/TOTP, estudiante, docente, administrador, diagnóstico, recomendaciones y reporte.
2. Un registro de ejecución de `pytest` con el resultado de la suite.
3. Un caso documentado de clasificación válida y uno de respuesta IA inválida.
4. Un caso de fallback o indisponibilidad del proveedor, indicando proveedor y ubicación de procesamiento.
5. Una matriz que relacione requisito, ruta/código, prueba y evidencia.
