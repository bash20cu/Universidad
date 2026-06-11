from __future__ import annotations

import json
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

import pytest

from ai_provider import ProviderStatus
from app import create_app, normalize_messages


class FakeFMHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        if self.path == "/health":
            self._json(
                {
                    "status": "fm serve is running",
                    "models": [{"name": "system", "available": True}],
                }
            )
            return
        self.send_error(404)

    def do_POST(self):
        if self.path != "/v1/chat/completions":
            self.send_error(404)
            return

        length = int(self.headers["Content-Length"])
        json.loads(self.rfile.read(length))
        chunks = [
            'data: {"choices":[{"delta":{"role":"assistant"}}]}\n\n',
            'data: {"choices":[{"delta":{"content":"Hola"}}]}\n\n',
            'data: {"choices":[{"delta":{"content":" estudiante"}}]}\n\n',
            "data: [DONE]\n\n",
        ]

        self.send_response(200)
        self.send_header("Content-Type", "text/event-stream")
        self.end_headers()
        for chunk in chunks:
            self.wfile.write(chunk.encode())
            self.wfile.flush()

    def log_message(self, *_args):
        return

    def _json(self, payload):
        data = json.dumps(payload).encode()
        self.send_response(200)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)


@pytest.fixture()
def fake_fm_server():
    server = ThreadingHTTPServer(("127.0.0.1", 0), FakeFMHandler)
    thread = threading.Thread(target=server.serve_forever, daemon=True)
    thread.start()
    try:
        yield server.server_port
    finally:
        server.shutdown()
        thread.join()


def test_normalize_messages_rejects_empty_history():
    with pytest.raises(ValueError):
        normalize_messages([])


def test_status_reports_fake_server(fake_fm_server):
    app = create_app({"TESTING": True, "FM_PORT": fake_fm_server})
    client = app.test_client()

    response = client.get("/api/status")

    assert response.status_code == 200
    assert response.json["available"] is True
    assert response.json["provider"] == "foundation_models"
    assert response.json["model"] == "system"


def test_chat_proxies_stream(fake_fm_server):
    app = create_app({"TESTING": True, "FM_PORT": fake_fm_server})
    client = app.test_client()

    response = client.post(
        "/api/chat",
        json={"messages": [{"role": "user", "content": "Hola"}]},
    )

    assert response.status_code == 200
    assert response.content_type.startswith("text/event-stream")
    assert b'"content":"Hola"' in response.data
    assert b"data: [DONE]" in response.data


class FakeProvider:
    name = "fake_remote"

    def __init__(self):
        self.shutdown_calls = 0

    def ensure_ready(self):
        return self.status()

    def status(self):
        return ProviderStatus(
            provider=self.name,
            available=True,
            model="demo",
            processing_location="remote",
            access_mode="remote",
            managed_by_app=False,
            detail="ready",
        )

    def stream_chat(self, _messages):
        yield b'data: {"choices":[{"delta":{"content":"Respuesta remota"}}]}\n\n'
        yield b"data: [DONE]\n\n"

    def shutdown(self):
        self.shutdown_calls += 1


def test_app_accepts_provider_without_local_fm_assumptions():
    provider = FakeProvider()
    app = create_app({"TESTING": True}, provider=provider)
    client = app.test_client()

    status = client.get("/api/status")
    chat = client.post(
        "/api/chat",
        json={"messages": [{"role": "user", "content": "Hola"}]},
    )

    assert status.json["provider"] == "fake_remote"
    assert status.json["processing_location"] == "remote"
    assert status.json["access_mode"] == "remote"
    assert b"Respuesta remota" in chat.data


def test_provider_shutdown_is_idempotent_for_external_server(fake_fm_server):
    app = create_app({"TESTING": True, "FM_PORT": fake_fm_server})
    provider = app.extensions["ai_provider"]

    provider.shutdown()
    provider.shutdown()

    assert provider.status().available is True
