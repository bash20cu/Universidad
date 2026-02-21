"""Fase 4: metricas y evaluacion del modelo entrenado."""

from __future__ import annotations

import argparse
import csv
import json
from pathlib import Path

import numpy as np
from PIL import Image, UnidentifiedImageError
from rich.console import Console
from rich.table import Table

from core.constants import ID_TO_CLASS, KNOWN_CLASSES
from core.manifest import load_manifest
from core.model import load_model_from_checkpoint

console = Console()


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Fase 4 - Evaluacion con accuracy, precision, recall, f1 y matriz de confusion."
    )
    parser.add_argument("--manifest-csv", type=Path, default=Path("phase2_artifacts/dataset_split.csv"))
    parser.add_argument("--checkpoint-path", type=Path, default=Path("phase3_artifacts/best_checkpoint.pt"))
    parser.add_argument("--output-dir", type=Path, default=Path("phase4_artifacts"))
    parser.add_argument("--split", type=str, default="test", choices=("train", "val", "test"))
    parser.add_argument("--image-size", type=int, default=128)
    return parser.parse_args()


def load_image_tensor(torch_module, image_path: Path, image_size: int):
    with Image.open(image_path) as img:
        arr = np.asarray(img.convert("RGB").resize((image_size, image_size)), dtype=np.float32) / 255.0
    return torch_module.from_numpy(arr).permute(2, 0, 1).contiguous().unsqueeze(0)


def safe_div(num: float, den: float) -> float:
    return float(num / den) if den else 0.0


def compute_metrics(y_true: list[int], y_pred: list[int], num_classes: int) -> dict[str, object]:
    y_true_arr = np.asarray(y_true, dtype=np.int64)
    y_pred_arr = np.asarray(y_pred, dtype=np.int64)

    cm = np.zeros((num_classes, num_classes), dtype=np.int64)
    for t, p in zip(y_true_arr, y_pred_arr):
        cm[t, p] += 1

    supports = cm.sum(axis=1)
    tp = np.diag(cm)
    fp = cm.sum(axis=0) - tp
    fn = cm.sum(axis=1) - tp

    per_class: dict[str, dict[str, float | int]] = {}
    precisions: list[float] = []
    recalls: list[float] = []
    f1s: list[float] = []

    for cls_id in range(num_classes):
        prec = safe_div(float(tp[cls_id]), float(tp[cls_id] + fp[cls_id]))
        rec = safe_div(float(tp[cls_id]), float(tp[cls_id] + fn[cls_id]))
        f1 = safe_div(2.0 * prec * rec, (prec + rec))
        supports_i = int(supports[cls_id])
        per_class[ID_TO_CLASS.get(cls_id, str(cls_id))] = {
            "precision": prec,
            "recall": rec,
            "f1": f1,
            "support": supports_i,
        }
        precisions.append(prec)
        recalls.append(rec)
        f1s.append(f1)

    accuracy = safe_div(float((y_true_arr == y_pred_arr).sum()), float(len(y_true_arr)))

    total_support = int(supports.sum())
    weighted_precision = safe_div(float((np.asarray(precisions) * supports).sum()), float(total_support))
    weighted_recall = safe_div(float((np.asarray(recalls) * supports).sum()), float(total_support))
    weighted_f1 = safe_div(float((np.asarray(f1s) * supports).sum()), float(total_support))

    return {
        "accuracy": accuracy,
        "macro_precision": float(np.mean(precisions)),
        "macro_recall": float(np.mean(recalls)),
        "macro_f1": float(np.mean(f1s)),
        "weighted_precision": weighted_precision,
        "weighted_recall": weighted_recall,
        "weighted_f1": weighted_f1,
        "confusion_matrix": cm.tolist(),
        "per_class": per_class,
        "n_samples": int(len(y_true_arr)),
    }


def save_outputs(output_dir: Path, metrics: dict[str, object], split: str) -> None:
    output_dir.mkdir(parents=True, exist_ok=True)

    metrics_json = output_dir / f"metrics_{split}.json"
    with metrics_json.open("w", encoding="utf-8") as f:
        json.dump(metrics, f, indent=2)

    cm_csv = output_dir / f"confusion_matrix_{split}.csv"
    cm = metrics["confusion_matrix"]
    with cm_csv.open("w", encoding="utf-8", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["true\\pred", *KNOWN_CLASSES])
        for cls_name, row in zip(KNOWN_CLASSES, cm):
            writer.writerow([cls_name, *row])

    report_csv = output_dir / f"classification_report_{split}.csv"
    with report_csv.open("w", encoding="utf-8", newline="") as f:
        writer = csv.writer(f)
        writer.writerow(["class", "precision", "recall", "f1", "support"])
        for class_name, vals in metrics["per_class"].items():
            writer.writerow(
                [
                    class_name,
                    f"{vals['precision']:.6f}",
                    f"{vals['recall']:.6f}",
                    f"{vals['f1']:.6f}",
                    vals["support"],
                ]
            )


def print_summary(metrics: dict[str, object], split: str, output_dir: Path) -> None:
    global_table = Table(title=f"Fase 4 - Metricas globales ({split})")
    global_table.add_column("Metrica", style="bold")
    global_table.add_column("Valor")
    global_table.add_row("samples", str(metrics["n_samples"]))
    global_table.add_row("accuracy", f"{metrics['accuracy']:.4f}")
    global_table.add_row("macro_precision", f"{metrics['macro_precision']:.4f}")
    global_table.add_row("macro_recall", f"{metrics['macro_recall']:.4f}")
    global_table.add_row("macro_f1", f"{metrics['macro_f1']:.4f}")
    global_table.add_row("weighted_precision", f"{metrics['weighted_precision']:.4f}")
    global_table.add_row("weighted_recall", f"{metrics['weighted_recall']:.4f}")
    global_table.add_row("weighted_f1", f"{metrics['weighted_f1']:.4f}")
    console.print(global_table)

    per_class_table = Table(title=f"Fase 4 - Reporte por clase ({split})")
    per_class_table.add_column("Clase", style="bold")
    per_class_table.add_column("Precision")
    per_class_table.add_column("Recall")
    per_class_table.add_column("F1")
    per_class_table.add_column("Support")
    for class_name, vals in metrics["per_class"].items():
        per_class_table.add_row(
            class_name,
            f"{vals['precision']:.4f}",
            f"{vals['recall']:.4f}",
            f"{vals['f1']:.4f}",
            str(vals["support"]),
        )
    console.print(per_class_table)

    console.print(f"[green]OK[/green] Artefactos guardados en: {output_dir}")


def main() -> int:
    args = parse_args()
    manifest_csv = args.manifest_csv.resolve()
    checkpoint_path = args.checkpoint_path.resolve()
    output_dir = args.output_dir.resolve()

    if not manifest_csv.exists():
        console.print(f"[red]ERROR:[/red] manifest no encontrado: {manifest_csv}")
        return 1
    if not checkpoint_path.exists():
        console.print(f"[red]ERROR:[/red] checkpoint no encontrado: {checkpoint_path}")
        return 1

    try:
        import torch
    except ImportError:
        console.print("[red]ERROR:[/red] Torch no instalado.")
        return 1

    model, device, device_name, checkpoint = load_model_from_checkpoint(checkpoint_path)
    all_items = load_manifest(manifest_csv)
    items = [i for i in all_items if i.split == args.split]
    if not items:
        console.print(f"[red]ERROR:[/red] no hay registros para split={args.split}")
        return 1

    console.rule("Fase 4 - Evaluacion")
    console.print(f"[bold]Split:[/bold] {args.split}")
    console.print(f"[bold]Device:[/bold] {device_name}")
    console.print(f"[bold]Checkpoint epoch:[/bold] {checkpoint.get('epoch', 'n/a')}")
    console.print(f"[bold]Samples:[/bold] {len(items)}")

    y_true: list[int] = []
    y_pred: list[int] = []
    skipped = 0

    for item in items:
        try:
            x = load_image_tensor(torch, item.path, args.image_size).to(device)
            with torch.no_grad():
                logits = model(x)
                pred_id = int(torch.argmax(logits, dim=1).item())
            y_true.append(int(item.label_id))
            y_pred.append(pred_id)
        except (OSError, UnidentifiedImageError, RuntimeError):
            skipped += 1
            continue

    if not y_true:
        console.print("[red]ERROR:[/red] no se pudo evaluar ninguna imagen.")
        return 1

    metrics = compute_metrics(y_true=y_true, y_pred=y_pred, num_classes=len(KNOWN_CLASSES))
    metrics["split"] = args.split
    metrics["skipped_images"] = skipped
    metrics["checkpoint_path"] = str(checkpoint_path)
    metrics["manifest_path"] = str(manifest_csv)

    save_outputs(output_dir=output_dir, metrics=metrics, split=args.split)
    print_summary(metrics=metrics, split=args.split, output_dir=output_dir)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
