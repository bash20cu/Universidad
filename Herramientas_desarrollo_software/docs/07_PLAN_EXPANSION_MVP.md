# Plan de expansión del MVP - TutorIA

## Propósito

Cerrar las brechas funcionales identificadas al comparar el MVP con el
enunciado oficial del Tema 4. El plan prioriza las funciones que el profesor
puede comprobar directamente durante la demostración.

## Fase 1 - Portal del estudiante

- [x] Vincular la cuenta `User` con un perfil `Student`.
- [x] Permitir que el estudiante complete y edite su información académica.
- [x] Crear un panel privado para consultar su nivel, evaluaciones,
  recomendaciones y progreso.
- [x] Evitar que un estudiante vea métricas o información de otros estudiantes.

## Fase 2 - Trazabilidad administrativa

- [x] Crear un visor de bitácora para el administrador.
- [x] Mostrar usuario, acción, entidad, detalle, IP y fecha.
- [x] Mantener el registro protegido por rol administrador.

## Fase 3 - Diagnóstico y contenido

- [x] Administrar preguntas diagnósticas desde la interfaz.
- [x] Agregar tipo de material, enlace y estado al repositorio educativo.
- [x] Guardar proveedor, modelo y fecha de clasificación IA.
- [x] Preparar compatibilidad de esquema SQLite para las columnas TOTP.
- [x] Integrar activación TOTP gratuita con QR y validación de código.
- [x] Exigir la activación TOTP directamente durante el autorregistro.

## Fase 4 - Reportes y calidad

- [x] Mejorar el reporte con indicadores de progreso y visualizaciones simples.
- [ ] Agregar pruebas funcionales de cada rol.
- [x] Crear matriz de capturas y primeras evidencias visuales para el informe académico.
- [ ] Revisar accesibilidad y visualización móvil.
- [ ] Agregar códigos de recuperación para cuentas con TOTP.

## Criterio de cierre de esta iteración

La Fase 1 y la Fase 2 deben quedar funcionales, protegidas por permisos,
documentadas y cubiertas por pruebas automatizadas. Las fases posteriores
permanecen como trabajo planificado para no introducir migraciones improvisadas
en la base SQLite de demostración.
