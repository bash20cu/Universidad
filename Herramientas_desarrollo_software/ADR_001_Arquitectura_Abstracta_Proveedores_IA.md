# ADR-001: Arquitectura abstracta para proveedores de IA

**Estado:** Aceptada para la implementación del MVP  
**Fecha:** 5 de agosto de 2026  
**Proyecto:** TutorIA

## Contexto

El MVP utiliza NVIDIA NIM como proveedor principal y Apple Foundation Models mediante `fm serve` como respaldo local. Sin embargo,
TutorIA no debe asumir que:

- el usuario siempre trabaja en la misma computadora que ejecuta el modelo;
- el proveedor siempre es Foundation Models;
- la inferencia siempre ocurre en el dispositivo;
- el servidor de IA siempre es iniciado por la aplicación;
- todos los despliegues tienen macOS o Apple Intelligence.

El proyecto debe admitir variantes locales, en red institucional y remotas sin
reescribir las rutas Flask ni la lógica educativa.

## Decisión

La aplicación dependerá de un contrato abstracto `ChatProvider` o `AIProvider`.
Las implementaciones concretas encapsularán SDK, transporte, autenticación,
disponibilidad, streaming y ciclo de vida.

Contrato mínimo:

```python
class ChatProvider(Protocol):
    def status(self) -> ProviderStatus: ...
    def ensure_ready(self) -> ProviderStatus: ...
    def stream_chat(self, messages): ...
    def shutdown(self) -> None: ...
```

NVIDIA NIM y Foundation Models serán implementaciones, no dependencias directas
del dominio. Ollama, un servicio institucional u otro proveedor remoto podrán
implementarse con el mismo contrato en una evolución posterior, pero no forman
parte del respaldo activo del MVP actual.

### Orden de proveedores aprobado para el MVP

1. NVIDIA NIM se utiliza cuando `AI_PRIMARY_PROVIDER=nvidia` y existe
   `NVIDIA_API_KEY`.
2. Foundation Models se utiliza como fallback local cuando NVIDIA no está
   disponible o devuelve un error.
3. El resultado registra proveedor, modelo, modo de acceso y ubicación de
   procesamiento.
4. En Windows se utiliza NVIDIA; Foundation Models requiere macOS compatible.

## Principios obligatorios

1. Las rutas y servicios de negocio no importarán SDK específicos.
2. La ubicación del usuario será independiente de la ubicación del modelo.
3. `access_mode` distinguirá acceso local, LAN o remoto.
4. `processing_location` distinguirá dispositivo, nube privada o remoto.
5. Solo se apagará un proceso de IA si la instancia de TutorIA es su propietaria.
6. La persistencia registrará proveedor, modelo y ubicación de procesamiento.
7. El frontend no afirmará que el procesamiento es local sin consultar el estado.
8. El fallback entre proveedores se implementará fuera de las rutas Flask.
9. Los mensajes y resultados usarán contratos comunes validados.
10. Las pruebas utilizarán proveedores simulados además de integraciones reales.

## Ciclo de vida

Para proveedores administrados por la aplicación:

- verificar salud antes de iniciar otro proceso;
- registrar propiedad del proceso iniciado;
- cerrar el proceso ante `Ctrl+C`, `SIGINT`, `SIGTERM`, `finally` y `atexit`;
- no cerrar servidores externos previamente existentes;
- aceptar que `SIGKILL` y pérdida de energía no permiten ejecutar limpieza.

## Consecuencias

- TutorIA puede cambiar el proveedor sin modificar las rutas ni la lógica educativa.
- La demostración puede usar NVIDIA o Foundation Models según el entorno.
- Se agregan interfaces y metadatos, pero disminuye el acoplamiento.
- Cada proveedor deberá documentar privacidad, requisitos y manejo de errores.
- Las afirmaciones de privacidad dependerán de la configuración activa.

## Implementación de referencia

El MVP en `Proyecto/mvp_minimo` implementa:

- `ChatProvider`;
- `ProviderStatus`;
- `FoundationModelsProvider`;
- `NVIDIAProvider`;
- `FallbackChatProvider` con NVIDIA como principal y Foundation Models como respaldo;
- inyección del proveedor en `create_app`;
- endpoint genérico `/api/provider/wake`;
- cierre controlado del proceso `fm serve`;
- prueba con proveedores simulados y pruebas de integración controladas.
