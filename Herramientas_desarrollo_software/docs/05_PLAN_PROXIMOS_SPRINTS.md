# Plan De Próximos Sprints - TutorIA

## Objetivo

Completar el alcance MVP sin expandir funcionalidades fuera de lo solicitado. Cada sprint debe dejar evidencia funcional, pruebas y documentación mínima actualizada.

## Sprint 1 - Contenidos Educativos

**Estado:** Implementado el 2026-07-11.

| Campo | Detalle |
|---|---|
| Objetivo | Crear CRUD visual de contenidos educativos. |
| Entregables | Rutas, formularios, templates, permisos, bitácora y pruebas. |
| Roles | Administrador y docente gestionan; estudiante solo consulta si se habilita vista. |
| Criterio de cierre | Cumplido: crear, listar, editar y eliminar contenidos con pytest aprobado. |

## Sprint 2 - Evaluación Diagnóstica

**Estado:** Implementado como flujo inicial sin IA el 2026-07-11.

| Campo | Detalle |
|---|---|
| Objetivo | Permitir seleccionar estudiante, responder preguntas y guardar evaluación. |
| Entregables | Rutas, formulario, persistencia de respuestas y estado de evaluación. |
| Roles | Administrador/docente ejecutan evaluación para un estudiante. |
| Criterio de cierre | Cumplido: evaluación queda guardada con respuestas asociadas. |

## Sprint 3 - Clasificación Con IA

| Campo | Detalle |
|---|---|
| Objetivo | Enviar respuestas a proveedor IA y recibir nivel estructurado. |
| Entregables | `diagnostic_service`, contrato de salida, validación y pruebas con proveedor simulado. |
| Roles | Servicio interno, sin dependencia directa en rutas. |
| Criterio de cierre | Resultado clasifica básico, intermedio o avanzado y se persiste. |

## Sprint 4 - Recomendaciones

| Campo | Detalle |
|---|---|
| Objetivo | Recomendar contenidos según nivel y tema. |
| Entregables | `recommendation_service`, vista de recomendaciones y bitácora. |
| Roles | Administrador/docente consultan recomendaciones por estudiante. |
| Criterio de cierre | Se muestran contenidos recomendados después de evaluación. |

## Sprint 5 - Reportes Básicos

| Campo | Detalle |
|---|---|
| Objetivo | Mostrar progreso por estudiante y conteos de evaluaciones. |
| Entregables | Reporte de nivel actual, cantidad de evaluaciones y contenidos recomendados. |
| Roles | Administrador/docente consultan. |
| Criterio de cierre | Dashboard o sección de reportes muestra datos reales. |

## Sprint 6 - Usuarios Y Resend Real

| Campo | Detalle |
|---|---|
| Objetivo | Completar administración básica de usuarios y validar envío real 2FA. |
| Entregables | CRUD simple de usuarios, prueba Resend y documentación `.env`. |
| Roles | Solo administrador. |
| Criterio de cierre | Usuario puede recibir código 2FA por correo real. |

## Orden De Trabajo Recomendado

1. CRUD de contenidos.
2. Evaluación diagnóstica sin IA.
3. Clasificación IA con proveedor simulado.
4. Integración con Foundation Models para diagnóstico.
5. Recomendaciones.
6. Reportes.
7. Usuarios.
8. Resend real.
9. Documentación final del informe.

## Regla De Cierre Por Sprint

Un sprint no se considera cerrado hasta cumplir:

- Funcionalidad visible o servicio probado.
- Pruebas automatizadas nuevas o actualizadas.
- Bitácora actualizada.
- Estado MVP actualizado si corresponde.
- Riesgos actualizados si aparece un bloqueo.
