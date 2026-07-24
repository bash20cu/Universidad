# Guion de presentación y demostración - TutorIA

## Duración sugerida: 8 a 10 minutos

### 1. Contexto y problema - 1 minuto

Explicar que TutorIA centraliza diagnóstico, clasificación, contenidos y
seguimiento para que el docente pueda acompañar a cada estudiante.

### 2. Arquitectura - 1 minuto

Mostrar Flask, SQLite/SQLAlchemy, servicios de dominio, Bootstrap local y la
abstracción de proveedores IA. Aclarar que NVIDIA es el proveedor principal y
Foundation Models es el fallback local.

### 3. Registro y seguridad - 1 minuto

Registrar un estudiante nuevo, escanear el QR con Google Authenticator, confirmar
el código y mostrar que el acceso posterior exige TOTP. Enseñar brevemente la
bitácora del administrador.

### 4. Gestión académica - 2 minutos

Como docente, crear o editar un estudiante, administrar una pregunta y registrar
un contenido con tema, nivel, competencia, tipo, enlace y estado.

### 5. Evaluación e IA - 2 minutos

Crear una evaluación, responder las preguntas, solicitar clasificación y mostrar
el nivel, explicación, proveedor, modelo y fecha. Enseñar cómo se generan las
recomendaciones según nivel y área de interés.

### 6. Reportes - 1 minuto

Mostrar los indicadores generales, distribución por nivel, progreso porcentual y
reporte individual del estudiante.

### 7. Cierre - 1 minuto

Mencionar que el MVP cubre los requisitos principales, registra trazabilidad y
queda preparado para códigos de recuperación, exportación avanzada y despliegue
productivo.

## Evidencias que deben capturarse

1. Registro con QR TOTP.
2. Login con código de Google Authenticator.
3. Panel por rol.
4. CRUD de estudiantes.
5. CRUD de preguntas.
6. CRUD de contenidos enriquecidos.
7. Evaluación antes y después de clasificar con IA.
8. Reporte general e individual.
9. Bitácora administrativa.
10. Centro de control PySide6 con servicios y logs.

