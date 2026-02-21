"""Manejo de manifest CSV (fase 2)."""

from __future__ import annotations

import csv
from collections import Counter
from dataclasses import dataclass
from pathlib import Path


@dataclass(frozen=True)
class ManifestItem:
    path: Path
    label: str
    label_id: int
    split: str


def load_manifest(manifest_csv: Path) -> list[ManifestItem]:
    items: list[ManifestItem] = []
    with manifest_csv.open("r", encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f)
        required = {"path", "label", "label_id", "split"}
        if not required.issubset(set(reader.fieldnames or [])):
            raise ValueError(f"Manifest invalido. Requiere columnas: {sorted(required)}")
        for row in reader:
            items.append(
                ManifestItem(
                    path=Path(row["path"]),
                    label=row["label"],
                    label_id=int(row["label_id"]),
                    split=row["split"],
                )
            )
    return items


def summarize_manifest(items: list[ManifestItem]) -> dict[str, object]:
    per_split = Counter(i.split for i in items)
    per_label = Counter(i.label for i in items)
    per_split_label = Counter((i.split, i.label) for i in items)
    return {
        "total": len(items),
        "per_split": dict(per_split),
        "per_label": dict(per_label),
        "per_split_label": {f"{k[0]}::{k[1]}": v for k, v in per_split_label.items()},
    }
