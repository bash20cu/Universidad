from flask import Blueprint, Response, current_app, jsonify, render_template, request, stream_with_context
from flask_login import login_required

from ai_provider import AIProviderError
from app.services.chat import SYSTEM_INSTRUCTIONS, normalize_messages, processing_description, sse_error


bp = Blueprint("chat", __name__, url_prefix="/chat")


@bp.get("")
@login_required
def page():
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
    return jsonify(current_app.extensions["ai_provider"].status().as_dict())


@bp.post("/api/provider/wake")
def wake_provider():
    try:
        status = current_app.extensions["ai_provider"].ensure_ready()
        return jsonify({"ok": True, "status": status.as_dict()})
    except AIProviderError as error:
        return jsonify({"ok": False, "error": str(error)}), 503


@bp.post("/api/chat")
@login_required
def chat():
    provider = current_app.extensions["ai_provider"]
    try:
        messages = normalize_messages((request.get_json(silent=True) or {}).get("messages"))
        provider.ensure_ready()
    except (ValueError, AIProviderError) as error:
        return jsonify({"error": str(error)}), 400 if isinstance(error, ValueError) else 503

    def generate():
        try:
            yield from provider.stream_chat([{"role": "system", "content": SYSTEM_INSTRUCTIONS}, *messages])
        except AIProviderError as error:
            yield sse_error(str(error))

    return Response(
        stream_with_context(generate()),
        content_type="text/event-stream; charset=utf-8",
        headers={"Cache-Control": "no-cache, no-transform", "X-Accel-Buffering": "no"},
    )

