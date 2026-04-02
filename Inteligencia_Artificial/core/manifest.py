"""Utilidades para leer y resolver el manifest generado en la fase 2."""

from __future__ import annotations

import csv
from collections import Counter
from dataclasses import dataclass
from functools import lru_cache
from pathlib import Path


@dataclass(frozen=True)
class ManifestItem:
    """Representa una fila del manifest con ruta, clase y split."""

    path: Path
    label: str
    label_id: int
    split: str


def build_manifest_candidates(
    manifest_csv: Path,
    raw_path: str,
    raw_relative: str | None,
) -> list[Path]:
    """Construye rutas candidatas para recuperar archivos movidos entre entornos."""

    candidate = Path(raw_path).expanduser()
    manifest_csv = manifest_csv.resolve()
    project_root = manifest_csv.parent.parent
    candidates = [candidate]

    if raw_relative:
        rel = Path(raw_relative)
        # Estas rutas cubren el caso comun en el que el manifest fue creado en
        # otra maquina y el dataset ahora vive dentro del proyecto actual.
        candidates.extend(
            [
                project_root / "gatos_perros_pandas" / rel,
                project_root / rel,
            ]
        )

    return candidates


@lru_cache(maxsize=8192)
def resolve_manifest_path(manifest_csv: Path, raw_path: str, raw_relative: str | None) -> Path:
    """Resuelve la ruta de una imagen del manifest sin hacer escaneos recursivos costosos."""

    for option in build_manifest_candidates(manifest_csv, raw_path, raw_relative):
        if option.exists():
            return option.resolve()

    return Path(raw_path).expanduser()


def load_manifest(manifest_csv: Path) -> list[ManifestItem]:
    """Carga el manifest CSV y valida que tenga las columnas minimas esperadas."""

    items: list[ManifestItem] = []
    with manifest_csv.open("r", encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f)
        required = {"path", "label", "label_id", "split"}
        if not required.issubset(set(reader.fieldnames or [])):
            raise ValueError(f"Manifest invalido. Requiere columnas: {sorted(required)}")
        for row in reader:
            # Cada fila se normaliza a Path resuelto para que las fases
            # posteriores no tengan que preocuparse por rutas relativas.
            items.append(
                ManifestItem(
                    path=resolve_manifest_path(
                        manifest_csv=manifest_csv,
                        raw_path=row["path"],
                        raw_relative=row.get("path_relative"),
                    ),
                    label=row["label"],
                    label_id=int(row["label_id"]),
                    split=row["split"],
                )
            )
    return items


def summarize_manifest(items: list[ManifestItem]) -> dict[str, object]:
    """Resume la distribucion total, por split y por clase del manifest."""

    # Este resumen alimenta el dashboard y varios mensajes de apoyo del flujo.
    per_split = Counter(i.split for i in items)
    per_label = Counter(i.label for i in items)
    per_split_label = Counter((i.split, i.label) for i in items)
    return {
        "total": len(items),
        "per_split": dict(per_split),
        "per_label": dict(per_label),
        "per_split_label": {f"{k[0]}::{k[1]}": v for k, v in per_split_label.items()},
    }
