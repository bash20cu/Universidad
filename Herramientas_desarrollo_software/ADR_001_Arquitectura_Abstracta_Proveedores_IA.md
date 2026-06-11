# ADR-001: Arquitectura abstracta para proveedores de IA

**Estado:** Aceptada para el diseño del proyecto  
**Fecha:** 10 de junio de 2026  
**Proyecto:** TutorIA

## Contexto

El MVP inicial utiliza Apple Foundation Models mediante `fm serve`. Sin embargo,
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

Foundation Models será una implementación, no una dependencia directa del
dominio. Ollama, un servicio institucional o un proveedor remoto podrán
implementarse con el mismo contrato.

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

- TutorIA puede evolucionar sin quedar atado a Apple.
- La demostración puede usar Foundation Models sin convertirlo en supuesto global.
- Se agregan interfaces y metadatos, pero disminuye el acoplamiento.
- Cada proveedor deberá documentar privacidad, requisitos y manejo de errores.
- Las afirmaciones de privacidad dependerán de la configuración activa.

## Implementación de referencia

El MVP en `Proyecto/mvp_minimo` implementa:

- `ChatProvider`;
- `ProviderStatus`;
- `FoundationModelsProvider`;
- inyección del proveedor en `create_app`;
- endpoint genérico `/api/provider/wake`;
- cierre controlado del proceso `fm serve`;
- prueba con un proveedor remoto simulado.
