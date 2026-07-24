# Riesgos Del Proyecto TutorIA

## Matriz De Riesgos

| ID | Riesgo | Categoría | Probabilidad | Impacto | Mitigación | Estado |
|---|---|---|---|---|---|---|
| R-001 | Foundation Models no disponible en la máquina de demostración. | Técnico | Media | Alto | Mantener Ollama como respaldo documentado y validar estado antes de demo. | Abierto |
| R-002 | El canal de correo 2FA no está disponible o requiere configuración externa. | Técnico | Baja | Bajo | Se elimina del MVP; se utiliza TOTP local con código QR y aplicación autenticadora. | Cerrado |
| R-003 | Alcance demasiado grande para dos integrantes. | Proyecto | Alta | Alto | Priorizar MVP, evitar funciones fuera de alcance y registrar pendientes. | Abierto |
| R-004 | Se programa sin actualizar documentación. | Académico | Media | Alto | Actualizar `00_REGISTRO_SEGUIMIENTO.md` después de cada bloque. | Abierto |
| R-005 | La IA devuelve respuestas no estructuradas o ambiguas. | Técnico | Alta | Alto | Implementado contrato JSON con validación; falta probar modelo real. | Mitigado parcial |
| R-006 | SQLite se interpreta como insuficiente para producción. | Académico | Media | Medio | Justificar SQLite como decisión de desarrollo/MVP y documentar migración futura. | Abierto |
| R-007 | Falta de pruebas en módulos nuevos. | Calidad | Media | Alto | Crear pruebas pytest junto con cada ruta/servicio nuevo. | Abierto |
| R-008 | Comentarios excesivos reducen legibilidad. | Calidad | Media | Medio | Comentar lógica importante, no líneas obvias. | Abierto |
| R-009 | El equipo pierde trazabilidad de responsabilidades. | Proyecto | Media | Medio | Usar bitácora y plan de sprints por responsable. | Abierto |
| R-010 | El informe Word queda desalineado del código real. | Académico | Alta | Alto | Actualizar documentación técnica antes de pasar al informe final. | Abierto |

## Riesgos Cerrados O Controlados

| ID | Riesgo | Resultado |
|---|---|---|
| RC-001 | Dependencia de CDN para Bootstrap. | Controlado: Bootstrap está versionado localmente. |
| RC-002 | Aplicación acoplada directamente a Foundation Models en rutas Flask. | Controlado parcialmente: existe contrato `ChatProvider`. |

## Criterios De Escalamiento

Un riesgo debe escalarse cuando:

- Bloquea una entrega del MVP.
- Impide una demostración en clase.
- Requiere decisión del profesor.
- Cambia el alcance aprobado.
- Obliga a sustituir una tecnología del stack.
