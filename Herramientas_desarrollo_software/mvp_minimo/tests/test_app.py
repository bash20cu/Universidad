# Archivo: test_app.py
# Propósito: Verifica funcionalidades, seguridad e integración del MVP.
# Responsabilidades: Prueba autenticación, TOTP, roles, CRUD, evaluaciones, clasificación, recomendaciones, reportes y proveedores simulados.
# Dependencias: pytest, PyOTP, servidor HTTP estándar y módulos de la aplicación.
# Entradas y salidas: Crea bases y servidores temporales; produce aserciones y resultados de pytest.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Pruebas funcionales, de seguridad y de integración del MVP de TutorIA.

La suite utiliza una base SQLite aislada y proveedores controlados para probar
la aplicación sin depender de NVIDIA, Foundation Models ni credenciales reales.
Cada prueba verifica una capacidad observable del sistema y, cuando aplica,
confirma también la trazabilidad registrada en la bitácora.
"""

from __future__ import annotations

import json
import threading
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer

import pytest
import pyotp

from ai_provider import AIProviderError, FallbackChatProvider, ProviderStatus
from app import create_app, seed_database
from app.extensions import db
from app.models import AuditLog, ContentRecommendation, DiagnosticAnswer, DiagnosticEvaluation, EducationalContent, Student, TwoFactorCode, User
from app.services.chat import normalize_messages
from app.services.two_factor import TwoFactorError, issue_code, verify_code


# PRUEBAS UNITARIAS: validan reglas pequeñas sin recorrer una ruta HTTP completa.
# En esta suite se comprueba la normalización del historial de mensajes.
class FakeFMHandler(BaseHTTPRequestHandler):
    """Servidor HTTP mínimo que simula el contrato compatible con ``fm serve``."""

    def do_GET(self):
        """Responde la verificación de salud usada por el proveedor local."""

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
        """Emite una respuesta SSE pequeña para probar el streaming del chat."""

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
        """Silencia el log del servidor para mantener limpia la salida de pytest."""

        return

    def _json(self, payload):
        """Envía una respuesta JSON con los encabezados mínimos requeridos."""

        data = json.dumps(payload).encode()
        self.send_response(200)
        self.send_header("Content-Type", "application/json")
        self.send_header("Content-Length", str(len(data)))
        self.end_headers()
        self.wfile.write(data)


@pytest.fixture()
def fake_fm_server():
    """Levanta el servidor simulado en un puerto temporal y lo libera al terminar."""

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


# PRUEBAS DE INTEGRACIÓN: conectan Flask con proveedores simulados, streaming,
# base de datos y ciclo de vida de los servicios sin utilizar credenciales reales.
def test_help_page_is_public_and_explains_roles():
    app = create_app({"TESTING": True, "WTF_CSRF_ENABLED": False})
    response = app.test_client().get("/help")

    assert response.status_code == 200
    assert b"Aprende a usar TutorIA" in response.data
    assert b"Estudiante" in response.data
    assert b"Docente" in response.data
    assert b"Administrador" in response.data
    assert b"NVIDIA NIM" in response.data


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
    assert b"event: meta" in response.data
    assert b'"source": "estimados"' in response.data


class FakeProvider:
    """Proveedor remoto controlado para probar la abstracción sin red externa."""

    name = "fake_remote"

    def __init__(self):
        """Inicializa el contador usado para verificar el cierre idempotente."""

        self.shutdown_calls = 0

    def ensure_ready(self):
        """Devuelve disponibilidad inmediata porque el proveedor es simulado."""

        return self.status()

    def status(self):
        """Expone metadatos seguros que la interfaz mostraría al usuario."""

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
        """Entrega dos eventos SSE para validar la respuesta incremental."""

        yield b'data: {"choices":[{"delta":{"content":"Respuesta remota"}}]}\n\n'
        yield b"data: [DONE]\n\n"

    def complete_chat(self, _messages):
        """Devuelve una clasificación JSON válida para las pruebas académicas."""

        return '{"level":"basico","explanation":"Respuesta base","strengths":["Identifica conceptos"],"improvement_areas":["Profundizar"]}'

    def shutdown(self):
        """Registra la solicitud de cierre sin terminar procesos externos."""

        self.shutdown_calls += 1


class UsageOnlyStreamProvider(FakeProvider):
    """Proveedor que emite un evento de uso sin ``choices`` antes del contenido."""

    def stream_chat(self, _messages):
        """Reproduce el formato que provocaba el índice vacío en el parser."""

        yield b'data: {"choices":[],"usage":{"prompt_tokens":3,"completion_tokens":2,"total_tokens":5}}\n\n'
        yield b'data: {"choices":[{"delta":{"content":"Respuesta estable"}}]}\n\n'
        yield b"data: [DONE]\n\n"


class ClassifierProvider(FakeProvider):
    """Variante del proveedor falso que permite inyectar respuestas de clasificación."""

    name = "fake_classifier"

    def __init__(self, response):
        """Guarda la respuesta controlada y reinicia el contador de llamadas."""

        super().__init__()
        self.response = response
        self.complete_calls = 0

    def complete_chat(self, _messages):
        """Devuelve la carga configurada y permite comprobar que se llamó una vez."""

        self.complete_calls += 1
        return self.response


class FailingProvider(FakeProvider):
    """Proveedor que falla de forma controlada para verificar el fallback."""

    name = "failing_primary"

    def ensure_ready(self):
        """Simula una indisponibilidad antes de iniciar una conversación."""

        raise AIProviderError("Proveedor principal no disponible")

    def stream_chat(self, _messages):
        """Simula un fallo durante el streaming del proveedor principal."""

        raise AIProviderError("Fallo de streaming del proveedor principal")

    def complete_chat(self, _messages):
        """Simula un fallo durante una clasificación completa."""

        raise AIProviderError("Fallo de clasificación del proveedor principal")


class SSEErrorProvider(FakeProvider):
    """Proveedor que devuelve un error estructurado dentro de un stream HTTP 200."""

    name = "sse_error_primary"

    def stream_chat(self, _messages):
        """Reproduce el formato de error que puede devolver NVIDIA durante streaming."""

        yield b'event: error\ndata: {"error":{"message":"Load failed"}}\n\n'


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


def test_chat_ignores_usage_chunk_without_choices():
    """Comprueba que un evento de métricas no rompe el streaming del chat."""

    provider = UsageOnlyStreamProvider()
    app = create_app({"TESTING": True, "WTF_CSRF_ENABLED": False}, provider=provider)
    client = app.test_client()
    with app.app_context():
        db.create_all()
        seed_database()
        user_id = User.query.filter_by(username="admin").one().id
    with client.session_transaction() as session:
        session["_user_id"] = str(user_id)
        session["_fresh"] = True

    response = client.post("/chat/api/chat", json={"messages": [{"role": "user", "content": "Hola"}]})

    assert response.status_code == 200
    assert b"Respuesta estable" in response.data
    assert b'"total": 5' in response.data


def test_provider_shutdown_is_idempotent_for_external_server(fake_fm_server):
    app = create_app({"TESTING": True, "FM_PORT": fake_fm_server})
    provider = app.extensions["ai_provider"]

    provider.shutdown()
    provider.shutdown()

    assert provider.status().available is True


def test_fallback_uses_secondary_provider_when_primary_fails():
    """Verifica que el proveedor secundario atiende chat y clasificación."""

    primary = FailingProvider()
    fallback = FakeProvider()
    provider = FallbackChatProvider(primary=primary, fallback=fallback)

    ready = provider.ensure_ready()
    streamed = b"".join(provider.stream_chat([{"role": "user", "content": "Hola"}]))
    completed = provider.complete_chat([{"role": "user", "content": "Clasifica"}])

    assert ready.provider == "fake_remote"
    assert b"Respuesta remota" in streamed
    assert "level" in completed
    assert provider.active_provider is fallback


def test_fallback_handles_structured_sse_error_before_content():
    """Verifica fallback cuando el proveedor remoto informa error dentro de SSE."""

    fallback = FakeProvider()
    provider = FallbackChatProvider(primary=SSEErrorProvider(), fallback=fallback)

    streamed = b"".join(provider.stream_chat([{"role": "user", "content": "Hola"}]))

    assert b"Load failed" not in streamed
    assert b"Respuesta remota" in streamed
    assert provider.active_provider is fallback


def test_chat_rejects_invalid_history_before_calling_provider():
    """Comprueba que roles inválidos y mensajes vacíos no llegan al proveedor."""

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

    invalid_role = client.post(
        "/chat/api/chat",
        json={"messages": [{"role": "system", "content": "No permitido"}]},
    )
    empty_content = client.post(
        "/chat/api/chat",
        json={"messages": [{"role": "user", "content": "   "}]},
    )

    assert invalid_role.status_code == 400
    assert empty_content.status_code == 400
    assert b"Solo se permiten" in invalid_role.data
    assert "no pueden estar vacíos" in empty_content.json["error"]


def test_duplicate_user_and_xss_content_are_rejected_or_escaped(fake_fm_server):
    """Comprueba duplicados y que el contenido enviado por usuario no se interpreta como HTML."""

    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "admin")

    duplicate = client.post(
        "/users/new",
        data={
            "username": "admin",
            "email": "otro@example.com",
            "password": "OtraClave123!",
            "role": "docente",
            "active": "1",
        },
    )
    assert duplicate.status_code == 409
    with app.app_context():
        assert User.query.filter_by(username="admin").count() == 1

    payload = {
        "title": "<script>alert('xss')</script>",
        "topic": "Seguridad",
        "level": "basico",
        "competency": "Reconocer riesgos",
        "description": "Contenido controlado",
    }
    created = client.post("/contents/new", data=payload)
    page = client.get("/contents")

    assert created.status_code == 302
    assert b"&lt;script&gt;" in page.data
    assert b"<script>alert('xss')</script>" not in page.data


def test_admin_cannot_delete_content_with_recommendation(fake_fm_server):
    """Comprueba que la trazabilidad impide eliminar contenido ya recomendado."""

    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "admin")
    with app.app_context():
        content = EducationalContent.query.first()
        student = Student(name="Estudiante vinculado", age=18, school="Demo", interest_area="Bases de datos")
        db.session.add(student)
        db.session.flush()
        evaluation = DiagnosticEvaluation(
            student_id=student.id,
            status="clasificada",
            classified_level="basico",
            explanation="Clasificación controlada",
        )
        db.session.add(evaluation)
        db.session.flush()
        db.session.add(ContentRecommendation(student_id=student.id, content_id=content.id, evaluation_id=evaluation.id, reason="Recomendado para reforzar"))
        db.session.commit()
        content_id = content.id

    response = client.post(f"/contents/{content_id}/delete")

    assert response.status_code == 409
    with app.app_context():
        assert db.session.get(EducationalContent, content_id) is not None


# PRUEBAS FUNCIONALES: recorren casos de uso completos mediante el cliente de
# Flask, incluyendo autenticación, roles, evaluaciones, IA, recomendaciones y reportes.
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


def test_user_can_activate_totp_with_qr_and_code(fake_fm_server):
    """Comprueba que el usuario puede vincular y confirmar un autenticador TOTP."""

    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "admin")

    setup_page = client.get("/auth/totp/setup")

    assert setup_page.status_code == 200
    assert b"data:image/png;base64," in setup_page.data

    with app.app_context():
        user = User.query.filter_by(username="admin").one()
        code = pyotp.TOTP(user.totp_secret).now()

    response = client.post("/auth/totp/setup", data={"code": code})

    assert response.status_code == 302
    with app.app_context():
        assert User.query.filter_by(username="admin").one().totp_enabled is True


def test_login_uses_totp_when_it_is_enabled(fake_fm_server):
    """Comprueba que TOTP reemplaza el desafío por correo para una cuenta configurada."""

    app = build_database_app(fake_fm_server)
    client = app.test_client()
    with app.app_context():
        user = User.query.filter_by(username="admin").one()
        user.totp_secret = pyotp.random_base32()
        user.totp_enabled = True
        db.session.commit()
        secret = user.totp_secret

    login_response = client.post(
        "/auth/login",
        data={"username": "admin", "password": "Administrador123!"},
    )

    assert login_response.status_code == 302
    assert login_response.location.endswith("/auth/verify-totp")
    verify_response = client.post(
        "/auth/verify-totp",
        data={"code": pyotp.TOTP(secret).now()},
    )

    assert verify_response.status_code == 302
    assert verify_response.location.endswith("/dashboard")


def test_public_registration_creates_student_account_and_audit_log(fake_fm_server):
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    })
    client = app.test_client()
    with app.app_context():
        db.create_all()
        seed_database()

    response = client.post(
        "/auth/register",
        data={
            "username": "nueva_estudiante",
            "email": "nueva@example.com",
            "password": "Estudiante123!",
            "confirm_password": "Estudiante123!",
        },
    )

    assert response.status_code == 302
    assert response.location.endswith("/auth/register/2fa")
    with app.app_context():
        user = User.query.filter_by(username="nueva_estudiante").one()
        assert user.role == "estudiante"
        assert user.check_password("Estudiante123!")
        assert user.password_hash != "Estudiante123!"
        assert AuditLog.query.filter_by(action="user_registered", user_id=user.id).count() == 1


def test_registration_requires_totp_confirmation_before_entering_dashboard(fake_fm_server):
    """Comprueba que el registro muestra el QR y exige confirmar TOTP."""

    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    })
    client = app.test_client()
    with app.app_context():
        db.create_all()
        seed_database()

    registration = client.post(
        "/auth/register",
        data={
            "username": "registro_totp",
            "email": "registro_totp@example.com",
            "password": "Estudiante123!",
            "confirm_password": "Estudiante123!",
        },
    )

    assert registration.status_code == 302
    setup_page = client.get(registration.location)
    assert setup_page.status_code == 200
    assert b"data:image/png;base64," in setup_page.data
    with app.app_context():
        user = User.query.filter_by(username="registro_totp").one()
        code = pyotp.TOTP(user.totp_secret).now()

    activation = client.post("/auth/register/2fa", data={"code": code})

    assert activation.status_code == 302
    assert activation.location.endswith("/dashboard")
    with app.app_context():
        assert User.query.filter_by(username="registro_totp").one().totp_enabled is True


def test_student_can_complete_profile_and_access_private_progress(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")

    assert client.get("/dashboard").status_code == 302
    profile = client.post(
        "/student/profile",
        data={
            "name": "Estudiante Demo",
            "age": 19,
            "school": "Universidad Demo",
            "interest_area": "Bases de datos",
        },
    )

    assert profile.status_code == 302
    assert client.get("/student/dashboard").status_code == 200
    assert client.get("/student/progress").status_code == 200
    with app.app_context():
        student = Student.query.filter_by(name="Estudiante Demo").one()
        assert student.user_id == User.query.filter_by(username="estudiante").one().id


def test_student_can_answer_own_diagnostic(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")
    client.post(
        "/student/profile",
        data={
            "name": "Estudiante Evaluador",
            "age": 20,
            "school": "Universidad Demo",
            "interest_area": "Bases de datos",
        },
    )

    page = client.get("/student/diagnostic/new")
    assert page.status_code == 200
    assert b"Responde tu evaluaci\xc3\xb3n diagn\xc3\xb3stica" in page.data

    response = client.post(
        "/student/diagnostic/new",
        data={
            "answer_1": "Una clave primaria identifica de forma unica una fila.",
            "answer_2": "Normalizar reduce la repeticion de datos.",
            "answer_3": "Un indice acelera consultas frecuentes.",
        },
    )

    assert response.status_code == 302
    assert response.location.endswith("/student/progress")
    with app.app_context():
        evaluation = DiagnosticEvaluation.query.one()
        assert evaluation.student.name == "Estudiante Evaluador"
        assert evaluation.status == "pendiente_ia"
        assert DiagnosticAnswer.query.count() == 3
        assert AuditLog.query.filter_by(action="student_diagnostic_created").count() == 1


def test_student_diagnostic_requires_all_answers(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")
    client.post(
        "/student/profile",
        data={"name": "Estudiante Incompleto", "age": 20, "school": "Universidad Demo", "interest_area": "Bases de datos"},
    )

    response = client.post(
        "/student/diagnostic/new",
        data={"answer_1": "Solo una respuesta.", "answer_2": "", "answer_3": "Otra respuesta."},
    )

    assert response.status_code == 400
    with app.app_context():
        assert DiagnosticEvaluation.query.count() == 0
        assert DiagnosticAnswer.query.count() == 0


def test_only_admin_can_view_audit_log(fake_fm_server):
    app = build_database_app(fake_fm_server)
    admin = app.test_client()
    teacher = app.test_client()
    authenticate(admin, app, "admin")
    authenticate(teacher, app, "docente")

    assert admin.get("/users/audit").status_code == 200
    assert teacher.get("/users/audit").status_code == 403


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
    """Crea una aplicación de prueba completa con SQLite en memoria y datos demo."""

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
    """Autentica directamente una cuenta para enfocar cada prueba en su objetivo."""

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


def test_student_role_cannot_access_content_management(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")

    response = client.get("/contents")

    assert response.status_code == 403


def test_teacher_can_create_and_edit_content_with_audit_log(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "docente")
    payload = {
        "title": "Pensamiento computacional inicial",
        "topic": "Programación",
        "level": "basico",
        "competency": "Reconocer secuencias lógicas",
        "description": "Contenido introductorio para resolver problemas paso a paso.",
    }

    created = client.post("/contents/new", data=payload)
    with app.app_context():
        content = EducationalContent.query.filter_by(title=payload["title"]).one()
        content_id = content.id
        assert AuditLog.query.filter_by(action="content_created").count() == 1

    payload["level"] = "intermedio"
    updated = client.post(f"/contents/{content_id}/edit", data=payload)

    assert created.status_code == 302
    assert updated.status_code == 302
    with app.app_context():
        assert db.session.get(EducationalContent, content_id).level == "intermedio"
        assert AuditLog.query.filter_by(action="content_updated").count() == 1


def test_only_admin_can_delete_content(fake_fm_server):
    app = build_database_app(fake_fm_server)
    teacher = app.test_client()
    authenticate(teacher, app, "docente")
    teacher.post("/contents/new", data={"title": "Lectura crítica", "topic": "Comunicación", "level": "basico", "competency": "Identificar ideas principales", "description": "Actividad breve de lectura guiada."})
    with app.app_context():
        content_id = EducationalContent.query.filter_by(title="Lectura crítica").one().id

    assert teacher.post(f"/contents/{content_id}/delete").status_code == 403

    admin = app.test_client()
    authenticate(admin, app, "admin")
    assert admin.post(f"/contents/{content_id}/delete").status_code == 302
    with app.app_context():
        assert db.session.get(EducationalContent, content_id) is None
        assert AuditLog.query.filter_by(action="content_deleted").count() == 1


def test_student_role_cannot_access_diagnostic_management(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")

    response = client.get("/diagnostics")

    assert response.status_code == 403


def test_teacher_can_create_diagnostic_evaluation_with_answers(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "docente")

    client.post("/students/new", data={
        "name": "María Rojas",
        "age": 16,
        "school": "Colegio Central",
        "interest_area": "Bases de datos",
        "assigned_level": "basico",
    })
    with app.app_context():
        student_id = Student.query.filter_by(name="María Rojas").one().id

    response = client.post("/diagnostics/new", data={
        "student_id": student_id,
        "answer_1": "La clave primaria identifica un registro único.",
        "answer_2": "Normalizar evita repetir datos innecesarios.",
        "answer_3": "Un índice acelera búsquedas, pero tiene costo de escritura.",
    })

    assert response.status_code == 302
    with app.app_context():
        evaluation = DiagnosticEvaluation.query.one()
        assert evaluation.student_id == student_id
        assert evaluation.status == "pendiente_ia"
        assert DiagnosticAnswer.query.count() == 3
        assert AuditLog.query.filter_by(action="diagnostic_created").count() == 1


def test_diagnostic_requires_all_answers(fake_fm_server):
    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "docente")

    client.post("/students/new", data={
        "name": "Carlos Méndez",
        "age": 17,
        "school": "Liceo Sur",
        "interest_area": "Bases de datos",
        "assigned_level": "basico",
    })
    with app.app_context():
        student_id = Student.query.filter_by(name="Carlos Méndez").one().id

    response = client.post("/diagnostics/new", data={
        "student_id": student_id,
        "answer_1": "Respuesta completa.",
        "answer_2": "",
        "answer_3": "Respuesta completa.",
    })

    assert response.status_code == 400
    with app.app_context():
        assert DiagnosticEvaluation.query.count() == 0
        assert DiagnosticAnswer.query.count() == 0


def create_diagnostic_for_classification(client, app):
    """Crea un estudiante y una evaluación completa lista para clasificar."""

    client.post("/students/new", data={
        "name": "Daniela Castro",
        "age": 16,
        "school": "Colegio Técnico",
        "interest_area": "Bases de datos",
        "assigned_level": "basico",
    })
    with app.app_context():
        student_id = Student.query.filter_by(name="Daniela Castro").one().id
    client.post("/diagnostics/new", data={
        "student_id": student_id,
        "answer_1": "Una clave primaria identifica una fila única.",
        "answer_2": "Normalizar reduce repetición y anomalías.",
        "answer_3": "Usaría índices para acelerar consultas frecuentes.",
    })
    with app.app_context():
        return DiagnosticEvaluation.query.one().id


def test_teacher_can_classify_diagnostic_with_fake_provider(fake_fm_server):
    provider = ClassifierProvider(
        '{"level":"intermedio","explanation":"Comprende conceptos relacionales esenciales.","strengths":["Distingue claves","Reconoce normalización"],"improvement_areas":["Profundizar costos de índices"]}'
    )
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    }, provider=provider)
    with app.app_context():
        db.create_all()
        seed_database()
    client = app.test_client()
    authenticate(client, app, "docente")
    evaluation_id = create_diagnostic_for_classification(client, app)

    response = client.post(f"/diagnostics/{evaluation_id}/classify")

    assert response.status_code == 302
    assert provider.complete_calls == 1
    with app.app_context():
        evaluation = db.session.get(DiagnosticEvaluation, evaluation_id)
        assert evaluation.status == "clasificada"
        assert evaluation.classified_level == "intermedio"
        assert "Fortalezas" in evaluation.explanation
        assert evaluation.student.assigned_level == "intermedio"
        assert AuditLog.query.filter_by(action="diagnostic_classified").count() == 1


def test_invalid_ai_classification_does_not_update_evaluation(fake_fm_server):
    provider = ClassifierProvider("No soy JSON")
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    }, provider=provider)
    with app.app_context():
        db.create_all()
        seed_database()
    client = app.test_client()
    authenticate(client, app, "docente")
    evaluation_id = create_diagnostic_for_classification(client, app)

    response = client.post(f"/diagnostics/{evaluation_id}/classify")

    assert response.status_code == 302
    with app.app_context():
        evaluation = db.session.get(DiagnosticEvaluation, evaluation_id)
        assert evaluation.status == "pendiente_ia"
        assert evaluation.classified_level is None
        assert AuditLog.query.filter_by(action="diagnostic_classification_failed").count() == 1


def test_classified_student_receives_traceable_recommendations_and_report(fake_fm_server):
    """Comprueba el flujo completo que conecta diagnóstico, contenidos y reporte."""

    provider = ClassifierProvider(
        '{"level":"basico","explanation":"Tiene una base inicial.","strengths":["Reconoce conceptos"],"improvement_areas":["Practicar relaciones"]}'
    )
    app = create_app({
        "TESTING": True,
        "WTF_CSRF_ENABLED": False,
        "SQLALCHEMY_DATABASE_URI": "sqlite:///:memory:",
        "FM_PORT": fake_fm_server,
    }, provider=provider)
    with app.app_context():
        db.create_all()
        seed_database()
    client = app.test_client()
    authenticate(client, app, "docente")
    evaluation_id = create_diagnostic_for_classification(client, app)
    assert client.post(f"/diagnostics/{evaluation_id}/classify").status_code == 302

    with app.app_context():
        student_id = DiagnosticEvaluation.query.one().student_id

    recommendations = client.get(f"/recommendations/{student_id}")
    report = client.get(f"/reports/{student_id}")

    assert recommendations.status_code == 200
    assert report.status_code == 200
    with app.app_context():
        assert ContentRecommendation.query.filter_by(student_id=student_id).count() > 0
        assert AuditLog.query.filter_by(action="recommendations_generated").count() == 1
        assert AuditLog.query.filter_by(action="report_viewed").count() == 1


def test_student_cannot_access_users_recommendations_or_reports(fake_fm_server):
    """Los reportes y la administración académica pertenecen a personal autorizado."""

    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "estudiante")

    assert client.get("/users").status_code == 403
    assert client.get("/recommendations").status_code == 403
    assert client.get("/reports").status_code == 403


def test_admin_can_create_user_with_hashed_password_and_audit_log(fake_fm_server):
    """Verifica que el alta de usuario no guarde la contraseña en texto plano."""

    app = build_database_app(fake_fm_server)
    client = app.test_client()
    authenticate(client, app, "admin")

    response = client.post("/users/new", data={
        "username": "orientadora",
        "email": "orientadora@example.com",
        "password": "Orientadora123!",
        "role": "docente",
        "active": "1",
    })

    assert response.status_code == 302
    with app.app_context():
        user = User.query.filter_by(username="orientadora").one()
        assert user.password_hash != "Orientadora123!"
        assert user.check_password("Orientadora123!")
        assert AuditLog.query.filter_by(action="user_created").count() == 1
