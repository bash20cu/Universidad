"""Fase 1: inspeccion y prueba basica de procesamiento de imagenes."""

from __future__ import annotations

import argparse
import time
from collections import Counter
from dataclasses import dataclass
from pathlib import Path
from typing import Iterable

import numpy as np
from PIL import Image, UnidentifiedImageError

from core.constants import IMAGE_EXTENSIONS
from core.labeling import detect_label
from core.model import explain_device_choice, select_device


@dataclass(frozen=True)
class ScanSummary:
    total_images: int
    per_class: Counter[str]
    unknown_class: int


def list_image_files(root: Path) -> list[Path]:
    return [
        file_path
        for file_path in root.rglob("*")
        if file_path.is_file() and file_path.suffix.lower() in IMAGE_EXTENSIONS
    ]


def scan_dataset(dataset_root: Path) -> tuple[ScanSummary, list[Path]]:
    image_files = list_image_files(dataset_root)
    per_class: Counter[str] = Counter(detect_label(path) for path in image_files)
    unknown_class = per_class.pop("unknown", 0)
    summary = ScanSummary(
        total_images=len(image_files),
        per_class=per_class,
        unknown_class=unknown_class,
    )
    return summary, image_files


def validate_images(paths: Iterable[Path], max_files: int) -> tuple[int, int]:
    ok_count = 0
    error_count = 0

    for image_path in list(paths)[:max_files]:
        try:
            with Image.open(image_path) as img:
                img.verify()
            ok_count += 1
        except (OSError, UnidentifiedImageError):
            error_count += 1

    return ok_count, error_count


def preprocess_sample(paths: Iterable[Path], sample_size: int) -> tuple[int, tuple[int, ...] | None]:
    processed = 0
    last_shape: tuple[int, ...] | None = None

    for image_path in list(paths)[:sample_size]:
        try:
            with Image.open(image_path) as img:
                rgb = img.convert("RGB").resize((224, 224))
                image_array = np.asarray(rgb, dtype=np.float32) / 255.0
            last_shape = image_array.shape
            processed += 1
        except (OSError, UnidentifiedImageError):
            continue

    return processed, last_shape


def run_compute_backend_test(size: int) -> str:
    try:
        import torch
    except ImportError:
        return "Torch no instalado. Backend de computo no probado."

    device, device_name = select_device(torch)
    device_note = explain_device_choice(torch)

    start = time.perf_counter()
    x = torch.rand((size, size), device=device)
    y = torch.rand((size, size), device=device)
    _ = x @ y
    elapsed_ms = (time.perf_counter() - start) * 1000
    return (
        f"Backend detectado: {device_name}. {device_note} "
        f"MatMul {size}x{size} en {elapsed_ms:.2f} ms."
    )


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(description="Fase 1 - Conteo y prueba basica de imagenes.")
    parser.add_argument("--dataset-root", type=Path, default=Path("gatos_perros_pandas"))
    parser.add_argument("--validate-max", type=int, default=300)
    parser.add_argument("--sample-size", type=int, default=64)
    parser.add_argument("--gpu-test", action="store_true")
    parser.add_argument("--matmul-size", type=int, default=1024)
    return parser.parse_args()


def main() -> int:
    args = parse_args()
    dataset_root = args.dataset_root.resolve()

    if not dataset_root.exists() or not dataset_root.is_dir():
        print(f"ERROR: ruta no valida: {dataset_root}")
        return 1

    summary, image_files = scan_dataset(dataset_root)

    print("=== FASE 1: CHECK DE DATASET ===")
    print(f"Dataset root: {dataset_root}")
    print(f"Total imagenes encontradas: {summary.total_images}")
    print(f"Conteo cats: {summary.per_class.get('cats', 0)}")
    print(f"Conteo dogs: {summary.per_class.get('dogs', 0)}")
    print(f"Conteo panda: {summary.per_class.get('panda', 0)}")
    print(f"Sin clase detectada: {summary.unknown_class}")

    ok_count, error_count = validate_images(image_files, args.validate_max)
    print("\n=== VALIDACION BASICA ===")
    print(f"Imagenes validadas (max {args.validate_max}): {ok_count}")
    print(f"Errores de lectura: {error_count}")

    processed, last_shape = preprocess_sample(image_files, args.sample_size)
    print("\n=== PREPROCESAMIENTO BASICO ===")
    print(f"Imagenes procesadas (muestra {args.sample_size}): {processed}")
    print(f"Ultima forma de tensor: {last_shape}")

    if args.gpu_test:
        print("\n=== PRUEBA DE BACKEND ===")
        print(run_compute_backend_test(args.matmul_size))

    print("\nEstado Fase 1: OK")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
