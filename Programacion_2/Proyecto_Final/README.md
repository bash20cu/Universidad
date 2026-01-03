# Proyecto Final: Explorador de Archivos

Aplicación de escritorio desarrollada en Python con PyQt6 que simula un explorador de archivos con funciones de seguridad y editor de texto integrado.

## 🛠 Tecnologías
![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)
![Qt](https://img.shields.io/badge/Qt-%23217346.svg?style=for-the-badge&logo=Qt&logoColor=white)
![SQLite](https://img.shields.io/badge/sqlite-%2307405e.svg?style=for-the-badge&logo=sqlite&logoColor=white)

## 📖 Detalles
Características principales:
- **Explorador de Archivos**: Navegación por directorios, visualización de archivos.
- **Bloc de Notas**: Editor de texto integrado (`bloc_notas.py`) para abrir y editar archivos directamente.
- **Seguridad**: Módulo de autenticación (`security_manager.py`) respaldado por base de datos SQLite.
- **Interfaz Gráfica**: Diseño moderno utilizando archivos `.ui` cargados dinámicamente.
- **Arquitectura MVC**: Separación clara entre Modelo (Gestor de seguridad), Vista (Archivos UI) y Controlador (Lógica de negocio).
