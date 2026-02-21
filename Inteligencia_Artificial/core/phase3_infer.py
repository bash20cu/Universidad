"""Inferencia de una imagen con checkpoint entrenado en Fase 3."""

from __future__ import annotations

import argparse
from pathlib import Path

from rich.console import Console
from rich.table import Table

from core.model import load_model_from_checkpoint, predict_image

console = Console()


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Fase 3 - Inferencia de una imagen (cats/dogs/panda)."
    )
    parser.add_argument("--image-path", type=Path, required=True)
    parser.add_argument("--checkpoint-path", type=Path, default=Path("phase3_artifacts/best_checkpoint.pt"))
    parser.add_argument("--image-size", type=int, default=128)
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    image_path = args.image_path.resolve()
    checkpoint_path = args.checkpoint_path.resolve()

    if not image_path.exists():
        console.print(f"[red]ERROR:[/red] imagen no encontrada: {image_path}")
        return 1
    if not image_path.is_file():
        console.print(
            f"[red]ERROR:[/red] --image-path debe ser un archivo de imagen, no una carpeta: {image_path}"
        )
        return 1
    if not checkpoint_path.exists():
        console.print(f"[red]ERROR:[/red] checkpoint no encontrado: {checkpoint_path}")
        return 1

    model, device, device_name, checkpoint = load_model_from_checkpoint(checkpoint_path)
    result = predict_image(model=model, device=device, image_path=image_path, image_size=args.image_size)

    console.rule("Fase 3 - Inferencia")
    console.print(f"[bold]Imagen:[/bold] {image_path}")
    console.print(f"[bold]Checkpoint:[/bold] {checkpoint_path}")
    console.print(f"[bold]Device:[/bold] {device_name}")
    console.print(f"[bold]Epoch checkpoint:[/bold] {checkpoint.get('epoch', 'n/a')}")
    console.print(f"[green]Prediccion:[/green] {result['pred_label']} ({result['confidence']:.4f})")

    table = Table(title="Probabilidades por clase")
    table.add_column("Clase", style="bold")
    table.add_column("Probabilidad")
    for class_name, prob in result["probabilities"].items():
        table.add_row(class_name, f"{prob:.4f}")
    console.print(table)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
