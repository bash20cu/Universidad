"""Constantes compartidas del dominio de clasificacion.

Este archivo concentra nombres de clases, mapeos numericos y extensiones
soportadas para que todas las fases hablen el mismo idioma.
"""

# Las clases viven en un solo lugar para evitar inconsistencias entre
# preparacion, entrenamiento, evaluacion e inferencia.
KNOWN_CLASSES = ("cats", "dogs", "panda")
CLASS_TO_ID = {name: idx for idx, name in enumerate(KNOWN_CLASSES)}
ID_TO_CLASS = {idx: name for name, idx in CLASS_TO_ID.items()}

# Algunos datasets mezclan singular/plural en carpetas o nombres de archivo.
# Este aliasado ayuda a normalizar esas variantes antes de etiquetar.
LABEL_ALIASES = {
    "cat": "cats",
    "cats": "cats",
    "dog": "dogs",
    "dogs": "dogs",
    "panda": "panda",
    "pandas": "panda",
}

# Extensiones de imagen aceptadas a lo largo del pipeline.
IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".bmp", ".webp"}
