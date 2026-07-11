# Instrucciones Para Agentes - TutorIA

Este repositorio contiene el proyecto universitario **TutorIA**, una herramienta web de tutor inteligente adaptativo. Cualquier agente o desarrollador que trabaje aquí debe priorizar claridad académica, trazabilidad y código defendible para revisión docente.

## Reglas Generales

- Trabajar sobre el MVP ubicado en `mvp_minimo` salvo que el usuario indique otra ruta.
- Mantener la arquitectura modular existente: rutas en `app/routes`, lógica de negocio en `app/services`, modelos en `app/models`, formularios en `app/forms`, vistas en `app/templates` y recursos en `app/static`.
- No mezclar lógica de negocio compleja dentro de rutas o templates.
- No usar OpenAI API ni otros servicios pagos, excepto Resend para el segundo factor por correo.
- Mantener Foundation Models y cualquier proveedor futuro detrás de una abstracción común.
- No asumir que el usuario, la aplicación y el modelo de IA siempre se ejecutan en el mismo equipo.
- Ejecutar pruebas antes de cerrar cambios funcionales importantes.
- Actualizar `docs/00_REGISTRO_SEGUIMIENTO.md` después de cada bloque relevante de trabajo.

## Comentarios En Código

- Todo comentario nuevo debe estar en español.
- El código debe ser didáctico porque será revisado como proyecto universitario.
- Comentar la lógica importante: reglas de negocio, validaciones, seguridad, 2FA, IA, persistencia, decisiones de arquitectura y manejo de errores.
- Evitar comentarios mecánicos que repitan lo obvio, por ejemplo `# asigna el valor a la variable`.
- Preferir comentarios breves antes de bloques importantes, no comentarios ruidosos en cada línea simple.
- Cuando una función tenga una regla académica o de seguridad relevante, explicar el motivo de esa regla.

## Estilo De Implementación

- Priorizar código claro sobre abstracciones innecesarias.
- Usar nombres consistentes con el código existente.
- Mantener formularios protegidos con CSRF.
- Respetar roles: administrador, docente y estudiante.
- Registrar en bitácora acciones importantes: creación, edición, eliminación, login, 2FA, evaluaciones y recomendaciones.
- Validar entradas del usuario desde formularios o servicios antes de persistir.
- No guardar secretos en el repositorio; usar `.env.example` para documentar variables.

## Documentación Esperada

- Cada avance importante debe quedar reflejado en `docs/00_REGISTRO_SEGUIMIENTO.md`.
- Riesgos nuevos o modificados deben registrarse en `docs/03_RIESGOS.md`.
- Si se completa un requisito del MVP, actualizar `docs/02_ESTADO_MVP.md`.
- Si una decisión afecta arquitectura, crear o actualizar un ADR o la documentación de arquitectura.
