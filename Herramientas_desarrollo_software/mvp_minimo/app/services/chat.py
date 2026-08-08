# Archivo: chat.py
# Propósito: Valida y normaliza mensajes del chat.
# Responsabilidades: Limita cantidad, roles y longitud de mensajes, traduce ubicación de procesamiento y construye errores SSE.
# Dependencias: __future__, json, typing
# Entradas y salidas: Recibe historial o ubicación técnica; devuelve mensajes normalizados, textos de interfaz o bytes SSE.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Reglas compartidas para validar y presentar conversaciones del chat."""

from __future__ import annotations

import json
from typing import Any


MAX_MESSAGES = 20
MAX_MESSAGE_LENGTH = 4_000
SYSTEM_INSTRUCTIONS = (
    "Eres TutorIA, un tutor académico claro, paciente y riguroso. "
    "Responde siempre en español, reconoce la incertidumbre y no inventes fuentes."
)


def normalize_messages(raw_messages: Any) -> list[dict[str, str]]:
    """Limita tamaño, roles y longitud del historial antes de enviarlo a IA."""

    if not isinstance(raw_messages, list) or not raw_messages:
        raise ValueError("Debes enviar al menos un mensaje.")
    normalized = []
    for message in raw_messages[-MAX_MESSAGES:]:
        if not isinstance(message, dict):
            raise ValueError("El historial contiene un mensaje inválido.")
        role, content = message.get("role"), message.get("content")
        if role not in {"user", "assistant"}:
            raise ValueError("Solo se permiten mensajes de usuario y asistente.")
        if not isinstance(content, str) or not content.strip():
            raise ValueError("Los mensajes no pueden estar vacíos.")
        normalized.append({"role": role, "content": content.strip()[:MAX_MESSAGE_LENGTH]})
    return normalized


def sse_error(message: str) -> bytes:
    """Construye un evento SSE de error compatible con el cliente web."""

    payload = json.dumps({"error": message}, ensure_ascii=False)
    return f"event: error\ndata: {payload}\n\n".encode()


def processing_description(location: str) -> str:
    """Traduce la ubicación técnica del procesamiento a lenguaje de interfaz."""

    return {
        "device": "en el dispositivo que ejecuta el modelo",
        "private_cloud": "en una nube privada administrada por el proveedor",
        "remote": "en una infraestructura remota configurada",
    }.get(location, "según la configuración del proveedor activo")
