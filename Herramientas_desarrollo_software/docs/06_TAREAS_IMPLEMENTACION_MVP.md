# Tareas de implementación del MVP

Este tablero delimita el trabajo de programación y documentación de TutorIA. `caso5` no forma parte del alcance.

## Bloque funcional

- [x] Base Flask modular, SQLite y modelos SQLAlchemy.
- [x] Autenticación, roles, CSRF y segundo factor preparado.
- [x] CRUD de estudiantes y contenidos.
- [x] Evaluación diagnóstica y persistencia de respuestas.
- [x] Clasificación IA con contrato JSON validado.
- [x] Servicio explicable de recomendaciones por nivel y área de interés.
- [x] Vista de recomendaciones por estudiante.
- [x] Reporte general y reporte individual de progreso.
- [x] Administración básica de usuarios para el administrador.
- [x] Autorregistro público de usuarios con rol estudiante y auditoría.
- [x] Validación con Foundation Models real en equipo compatible.
- [x] Validación con NVIDIA real usando el proveedor principal configurado.

## Calidad y seguridad

- [x] Eventos de auditoría para usuarios, recomendaciones y reportes.
- [x] Pruebas automatizadas de usuarios, registro, recomendaciones y reportes (`22 passed`).
- [ ] Pruebas funcionales con evidencia de cada flujo.
- [ ] Revisar mensajes de error y estados vacíos.
- [ ] Confirmar que cada ruta protegida conserva CSRF y permisos por rol.

## Interfaz y experiencia

- [x] Tono de interfaz más colegial, cercano y orientado al acompañamiento.
- [x] Navegación hacia recomendaciones, reportes y usuarios.
- [x] Tarjetas de ruta de aprendizaje y reporte individual.
- [ ] Revisión visual final en escritorio y móvil.
- [ ] Capturas para el informe académico.
- [x] Centro de control PySide6 para encender/apagar FM y Flask.
- [x] Indicadores de estado, PID, puertos y logs de ejecución.
- [x] Tarjeta visible para el código 2FA únicamente en modo de desarrollo.
- [x] Lanzador `.command` para macOS.

## Documentación académica

- [x] Documento técnico de arquitectura y explicación del programa.
- [x] Documentación del centro de control PySide6.
- [x] Actualizar endpoints y alcance en README.
- [x] Crear UML de casos de uso, clases y secuencia.
- [x] Crear borrador del informe APA 7 con resultados, conclusiones y bibliografía.
- [x] Preparar presentación, guion y demo.
