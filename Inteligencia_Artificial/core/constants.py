"""Constantes de dominio."""

KNOWN_CLASSES = ("cats", "dogs", "panda")
CLASS_TO_ID = {name: idx for idx, name in enumerate(KNOWN_CLASSES)}
ID_TO_CLASS = {idx: name for name, idx in CLASS_TO_ID.items()}

LABEL_ALIASES = {
    "cat": "cats",
    "cats": "cats",
    "dog": "dogs",
    "dogs": "dogs",
    "panda": "panda",
    "pandas": "panda",
}

IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".bmp", ".webp"}
