# Archivo: main.py
# Propósito: Sirve las páginas públicas y el panel general de TutorIA.
# Responsabilidades: Presenta inicio, ayuda, dashboard y métricas según el rol autenticado.
# Dependencias: Flask, Flask-Login y modelos académicos.
# Entradas y salidas: Recibe la sesión actual; devuelve páginas HTML y redirecciones por rol.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
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
