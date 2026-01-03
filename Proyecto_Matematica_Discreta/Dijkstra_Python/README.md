# Algoritmo de Dijkstra con OpenCV

Script en Python que utiliza visión por computadora (OpenCV) para visualizar la ruta más corta en un mapa gráfico predefinido.

## 🚀 Descripción
El script calcula la ruta más corta entre dos nodos ingresados por consola (ej. de 'Q' a 'TB') y visualiza el trayecto dibujando flechas sobre una imagen de mapa (`imagen.jpg`).

- **Grafo Predefinido**: Contiene un grafo hardcodeado con 66 nodos y sus conexiones.
- **Visualización Gráfica**: Usa `cv2.arrowedLine` para trazar el camino sobre la imagen original.

## 🛠 Tecnologías
![Python](https://img.shields.io/badge/python-3670A0?style=for-the-badge&logo=python&logoColor=ffdd54)
![OpenCV](https://img.shields.io/badge/opencv-%23white.svg?style=for-the-badge&logo=opencv&logoColor=white)

## 📋 Ejecución
```bash
python Dijkstra.py
```
1. El programa solicitará el **Nodo Origen** (ej. `Q`).
2. Luego el **Nodo Destino** (ej. `TB`).
3. Se abrirá una ventana mostrando el mapa con la ruta trazada.
