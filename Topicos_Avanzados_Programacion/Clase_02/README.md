# Estudiante de Ingeniería de Software  

# en la Universidad Internacional de las Américas Costa Rica  

<!--START_SECTION:badges-->

![Visual Studio](https://img.shields.io/badge/Visual%20Studio-5C2D91.svg?style=for-the-badge&logo=visual-studio&logoColor=white)

![GitHub language count](https://img.shields.io/github/languages/count/bash20cu/Universidad?style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/bash20cu/Universidad?style=for-the-badge)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/bash20cu/Universidad?style=for-the-badge)
![GitHub last commit](https://img.shields.io/github/last-commit/bash20cu/Universidad?style=for-the-badge)

[![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/miguel1990/)
[![Portfolio](https://img.shields.io/badge/Portfolio-%23000000.svg?style=for-the-badge&logo=firefox&logoColor=#FF7139)](https://portfoliomiguel2025.netlify.app/)

<!--END_SECTION:badges-->

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
