# Archivo: recommendations.py
# Propósito: Genera y consulta rutas de aprendizaje recomendadas.
# Responsabilidades: Selecciona contenidos para estudiantes clasificados, persiste razones y audita la generación.
# Dependencias: Flask, Flask-Login, SQLAlchemy, modelos, servicio de recomendaciones y auditoría.
# Entradas y salidas: Recibe el id del estudiante; devuelve vistas, redirecciones y recomendaciones persistidas.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Rutas para generar y consultar rutas de aprendizaje explicables."""

from flask import Blueprint, flash, redirect, render_template, url_for
from flask_login import current_user, login_required

from app.extensions import db
from app.models import ContentRecommendation, DiagnosticEvaluation, Student
from app.routes import roles_required
from app.services.audit import record_event
from app.services.recommendation import recommend_contents, recommendation_reason


bp = Blueprint("recommendations", __name__, url_prefix="/recommendations")


@bp.get("")
@login_required
@roles_required("administrador", "docente")
def index():
    """Muestra estudiantes que ya tienen recomendaciones persistidas."""

    students = Student.query.order_by(Student.name.asc()).all()
    return render_template("recommendations/index.html", students=students)


@bp.route("/<int:student_id>", methods=["GET", "POST"])
@login_required
@roles_required("administrador", "docente")
def detail(student_id: int):
    """Consulta o genera recomendaciones para un estudiante específico."""

    student = db.get_or_404(Student, student_id)
    if not student.evaluations:
        flash("Primero registra y clasifica una evaluación diagnóstica.", "warning")
        return redirect(url_for("diagnostics.create"))

    latest_evaluation = max(student.evaluations, key=lambda evaluation: evaluation.created_at)
    if latest_evaluation.status != "clasificada":
        flash("La evaluación todavía está pendiente de clasificación con IA.", "warning")
        return redirect(url_for("diagnostics.detail", evaluation_id=latest_evaluation.id))
    recommendations = ContentRecommendation.query.filter_by(student_id=student.id).order_by(ContentRecommendation.created_at.desc()).all()
    if not recommendations or (latest_evaluation.status == "clasificada" and not any(
        recommendation.evaluation_id == latest_evaluation.id for recommendation in recommendations
    )):
        recommendations = _persist_recommendations(student, latest_evaluation)
    return render_template(
        "recommendations/detail.html",
        student=student,
        evaluation=latest_evaluation,
        recommendations=recommendations,
    )


def _persist_recommendations(student: Student, evaluation: DiagnosticEvaluation):
    """Crea recomendaciones nuevas y conserva la evidencia de cada diagnóstico."""

    candidates = recommend_contents(student)
    recommendations = []
    for content in candidates:
        recommendation = ContentRecommendation(
            student_id=student.id,
            content_id=content.id,
            evaluation_id=evaluation.id,
            reason=recommendation_reason(student, content),
        )
        db.session.add(recommendation)
        recommendations.append(recommendation)
    db.session.commit()
    record_event(
        "recommendations_generated",
        user_id=current_user.id,
        entity_type="student",
        entity_id=str(student.id),
        detail=f"{len(recommendations)} recomendaciones para {student.name}",
    )
    return recommendations
