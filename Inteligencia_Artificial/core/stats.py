"""Estadisticas y agregaciones reutilizables para dashboard y scripts.

La idea de este modulo es centralizar la lectura de artefactos para que la UI
no tenga que conocer detalles de archivos JSON o manifests.
"""

from __future__ import annotations

import json
from pathlib import Path
from typing import Any

from .manifest import load_manifest, summarize_manifest

TECH_LIMITATIONS = [
    "Generalizacion limitada a 3 clases cerradas (cats, dogs, panda).",
    "La CNN base puede quedarse corta ante variaciones fuertes de fondo, luz y pose.",
    "El dataset original tenia duplicados/estructura redundante; la limpieza mitiga pero no elimina todo sesgo.",
    "No hay calibracion formal de probabilidades ni umbral robusto de rechazo para clases desconocidas.",
    "En AMD DirectML algunas operaciones hacen fallback a CPU, afectando rendimiento y consistencia.",
]


def load_metrics_history(metrics_path: Path) -> list[dict[str, Any]]:
    """Lee el historial de entrenamiento si existe y devuelve una lista segura."""

    if not metrics_path.exists():
        return []
    with metrics_path.open("r", encoding="utf-8") as f:
        data = json.load(f)
    if isinstance(data, list):
        return data
    return []


def summarize_training(metrics_history: list[dict[str, Any]]) -> dict[str, Any]:
    """Extrae mejor epoca y ultimo estado a partir del historial de entrenamiento."""

    if not metrics_history:
        return {
            "epochs": 0,
            "best_val_acc": None,
            "best_epoch": None,
            "last_train_acc": None,
            "last_val_acc": None,
        }

    # La mejor epoca se decide por validacion porque es el criterio usado
    # tambien al guardar el best checkpoint.
    best = max(metrics_history, key=lambda r: float(r.get("val_acc", 0.0)))
    last = metrics_history[-1]
    return {
        "epochs": len(metrics_history),
        "best_val_acc": float(best.get("val_acc", 0.0)),
        "best_epoch": int(best.get("epoch", 0)),
        "last_train_acc": float(last.get("train_acc", 0.0)),
        "last_val_acc": float(last.get("val_acc", 0.0)),
    }


def load_phase4_metrics(metrics_path: Path) -> dict[str, Any]:
    """Lee el JSON de evaluacion final si esta disponible."""

    if not metrics_path.exists():
        return {}
    with metrics_path.open("r", encoding="utf-8") as f:
        data = json.load(f)
    return data if isinstance(data, dict) else {}


def load_classification_examples(examples_path: Path) -> list[dict[str, Any]]:
    """Lee ejemplos de clasificacion generados como evidencia automatica."""

    if not examples_path.exists():
        return []
    with examples_path.open("r", encoding="utf-8") as f:
        data = json.load(f)
    return data if isinstance(data, list) else []


def build_dashboard_stats(
    manifest_csv: Path,
    metrics_history_json: Path,
    phase4_metrics_json: Path | None = None,
    classification_examples_json: Path | None = None,
) -> dict[str, Any]:
    """Construye el paquete de datos que consume el dashboard Flask."""

    # Este punto centraliza todo lo que la app necesita para evitar duplicar
    # logica de lectura y resumen en distintas rutas o plantillas.
    manifest_items = load_manifest(manifest_csv) if manifest_csv.exists() else []
    manifest_summary = summarize_manifest(manifest_items) if manifest_items else {}
    metrics_history = load_metrics_history(metrics_history_json)
    training_summary = summarize_training(metrics_history)
    phase4_metrics = load_phase4_metrics(phase4_metrics_json) if phase4_metrics_json else {}
    classification_examples = (
        load_classification_examples(classification_examples_json)
        if classification_examples_json
        else []
    )
    return {
        "manifest_summary": manifest_summary,
        "training_summary": training_summary,
        "metrics_history": metrics_history,
        "phase4_metrics": phase4_metrics,
        "classification_examples": classification_examples,
        "technical_limitations": TECH_LIMITATIONS,
    }
