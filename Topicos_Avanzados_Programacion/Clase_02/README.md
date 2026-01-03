
## Clase 2 - Consumo de Servicios Web Legacy (WCF y ASMX)

- Se estudia cómo consumir servicios web en **.NET Framework 4.8** utilizando tecnologías **legacy** como **WCF** y **ASMX**.  
- Ejemplo práctico de invocación de un **servicio de personas**, realizando operaciones de **Agregar** y **Obtener Lista**.  
- Comparación entre el consumo de un servicio **WCF** y un servicio **ASMX**.  
- Uso de **Visual Studio 2022** para gestionar proyectos de consola con múltiples capas (BL, Model, SI.WCF, SI.WebService).  

### Tecnologías utilizadas:
- **.NET Framework 4.8**
- **C#**
- **Visual Studio 2022**
- **Servicios Web WCF**
- **Servicios Web ASMX**

### Ejemplo de código (consumo ASMX)
```csharp
var elServicio = new webservice.ServicioPersonaAsmx.ServicioPersona();
var laPersona = new webservice.ServicioPersonaAsmx.Persona()
{
    Apellidos = "111",
    Estado = webservice.ServicioPersonaAsmx.Estado.Activo,
    Identificacion = "qqq",
    Nombre = "Maria",
    FechaDeNacimiento = DateTime.Now
};
elServicio.Agregue(laPersona);

var lasPersonas = elServicio.ObtengaLaLista();
Console.WriteLine(lasPersonas);
```
