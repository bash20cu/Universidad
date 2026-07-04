from __future__ import annotations

import os
from pathlib import Path
from typing import Any

from flask import Flask

from ai_provider import ChatProvider, FoundationModelsProvider
from fm_server import FMServerManager

from app.extensions import csrf, db, login_manager
from app.models import DiagnosticQuestion, EducationalContent, User


def create_app(config: dict[str, Any] | None = None, provider: ChatProvider | None = None) -> Flask:
    app = Flask(__name__)
    instance_path = Path(app.instance_path)
    instance_path.mkdir(parents=True, exist_ok=True)
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
        manager = FMServerManager(
            command=app.config["FM_COMMAND"], host=app.config["FM_HOST"],
            port=app.config["FM_PORT"], start_timeout=app.config["FM_START_TIMEOUT"],
        )
        provider = FoundationModelsProvider(
            manager=manager, model=app.config["FM_MODEL"],
            processing_location=app.config["AI_PROCESSING_LOCATION"],
            access_mode=app.config["APP_ACCESS_MODE"],
        )
    app.extensions["ai_provider"] = provider

    from app.routes.auth import bp as auth_bp
    from app.routes.chat import bp as chat_bp
    from app.routes.main import bp as main_bp
    from app.routes.students import bp as students_bp
    app.register_blueprint(auth_bp)
    app.register_blueprint(chat_bp)
    app.register_blueprint(main_bp)
    app.register_blueprint(students_bp)

    @login_manager.user_loader
    def load_user(user_id: str):
        return db.session.get(User, int(user_id))

    @app.cli.command("init-db")
    def init_db_command():
        db.create_all()
        seed_database()
        print("Base de datos inicializada.")

    return app


def seed_database() -> None:
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
            EducationalContent(title="Introducción a bases de datos", topic="Bases de datos", level="basico", competency="Reconocer tablas, claves y relaciones", description="Conceptos esenciales del modelo relacional."),
            EducationalContent(title="Normalización práctica", topic="Bases de datos", level="intermedio", competency="Aplicar formas normales", description="Ejercicios de primera, segunda y tercera forma normal."),
            EducationalContent(title="Optimización de consultas", topic="Bases de datos", level="avanzado", competency="Analizar planes de ejecución", description="Índices, planes y estrategias de optimización."),
        ])
    if DiagnosticQuestion.query.count() == 0:
        db.session.add_all([
            DiagnosticQuestion(topic="Bases de datos", prompt="¿Qué diferencia existe entre una clave primaria y una foránea?", expected_competency="Distinguir claves y relaciones"),
            DiagnosticQuestion(topic="Bases de datos", prompt="Explica por qué normalizarías una tabla con datos repetidos.", expected_competency="Comprender normalización"),
            DiagnosticQuestion(topic="Bases de datos", prompt="¿Cuándo utilizarías un índice y qué costo puede tener?", expected_competency="Evaluar optimización"),
        ])
    db.session.commit()
