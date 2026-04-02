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
