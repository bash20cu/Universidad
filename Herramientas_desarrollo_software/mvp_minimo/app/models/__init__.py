"""Modelos persistentes del MVP de TutorIA.

Cada clase representa una entidad del dominio académico o de seguridad. Las
relaciones se mantienen en SQLAlchemy para que las rutas trabajen con objetos
Python y no mezclen reglas de negocio con consultas SQL manuales.
"""

from __future__ import annotations

from datetime import datetime, timezone

from flask_login import UserMixin
from werkzeug.security import check_password_hash, generate_password_hash

from app.extensions import db


def utcnow() -> datetime:
    """Devuelve la fecha y hora actual en UTC para datos auditables."""

    return datetime.now(timezone.utc)


class TimestampMixin:
    """Agrega fechas de creación y actualización a entidades editables."""

    created_at = db.Column(db.DateTime(timezone=True), default=utcnow, nullable=False)
    updated_at = db.Column(
        db.DateTime(timezone=True), default=utcnow, onupdate=utcnow, nullable=False
    )


class User(UserMixin, TimestampMixin, db.Model):
    """Cuenta de acceso con rol, correo, estado y contraseña cifrada."""

    __tablename__ = "users"

    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(80), unique=True, nullable=False, index=True)
    email = db.Column(db.String(255), unique=True, nullable=False, index=True)
    password_hash = db.Column(db.String(255), nullable=False)
    role = db.Column(db.String(20), nullable=False, default="estudiante")
    active = db.Column(db.Boolean, nullable=False, default=True)
    last_login_at = db.Column(db.DateTime(timezone=True))
    totp_secret = db.Column(db.String(64))
    totp_enabled = db.Column(db.Boolean, nullable=False, default=False)

    def set_password(self, password: str) -> None:
        """Genera y almacena un hash seguro; nunca guarda la contraseña plana."""

        self.password_hash = generate_password_hash(password)

    def check_password(self, password: str) -> bool:
        """Comprueba una contraseña contra el hash almacenado."""

        return check_password_hash(self.password_hash, password)

    @property
    def is_active(self) -> bool:
        """Expone a Flask-Login si la cuenta puede iniciar sesión."""

        return self.active


class TwoFactorCode(db.Model):
    """Desafío temporal de segundo factor asociado a una cuenta."""

    __tablename__ = "two_factor_codes"

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("users.id"), nullable=False, index=True)
    code_hash = db.Column(db.String(255), nullable=False)
    expires_at = db.Column(db.DateTime(timezone=True), nullable=False)
    attempts = db.Column(db.Integer, nullable=False, default=0)
    used_at = db.Column(db.DateTime(timezone=True))
    created_at = db.Column(db.DateTime(timezone=True), default=utcnow, nullable=False)
    user = db.relationship("User", backref=db.backref("two_factor_codes", lazy=True))

    def set_code(self, code: str) -> None:
        """Almacena el código 2FA como hash de un solo uso."""

        self.code_hash = generate_password_hash(code)

    def check_code(self, code: str) -> bool:
        """Valida el código recibido sin exponer el valor original."""

        return check_password_hash(self.code_hash, code)


class AuditLog(db.Model):
    """Registro inmutable de acciones relevantes para trazabilidad."""

    __tablename__ = "audit_logs"

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("users.id"), index=True)
    action = db.Column(db.String(80), nullable=False, index=True)
    entity_type = db.Column(db.String(80))
    entity_id = db.Column(db.String(80))
    detail = db.Column(db.Text)
    ip_address = db.Column(db.String(45))
    created_at = db.Column(db.DateTime(timezone=True), default=utcnow, nullable=False)
    user = db.relationship("User", backref=db.backref("audit_logs", lazy=True))


class Student(TimestampMixin, db.Model):
    """Perfil académico utilizado por diagnósticos y recomendaciones."""

    __tablename__ = "students"

    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey("users.id"), unique=True)
    name = db.Column(db.String(150), nullable=False)
    age = db.Column(db.Integer, nullable=False)
    school = db.Column(db.String(180), nullable=False)
    interest_area = db.Column(db.String(120), nullable=False)
    assigned_level = db.Column(db.String(20), nullable=False, default="basico")
    user = db.relationship("User", backref=db.backref("student_profile", uselist=False))


class EducationalContent(TimestampMixin, db.Model):
    """Recurso de aprendizaje clasificado por tema, nivel y competencia."""

    __tablename__ = "educational_contents"

    id = db.Column(db.Integer, primary_key=True)
    title = db.Column(db.String(180), nullable=False)
    topic = db.Column(db.String(120), nullable=False, index=True)
    level = db.Column(db.String(20), nullable=False, index=True)
    competency = db.Column(db.String(180), nullable=False)
    description = db.Column(db.Text, nullable=False)
    material_type = db.Column(db.String(40), nullable=False, default="lectura")
    resource_url = db.Column(db.String(500))
    status = db.Column(db.String(20), nullable=False, default="activo")


class DiagnosticQuestion(TimestampMixin, db.Model):
    """Pregunta activa que mide una competencia de una materia."""

    __tablename__ = "diagnostic_questions"

    id = db.Column(db.Integer, primary_key=True)
    topic = db.Column(db.String(120), nullable=False, index=True)
    prompt = db.Column(db.Text, nullable=False)
    expected_competency = db.Column(db.String(180), nullable=False)
    active = db.Column(db.Boolean, nullable=False, default=True)


class DiagnosticEvaluation(TimestampMixin, db.Model):
    """Evaluación de un estudiante y resultado de su clasificación IA."""

    __tablename__ = "diagnostic_evaluations"

    id = db.Column(db.Integer, primary_key=True)
    student_id = db.Column(db.Integer, db.ForeignKey("students.id"), nullable=False)
    status = db.Column(db.String(20), nullable=False, default="iniciada")
    classified_level = db.Column(db.String(20))
    explanation = db.Column(db.Text)
    ai_provider = db.Column(db.String(80))
    ai_model = db.Column(db.String(160))
    classified_at = db.Column(db.DateTime(timezone=True))
    student = db.relationship("Student", backref=db.backref("evaluations", lazy=True))


class DiagnosticAnswer(db.Model):
    """Respuesta individual vinculada a una pregunta y evaluación."""

    __tablename__ = "diagnostic_answers"

    id = db.Column(db.Integer, primary_key=True)
    evaluation_id = db.Column(
        db.Integer, db.ForeignKey("diagnostic_evaluations.id"), nullable=False
    )
    question_id = db.Column(
        db.Integer, db.ForeignKey("diagnostic_questions.id"), nullable=False
    )
    answer = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=utcnow, nullable=False)
    evaluation = db.relationship("DiagnosticEvaluation", backref=db.backref("answers", lazy=True))
    question = db.relationship("DiagnosticQuestion")


class ContentRecommendation(db.Model):
    """Relaciona un contenido educativo con una recomendación para un estudiante."""

    __tablename__ = "content_recommendations"

    id = db.Column(db.Integer, primary_key=True)
    student_id = db.Column(db.Integer, db.ForeignKey("students.id"), nullable=False, index=True)
    content_id = db.Column(db.Integer, db.ForeignKey("educational_contents.id"), nullable=False, index=True)
    evaluation_id = db.Column(db.Integer, db.ForeignKey("diagnostic_evaluations.id"), index=True)
    reason = db.Column(db.Text, nullable=False)
    created_at = db.Column(db.DateTime(timezone=True), default=utcnow, nullable=False)
    student = db.relationship("Student", backref=db.backref("recommendations", lazy=True))
    content = db.relationship("EducationalContent", backref=db.backref("recommendations", lazy=True))
    evaluation = db.relationship("DiagnosticEvaluation", backref=db.backref("recommendations", lazy=True))
