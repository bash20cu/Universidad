"""Logica de deteccion de etiquetas."""

from __future__ import annotations

import re
from pathlib import Path

from .constants import KNOWN_CLASSES, LABEL_ALIASES


def detect_label(file_path: Path) -> str:
    """Detecta clase por carpeta o por patron prefijo_[numero] en nombre de archivo."""
    parts = {part.lower() for part in file_path.parts}
    for class_name in KNOWN_CLASSES:
        if class_name in parts:
            return class_name

    stem = file_path.stem.lower()
    match = re.match(r"^([a-z]+)_(\d+)$", stem)
    if match:
        return LABEL_ALIASES.get(match.group(1), "unknown")

    return "unknown"
