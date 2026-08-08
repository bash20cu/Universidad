# Archivo: __init__.py
# Propósito: Construye y configura la aplicación Flask TutorIA.
# Responsabilidades: Inicializa extensiones, selecciona proveedores IA, registra blueprints, mantiene el esquema SQLite y carga datos demo.
# Dependencias: Flask, SQLAlchemy, Flask-Login, Flask-WTF y módulos propios de modelos/proveedores.
# Entradas y salidas: Recibe configuración opcional y devuelve una instancia Flask; los comandos CLI modifican la base configurada.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Factoría y configuración central de la aplicación Flask TutorIA."""

from __future__ import annotations

import os
from pathlib import Path
from typing import Any

from flask import Flask

from dotenv import load_dotenv
from sqlalchemy import inspect, text

from ai_provider import ChatProvider, FallbackChatProvider, FoundationModelsProvider, NVIDIAProvider
from fm_server import FMServerManager

from app.extensions import csrf, db, login_manager
from app.models import DiagnosticQuestion, EducationalContent, User


def create_app(config: dict[str, Any] | None = None, provider: ChatProvider | None = None) -> Flask:
    """Construye la app, registra extensiones, proveedores y blueprints."""

    app = Flask(__name__)
    instance_path = Path(app.instance_path)
    instance_path.mkdir(parents=True, exist_ok=True)
    # Las claves viven en `.env`, nunca en el código ni en `.env.example`.
    # Las pruebas no deben leer credenciales reales ni llamar proveedores externos.
    if not (config and config.get("TESTING")):
        load_dotenv(Path(app.root_path).parent / ".env")
    app.config.from_mapping(
        SECRET_KEY=os.getenv("SECRET_KEY", "dev-change-this-secret"),
        SQLALCHEMY_DATABASE_URI=os.getenv("DATABASE_URL", f"sqlite:///{instance_path / 'tutoria.db'}"),
        SQLALCHEMY_TRACK_MODIFICATIONS=False,
        SESSION_COOKIE_HTTPONLY=True,
        SESSION_COOKIE_SAMESITE="Lax",
        SESSION_COOKIE_SECURE=os.getenv("COOKIE_SECURE", "0") == "1",
        WTF_CSRF_TIME_LIMIT=3600,
        TWO_FACTOR_DELIVERY=os.getenv("TWO_FACTOR_DELIVERY", "console"),
        RESEND_API_KEY=os.getenv("RESEND_API_KEY"),
        RESEND_FROM_EMAIL=os.getenv("RESEND_FROM_EMAIL", "TutorIA <onboarding@resend.dev>"),
        FM_HOST=os.getenv("FM_HOST", "127.0.0.1"),
        FM_PORT=int(os.getenv("FM_PORT", "1976")),
        FM_COMMAND=os.getenv("FM_COMMAND", "/usr/bin/fm"),
        FM_MODEL=os.getenv("FM_MODEL", "system"),
        FM_START_TIMEOUT=float(os.getenv("FM_START_TIMEOUT", "20")),
        AI_PROCESSING_LOCATION=os.getenv("AI_PROCESSING_LOCATION", "device"),
        APP_ACCESS_MODE=os.getenv("APP_ACCESS_MODE", "local"),
        AI_PRIMARY_PROVIDER=os.getenv("AI_PRIMARY_PROVIDER", "nvidia"),
        NVIDIA_API_KEY=os.getenv("NVIDIA_API_KEY") or os.getenv("API_KEY"),
        NVIDIA_MODEL=os.getenv("NVIDIA_MODEL", "meta/llama-3.1-8b-instruct"),
        NVIDIA_BASE_URL=os.getenv("NVIDIA_BASE_URL", "https://integrate.api.nvidia.com/v1"),
    )
    if config:
        app.config.update(config)

    db.init_app(app)
    login_manager.init_app(app)
    csrf.init_app(app)
    login_manager.login_view = "auth.login"
    login_manager.login_message = "Inicia sesión para continuar."
    login_manager.login_message_category = "warning"

    if provider is None:
        # La aplicación construye los proveedores aquí para que las rutas solo
        # dependan del contrato ChatProvider y no conozcan NVIDIA ni FM.
        manager = FMServerManager(
            command=app.config["FM_COMMAND"], host=app.config["FM_HOST"],
            port=app.config["FM_PORT"], start_timeout=app.config["FM_START_TIMEOUT"],
        )
        foundation_provider = FoundationModelsProvider(
            manager=manager, model=app.config["FM_MODEL"],
            processing_location=app.config["AI_PROCESSING_LOCATION"],
            access_mode=app.config["APP_ACCESS_MODE"],
        )
        if app.config["AI_PRIMARY_PROVIDER"] == "nvidia" and app.config["NVIDIA_API_KEY"]:
            # NVIDIA es el proveedor principal; FM queda como respaldo local
            # cuando el servicio remoto no está disponible o falla una llamada.
            provider = FallbackChatProvider(
                primary=NVIDIAProvider(
                    api_key=app.config["NVIDIA_API_KEY"],
                    model=app.config["NVIDIA_MODEL"],
                    base_url=app.config["NVIDIA_BASE_URL"],
                ),
                fallback=foundation_provider,
            )
        else:
            provider = foundation_provider
    app.extensions["ai_provider"] = provider

    from app.routes.auth import bp as auth_bp
    from app.routes.chat import bp as chat_bp
    from app.routes.contents import bp as contents_bp
    from app.routes.diagnostics import bp as diagnostics_bp
    from app.routes.main import bp as main_bp
    from app.routes.recommendations import bp as recommendations_bp
    from app.routes.reports import bp as reports_bp
    from app.routes.students import bp as students_bp
    from app.routes.student_portal import bp as student_portal_bp
    from app.routes.users import bp as users_bp
    app.register_blueprint(auth_bp)
    app.register_blueprint(chat_bp)
    app.register_blueprint(contents_bp)
    app.register_blueprint(diagnostics_bp)
    app.register_blueprint(main_bp)
    app.register_blueprint(recommendations_bp)
    app.register_blueprint(reports_bp)
    app.register_blueprint(students_bp)
    app.register_blueprint(student_portal_bp)
    app.register_blueprint(users_bp)

    @login_manager.user_loader
    def load_user(user_id: str):
        """Recupera una cuenta desde la sesión de Flask-Login."""

        return db.session.get(User, int(user_id))

    @app.cli.command("init-db")
    def init_db_command():
        """Crea tablas y datos mínimos para una instalación nueva."""

        db.create_all()
        ensure_schema_compatibility()
        seed_database()
        print("Base de datos inicializada.")

    return app


def ensure_schema_compatibility() -> None:
    """Agrega columnas nuevas a una base SQLite creada por versiones anteriores.

    El MVP no utiliza Alembic todavía; por eso esta migración pequeña y explícita
    permite que una instalación existente adopte TOTP sin perder sus usuarios.
    """

    inspector = inspect(db.engine)
    if not inspector.has_table("users"):
        return
    # Las comprobaciones por columna hacen la actualización repetible: ejecutar
    # la aplicación varias veces no intenta agregar dos veces el mismo campo.
    columns = {column["name"] for column in inspector.get_columns("users")}
    if "totp_secret" not in columns:
        db.session.execute(text("ALTER TABLE users ADD COLUMN totp_secret VARCHAR(64)"))
    if "totp_enabled" not in columns:
        db.session.execute(text("ALTER TABLE users ADD COLUMN totp_enabled BOOLEAN NOT NULL DEFAULT 0"))
    content_columns = {column["name"] for column in inspector.get_columns("educational_contents")}
    if "material_type" not in content_columns:
        db.session.execute(text("ALTER TABLE educational_contents ADD COLUMN material_type VARCHAR(40) NOT NULL DEFAULT 'lectura'"))
    if "resource_url" not in content_columns:
        db.session.execute(text("ALTER TABLE educational_contents ADD COLUMN resource_url VARCHAR(500)"))
    if "status" not in content_columns:
        db.session.execute(text("ALTER TABLE educational_contents ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'activo'"))
    evaluation_columns = {column["name"] for column in inspector.get_columns("diagnostic_evaluations")}
    if "ai_provider" not in evaluation_columns:
        db.session.execute(text("ALTER TABLE diagnostic_evaluations ADD COLUMN ai_provider VARCHAR(80)"))
    if "ai_model" not in evaluation_columns:
        db.session.execute(text("ALTER TABLE diagnostic_evaluations ADD COLUMN ai_model VARCHAR(160)"))
    if "classified_at" not in evaluation_columns:
        db.session.execute(text("ALTER TABLE diagnostic_evaluations ADD COLUMN classified_at DATETIME"))
    db.session.commit()


def seed_database() -> None:
    """Inserta usuarios, contenidos y preguntas demo solo si faltan."""

    ensure_schema_compatibility()
    # Los datos demo permiten arrancar una instalación nueva y reproducir la
    # demostración académica sin sobrescribir información existente.
    if User.query.count() == 0:
        users = [
            ("admin", "admin@tutoria.local", "Administrador123!", "administrador"),
            ("docente", "docente@tutoria.local", "Docente123!", "docente"),
            ("estudiante", "estudiante@tutoria.local", "Estudiante123!", "estudiante"),
        ]
        for username, email, password, role in users:
            user = User(username=username, email=email, role=role)
            user.set_password(password)
            db.session.add(user)
    if EducationalContent.query.count() == 0:
        db.session.add_all([
            EducationalContent(title="Introducción a bases de datos", topic="Bases de datos", level="basico", competency="Reconocer tablas, claves y relaciones", description="Conceptos esenciales del modelo relacional.", material_type="lectura", status="activo"),
            EducationalContent(title="Normalización práctica", topic="Bases de datos", level="intermedio", competency="Aplicar formas normales", description="Ejercicios de primera, segunda y tercera forma normal.", material_type="ejercicio", status="activo"),
            EducationalContent(title="Optimización de consultas", topic="Bases de datos", level="avanzado", competency="Analizar planes de ejecución", description="Índices, planes y estrategias de optimización.", material_type="video", status="activo"),
        ])
    if DiagnosticQuestion.query.count() == 0:
        db.session.add_all([
            DiagnosticQuestion(topic="Bases de datos", prompt="¿Qué diferencia existe entre una clave primaria y una foránea?", expected_competency="Distinguir claves y relaciones"),
            DiagnosticQuestion(topic="Bases de datos", prompt="Explica por qué normalizarías una tabla con datos repetidos.", expected_competency="Comprender normalización"),
            DiagnosticQuestion(topic="Bases de datos", prompt="¿Cuándo utilizarías un índice y qué costo puede tener?", expected_competency="Evaluar optimización"),
        ])
    db.session.commit()
