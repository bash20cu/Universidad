# Portafolio Universitario con Astro

Este es un sitio web estático construido con [Astro](https://astro.build) diseñado para mostrar automáticamente los proyectos y tareas de este repositorio.

## 🚀 Características

- **Generación Automática**: Un script (`src/utils/content.ts`) escanea las carpetas del repositorio padre (`../`) y crea páginas para cada materia automáticamente.
- **Diseño Moderno**: Estilizado con Tailwind CSS, soporte para modo oscuro y animaciones con Framer Motion.
- **Rápido y Ligero**: Generación estática para un rendimiento óptimo.

## 🛠️ Instalación y Uso Local

Para correr el proyecto localmente (necesitas Node.js instalado):

1. Entra a la carpeta web:
   ```bash
   cd web
   ```

2. Instala las dependencias:
   ```bash
   npm install
   ```

3. Inicia el servidor de desarrollo:
   ```bash
   npm run dev
   ```
   El sitio estará disponible en `http://localhost:4321`.

## 📦 Estructura del Proyecto

```text
/
├── src/
│   ├── layouts/      # Layout principal (Layout.astro)
│   ├── pages/        # Rutas de la web
│   │   ├── index.astro       # Página de inicio
│   │   └── [course]/         # Ruta dinámica para cada materia
│   └── utils/
│       └── content.ts # Script que lee los archivos del repo
├── netlify.toml      # Configuración de despliegue
└── ...
```

## ☁️ Despliegue en Netlify

El proyecto ya incluye un archivo `netlify.toml` configurado.

1. Sube tus cambios a GitHub.
2. Crea un nuevo sitio en Netlify importando este repositorio.
3. Netlify detectará la configuración automáticamente (Directorio base: `web`, Comando: `npm run build`).
4. ¡Listo! Tu portafolio se actualizará cada vez que hagas push.
