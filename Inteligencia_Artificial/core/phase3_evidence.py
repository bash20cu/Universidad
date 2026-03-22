"""Fase 3: generar evidencia automatica de clasificacion para 3 clases."""

from __future__ import annotations

import argparse
import csv
import json
from pathlib import Path

from core.constants import KNOWN_CLASSES
from core.manifest import load_manifest
from core.model import load_model_from_checkpoint, predict_image

try:
    from rich.console import Console
    from rich.table import Table
except ImportError:  # pragma: no cover
    Console = None
    Table = None

console = Console() if Console is not None else None


def print_line(message: str) -> None:
    if console is not None:
        console.print(message)
    else:
        print(message)


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Fase 3 - Genera evidencia de clasificacion por clase usando el modelo entrenado."
    )
    parser.add_argument("--manifest-csv", type=Path, default=Path("phase2_artifacts/dataset_split.csv"))
    parser.add_argument("--checkpoint-path", type=Path, default=Path("phase3_artifacts/best_checkpoint.pt"))
    parser.add_argument("--output-dir", type=Path, default=Path("phase3_artifacts"))
    parser.add_argument("--split", type=str, default="test", choices=("train", "val", "test"))
    parser.add_argument("--samples-per-class", type=int, default=1)
    parser.add_argument("--image-size", type=int, default=128)
    return parser.parse_args()


def select_examples(items, split: str, samples_per_class: int):
    selected = []
    for class_name in KNOWN_CLASSES:
        class_items = [item for item in items if item.split == split and item.label == class_name]
        selected.extend(class_items[:samples_per_class])
    return selected


def save_outputs(output_dir: Path, rows: list[dict[str, object]], split: str) -> tuple[Path, Path]:
    output_dir.mkdir(parents=True, exist_ok=True)
    json_path = output_dir / f"classification_examples_{split}.json"
    csv_path = output_dir / f"classification_examples_{split}.csv"

    with json_path.open("w", encoding="utf-8") as f:
        json.dump(rows, f, indent=2)

    with csv_path.open("w", encoding="utf-8", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(
            [
                "true_label",
                "pred_label",
                "confidence",
                "is_correct",
                "split",
                "image_path",
            ]
        )
        for row in rows:
            writer.writerow(
                [
                    row["true_label"],
                    row["pred_label"],
                    f"{float(row['confidence']):.6f}",
                    row["is_correct"],
                    row["split"],
                    row["image_path"],
                ]
            )

    return json_path, csv_path


def main() -> int:
    args = parse_args()
    manifest_csv = args.manifest_csv.resolve()
    checkpoint_path = args.checkpoint_path.resolve()
    output_dir = args.output_dir.resolve()

    if not manifest_csv.exists():
        print_line(f"ERROR: manifest no encontrado: {manifest_csv}")
        return 1
    if not checkpoint_path.exists():
        print_line(f"ERROR: checkpoint no encontrado: {checkpoint_path}")
        return 1
    if args.samples_per_class < 1:
        print_line("ERROR: --samples-per-class debe ser >= 1")
        return 1

    items = load_manifest(manifest_csv)
    selected = select_examples(items, split=args.split, samples_per_class=args.samples_per_class)
    if not selected:
        print_line(f"ERROR: no se encontraron muestras para split={args.split}")
        return 1

    model, device, device_name, checkpoint = load_model_from_checkpoint(checkpoint_path)
    rows: list[dict[str, object]] = []

    for item in selected:
        prediction = predict_image(
            model=model,
            device=device,
            image_path=item.path,
            image_size=args.image_size,
        )
        rows.append(
            {
                "true_label": item.label,
                "pred_label": prediction["pred_label"],
                "confidence": prediction["confidence"],
                "is_correct": prediction["pred_label"] == item.label,
                "split": item.split,
                "image_path": str(item.path),
                "probabilities": prediction["probabilities"],
            }
        )

    json_path, csv_path = save_outputs(output_dir=output_dir, rows=rows, split=args.split)

    if console is not None and Table is not None:
        table = Table(title=f"Fase 3 - Evidencia de clasificacion ({args.split})")
        table.add_column("Clase real", style="bold")
        table.add_column("Prediccion")
        table.add_column("Confidence")
        table.add_column("Correcta")
        for row in rows:
            table.add_row(
                str(row["true_label"]),
                str(row["pred_label"]),
                f"{float(row['confidence']):.4f}",
                "si" if bool(row["is_correct"]) else "no",
            )
        console.print(table)
        console.print(f"[bold]Device:[/bold] {device_name}")
        console.print(f"[bold]Checkpoint epoch:[/bold] {checkpoint.get('epoch', 'n/a')}")
        console.print(f"[green]OK[/green] Evidencia JSON: {json_path}")
        console.print(f"[green]OK[/green] Evidencia CSV: {csv_path}")
    else:
        print(f"Fase 3 - Evidencia de clasificacion ({args.split})")
        for row in rows:
            print(
                f"- real={row['true_label']} pred={row['pred_label']} "
                f"confidence={float(row['confidence']):.4f} "
                f"correcta={'si' if bool(row['is_correct']) else 'no'}"
            )
        print(f"Device: {device_name}")
        print(f"Checkpoint epoch: {checkpoint.get('epoch', 'n/a')}")
        print(f"OK Evidencia JSON: {json_path}")
        print(f"OK Evidencia CSV: {csv_path}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
