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


def resolve_manifest_path(manifest_csv: Path, raw_path: str, raw_relative: str | None) -> Path:
    candidate = Path(raw_path)
    if candidate.exists():
        return candidate

    manifest_csv = manifest_csv.resolve()
    project_root = manifest_csv.parent.parent
    relative_candidates: list[Path] = []

    if raw_relative:
        rel = Path(raw_relative)
        relative_candidates.extend(
            [
                project_root / "gatos_perros_pandas" / rel,
                project_root / rel,
            ]
        )

    if candidate.name:
        relative_candidates.extend(project_root.rglob(candidate.name))

    for option in relative_candidates:
        if option.exists():
            return option.resolve()

    return candidate


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
    per_split = Counter(i.split for i in items)
    per_label = Counter(i.label for i in items)
    per_split_label = Counter((i.split, i.label) for i in items)
    return {
        "total": len(items),
        "per_split": dict(per_split),
        "per_label": dict(per_label),
        "per_split_label": {f"{k[0]}::{k[1]}": v for k, v in per_split_label.items()},
    }
