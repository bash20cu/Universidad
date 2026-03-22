"""Aplicacion Flask con dashboard, ejecucion de fases y logs en vivo."""

from __future__ import annotations

import subprocess
import sys
import threading
import uuid
from datetime import datetime
from pathlib import Path
from typing import Any

from flask import Flask, jsonify, redirect, render_template, request, url_for
from werkzeug.utils import secure_filename

from core.model import load_model_from_checkpoint, predict_image
from core.stats import build_dashboard_stats

BASE_DIR = Path(__file__).resolve().parent
DEFAULT_MANIFEST = BASE_DIR / "phase2_artifacts" / "dataset_split.csv"
DEFAULT_METRICS = BASE_DIR / "phase3_artifacts" / "metrics_history.json"
DEFAULT_PHASE3_EXAMPLES = BASE_DIR / "phase3_artifacts" / "classification_examples_test.json"
DEFAULT_PHASE4_METRICS = BASE_DIR / "phase4_artifacts" / "metrics_test.json"
DEFAULT_CHECKPOINT = BASE_DIR / "phase3_artifacts" / "best_checkpoint.pt"
UPLOADS_DIR = BASE_DIR / "uploads"
UPLOADS_DIR.mkdir(parents=True, exist_ok=True)

app = Flask(__name__)

TASKS: dict[str, dict[str, Any]] = {}
TASKS_LOCK = threading.Lock()


def now_iso() -> str:
    return datetime.utcnow().isoformat(timespec="seconds") + "Z"


def build_task_command(task_type: str, payload: dict[str, Any]) -> list[str]:
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
    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return
        task["logs"].append(line.rstrip("\n"))
        if len(task["logs"]) > 4000:
            task["logs"] = task["logs"][-4000:]
        task["updated_at"] = now_iso()


def set_task_status(task_id: str, status: str, returncode: int | None = None) -> None:
    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return
        task["status"] = status
        task["updated_at"] = now_iso()
        if returncode is not None:
            task["returncode"] = returncode


def run_task_thread(task_id: str, cmd: list[str]) -> None:
    set_task_status(task_id, "running")
    append_log(task_id, f"$ {' '.join(cmd)}")
    try:
        proc = subprocess.Popen(
            cmd,
            cwd=BASE_DIR,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            bufsize=1,
        )
        assert proc.stdout is not None
        for line in proc.stdout:
            append_log(task_id, line)
        proc.wait()
        status = "success" if proc.returncode == 0 else "failed"
        set_task_status(task_id, status, proc.returncode)
    except Exception as exc:  # pragma: no cover
        append_log(task_id, f"ERROR ejecutando tarea: {exc}")
        set_task_status(task_id, "failed", 1)


def create_task(task_type: str, payload: dict[str, Any]) -> dict[str, Any]:
    cmd = build_task_command(task_type, payload)
    task_id = uuid.uuid4().hex[:12]
    task = {
        "id": task_id,
        "task_type": task_type,
        "status": "queued",
        "returncode": None,
        "cmd": cmd,
        "logs": [],
        "created_at": now_iso(),
        "updated_at": now_iso(),
    }
    with TASKS_LOCK:
        TASKS[task_id] = task
    worker = threading.Thread(target=run_task_thread, args=(task_id, cmd), daemon=True)
    worker.start()
    return task


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
    stats = build_dashboard_stats(
        DEFAULT_MANIFEST,
        DEFAULT_METRICS,
        DEFAULT_PHASE4_METRICS,
        DEFAULT_PHASE3_EXAMPLES,
    )
    return jsonify(stats)


@app.post("/api/predict-upload")
def api_predict_upload():
    image_file = request.files.get("image")
    checkpoint_path_raw = request.form.get("checkpoint_path", str(DEFAULT_CHECKPOINT)).strip()
    image_size_raw = request.form.get("image_size", "128").strip()

    if not image_file or not image_file.filename:
        return jsonify({"ok": False, "error": "Debes seleccionar una imagen."}), 400

    try:
        image_size = int(image_size_raw)
    except ValueError:
        return jsonify({"ok": False, "error": "image_size debe ser entero."}), 400

    checkpoint_path = Path(checkpoint_path_raw).resolve()
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
    with TASKS_LOCK:
        task = TASKS.get(task_id)
        if not task:
            return jsonify({"ok": False, "error": "task no encontrada"}), 404
        return jsonify(
            {
                "ok": True,
                "id": task["id"],
                "task_type": task["task_type"],
                "status": task["status"],
                "returncode": task["returncode"],
                "created_at": task["created_at"],
                "updated_at": task["updated_at"],
                "cmd": task["cmd"],
                "log_text": "\n".join(task["logs"]),
            }
        )


if __name__ == "__main__":
    app.run(host="127.0.0.1", port=5000, debug=True)
