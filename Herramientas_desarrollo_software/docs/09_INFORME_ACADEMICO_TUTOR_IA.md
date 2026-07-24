# Informe académico - TutorIA

## Portada

**Universidad Internacional de las Américas**  
**Curso:** Herramientas para el Desarrollo de Sistemas de Información  
**Proyecto:** TutorIA, tutor inteligente adaptativo mediante IA  
**Integrantes:** Miguel Alejandro Fernández Arteaga y Roberto José Rojas García  
**Tema:** 4  
**Fecha:** 2026

## Resumen

TutorIA es una aplicación web académica que apoya el diagnóstico del nivel de
estudiantes, la clasificación de competencias y la recomendación de contenidos
educativos. El sistema utiliza Flask, SQLite, SQLAlchemy, autenticación con
roles y TOTP, además de una arquitectura de proveedores IA que permite usar
NVIDIA como proveedor principal y Foundation Models como alternativa local.

## 1. Introducción

El problema abordado es la dificultad de dar seguimiento individualizado al
progreso académico cuando las evaluaciones, contenidos y recomendaciones se
gestionan de forma aislada. TutorIA integra esos procesos en un flujo
demostrable para estudiantes, docentes y administradores.

## 2. Objetivos

### Objetivo general

Desarrollar un MVP web para diagnosticar el nivel académico de estudiantes y
recomendar contenidos educativos mediante inteligencia artificial.

### Objetivos específicos

1. Implementar usuarios, roles, autorregistro y autenticación TOTP.
2. Registrar estudiantes, preguntas, respuestas y evaluaciones diagnósticas.
3. Clasificar respuestas con un proveedor IA y guardar la trazabilidad técnica.
4. Gestionar contenidos por tema, nivel, competencia y tipo de material.
5. Presentar reportes de progreso y recomendaciones explicables.
6. Registrar transacciones relevantes en una bitácora auditable.

## 3. Metodología y alcance

Se utilizó un enfoque incremental orientado a MVP. El caso 5 queda fuera del
alcance. La aplicación se diseñó para ejecución local y demostración académica;
SQLite se utiliza como base de datos de desarrollo y el acceso IA se abstrae
para permitir NVIDIA y Foundation Models.

## 4. Arquitectura y tecnologías

La solución se organiza en rutas Flask, formularios WTForms, modelos SQLAlchemy,
servicios de dominio, plantillas Jinja2 y Bootstrap local. La arquitectura
completa y los diagramas UML se encuentran en `docs/01_ARQUITECTURA_TUTORIA.md`
y `docs/08_UML_TUTOR_IA.md`.

## 5. Resultados

El MVP implementa gestión de usuarios, roles, TOTP, bitácora, estudiantes,
contenidos, preguntas, evaluaciones, clasificación IA, recomendaciones y
reportes. La suite automatizada valida autenticación, autorización, CRUD,
diagnóstico, recomendaciones, reportes y registro con TOTP.

> Completar antes de entregar: cantidad final de casos ejecutados, proveedor IA
> utilizado en la demostración, capturas numeradas y resultado exacto de `pytest`.

## 6. Discusión

La separación entre proveedor IA y lógica de negocio permite cambiar NVIDIA por
Foundation Models sin modificar el flujo académico. La validación del JSON y la
regla de evidencia mínima reducen el riesgo de aceptar clasificaciones sin
respaldo en las respuestas escritas.

## 7. Limitaciones y trabajo futuro

- SQLite y el servidor Flask integrado son apropiados para el MVP, no para
  producción multiusuario.
- Se deben agregar códigos de recuperación para TOTP.
- Los reportes pueden evolucionar hacia series históricas y exportación PDF.
- La clasificación IA requiere validación con datos reales y revisión docente.

## 8. Conclusiones

TutorIA demuestra que un flujo académico integrado puede construirse con una
arquitectura modular, trazable y adecuada para una demostración universitaria.
El MVP cubre los requisitos funcionales principales y deja identificadas las
mejoras necesarias para una versión productiva.

## 9. Referencias sugeridas en APA 7

- Flask Documentation. (2026). *Flask documentation*. https://flask.palletsprojects.com/
- SQLAlchemy Documentation. (2026). *SQLAlchemy documentation*. https://docs.sqlalchemy.org/
- PyOTP Documentation. (2026). *PyOTP: Python One-Time Password Library*. https://pyauth.github.io/pyotp/
- National Institute of Standards and Technology. (2017). *Digital identity guidelines: Authentication and lifecycle management* (SP 800-63B). https://pages.nist.gov/800-63-3/sp800-63b.html

