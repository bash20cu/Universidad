# Laboratorio: Web API con API Key

Sistema de gestión de personas utilizando ASP.NET Core MVC y Web API protegida por autenticación de API Key.

## 🛠 Tecnologías
![C#](https://img.shields.io/badge/C%23-239120?style=for-the-badge&logo=c-sharp&logoColor=white)
![.NET](https://img.shields.io/badge/.NET-512BD4?style=for-the-badge&logo=dotnet&logoColor=white)
![Bootstrap](https://img.shields.io/badge/bootstrap-%238511FA.svg?style=for-the-badge&logo=bootstrap&logoColor=white)

## 📖 Detalles
El proyecto implementa una arquitectura en capas:
- **GestionDePersonas.SI**: Web API que expone endpoints para CRUD de personas, protegida por un Middleware de API Key.
- **GestionDePersonas.UI**: Interfaz de usuario MVC que consume la API.
- **GestionDePersonas.BL**: Lógica de negocio.
- **GestionDePersonas.DA**: Acceso a datos.
