"""Rutas públicas y panel general de TutorIA."""

from flask import Blueprint, redirect, render_template, url_for
from flask_login import current_user, login_required

from app.models import DiagnosticEvaluation, EducationalContent, Student, User


bp = Blueprint("main", __name__)


@bp.get("/")
def index():
    """Muestra la página pública de bienvenida."""

    return render_template("main/home.html")


@bp.get("/help")
def help_page():
    """Explica el uso de TutorIA según el rol de la persona usuaria."""

    return render_template("main/help.html")


@bp.get("/dashboard")
@login_required
def dashboard():
    """Calcula y muestra las métricas resumidas del sistema."""

    if current_user.role == "estudiante":
        return redirect(url_for("student_portal.dashboard"))
    metrics = {
        "users": User.query.count(),
        "students": Student.query.count(),
        "contents": EducationalContent.query.count(),
        "evaluations": DiagnosticEvaluation.query.count(),
    }
    return render_template("main/dashboard.html", metrics=metrics)
