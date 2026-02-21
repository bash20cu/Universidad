"""Fase 3: entrenamiento de una CNN basica para 3 clases."""

from __future__ import annotations

import argparse
import csv
import json
import random
import time
from dataclasses import dataclass
from pathlib import Path

import numpy as np
from PIL import Image, UnidentifiedImageError
from rich.console import Console
from rich.progress import track
from rich.table import Table

from core.model import build_simple_cnn, select_device

console = Console()


@dataclass(frozen=True)
class Item:
    path: Path
    label_id: int
    split: str


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Fase 3 - Entrenamiento de CNN basica (cats, dogs, panda)."
    )
    parser.add_argument("--manifest-csv", type=Path, default=Path("phase2_artifacts/dataset_split.csv"))
    parser.add_argument("--output-dir", type=Path, default=Path("phase3_artifacts"))
    parser.add_argument("--epochs", type=int, default=5)
    parser.add_argument("--batch-size", type=int, default=32)
    parser.add_argument("--lr", type=float, default=1e-3)
    parser.add_argument("--weight-decay", type=float, default=1e-4)
    parser.add_argument("--image-size", type=int, default=128)
    parser.add_argument("--num-workers", type=int, default=0)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument("--debug", action="store_true")
    return parser.parse_args()


def set_seed(seed: int) -> None:
    random.seed(seed)
    np.random.seed(seed)


def load_manifest(path: Path) -> list[Item]:
    items: list[Item] = []
    with path.open("r", encoding="utf-8", newline="") as f:
        reader = csv.DictReader(f)
        required = {"path", "label_id", "split"}
        if not required.issubset(reader.fieldnames or set()):
            raise ValueError(f"CSV invalido. Debe incluir columnas: {sorted(required)}")
        for row in reader:
            items.append(
                Item(path=Path(row["path"]), label_id=int(row["label_id"]), split=row["split"])
            )
    return items


def build_components():
    try:
        import torch
        import torch.nn as nn
        from torch.utils.data import DataLoader, Dataset
    except ImportError as exc:
        raise RuntimeError("Torch no esta instalado. Instala requirements y reintenta.") from exc

    class SplitDataset(Dataset):  # type: ignore[misc]
        def __init__(self, rows: list[Item], split: str, image_size: int):
            self.rows = [r for r in rows if r.split == split]
            self.split = split
            self.image_size = image_size

        def __len__(self) -> int:
            return len(self.rows)

        def __getitem__(self, idx: int):
            row = self.rows[idx]
            try:
                with Image.open(row.path) as img:
                    img = img.convert("RGB").resize((self.image_size, self.image_size))
                    arr = np.asarray(img, dtype=np.float32) / 255.0
            except (OSError, UnidentifiedImageError) as exc:
                raise RuntimeError(f"Error cargando imagen: {row.path}") from exc

            if self.split == "train" and random.random() < 0.5:
                arr = np.ascontiguousarray(np.flip(arr, axis=1))

            x = torch.from_numpy(arr).permute(2, 0, 1).contiguous()
            y = torch.tensor(row.label_id, dtype=torch.long)
            return x, y

    SimpleCNN = build_simple_cnn(nn)

    def run_epoch(model, loader, criterion, optimizer, device, train: bool):
        if train:
            model.train()
        else:
            model.eval()

        total_loss = 0.0
        total_correct = 0
        total_count = 0

        context = torch.enable_grad() if train else torch.no_grad()
        with context:
            for x, y in loader:
                x = x.to(device)
                y = y.to(device)
                logits = model(x)
                loss = criterion(logits, y)

                if train:
                    optimizer.zero_grad()
                    loss.backward()
                    optimizer.step()

                preds = logits.argmax(dim=1)
                total_correct += int((preds == y).sum().item())
                total_count += int(y.size(0))
                total_loss += float(loss.item()) * int(y.size(0))

        if total_count == 0:
            return 0.0, 0.0
        return total_loss / total_count, total_correct / total_count

    return torch, nn, DataLoader, SplitDataset, SimpleCNN, run_epoch


def main() -> int:
    args = parse_args()
    set_seed(args.seed)
    start = time.perf_counter()

    manifest_csv = args.manifest_csv.resolve()
    output_dir = args.output_dir.resolve()
    output_dir.mkdir(parents=True, exist_ok=True)

    if not manifest_csv.exists():
        console.print(f"[red]ERROR:[/red] manifest no encontrado: {manifest_csv}")
        return 1

    try:
        torch, nn, DataLoader, SplitDataset, SimpleCNN, run_epoch = build_components()
    except RuntimeError as exc:
        console.print(f"[red]ERROR:[/red] {exc}")
        return 1

    device, device_name = select_device(torch)
    console.rule("Fase 3 - Entrenamiento CNN")
    console.print(f"[bold]Manifest:[/bold] {manifest_csv}")
    console.print(f"[bold]Output:[/bold] {output_dir}")
    console.print(f"[bold]Device:[/bold] {device_name}")

    items = load_manifest(manifest_csv)
    train_count = sum(1 for i in items if i.split == "train")
    val_count = sum(1 for i in items if i.split == "val")
    test_count = sum(1 for i in items if i.split == "test")
    if train_count == 0 or val_count == 0:
        console.print("[red]ERROR:[/red] train/val vacios en el manifest.")
        return 1

    console.print(f"[green]OK[/green] Registros -> train={train_count}, val={val_count}, test={test_count}")

    train_ds = SplitDataset(items, "train", args.image_size)
    val_ds = SplitDataset(items, "val", args.image_size)
    train_loader = DataLoader(train_ds, batch_size=args.batch_size, shuffle=True, num_workers=args.num_workers)
    val_loader = DataLoader(val_ds, batch_size=args.batch_size, shuffle=False, num_workers=args.num_workers)

    model = SimpleCNN(num_classes=3).to(device)
    criterion = nn.CrossEntropyLoss()
    optimizer = torch.optim.Adam(model.parameters(), lr=args.lr, weight_decay=args.weight_decay)

    history: list[dict[str, float]] = []
    best_val_acc = -1.0
    best_epoch = -1

    for epoch in track(range(1, args.epochs + 1), description="Entrenando epocas..."):
        train_loss, train_acc = run_epoch(model, train_loader, criterion, optimizer, device, train=True)
        val_loss, val_acc = run_epoch(model, val_loader, criterion, optimizer, device, train=False)

        row = {
            "epoch": float(epoch),
            "train_loss": train_loss,
            "train_acc": train_acc,
            "val_loss": val_loss,
            "val_acc": val_acc,
        }
        history.append(row)

        if args.debug:
            console.print(
                f"DEBUG: epoch={epoch} train_loss={train_loss:.4f} train_acc={train_acc:.4f} "
                f"val_loss={val_loss:.4f} val_acc={val_acc:.4f}"
            )

        if val_acc > best_val_acc:
            best_val_acc = val_acc
            best_epoch = epoch
            torch.save(
                {
                    "epoch": epoch,
                    "model_state_dict": model.state_dict(),
                    "optimizer_state_dict": optimizer.state_dict(),
                    "val_acc": val_acc,
                },
                output_dir / "best_checkpoint.pt",
            )

    torch.save(
        {
            "epoch": args.epochs,
            "model_state_dict": model.state_dict(),
            "optimizer_state_dict": optimizer.state_dict(),
            "val_acc": history[-1]["val_acc"] if history else 0.0,
        },
        output_dir / "last_checkpoint.pt",
    )

    metrics_path = output_dir / "metrics_history.json"
    with metrics_path.open("w", encoding="utf-8") as f:
        json.dump(history, f, indent=2)

    table = Table(title="Fase 3 - Resultado de entrenamiento")
    table.add_column("Metrica", style="bold")
    table.add_column("Valor")
    table.add_row("Best epoch", str(best_epoch))
    table.add_row("Best val_acc", f"{best_val_acc:.4f}")
    table.add_row("Ultimo train_acc", f"{history[-1]['train_acc']:.4f}" if history else "0.0000")
    table.add_row("Ultimo val_acc", f"{history[-1]['val_acc']:.4f}" if history else "0.0000")
    table.add_row("best_checkpoint", str(output_dir / "best_checkpoint.pt"))
    table.add_row("last_checkpoint", str(output_dir / "last_checkpoint.pt"))
    table.add_row("metrics_history", str(metrics_path))
    console.print(table)

    elapsed = time.perf_counter() - start
    console.print(f"[bold green]Estado Fase 3: OK[/bold green] ({elapsed:.2f}s)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
