from __future__ import annotations

import json
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

import pytest

from ai_provider import ProviderStatus
from app import create_app, seed_database
from app.extensions import db
from app.models import AuditLog, Student, TwoFactorCode, User
from app.services.chat import normalize_messages
from app.services.two_factor import TwoFactorError, issue_code, verify_code


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
    app = create_app({"TESTING": True, "WTF_CSRF_ENABLED": False, "FM_PORT": fake_fm_server})
    client = app.test_client()

    response = client.get("/chat/api/status")

    assert response.status_code == 200
    assert response.json["available"] is True
    assert response.json["provider"] == "foundation_models"
    assert response.json["model"] == "system"


def test_chat_proxies_stream(fake_fm_server):
    app = create_app({"TESTING": True, "WTF_CSRF_ENABLED": False, "FM_PORT": fake_fm_server})
    client = app.test_client()

    with app.app_context():
        db.create_all()
        seed_database()
        user = User.query.filter_by(username="admin").one()
        user_id = user.id
    with client.session_transaction() as session:
        session["_user_id"] = str(user_id)
        session["_fresh"] = True

    response = client.post(
        "/chat/api/chat",
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
    app = create_app({"TESTING": True, "WTF_CSRF_ENABLED": False}, provider=provider)
    client = app.test_client()

    with app.app_context():
        db.create_all()
        seed_database()
        user_id = User.query.filter_by(username="admin").one().id
    with client.session_transaction() as session:
        session["_user_id"] = str(user_id)
        session["_fresh"] = True

    status = client.get("/chat/api/status")
    chat = client.post(
        "/chat/api/chat",
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


def test_login_requires_second_factor(fake_fm_server):
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "TWO_FACTOR_DELIVERY": "console",
        "FM_PORT": fake_fm_server,
    })
    client = app.test_client()
    with app.app_context():
        db.create_all()
        seed_database()

    response = client.post("/auth/login", data={"username": "admin", "password": "Administrador123!"})

    assert response.status_code == 302
    assert response.location.endswith("/auth/verify")
    with app.app_context():
        assert TwoFactorCode.query.count() == 1


def test_two_factor_code_is_hashed_and_single_use(fake_fm_server):
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    })
    with app.app_context():
        db.create_all()
        seed_database()
        challenge, code = issue_code(User.query.filter_by(username="admin").one())
        assert challenge.code_hash != code
        verify_code(challenge, code)
        with pytest.raises(TwoFactorError, match="utilizado"):
            verify_code(challenge, code)


def build_database_app(fake_fm_server):
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    })
    with app.app_context():
        db.create_all()
        seed_database()
    return app


def authenticate(client, app, username):
    with app.app_context():
        user_id = User.query.filter_by(username=username).one().id
    with client.session_transaction() as session:
        session["_user_id"] = str(user_id)
        session["_fresh"] = True


def test_student_role_cannot_access_student_management(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")

    response = client.get("/students")

    assert response.status_code == 403


def test_teacher_can_create_and_edit_student_with_audit_log(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "docente")
    payload = {
        "name": "Ana Solís",
        "age": 16,
        "school": "Colegio Central",
        "interest_area": "Bases de datos",
        "assigned_level": "basico",
    }

    created = client.post("/students/new", data=payload)
    with app.app_context():
        student = Student.query.one()
        student_id = student.id
        assert AuditLog.query.filter_by(action="student_created").count() == 1

    payload["assigned_level"] = "intermedio"
    updated = client.post(f"/students/{student_id}/edit", data=payload)

    assert created.status_code == 302
    assert updated.status_code == 302
    with app.app_context():
        assert db.session.get(Student, student_id).assigned_level == "intermedio"
        assert AuditLog.query.filter_by(action="student_updated").count() == 1


def test_only_admin_can_delete_student(fake_fm_server):
    app = build_database_app(fake_fm_server)
    teacher = app.test_client()
    authenticate(teacher, app, "docente")
    teacher.post("/students/new", data={"name": "Luis Mora", "age": 17, "school": "Liceo Norte", "interest_area": "Programación", "assigned_level": "basico"})
    with app.app_context():
        student_id = Student.query.one().id

    assert teacher.post(f"/students/{student_id}/delete").status_code == 403

    admin = app.test_client()
    authenticate(admin, app, "admin")
    assert admin.post(f"/students/{student_id}/delete").status_code == 302
    with app.app_context():
        assert db.session.get(Student, student_id) is None
        assert AuditLog.query.filter_by(action="student_deleted").count() == 1
