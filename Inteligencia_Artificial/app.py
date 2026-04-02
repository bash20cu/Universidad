"""Aplicacion Flask con dashboard en tiempo real y ejecucion de fases."""

from __future__ import annotations

import re
import subprocess
import sys
import threading
import time
import uuid
from datetime import datetime
from pathlib import Path
from typing import Any

from flask import Flask, jsonify, redirect, render_template, request, url_for
from flask_socketio import SocketIO
from werkzeug.utils import secure_filename

from core.model import load_model_from_checkpoint, predict_image
from core.stats import build_dashboard_stats

try:
    import psutil
except ImportError:  # pragma: no cover
    psutil = None

BASE_DIR = Path(__file__).resolve().parent
DEFAULT_MANIFEST = BASE_DIR / "phase2_artifacts" / "dataset_split.csv"
DEFAULT_METRICS = BASE_DIR / "phase3_artifacts" / "metrics_history.json"
DEFAULT_PHASE3_EXAMPLES = BASE_DIR / "phase3_artifacts" / "classification_examples_test.json"
DEFAULT_PHASE4_METRICS = BASE_DIR / "phase4_artifacts" / "metrics_test.json"
DEFAULT_CHECKPOINT = BASE_DIR / "phase3_artifacts" / "best_checkpoint.pt"
UPLOADS_DIR = BASE_DIR / "uploads"
UPLOADS_DIR.mkdir(parents=True, exist_ok=True)

app = Flask(__name__)
socketio = SocketIO(app, cors_allowed_origins="*", async_mode="threading")

TASKS: dict[str, dict[str, Any]] = {}
TASKS_LOCK = threading.Lock()
RESOURCE_MONITORS: dict[str, threading.Event] = {}


def now_iso() -> str:
    """Devuelve la fecha actual en formato ISO UTC."""

    return datetime.utcnow().isoformat(timespec="seconds") + "Z"


def resolve_user_path(raw_path: str) -> Path:
    """Resuelve rutas ingresadas por el usuario respecto al proyecto cuando son relativas."""

    candidate = Path(raw_path).expanduser()
    if candidate.is_absolute():
        return candidate.resolve()
    return (BASE_DIR / candidate).resolve()


def parse_task_hint(task: dict[str, Any], line: str) -> tuple[str | None, float | None]:
    """Intenta inferir etapa actual y progreso aproximado a partir del log recibido."""

    task_type = task["task_type"]
    progress_pct = task.get("progress_pct")
    text = line.strip()
    lower = text.lower()

    phase_hints = {
        "phase1": [
            ("check de dataset", 18.0),
            ("validacion basica", 48.0),
            ("preprocesamiento basico", 72.0),
            ("prueba de backend", 88.0),
        ],
        "phase2": [
            ("escaneando y etiquetando imagenes", 18.0),
            ("eliminando duplicados", 42.0),
            ("generando split estratificado", 68.0),
            ("exportando manifest csv", 88.0),
        ],
        "phase3_evidence": [
            ("fase 3 - evidencia", 30.0),
        ],
        "phase4_evaluate": [
            ("fase 4 - evaluacion", 22.0),
            ("ok artefactos guardados", 96.0),
        ],
    }

    for snippet, pct in phase_hints.get(task_type, []):
        if snippet in lower:
            return text, pct

    if task_type == "phase3_train":
        match = re.search(r"epoch=(\d+)", text)
        total_match = re.search(r"--epochs\s+(\d+)", " ".join(task.get("cmd", [])))
        if match:
            current_epoch = int(match.group(1))
            total_epochs = int(total_match.group(1)) if total_match else 0
            if total_epochs > 0:
                pct = min(96.0, 8.0 + (current_epoch / total_epochs) * 84.0)
                return f"Entrenando época {current_epoch} de {total_epochs}", pct
        if "entrenando epocas" in lower:
            return "Entrenamiento en curso", progress_pct or 12.0

    if text.startswith("ERROR"):
        return text, progress_pct
    return None, progress_pct


def parse_phase1_details(logs: list[str]) -> dict[str, Any]:
    """Convierte la salida textual de la fase 1 en datos mas faciles de visualizar."""

    details: dict[str, Any] = {
        "dataset_root": None,
        "status_text": None,
        "backend_text": None,
        "backend_name": None,
        "tensor_shape": None,
        "total_images": None,
        "validated_images": None,
        "validate_max": None,
        "processed_images": None,
        "sample_size": None,
        "read_errors": None,
        "unknown_class": None,
        "class_counts": {},
        "stage_order": [
            "check de dataset",
            "validacion basica",
            "preprocesamiento basico",
            "prueba de backend",
        ],
        "completed_stages": [],
        "show_backend_stage": False,
    }

    completed: list[str] = []

    for raw_line in logs:
        line = raw_line.strip()
        lower = line.lower()
        if not line:
            continue

        if "=== fase 1: check de dataset" in lower and "check de dataset" not in completed:
            completed.append("check de dataset")
        elif "=== validacion basica" in lower and "validacion basica" not in completed:
            completed.append("validacion basica")
        elif "=== preprocesamiento basico" in lower and "preprocesamiento basico" not in completed:
            completed.append("preprocesamiento basico")
        elif "=== prueba de backend" in lower:
            details["show_backend_stage"] = True
            if "prueba de backend" not in completed:
                completed.append("prueba de backend")

        if line.startswith("Dataset root:"):
            details["dataset_root"] = line.split(":", 1)[1].strip()
        elif line.startswith("Total imagenes encontradas:"):
            details["total_images"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Conteo cats:"):
            details["class_counts"]["cats"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Conteo dogs:"):
            details["class_counts"]["dogs"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Conteo panda:"):
            details["class_counts"]["panda"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Sin clase detectada:"):
            details["unknown_class"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Imagenes validadas"):
            match = re.search(r"max\s+(\d+)\):\s*(\d+)", line)
            if match:
                details["validate_max"] = int(match.group(1))
                details["validated_images"] = int(match.group(2))
        elif line.startswith("Errores de lectura:"):
            details["read_errors"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Imagenes procesadas"):
            match = re.search(r"muestra\s+(\d+)\):\s*(\d+)", line)
            if match:
                details["sample_size"] = int(match.group(1))
                details["processed_images"] = int(match.group(2))
        elif line.startswith("Ultima forma de tensor:"):
            details["tensor_shape"] = line.split(":", 1)[1].strip()
        elif line.startswith("Backend detectado:"):
            details["backend_text"] = line
            backend_name = line.split(":", 1)[1].split(".", 1)[0].strip()
            details["backend_name"] = backend_name
        elif line.startswith("Estado Fase 1:"):
            details["status_text"] = line.split(":", 1)[1].strip()

    details["completed_stages"] = completed
    return details


def parse_phase2_details(logs: list[str]) -> dict[str, Any]:
    """Convierte la salida textual de la fase 2 en datos legibles para la UI."""

    details: dict[str, Any] = {
        "dataset_root": None,
        "output_dir": None,
        "status_text": None,
        "manifest_csv": None,
        "dedupe_mode": None,
        "total_scanned": None,
        "duplicates_removed": None,
        "records_after_dedupe": None,
        "train_count": None,
        "val_count": None,
        "test_count": None,
        "batch_size": None,
        "verify_dataloaders": None,
        "class_counts": {},
        "completed_stages": [],
        "show_verify_stage": False,
    }

    completed: list[str] = []

    for raw_line in logs:
        line = raw_line.strip()
        lower = line.lower()
        if not line:
            continue

        if "escaneando y etiquetando imagenes" in lower and "escaneo" not in completed:
            completed.append("escaneo")
        elif "eliminando duplicados" in lower and "deduplicacion" not in completed:
            completed.append("deduplicacion")
        elif "generando split estratificado" in lower and "split" not in completed:
            completed.append("split")
        elif "exportando manifest csv" in lower and "manifest" not in completed:
            completed.append("manifest")
        elif "verificando dataloaders" in lower:
            details["show_verify_stage"] = True
            if "verificacion" not in completed:
                completed.append("verificacion")

        if line.startswith("Dataset root:"):
            details["dataset_root"] = line.split(":", 1)[1].strip()
        elif line.startswith("Output dir:"):
            details["output_dir"] = line.split(":", 1)[1].strip()
        elif line.startswith("Dedupe mode:"):
            details["dedupe_mode"] = line.split(":", 1)[1].strip()
        elif line.startswith("Batch size:"):
            try:
                details["batch_size"] = int(line.split(":", 1)[1].strip())
            except ValueError:
                pass
        elif line.startswith("Verify dataloaders:"):
            details["verify_dataloaders"] = line.split(":", 1)[1].strip().lower() == "true"
        elif line.startswith("Imagenes encontradas:"):
            details["total_scanned"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Conteo cats:"):
            details["class_counts"]["cats"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Conteo dogs:"):
            details["class_counts"]["dogs"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Conteo panda:"):
            details["class_counts"]["panda"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Duplicados eliminados:"):
            details["duplicates_removed"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Registros finales:"):
            details["records_after_dedupe"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Train:"):
            details["train_count"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Val:"):
            details["val_count"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Test:"):
            details["test_count"] = int(line.split(":", 1)[1].strip())
        elif line.startswith("Manifest CSV:"):
            details["manifest_csv"] = line.split(":", 1)[1].strip()
        elif line.startswith("Estado Fase 2:"):
            details["status_text"] = line.split(":", 1)[1].strip()

    details["completed_stages"] = completed
    return details


def parse_phase3_details(task: dict[str, Any], logs: list[str]) -> dict[str, Any]:
    """Convierte la salida textual de la fase 3 en datos utiles para una vista visual."""

    cmd = task.get("cmd", [])

    def cmd_value(flag: str) -> str | None:
        if flag not in cmd:
            return None
        idx = cmd.index(flag)
        if idx + 1 >= len(cmd):
            return None
        return cmd[idx + 1]

    details: dict[str, Any] = {
        "manifest_csv": cmd_value("--manifest-csv"),
        "output_dir": cmd_value("--output-dir"),
        "epochs": int(cmd_value("--epochs") or 0) or None,
        "batch_size": int(cmd_value("--batch-size") or 0) or None,
        "image_size": int(cmd_value("--image-size") or 0) or None,
        "device_name": None,
        "train_count": None,
        "val_count": None,
        "test_count": None,
        "current_epoch": None,
        "last_train_loss": None,
        "last_train_acc": None,
        "last_val_loss": None,
        "last_val_acc": None,
        "best_epoch": None,
        "best_val_acc": None,
        "best_checkpoint": None,
        "last_checkpoint": None,
        "metrics_history": None,
        "status_text": None,
        "completed_stages": [],
    }

    completed: list[str] = []

    for raw_line in logs:
        line = raw_line.strip()
        lower = line.lower()
        if not line:
            continue

        if "fase 3 - entrenamiento cnn" in lower and "inicio" not in completed:
            completed.append("inicio")
        elif "registros -> train=" in lower and "carga" not in completed:
            completed.append("carga")
        elif "epoch=" in lower and "entrenamiento" not in completed:
            completed.append("entrenamiento")
        elif "fase 3 - resultado de entrenamiento" in lower and "resumen" not in completed:
            completed.append("resumen")

        if line.startswith("Manifest:"):
            details["manifest_csv"] = line.split(":", 1)[1].strip()
        elif line.startswith("Output:"):
            details["output_dir"] = line.split(":", 1)[1].strip()
        elif line.startswith("Device:"):
            details["device_name"] = line.split(":", 1)[1].strip()
        elif "Registros -> train=" in line:
            match = re.search(r"train=(\d+),\s*val=(\d+),\s*test=(\d+)", line)
            if match:
                details["train_count"] = int(match.group(1))
                details["val_count"] = int(match.group(2))
                details["test_count"] = int(match.group(3))
        elif line.startswith("DEBUG: epoch="):
            match = re.search(
                r"epoch=(\d+)\s+train_loss=([0-9.]+)\s+train_acc=([0-9.]+)\s+val_loss=([0-9.]+)\s+val_acc=([0-9.]+)",
                line,
            )
            if match:
                details["current_epoch"] = int(match.group(1))
                details["last_train_loss"] = float(match.group(2))
                details["last_train_acc"] = float(match.group(3))
                details["last_val_loss"] = float(match.group(4))
                details["last_val_acc"] = float(match.group(5))
        elif line.startswith("Best epoch"):
            match = re.search(r"Best epoch\s*[│|]?\s*(\d+)", line)
            if match:
                details["best_epoch"] = int(match.group(1))
        elif line.startswith("Best val_acc"):
            match = re.search(r"Best val_acc\s*[│|]?\s*([0-9.]+)", line)
            if match:
                details["best_val_acc"] = float(match.group(1))
        elif line.startswith("Ultimo train_acc"):
            match = re.search(r"Ultimo train_acc\s*[│|]?\s*([0-9.]+)", line)
            if match:
                details["last_train_acc"] = float(match.group(1))
        elif line.startswith("Ultimo val_acc"):
            match = re.search(r"Ultimo val_acc\s*[│|]?\s*([0-9.]+)", line)
            if match:
                details["last_val_acc"] = float(match.group(1))
        elif line.startswith("best_checkpoint"):
            value = line.split("│")[-1].strip() if "│" in line else line.split(":", 1)[-1].strip()
            details["best_checkpoint"] = value
        elif line.startswith("last_checkpoint"):
            value = line.split("│")[-1].strip() if "│" in line else line.split(":", 1)[-1].strip()
            details["last_checkpoint"] = value
        elif line.startswith("metrics_history"):
            value = line.split("│")[-1].strip() if "│" in line else line.split(":", 1)[-1].strip()
            details["metrics_history"] = value
        elif "Estado Fase 3: OK" in line:
            details["status_text"] = "OK"

    details["completed_stages"] = completed
    return details


def parse_phase4_details(task: dict[str, Any], logs: list[str]) -> dict[str, Any]:
    """Convierte la salida textual de la fase 4 en datos claros para la interfaz."""

    cmd = task.get("cmd", [])

    def cmd_value(flag: str) -> str | None:
        if flag not in cmd:
            return None
        idx = cmd.index(flag)
        if idx + 1 >= len(cmd):
            return None
        return cmd[idx + 1]

    details: dict[str, Any] = {
        "manifest_csv": cmd_value("--manifest-csv"),
        "checkpoint_path": cmd_value("--checkpoint-path"),
        "output_dir": cmd_value("--output-dir"),
        "split": cmd_value("--split"),
        "image_size": int(cmd_value("--image-size") or 0) or None,
        "device_name": None,
        "checkpoint_epoch": None,
        "samples": None,
        "artifact_dir": None,
        "status_text": None,
        "completed_stages": [],
    }

    completed: list[str] = []

    for raw_line in logs:
        line = raw_line.strip()
        lower = line.lower()
        if not line:
            continue

        if "fase 4 - evaluacion" in lower and "inicio" not in completed:
            completed.append("inicio")
        elif line.startswith("Split:") and "configuracion" not in completed:
            completed.append("configuracion")
        elif line.startswith("Samples:") and "evaluacion" not in completed:
            completed.append("evaluacion")
        elif "metricas globales" in lower and "metricas" not in completed:
            completed.append("metricas")
        elif "reporte por clase" in lower and "reporte" not in completed:
            completed.append("reporte")
        elif "artefactos guardados en:" in lower and "artefactos" not in completed:
            completed.append("artefactos")

        if line.startswith("Split:"):
            details["split"] = line.split(":", 1)[1].strip()
        elif line.startswith("Device:"):
            details["device_name"] = line.split(":", 1)[1].strip()
        elif line.startswith("Checkpoint epoch:"):
            details["checkpoint_epoch"] = line.split(":", 1)[1].strip()
        elif line.startswith("Samples:"):
            try:
                details["samples"] = int(line.split(":", 1)[1].strip())
            except ValueError:
                pass
        elif "Artefactos guardados en:" in line:
            details["artifact_dir"] = line.split(":", 1)[1].strip()
            details["status_text"] = "OK"

    details["completed_stages"] = completed
    return details


def parse_phase3_evidence_details(task: dict[str, Any], logs: list[str]) -> dict[str, Any]:
    """Convierte la salida textual de evidencia automatica en datos legibles para la UI."""

    cmd = task.get("cmd", [])

    def cmd_value(flag: str) -> str | None:
        if flag not in cmd:
            return None
        idx = cmd.index(flag)
        if idx + 1 >= len(cmd):
            return None
        return cmd[idx + 1]

    details: dict[str, Any] = {
        "manifest_csv": cmd_value("--manifest-csv"),
        "checkpoint_path": cmd_value("--checkpoint-path"),
        "output_dir": cmd_value("--output-dir"),
        "split": cmd_value("--split"),
        "samples_per_class": int(cmd_value("--samples-per-class") or 0) or None,
        "image_size": int(cmd_value("--image-size") or 0) or None,
        "device_name": None,
        "checkpoint_epoch": None,
        "json_path": None,
        "csv_path": None,
        "rows_count": None,
        "completed_stages": [],
    }

    completed: list[str] = []
    rows_count = 0

    pending_key: str | None = None

    for raw_line in logs:
        line = raw_line.strip()
        lower = line.lower()
        if not line:
            continue

        if pending_key and (":" in line or line.startswith(str(BASE_DIR.drive)) or "\\" in line or "/" in line):
            details[pending_key] = line
            pending_key = None
            continue

        if "fase 3 - evidencia de clasificacion" in lower and "inicio" not in completed:
            completed.append("inicio")
        elif line.startswith("Clase real") and "predicciones" not in completed:
            completed.append("predicciones")
        elif line.startswith("Device:") and "device" not in completed:
            completed.append("device")
        elif "Evidencia JSON:" in line and "artefactos" not in completed:
            completed.append("artefactos")

        if line.startswith("Clase real") or line.startswith("Prediccion") or line.startswith("Confidence") or line.startswith("Correcta"):
            continue
        if line.startswith("Device:"):
            details["device_name"] = line.split(":", 1)[1].strip()
        elif line.startswith("Checkpoint epoch:"):
            details["checkpoint_epoch"] = line.split(":", 1)[1].strip()
        elif "Evidencia JSON:" in line:
            value = line.split(":", 1)[1].strip()
            if value:
                details["json_path"] = value
            else:
                pending_key = "json_path"
        elif "Evidencia CSV:" in line:
            value = line.split(":", 1)[1].strip()
            if value:
                details["csv_path"] = value
            else:
                pending_key = "csv_path"
        elif re.match(r"^[│\s]*[a-zA-Z]+[│\s]+[a-zA-Z]+", line) and "Fase 3 -" not in line:
            rows_count += 1

    details["rows_count"] = rows_count or None
    details["completed_stages"] = completed
    return details


def extract_task_details(task: dict[str, Any]) -> dict[str, Any]:
    """Genera detalles estructurados por tipo de tarea a partir del log acumulado."""

    logs = task.get("logs", [])
    if task.get("task_type") == "phase1":
        return {"phase1": parse_phase1_details(logs)}
    if task.get("task_type") == "phase2":
        return {"phase2": parse_phase2_details(logs)}
    if task.get("task_type") == "phase3_train":
        return {"phase3": parse_phase3_details(task, logs)}
    if task.get("task_type") == "phase3_evidence":
        return {"phase3_evidence": parse_phase3_evidence_details(task, logs)}
    if task.get("task_type") == "phase4_evaluate":
        return {"phase4": parse_phase4_details(task, logs)}
    return {}


def summarize_task(task: dict[str, Any]) -> dict[str, Any]:
    """Construye la vista serializable que usa la API y el canal WebSocket."""

    logs = task.get("logs", [])
    return {
        "id": task["id"],
        "task_type": task["task_type"],
        "status": task["status"],
        "returncode": task["returncode"],
        "created_at": task["created_at"],
        "updated_at": task["updated_at"],
        "cmd": task["cmd"],
        "progress_pct": task.get("progress_pct"),
        "current_step": task.get("current_step"),
        "elapsed_seconds": task.get("elapsed_seconds", 0.0),
        "resource_usage": task.get("resource_usage", {}),
        "log_text": "\n".join(logs),
        "log_tail": logs[-18:],
        "details": extract_task_details(task),
    }


def emit_task_update(task_id: str) -> None:
    """Emite por WebSocket el estado mas reciente de una tarea."""

    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return
        payload = summarize_task(task)
    socketio.emit("task_update", payload)


def get_live_system_metrics() -> dict[str, Any]:
    """Obtiene una muestra de CPU y memoria del equipo anfitrion."""

    if psutil is None:
        return {"available": False}

    vm = psutil.virtual_memory()
    return {
        "available": True,
        "cpu_percent": round(psutil.cpu_percent(interval=None), 1),
        "memory_percent": round(vm.percent, 1),
        "memory_used_gb": round(vm.used / (1024 ** 3), 2),
        "memory_total_gb": round(vm.total / (1024 ** 3), 2),
    }


def emit_system_metrics() -> None:
    """Publica una muestra instantanea del sistema para el dashboard."""

    socketio.emit("system_metrics", get_live_system_metrics())


def build_task_command(task_type: str, payload: dict[str, Any]) -> list[str]:
    """Construye el comando CLI para ejecutar una fase concreta del proyecto."""

    cmd = [sys.executable]

    if task_type == "phase1":
        cmd += ["-m", "core.phase1", "--dataset-root", payload.get("dataset_root", "gatos_perros_pandas")]
        cmd += ["--validate-max", str(payload.get("validate_max", 300))]
        cmd += ["--sample-size", str(payload.get("sample_size", 64))]
        if payload.get("gpu_test"):
            cmd += ["--gpu-test", "--matmul-size", str(payload.get("matmul_size", 512))]
        return cmd

    if task_type == "phase2":
        cmd += ["-m", "core.phase2", "--dataset-root", payload.get("dataset_root", "gatos_perros_pandas")]
        cmd += ["--output-dir", payload.get("output_dir", "phase2_artifacts")]
        cmd += ["--dedupe-mode", payload.get("dedupe_mode", "sha1")]
        cmd += ["--batch-size", str(payload.get("batch_size", 16))]
        if payload.get("verify_dataloaders", True):
            cmd += ["--verify-dataloaders"]
        if payload.get("debug", True):
            cmd += ["--debug"]
        return cmd

    if task_type == "phase3_train":
        cmd += ["-m", "core.phase3_train", "--manifest-csv", payload.get("manifest_csv", "phase2_artifacts/dataset_split.csv")]
        cmd += ["--output-dir", payload.get("output_dir", "phase3_artifacts")]
        cmd += ["--epochs", str(payload.get("epochs", 5))]
        cmd += ["--batch-size", str(payload.get("batch_size", 32))]
        cmd += ["--image-size", str(payload.get("image_size", 128))]
        if payload.get("debug", True):
            cmd += ["--debug"]
        return cmd

    if task_type == "phase3_infer":
        image_path = payload.get("image_path", "")
        if not image_path:
            raise ValueError("image_path es requerido para phase3_infer")
        cmd += ["-m", "core.phase3_infer", "--image-path", image_path]
        cmd += ["--checkpoint-path", payload.get("checkpoint_path", "phase3_artifacts/best_checkpoint.pt")]
        cmd += ["--image-size", str(payload.get("image_size", 128))]
        return cmd

    if task_type == "phase3_evidence":
        cmd += ["-m", "core.phase3_evidence"]
        cmd += ["--manifest-csv", payload.get("manifest_csv", "phase2_artifacts/dataset_split.csv")]
        cmd += ["--checkpoint-path", payload.get("checkpoint_path", "phase3_artifacts/best_checkpoint.pt")]
        cmd += ["--output-dir", payload.get("output_dir", "phase3_artifacts")]
        cmd += ["--split", payload.get("split", "test")]
        cmd += ["--samples-per-class", str(payload.get("samples_per_class", 1))]
        cmd += ["--image-size", str(payload.get("image_size", 128))]
        return cmd

    if task_type == "phase4_evaluate":
        cmd += ["-m", "core.phase4_evaluate"]
        cmd += ["--manifest-csv", payload.get("manifest_csv", "phase2_artifacts/dataset_split.csv")]
        cmd += ["--checkpoint-path", payload.get("checkpoint_path", "phase3_artifacts/best_checkpoint.pt")]
        cmd += ["--output-dir", payload.get("output_dir", "phase4_artifacts")]
        cmd += ["--split", payload.get("split", "test")]
        cmd += ["--image-size", str(payload.get("image_size", 128))]
        return cmd

    raise ValueError(f"task_type no soportado: {task_type}")


def append_log(task_id: str, line: str) -> None:
    """Agrega una linea al buffer de logs, actualiza hint de progreso y emite cambios."""

    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return

        clean_line = line.rstrip("\n")
        task["logs"].append(clean_line)
        if len(task["logs"]) > 4000:
            task["logs"] = task["logs"][-4000:]
        task["updated_at"] = now_iso()

        current_step, progress_pct = parse_task_hint(task, clean_line)
        if current_step:
            task["current_step"] = current_step
        if progress_pct is not None:
            task["progress_pct"] = progress_pct

    emit_task_update(task_id)


def set_task_status(task_id: str, status: str, returncode: int | None = None) -> None:
    """Actualiza el estado de una tarea y publica el cambio a los clientes conectados."""

    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return
        task["status"] = status
        task["updated_at"] = now_iso()
        if returncode is not None:
            task["returncode"] = returncode
        if status == "queued":
            task["progress_pct"] = 2.0
            task["current_step"] = "Tarea en cola"
        elif status == "running":
            task["progress_pct"] = max(float(task.get("progress_pct") or 0.0), 8.0)
            task["current_step"] = task.get("current_step") or "Proceso iniciado"
        elif status == "success":
            task["progress_pct"] = 100.0
            task["current_step"] = "Proceso completado"
        elif status == "failed":
            task["progress_pct"] = task.get("progress_pct") or 100.0
            task["current_step"] = task.get("current_step") or "Proceso fallido"

    emit_task_update(task_id)


def monitor_task_resources(task_id: str, proc: subprocess.Popen[str], stop_event: threading.Event) -> None:
    """Muestra uso de CPU y memoria del proceso de tarea mientras siga vivo."""

    if psutil is None:
        return

    try:
        process = psutil.Process(proc.pid)
        process.cpu_percent(interval=None)
    except (psutil.Error, AttributeError):
        return

    while not stop_event.wait(1.0):
        try:
            with TASKS_LOCK:
                task = TASKS.get(task_id)
                if not task:
                    return
                task["elapsed_seconds"] = round(time.time() - task["started_at_ts"], 1)
                task["resource_usage"] = {
                    "cpu_percent": round(process.cpu_percent(interval=None), 1),
                    "memory_mb": round(process.memory_info().rss / (1024 ** 2), 1),
                }
                task["updated_at"] = now_iso()
            emit_task_update(task_id)
            emit_system_metrics()
        except (psutil.Error, AttributeError):
            return


def run_task_thread(task_id: str, cmd: list[str]) -> None:
    """Ejecuta una tarea en segundo plano, captura logs y publica actualizaciones en vivo."""

    set_task_status(task_id, "running")
    append_log(task_id, f"$ {' '.join(cmd)}")
    stop_event = threading.Event()
    try:
        proc = subprocess.Popen(
            cmd,
            cwd=BASE_DIR,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            bufsize=1,
        )

        with TASKS_LOCK:
            task = TASKS.get(task_id)
            if task:
                task["pid"] = proc.pid

        monitor = threading.Thread(
            target=monitor_task_resources,
            args=(task_id, proc, stop_event),
            daemon=True,
        )
        RESOURCE_MONITORS[task_id] = stop_event
        monitor.start()

        assert proc.stdout is not None
        for line in proc.stdout:
            append_log(task_id, line)

        proc.wait()
        stop_event.set()
        status = "success" if proc.returncode == 0 else "failed"
        set_task_status(task_id, status, proc.returncode)
    except Exception as exc:  # pragma: no cover
        stop_event.set()
        append_log(task_id, f"ERROR ejecutando tarea: {exc}")
        set_task_status(task_id, "failed", 1)
    finally:
        RESOURCE_MONITORS.pop(task_id, None)
        emit_system_metrics()


def create_task(task_type: str, payload: dict[str, Any]) -> dict[str, Any]:
    """Registra una tarea, la arranca en un hilo y devuelve su estado inicial."""

    cmd = build_task_command(task_type, payload)
    task_id = uuid.uuid4().hex[:12]
    now_ts = time.time()
    task = {
        "id": task_id,
        "task_type": task_type,
        "status": "queued",
        "returncode": None,
        "cmd": cmd,
        "logs": [],
        "created_at": now_iso(),
        "updated_at": now_iso(),
        "started_at_ts": now_ts,
        "elapsed_seconds": 0.0,
        "progress_pct": 2.0,
        "current_step": "Tarea en cola",
        "resource_usage": {},
    }
    with TASKS_LOCK:
        TASKS[task_id] = task
    worker = threading.Thread(target=run_task_thread, args=(task_id, cmd), daemon=True)
    worker.start()
    emit_task_update(task_id)
    return task


@socketio.on("connect")
def handle_connect() -> None:
    """Entrega un estado inicial al cliente que acaba de conectarse."""

    emit_system_metrics()
    with TASKS_LOCK:
        tasks = [summarize_task(task) for task in TASKS.values()]
    socketio.emit("task_snapshot", tasks)


@app.get("/")
def home():
    return redirect(url_for("dashboard"))


@app.get("/dashboard")
def dashboard():
    stats = build_dashboard_stats(
        DEFAULT_MANIFEST,
        DEFAULT_METRICS,
        DEFAULT_PHASE4_METRICS,
        DEFAULT_PHASE3_EXAMPLES,
    )
    return render_template(
        "dashboard.html",
        manifest_path=str(DEFAULT_MANIFEST),
        metrics_path=str(DEFAULT_METRICS),
        checkpoint_path=str(DEFAULT_CHECKPOINT),
        stats=stats,
    )


@app.get("/api/stats")
def api_stats():
    """Devuelve el resumen principal de artefactos consumido por el dashboard."""

    stats = build_dashboard_stats(
        DEFAULT_MANIFEST,
        DEFAULT_METRICS,
        DEFAULT_PHASE4_METRICS,
        DEFAULT_PHASE3_EXAMPLES,
    )
    return jsonify(stats)


@app.get("/api/system-metrics")
def api_system_metrics():
    """Expone una lectura puntual del estado de CPU y memoria del host."""

    return jsonify(get_live_system_metrics())


@app.post("/api/predict-upload")
def api_predict_upload():
    """Procesa una imagen subida por el usuario y devuelve la inferencia del checkpoint."""

    image_file = request.files.get("image")
    checkpoint_path_raw = request.form.get("checkpoint_path", str(DEFAULT_CHECKPOINT)).strip()
    image_size_raw = request.form.get("image_size", "128").strip()

    if not image_file or not image_file.filename:
        return jsonify({"ok": False, "error": "Debes seleccionar una imagen."}), 400

    try:
        image_size = int(image_size_raw)
    except ValueError:
        return jsonify({"ok": False, "error": "image_size debe ser entero."}), 400

    checkpoint_path = resolve_user_path(checkpoint_path_raw)
    if not checkpoint_path.exists():
        return jsonify({"ok": False, "error": f"Checkpoint no encontrado: {checkpoint_path}"}), 400

    safe_name = secure_filename(image_file.filename)
    if not safe_name:
        safe_name = f"upload_{uuid.uuid4().hex[:8]}.jpg"
    save_path = UPLOADS_DIR / f"{uuid.uuid4().hex[:8]}_{safe_name}"
    image_file.save(save_path)

    try:
        model, device, device_name, checkpoint = load_model_from_checkpoint(checkpoint_path)
        result = predict_image(
            model=model,
            device=device,
            image_path=save_path,
            image_size=image_size,
        )
        return jsonify(
            {
                "ok": True,
                "result": {
                    "pred_label": result["pred_label"],
                    "confidence": result["confidence"],
                    "probabilities": result["probabilities"],
                    "device_name": device_name,
                    "checkpoint_epoch": checkpoint.get("epoch", "n/a"),
                    "saved_path": str(save_path),
                },
            }
        )
    except Exception as exc:
        return jsonify({"ok": False, "error": str(exc)}), 500


@app.post("/api/run")
def api_run():
    """Arranca una fase del proyecto y devuelve el identificador de tarea creado."""

    data = request.get_json(silent=True) or {}
    task_type = str(data.get("task_type", "")).strip()
    payload = data.get("payload", {}) or {}
    try:
        task = create_task(task_type, payload)
        return jsonify({"ok": True, "task_id": task["id"]})
    except Exception as exc:
        return jsonify({"ok": False, "error": str(exc)}), 400


@app.get("/api/task/<task_id>")
def api_task(task_id: str):
    """Devuelve el estado actual de una tarea en formato JSON."""

    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return jsonify({"ok": False, "error": "task no encontrada"}), 404
        return jsonify({"ok": True, **summarize_task(task)})


if __name__ == "__main__":
    socketio.run(app, host="127.0.0.1", port=5000, debug=True)
