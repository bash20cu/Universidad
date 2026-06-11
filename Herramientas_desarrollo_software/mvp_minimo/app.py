from __future__ import annotations

import atexit
import json
import os
import signal
from typing import Any

from flask import Flask, Response, jsonify, render_template, request, stream_with_context

from ai_provider import (
    AIProviderError,
    ChatProvider,
    FoundationModelsProvider,
)
from fm_server import FMServerManager


MAX_MESSAGES = 20
MAX_MESSAGE_LENGTH = 4_000

SYSTEM_INSTRUCTIONS = (
    "Eres TutorIA, un tutor académico claro, paciente y riguroso. "
    "Responde siempre en español. Explica paso a paso cuando sea útil, "
    "reconoce la incertidumbre y no inventes fuentes ni resultados. "
    "Mantén las respuestas enfocadas y apropiadas para un estudiante universitario."
)


def create_app(
    config: dict[str, Any] | None = None,
    provider: ChatProvider | None = None,
) -> Flask:
    app = Flask(__name__, template_folder="templates", static_folder="static")
    app.config.from_mapping(
        FM_HOST=os.getenv("FM_HOST", "127.0.0.1"),
        FM_PORT=int(os.getenv("FM_PORT", "1976")),
        FM_COMMAND=os.getenv("FM_COMMAND", "/usr/bin/fm"),
        FM_MODEL=os.getenv("FM_MODEL", "system"),
        FM_START_TIMEOUT=float(os.getenv("FM_START_TIMEOUT", "20")),
        AI_PROCESSING_LOCATION=os.getenv("AI_PROCESSING_LOCATION", "device"),
        APP_ACCESS_MODE=os.getenv("APP_ACCESS_MODE", "local"),
        JSON_AS_ASCII=False,
    )
    if config:
        app.config.update(config)

    if provider is None:
        manager = FMServerManager(
            command=app.config["FM_COMMAND"],
            host=app.config["FM_HOST"],
            port=app.config["FM_PORT"],
            start_timeout=app.config["FM_START_TIMEOUT"],
        )
        provider = FoundationModelsProvider(
            manager=manager,
            model=app.config["FM_MODEL"],
            processing_location=app.config["AI_PROCESSING_LOCATION"],
            access_mode=app.config["APP_ACCESS_MODE"],
        )
    app.extensions["ai_provider"] = provider

    @app.get("/")
    def index() -> str:
        provider_status = provider.status()
        return render_template(
            "index.html",
            provider_status=provider_status,
            processing_description=processing_description(provider_status.processing_location),
        )

    @app.get("/api/status")
    def status() -> Response:
        return jsonify(provider.status().as_dict())

    @app.post("/api/chat")
    def chat() -> Response:
        payload = request.get_json(silent=True) or {}

        try:
            messages = normalize_messages(payload.get("messages"))
            provider.ensure_ready()
        except (ValueError, AIProviderError) as error:
            status_code = 400 if isinstance(error, ValueError) else 503
            return jsonify({"error": str(error)}), status_code

        provider_messages = [
                {"role": "system", "content": SYSTEM_INSTRUCTIONS},
                *messages,
        ]

        def generate():
            try:
                yield from provider.stream_chat(provider_messages)
            except AIProviderError as error:
                yield sse_error(str(error))

        return Response(
            stream_with_context(generate()),
            content_type="text/event-stream; charset=utf-8",
            headers={
                "Cache-Control": "no-cache, no-transform",
                "X-Accel-Buffering": "no",
                "Connection": "keep-alive",
            },
        )

    @app.post("/api/provider/wake")
    def wake_provider() -> Response:
        try:
            provider_status = provider.ensure_ready()
            return jsonify({"ok": True, "status": provider_status.as_dict()})
        except AIProviderError as error:
            return jsonify({"ok": False, "error": str(error)}), 503

    return app


def normalize_messages(raw_messages: Any) -> list[dict[str, str]]:
    if not isinstance(raw_messages, list) or not raw_messages:
        raise ValueError("Debes enviar al menos un mensaje.")

    normalized: list[dict[str, str]] = []
    for message in raw_messages[-MAX_MESSAGES:]:
        if not isinstance(message, dict):
            raise ValueError("El historial contiene un mensaje inválido.")

        role = message.get("role")
        content = message.get("content")
        if role not in {"user", "assistant"}:
            raise ValueError("Solo se permiten mensajes de usuario y asistente.")
        if not isinstance(content, str) or not content.strip():
            raise ValueError("Los mensajes no pueden estar vacíos.")

        normalized.append(
            {
                "role": role,
                "content": content.strip()[:MAX_MESSAGE_LENGTH],
            }
        )

    return normalized


def sse_error(message: str) -> bytes:
    payload = json.dumps({"error": message}, ensure_ascii=False)
    return f"event: error\ndata: {payload}\n\n".encode()


def processing_description(location: str) -> str:
    descriptions = {
        "device": "en el dispositivo que ejecuta el modelo",
        "private_cloud": "en una nube privada administrada por el proveedor",
        "remote": "en una infraestructura remota configurada",
    }
    return descriptions.get(location, "según la configuración del proveedor activo")


def install_shutdown_handlers(provider: ChatProvider) -> None:
    def shutdown_handler(signum, _frame):
        provider.shutdown()
        raise SystemExit(128 + signum)

    signal.signal(signal.SIGTERM, shutdown_handler)
    signal.signal(signal.SIGINT, shutdown_handler)


app = create_app()


if __name__ == "__main__":
    ai_provider: ChatProvider = app.extensions["ai_provider"]
    atexit.register(ai_provider.shutdown)
    install_shutdown_handlers(ai_provider)
    try:
        ai_provider.ensure_ready()
    except AIProviderError as error:
        print(f"Advertencia: {error}")

    try:
        app.run(
            host=os.getenv("APP_HOST", "127.0.0.1"),
            port=int(os.getenv("APP_PORT", "5050")),
            debug=os.getenv("FLASK_DEBUG", "0") == "1",
            use_reloader=False,
            threaded=True,
        )
    finally:
        ai_provider.shutdown()
