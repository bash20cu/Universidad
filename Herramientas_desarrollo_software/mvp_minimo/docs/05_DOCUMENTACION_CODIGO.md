# Documentación del código TutorIA

## Alcance de la revisión

El código del MVP fue revisado para que una persona del equipo pueda explicar
su funcionamiento durante la demostración y continuar el mantenimiento sin
tener que inferir reglas importantes desde las rutas o las plantillas.

La revisión se enfocó en el código propio del proyecto. No se modifican ni se
documentan línea por línea las dependencias instaladas en `.venv` ni Bootstrap.

## Organización por responsabilidad

| Ubicación | Responsabilidad | Qué debe explicarse allí |
|---|---|---|
| `app/__init__.py` | Factoría Flask, configuración, proveedores, esquema y datos demo | Selección NVIDIA + Foundation Models, migración compatible y datos reproducibles |
| `app/models/` | Entidades persistentes y relaciones SQLAlchemy | Propósito de cada tabla, campos de trazabilidad y relaciones |
| `app/forms/` | Validación de entradas y protección CSRF | Restricciones de longitud, formato, rango y opciones válidas |
| `app/routes/` | Control HTTP, permisos y navegación | Rol requerido, flujo de pantalla, persistencia y bitácora |
| `app/services/` | Reglas de negocio reutilizables | Clasificación, recomendaciones, chat, auditoría y segundo factor |
| `ai_provider.py` y `fm_server.py` | Abstracción y ciclo de vida de IA | Contrato común, ubicación del procesamiento, errores y fallback |
| `runtime_manager.py` y `desktop_launcher.py` | Arranque y cierre local | Procesos propios frente a procesos externos y limpieza segura |
| `tests/` | Evidencia automatizada | Qué requisito verifica cada grupo de pruebas y qué proveedor se simula |

## Convención de documentación

- Cada módulo tiene un docstring que describe su responsabilidad.
- Cada clase pública tiene un docstring con su propósito dentro del dominio.
- Cada función pública tiene un docstring en español que explica su resultado,
  no solo el nombre de la operación.
- Los comentarios se reservan para decisiones que no son obvias: seguridad,
  autorización, trazabilidad, persistencia, proveedor IA y compatibilidad de
  esquema.
- No se agregan comentarios mecánicos que repitan una asignación o una llamada.
- Los secretos, códigos TOTP y respuestas sensibles no se escriben en logs,
  capturas ni documentación de ejecución.

## Reglas importantes que el código deja documentadas

1. NVIDIA NIM es el proveedor principal cuando existe `NVIDIA_API_KEY`; Apple
   Foundation Models queda detrás del mismo contrato como respaldo local.
2. La aplicación muestra proveedor, modelo, modo de acceso y ubicación del
   procesamiento para no confundir un servicio remoto con uno local.
3. Las contraseñas y códigos de segundo factor se almacenan como hashes; el
   código temporal se marca como usado después del primer acierto.
4. El estudiante solo puede consultar y modificar su propio perfil y progreso.
5. Cada respuesta se asocia mediante el id persistente de la pregunta y de la
   evaluación, de modo que el resultado sea auditable.
6. La clasificación IA se valida como JSON y se aplica una evidencia mínima:
   respuestas demasiado cortas no pueden producir un nivel superior a básico.
7. Las recomendaciones se calculan con reglas explicables y se relacionan con
   la evaluación que las originó.
8. La bitácora registra operaciones relevantes sin guardar contraseñas, secretos
   TOTP ni claves de proveedores.

## Verificación realizada

La revisión estática confirmó que las clases y funciones de la aplicación
cuentan con docstrings. El módulo de pruebas también documenta su propósito,
los servidores simulados, los proveedores controlados y sus helpers.

La validación funcional debe ejecutarse desde `mvp_minimo`:

```bash
.venv/bin/python -m pytest -q
```

Para la ronda UI se requiere Selenium Grid activo y las variables descritas en
`docs/04_PRUEBAS_UI_SELENIUM.md`.

