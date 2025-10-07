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

## Laboratorio 2

### INSTRUCCIONES PARA DESARROLLAR EL LABORATORIO:

Usted ha sido contratado para implementar una funcionalidad destinada a una empresa del sector automotriz, la cual desea exponer su inventario de vehículos mediante servicios tecnológicos. La solución es compatible con tecnologías legacy como WCF y Web Services, pero ahora deben migrar a API RESTful.  La implementación debe estar desacoplada de la tecnología de exposición, siguiendo los principios revisados en clase. 

### Estructura de datos del vehículo

# 🧩 Estructura de datos del vehículo

Cada vehículo debe contener la siguiente información:

- **Marca:**
  - `1` = Tesla  
  - `2` = Toyota  
  - `3` = BYD
- **Año:** Campo entero  
- **Modelo:** Campo tipo string  
- **Doble Tracción:** Valor booleano (`true` o `false`)

---

## ⚙️ Operaciones CRUD (API REST)

| Operación | Método | Descripción |
|------------|---------|--------------|
| **C - Create** | `POST` | Crear un nuevo vehículo |
| **R - Read** | `GET` | Leer/Listar vehículos existentes |
| **U - Update** | `PUT` | Actualizar información de un vehículo |
| **D - Delete** | `DELETE` | Eliminar un vehículo del inventario |

---

## 📦 ENTREGABLE

- Debe entregar la **dirección del repositorio en Git**.  
- El profesor debe tener acceso para **descargar el código fuente**.  
  - De lo contrario, **no se podrá evaluar** y se asignará una calificación de **0**.  
- **No deben existir commits posteriores** a la fecha de entrega establecida.  
- El sistema debe estar en **funcionamiento** al momento de la evaluación.  
  - Si no corre correctamente, **no se podrá probar** y se asignará una calificación de **0**.

📁 **Agregar dirección de Git aquí:**  



---

## 🧮 Evaluación (100 puntos)

| Criterio | Descripción | Puntos |
|-----------|--------------|--------|
| **Create** | Implementa correctamente el método para agregar vehículos usando Web Services (100% funcional) | **10%** |
| **Read** | Permite listar vehículos con formato estructurado (100% funcional) | **10%** |
| **Update** | Actualiza correctamente los datos de un vehículo existente (100% funcional) | **10%** |
| **Delete** | Elimina un vehículo del inventario sin errores (100% funcional) | **10%** |
| **Desacoplamiento** | La lógica de negocio está separada del servicio web (uso de capas o interfaces):<br>• Capa DA (2%)<br>• Capa BL (2%)<br>• Capa SI (2%)<br>• Capa Model (2%)<br>• Capa Test (2%) | **10%** |
| **Códigos de estado y métodos HTTP** | Uso correcto en todos los métodos y respuestas:<br>✅ Correcto: 10%<br>⚠️ Parcial: 5%<br>❌ Incorrecto: 0% | **10%** |
| **Nombres significativos** | Utiliza nombres claros y descriptivos en funciones y variables | **10%** |
| **Pruebas funcionales (30%)** | Debe contener al menos una prueba funcional en verde para cada funcionalidad:<br>• Create (7%)<br>• Read (7%)<br>• Update (9%)<br>• Delete (7%) | **30%** |

---

## 🧠 Notas finales

- Estructura de capas sugerida:
  - `Model` → Modelo de datos  
  - `DA` → Acceso a datos  
  - `BL` → Lógica de negocio  
  - `SI` → Servicios o controladores  
  - `Test` → Pruebas funcionales y unitarias  

- Asegúrese de que el proyecto compile y ejecute correctamente al momento de la revisión.  
- Verifique el correcto uso de **códigos HTTP estándar** (`200`, `201`, `204`, `400`, `404`, `500`, etc.).  
- Los nombres de funciones y variables deben reflejar su propósito claramente.  
- Mantenga el proyecto actualizado, pero **no haga commits después de la fecha límite**.

---



### Tecnologías utilizadas:
- **.NET Framework 8**
- **C#**
- **Visual Studio 2022**
- **Servicios Web API**
- **Servicios Web SOAP**

### Ejemplo de código (consumo API)
```csharp



```
