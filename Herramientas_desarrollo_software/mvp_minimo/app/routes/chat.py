# Archivo: chat.py
# Propósito: Expone la pantalla y API SSE del chat TutorIA.
# Responsabilidades: Valida mensajes, consulta disponibilidad del proveedor, transmite respuestas y publica metadatos de procesamiento.
# Dependencias: Flask, Flask-Login, proveedor IA y servicio de normalización de chat.
# Entradas y salidas: Recibe mensajes JSON o formulario; devuelve HTML, JSON o eventos SSE.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Interfaz web y API SSE del chat con el proveedor IA abstracto."""

import json
import time

from flask import Blueprint, Response, current_app, jsonify, render_template, request, stream_with_context
from flask_login import login_required

from ai_provider import AIProviderError
from app.services.chat import SYSTEM_INSTRUCTIONS, normalize_messages, processing_description, sse_error


bp = Blueprint("chat", __name__, url_prefix="/chat")


def _sse_event(name: str, payload: dict) -> bytes:
    """Serializa un evento técnico sin alterar los fragmentos del modelo."""

    return f"event: {name}\ndata: {json.dumps(payload, ensure_ascii=False)}\n\n".encode()


def _usage_from_chunk(chunk: bytes) -> dict | None:
    """Extrae el bloque ``usage`` opcional de una respuesta SSE."""

    for line in chunk.decode("utf-8", errors="ignore").splitlines():
        if not line.startswith("data:") or line[5:].strip() == "[DONE]":
            continue
        try:
            payload = json.loads(line[5:].strip())
        except json.JSONDecodeError:
            continue
        if isinstance(payload.get("usage"), dict):
            return payload["usage"]
    return None


def _content_from_chunk(chunk: bytes) -> str:
    """Obtiene texto del delta para calcular una estimación local si hace falta."""

    content = []
    for line in chunk.decode("utf-8", errors="ignore").splitlines():
        if not line.startswith("data:") or line[5:].strip() == "[DONE]":
            continue
        try:
            payload = json.loads(line[5:].strip())
        except json.JSONDecodeError:
            continue
        delta = payload.get("choices", [{}])[0].get("delta", {})
        if isinstance(delta, dict) and isinstance(delta.get("content"), str):
            content.append(delta["content"])
    return "".join(content)


@bp.get("")
@login_required
def page():
    """Renderiza el chat con el estado técnico inicial del proveedor."""

    provider = current_app.extensions["ai_provider"]
    status = provider.status()
    local_runtime = {
        "app_url": request.host_url.rstrip("/"),
        "fm_url": f"http://{current_app.config['FM_HOST']}:{current_app.config['FM_PORT']}",
        "fm_command": f"{current_app.config['FM_COMMAND']} serve --host {current_app.config['FM_HOST']} --port {current_app.config['FM_PORT']}",
    }
    return render_template(
        "main/chat.html",
        provider_status=status,
        processing_description=processing_description(status.processing_location),
        local_runtime=local_runtime,
    )


@bp.get("/api/status")
def status():
    """Devuelve al navegador el estado actual del proveedor IA."""

    return jsonify(current_app.extensions["ai_provider"].status().as_dict())


@bp.post("/api/provider/wake")
def wake_provider():
    """Intenta iniciar o despertar el proveedor antes de una conversación."""

    try:
        status = current_app.extensions["ai_provider"].ensure_ready()
        return jsonify({"ok": True, "status": status.as_dict()})
    except AIProviderError as error:
        return jsonify({"ok": False, "error": str(error)}), 503


@bp.post("/api/chat")
@login_required
def chat():
    """Valida mensajes y retransmite la respuesta como Server-Sent Events."""

    provider = current_app.extensions["ai_provider"]
    try:
        messages = normalize_messages((request.get_json(silent=True) or {}).get("messages"))
        ready_status = provider.ensure_ready()
    except (ValueError, AIProviderError) as error:
        return jsonify({"error": str(error)}), 400 if isinstance(error, ValueError) else 503

    def generate():
        """Emite metadatos, contenido SSE y métricas de la respuesta."""

        started = time.perf_counter()
        prompt_chars = sum(len(message["content"]) for message in messages)
        usage = None
        completion_chars = 0
        network = "Internet → proveedor remoto" if ready_status.processing_location == "remote" else "Mac local → fm serve"
        yield _sse_event(
            "meta",
            {
                "provider": ready_status.provider,
                "model": ready_status.model,
                "processing_location": ready_status.processing_location,
                "access_mode": ready_status.access_mode,
                "network": network,
                "status": "generando",
            },
        )
        try:
            for chunk in provider.stream_chat([{"role": "system", "content": SYSTEM_INSTRUCTIONS}, *messages]):
                usage = _usage_from_chunk(chunk) or usage
                completion_chars += len(_content_from_chunk(chunk))
                yield chunk
            if usage:
                tokens = {
                    "prompt": usage.get("prompt_tokens"),
                    "completion": usage.get("completion_tokens"),
                    "total": usage.get("total_tokens"),
                    "source": "reportados por el proveedor",
                }
            else:
                # FM y algunos endpoints no reportan usage en streaming. La
                # estimación es visible para no presentarla como un dato exacto.
                prompt_estimate = max(1, round(prompt_chars / 4))
                completion_estimate = max(1, round(completion_chars / 4))
                tokens = {
                    "prompt": prompt_estimate,
                    "completion": completion_estimate,
                    "total": prompt_estimate + completion_estimate,
                    "source": "estimados",
                }
            yield _sse_event(
                "meta",
                {
                    "provider": ready_status.provider,
                    "model": ready_status.model,
                    "processing_location": ready_status.processing_location,
                    "access_mode": ready_status.access_mode,
                    "network": network,
                    "status": "completada",
                    "elapsed_ms": round((time.perf_counter() - started) * 1000),
                    "tokens": tokens,
                },
            )
        except AIProviderError as error:
            yield sse_error(str(error))

    return Response(
        stream_with_context(generate()),
        content_type="text/event-stream; charset=utf-8",
        headers={"Cache-Control": "no-cache, no-transform", "X-Accel-Buffering": "no"},
    )
