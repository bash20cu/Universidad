# Guía breve para la exposición de TutorIA

## 1. ¿Qué es TutorIA?

TutorIA es una plataforma educativa web que permite a estudiantes completar su
perfil, responder evaluaciones diagnósticas y consultar su progreso. Los docentes
administran estudiantes, preguntas y contenidos; los administradores gestionan
usuarios, permisos y auditoría.

## 2. Arquitectura general

La aplicación utiliza una arquitectura modular basada en Flask:

```text
Navegador web
    |
    | HTTP / formularios / JavaScript / SSE
    v
Aplicación Flask
    |
    +-- Rutas y control de roles
    +-- Formularios con validación y CSRF
    +-- Servicios de chat, diagnóstico, auditoría y 2FA
    +-- Modelos SQLAlchemy
    |
    +--> SQLite
    |
    +--> Proveedor IA abstracto
             |
             +--> NVIDIA NIM, proveedor principal remoto
             +--> Foundation Models, fallback local
```

La interfaz no depende directamente de NVIDIA ni de Foundation Models. Las rutas
utilizan el contrato común `ChatProvider`, lo que permite cambiar de proveedor,
probar con dobles controlados y activar el fallback sin modificar la lógica del
negocio.

## 3. Componentes principales

| Componente | Responsabilidad |
|---|---|
| `app/routes/` | Atiende autenticación, estudiantes, contenidos, diagnósticos, chat, reportes y usuarios. |
| `app/models/` | Define usuarios, estudiantes, contenidos, evaluaciones, respuestas, recomendaciones y auditoría. |
| `app/services/` | Centraliza reglas de chat, diagnóstico, correo, auditoría y segundo factor. |
| `ai_provider.py` | Define la abstracción de proveedores y el fallback NVIDIA → Foundation Models. |
| `app/templates/` | Renderiza la interfaz web mediante Jinja2. |
| `app/static/` | Contiene estilos y JavaScript del navegador. |
| `tests/test_app.py` | Pruebas unitarias, integración y funcionales del backend. |
| `tests/test_selenium_ui.py` | Pruebas funcionales end-to-end en Chrome remoto. |

## 4. Flujo por roles

### Estudiante

1. Se registra o inicia sesión.
2. Confirma el segundo factor TOTP.
3. Completa su perfil académico.
4. Responde todas las preguntas de la evaluación.
5. Consulta su progreso y recomendaciones.

### Docente

1. Inicia sesión con TOTP.
2. Administra estudiantes y contenidos.
3. Mantiene el banco de preguntas.
4. Revisa evaluaciones.
5. Solicita la clasificación mediante IA.
6. Consulta recomendaciones y reportes.

### Administrador

1. Administra usuarios y roles.
2. Revisa la bitácora de auditoría.
3. Puede gestionar contenidos y estudiantes.
4. Supervisa la seguridad y la configuración general.

## 5. Seguridad implementada

- Contraseñas almacenadas mediante hash.
- Segundo factor TOTP para las cuentas.
- Protección CSRF en formularios.
- Control de roles en el backend.
- Auditoría de acciones relevantes.
- Escape automático de contenido HTML para reducir XSS.
- Validación de historial y roles de mensajes del chat.
- Protección para no eliminar contenidos con recomendaciones asociadas.
- Evidencias Selenium sanitizadas: no contienen QR, secretos TOTP ni códigos.

## 6. Estrategia de pruebas

La solución se valida en varias capas:

| Nivel | Qué demuestra |
|---|---|
| Unitaria | Reglas aisladas como normalización de mensajes y TOTP. |
| Integración | Flask, SQLite, proveedores simulados, streaming y persistencia. |
| Funcional | Recorridos completos de autenticación, roles, evaluaciones, IA y reportes. |
| End-to-end | Navegación real en Chrome remoto mediante Selenium Grid. |
| Seguridad | CSRF, permisos, hash, XSS, duplicados y trazabilidad. |
| Responsive | Comprobación de vistas en viewport móvil. |

## 7. Resultado actual

La ejecución completa más reciente terminó con:

```text
43 passed in 31.53s
```

La distribución corresponde a:

- 34 pruebas backend.
- 9 pruebas Selenium end-to-end.
- 46 capturas PNG y 46 HTML generados como evidencia.

Las pruebas Selenium pueden observarse en tiempo real mediante:

- Grid: `http://localhost:4444/ui`
- noVNC: `http://localhost:7900`

## 8. Comandos para demostrarlo

Desde `mvp_minimo`:

```bash
cd /Volumes/projects/proyectos/UIA/Herramientas_desarrollo_software/mvp_minimo
```

Backend, mostrando cada prueba:

```bash
.venv/bin/python -m pytest tests/test_app.py -vv -s --tb=short
```

Suite completa con Selenium activo:

```bash
SELENIUM_REMOTE_URL=http://localhost:4444/wd/hub \
SELENIUM_BASE_URL=http://host.docker.internal:5050 \
DATABASE_URL=sqlite:////private/tmp/tutoria_selenium.db \
RUN_SELENIUM=1 \
.venv/bin/python -m pytest -vv -s --tb=short
```

## 9. Respuestas rápidas para preguntas del profesor

### ¿Por qué usar una abstracción para IA?

Para evitar que la lógica del sistema dependa de un proveedor específico. Así se
puede utilizar NVIDIA como proveedor principal, Foundation Models como respaldo
y proveedores simulados durante las pruebas.

### ¿La IA decide directamente el nivel del estudiante?

La IA propone una clasificación estructurada. El sistema valida la respuesta,
guarda la evidencia y actualiza el nivel únicamente cuando la respuesta cumple el
formato esperado.

### ¿Cómo se garantiza que un estudiante no vea información administrativa?

Cada ruta protegida valida el rol en el backend. Ocultar un enlace en la interfaz
no es la medida de seguridad principal; el servidor rechaza el acceso no
autorizado.

### ¿Qué pasa si NVIDIA no está disponible?

El proveedor abstracto intenta utilizar Foundation Models como fallback. Esta
conducta está cubierta mediante proveedores controlados en las pruebas.

### ¿Por qué se utiliza una base temporal en las pruebas?

Para aislar los datos de prueba, permitir ejecuciones repetibles y evitar alterar
la base utilizada para la demostración.

### ¿Qué demuestra Selenium que no demuestra pytest con Flask?

Las pruebas backend validan rutas y lógica sin navegador. Selenium comprueba la
experiencia real: formularios, redirecciones, TOTP, JavaScript, mensajes visibles,
responsive y ejecución en Chrome remoto.

### ¿La prueba de NVIDIA utiliza una API real?

La suite reproducible no depende de credenciales reales. NVIDIA queda configurado
como proveedor principal en producción, mientras que las pruebas usan dobles
controlados para validar el contrato y el fallback sin costos ni dependencia de
Internet.

## 10. Cierre sugerido

> TutorIA separa la interfaz, la lógica de negocio, la persistencia y los
> proveedores de IA. La seguridad se valida en el backend y la funcionalidad se
> comprueba en varias capas. La ejecución completa alcanzó 43 pruebas aprobadas,
> incluyendo nueve recorridos reales en Chrome mediante Selenium Grid.
