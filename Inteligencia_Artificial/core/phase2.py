"""Fase 2: preparacion de datos para entrenamiento."""

from __future__ import annotations

import argparse
import csv
import hashlib
import random
import time
from collections import Counter, defaultdict
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable

import numpy as np
from PIL import Image, UnidentifiedImageError
from rich.console import Console
from rich.progress import track
from rich.table import Table

from core.constants import CLASS_TO_ID, IMAGE_EXTENSIONS, KNOWN_CLASSES
from core.labeling import detect_label

console = Console()


@dataclass(frozen=True)
class Record:
    """Representa una imagen etiquetada y su split asignado."""

    path: Path
    label: str
    split: str


def debug_log(enabled: bool, message: str) -> None:
    """Imprime mensajes de depuracion solo cuando el modo debug esta activo."""

    if enabled:
        console.print(f"[cyan]DEBUG:[/cyan] {message}")


def list_labeled_images(dataset_root: Path, debug: bool = False) -> tuple[list[tuple[Path, str]], int]:
    """Recorre el dataset y devuelve solo las imagenes cuya clase pudo inferirse."""

    labeled: list[tuple[Path, str]] = []
    unknown = 0
    all_paths = list(dataset_root.rglob("*"))
    debug_log(debug, f"Elementos detectados para escaneo: {len(all_paths)}")
    for path in track(all_paths, description="Escaneando imagenes..."):
        if not path.is_file() or path.suffix.lower() not in IMAGE_EXTENSIONS:
            continue
        label = detect_label(path)
        if label == "unknown":
            unknown += 1
            if debug and unknown <= 5:
                debug_log(debug, f"Sin clase: {path}")
            continue
        labeled.append((path, label))
    return labeled, unknown


def compute_sha1(file_path: Path, chunk_size: int = 1024 * 1024) -> str:
    """Calcula el hash SHA1 del contenido para detectar duplicados reales."""

    sha1 = hashlib.sha1()
    with file_path.open("rb") as f:
        while True:
            chunk = f.read(chunk_size)
            if not chunk:
                break
            sha1.update(chunk)
    return sha1.hexdigest()


def dedupe_records(
    items: Iterable[tuple[Path, str]], mode: str, debug: bool = False
) -> tuple[list[tuple[Path, str]], int]:
    """Elimina duplicados por nombre/tamano o por contenido segun el modo indicado."""

    if mode == "none":
        data = list(items)
        return data, 0

    items_list = list(items)
    seen: set[tuple[str, str] | tuple[str, int, str]] = set()
    deduped: list[tuple[Path, str]] = []
    removed = 0

    debug_log(debug, f"Deduplicando {len(items_list)} registros con modo={mode}")
    for path, label in track(items_list, description=f"Deduplicando ({mode})..."):
        stat = path.stat()
        if mode == "name_size":
            key = (label, path.name.lower(), stat.st_size)
        elif mode == "sha1":
            key = (label, stat.st_size, compute_sha1(path))
        else:
            raise ValueError(f"Modo de deduplicacion no soportado: {mode}")

        if key in seen:
            removed += 1
            if debug and removed <= 5:
                debug_log(debug, f"Duplicado removido: {path}")
            continue
        seen.add(key)
        deduped.append((path, label))

    return deduped, removed


def split_stratified(
    items: list[tuple[Path, str]],
    train_ratio: float,
    val_ratio: float,
    test_ratio: float,
    seed: int,
) -> list[Record]:
    """Genera un split estratificado intentando dejar muestras en todos los subconjuntos."""

    if abs((train_ratio + val_ratio + test_ratio) - 1.0) > 1e-9:
        raise ValueError("Las proporciones train/val/test deben sumar 1.0")

    rng = random.Random(seed)
    by_class: dict[str, list[Path]] = defaultdict(list)
    for path, label in items:
        by_class[label].append(path)

    records: list[Record] = []
    for class_name in KNOWN_CLASSES:
        class_items = by_class[class_name]
        rng.shuffle(class_items)
        n = len(class_items)

        n_train = int(n * train_ratio)
        n_val = int(n * val_ratio)
        n_test = n - n_train - n_val

        if n >= 3:
            if n_train == 0:
                n_train = 1
                n_test -= 1
            if n_val == 0:
                n_val = 1
                n_test -= 1
            if n_test == 0:
                n_test = 1
                if n_train > n_val:
                    n_train -= 1
                else:
                    n_val -= 1

        train_items = class_items[:n_train]
        val_items = class_items[n_train : n_train + n_val]
        test_items = class_items[n_train + n_val :]

        records.extend(Record(path=p, label=class_name, split="train") for p in train_items)
        records.extend(Record(path=p, label=class_name, split="val") for p in val_items)
        records.extend(Record(path=p, label=class_name, split="test") for p in test_items)

    rng.shuffle(records)
    return records


def export_manifest(records: list[Record], dataset_root: Path, output_csv: Path) -> None:
    """Exporta el split a un CSV reutilizable por entrenamiento y evaluacion."""

    output_csv.parent.mkdir(parents=True, exist_ok=True)
    with output_csv.open("w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(["path", "path_relative", "label", "label_id", "split"])
        for rec in records:
            writer.writerow(
                [
                    str(rec.path.resolve()),
                    str(rec.path.resolve().relative_to(dataset_root.resolve())),
                    rec.label,
                    CLASS_TO_ID[rec.label],
                    rec.split,
                ]
            )


def print_summary(records: list[Record]) -> None:
    """Muestra en consola un resumen legible del split generado."""

    total = len(records)
    per_split = Counter(rec.split for rec in records)
    per_label = Counter(rec.label for rec in records)
    per_split_label = Counter((rec.split, rec.label) for rec in records)

    table = Table(title="Fase 2 - Resumen de split")
    table.add_column("Metrica", style="bold")
    table.add_column("Valor")
    table.add_row("Total registros", str(total))
    table.add_row(
        "Split counts",
        (
            f"train={per_split.get('train', 0)} | "
            f"val={per_split.get('val', 0)} | test={per_split.get('test', 0)}"
        ),
    )
    table.add_row(
        "Label counts",
        (
            f"cats={per_label.get('cats', 0)} | "
            f"dogs={per_label.get('dogs', 0)} | panda={per_label.get('panda', 0)}"
        ),
    )
    for split_name in ("train", "val", "test"):
        table.add_row(
            f"{split_name} by class",
            (
                f"cats={per_split_label.get((split_name, 'cats'), 0)} | "
                f"dogs={per_split_label.get((split_name, 'dogs'), 0)} | "
                f"panda={per_split_label.get((split_name, 'panda'), 0)}"
            ),
        )
    console.print(table)


def verify_dataloaders(
    records: list[Record],
    image_size: int,
    batch_size: int,
    num_workers: int,
) -> str:
    """Prueba que los DataLoaders puedan leer al menos un batch por split."""

    try:
        import torch
        from torch.utils.data import DataLoader, Dataset
    except ImportError:
        return "Torch no instalado. Verificacion de dataloaders omitida."

    class ManifestDataset(Dataset):  # type: ignore[misc]
        def __init__(self, rows: list[Record], target_split: str):
            self.rows = [r for r in rows if r.split == target_split]

        def __len__(self) -> int:
            return len(self.rows)

        def __getitem__(self, idx: int) -> tuple[torch.Tensor, int]:
            rec = self.rows[idx]
            try:
                with Image.open(rec.path) as img:
                    arr = np.asarray(
                        img.convert("RGB").resize((image_size, image_size)),
                        dtype=np.float32,
                    ) / 255.0
            except (OSError, UnidentifiedImageError) as exc:
                raise RuntimeError(f"Error leyendo imagen {rec.path}") from exc

            tensor = torch.from_numpy(arr).permute(2, 0, 1).contiguous()
            label_id = CLASS_TO_ID[rec.label]
            return tensor, label_id

    outputs: list[str] = []
    for split_name in ("train", "val", "test"):
        ds = ManifestDataset(records, split_name)
        if len(ds) == 0:
            outputs.append(f"{split_name}: vacio")
            continue
        loader = DataLoader(
            ds,
            batch_size=batch_size,
            shuffle=(split_name == "train"),
            num_workers=num_workers,
            pin_memory=False,
        )
        x_batch, y_batch = next(iter(loader))
        outputs.append(
            f"{split_name}: batch_x={tuple(x_batch.shape)} batch_y={tuple(y_batch.shape)}"
        )

    return " | ".join(outputs)


def parse_args() -> argparse.Namespace:
    """Define los parametros CLI de la fase 2."""

    parser = argparse.ArgumentParser(
        description="Fase 2 - Preparacion de split train/val/test y verificacion de dataloaders."
    )
    parser.add_argument("--dataset-root", type=Path, default=Path("gatos_perros_pandas"))
    parser.add_argument("--output-dir", type=Path, default=Path("phase2_artifacts"))
    parser.add_argument("--train-ratio", type=float, default=0.7)
    parser.add_argument("--val-ratio", type=float, default=0.15)
    parser.add_argument("--test-ratio", type=float, default=0.15)
    parser.add_argument("--seed", type=int, default=42)
    parser.add_argument(
        "--dedupe-mode",
        choices=("none", "name_size", "sha1"),
        default="sha1",
    )
    parser.add_argument("--verify-dataloaders", action="store_true")
    parser.add_argument("--image-size", type=int, default=224)
    parser.add_argument("--batch-size", type=int, default=32)
    parser.add_argument("--num-workers", type=int, default=0)
    parser.add_argument("--debug", action="store_true")
    return parser.parse_args()


def main() -> int:
    """Orquesta limpieza, deduplicacion, split y exportacion del manifest."""

    args = parse_args()
    dataset_root = args.dataset_root.resolve()
    output_dir = args.output_dir.resolve()
    start_total = time.perf_counter()

    if not dataset_root.exists() or not dataset_root.is_dir():
        console.print(f"[red]ERROR:[/red] ruta de dataset no valida: {dataset_root}")
        return 1

    console.rule("Fase 2 - Preparacion de datos")
    console.print(f"[bold]Dataset root:[/bold] {dataset_root}")
    console.print(f"[bold]Output dir:[/bold] {output_dir}")
    console.print(f"[bold]Seed:[/bold] {args.seed}")
    console.print(f"[bold]Dedupe mode:[/bold] {args.dedupe_mode}")
    console.print(
        f"[bold]Ratios:[/bold] train={args.train_ratio}, val={args.val_ratio}, test={args.test_ratio}"
    )

    with console.status("Escaneando y etiquetando imagenes..."):
        labeled, unknown_count = list_labeled_images(dataset_root, debug=args.debug)
    if not labeled:
        console.print("[red]ERROR:[/red] no se encontraron imagenes etiquetadas en cats/dogs/panda.")
        return 1
    console.print(
        f"[green]OK[/green] Imagenes etiquetadas: {len(labeled)} | sin clase: {unknown_count}"
    )

    with console.status("Eliminando duplicados..."):
        deduped, removed = dedupe_records(labeled, args.dedupe_mode, debug=args.debug)
    console.print(f"[green]OK[/green] Duplicados removidos: {removed}")

    with console.status("Generando split estratificado..."):
        records = split_stratified(
            deduped,
            train_ratio=args.train_ratio,
            val_ratio=args.val_ratio,
            test_ratio=args.test_ratio,
            seed=args.seed,
        )
    console.print(f"[green]OK[/green] Registros para split: {len(records)}")

    manifest_path = output_dir / "dataset_split.csv"
    with console.status("Exportando manifest CSV..."):
        export_manifest(records, dataset_root, manifest_path)
    console.print(f"[green]OK[/green] Manifest CSV: {manifest_path}")

    print_summary(records)

    if args.verify_dataloaders:
        console.rule("Verificacion de dataloaders")
        with console.status("Creando dataloaders y leyendo primer batch por split..."):
            msg = verify_dataloaders(
                records=records,
                image_size=args.image_size,
                batch_size=args.batch_size,
                num_workers=args.num_workers,
            )
        console.print(msg)

    elapsed = time.perf_counter() - start_total
    console.print(f"\n[bold green]Estado Fase 2: OK[/bold green] ({elapsed:.2f}s)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
