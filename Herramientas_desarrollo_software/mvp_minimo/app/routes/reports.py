# Archivo: reports.py
# Propósito: Presenta reportes generales e individuales de progreso.
# Responsabilidades: Calcula métricas de evaluaciones, niveles y recomendaciones, y registra la consulta del reporte.
# Dependencias: Flask, Flask-Login, SQLAlchemy, modelos y auditoría.
# Entradas y salidas: Recibe la sesión y, opcionalmente, un id de estudiante; devuelve reportes HTML.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Reportes académicos calculados desde evaluaciones y recomendaciones."""

from flask import Blueprint, render_template
from flask_login import current_user, login_required

from app.extensions import db
from app.models import ContentRecommendation, DiagnosticEvaluation, Student
from app.routes import roles_required
from app.services.audit import record_event


bp = Blueprint("reports", __name__, url_prefix="/reports")


@bp.get("")
@login_required
@roles_required("administrador", "docente")
def index():
    """Presenta un resumen de progreso real de todos los estudiantes."""

    students = Student.query.order_by(Student.name.asc()).all()
    summaries = []
    for student in students:
        evaluations = DiagnosticEvaluation.query.filter_by(student_id=student.id).all()
        summaries.append({
            "student": student,
            "evaluations": evaluations,
            "classified": sum(evaluation.status == "clasificada" for evaluation in evaluations),
            "recommendations": ContentRecommendation.query.filter_by(student_id=student.id).count(),
            "progress": round((sum(evaluation.status == "clasificada" for evaluation in evaluations) / len(evaluations)) * 100) if evaluations else 0,
        })
    level_totals = {
        level: sum(student.assigned_level == level for student in students)
        for level in ("basico", "intermedio", "avanzado")
    }
    report_metrics = {
        "students": len(students),
        "evaluations": sum(len(summary["evaluations"]) for summary in summaries),
        "classified": sum(summary["classified"] for summary in summaries),
        "recommendations": sum(summary["recommendations"] for summary in summaries),
        "levels": level_totals,
    }
    record_event("report_viewed", user_id=current_user.id, entity_type="report", detail="Reporte general consultado")
    return render_template("reports/index.html", summaries=summaries, report_metrics=report_metrics)


@bp.get("/<int:student_id>")
@login_required
@roles_required("administrador", "docente")
def detail(student_id: int):
    """Muestra el historial académico y recomendaciones de un estudiante."""

    student = db.get_or_404(Student, student_id)
    evaluations = DiagnosticEvaluation.query.filter_by(student_id=student.id).order_by(DiagnosticEvaluation.created_at.desc()).all()
    recommendations = ContentRecommendation.query.filter_by(student_id=student.id).order_by(ContentRecommendation.created_at.desc()).all()
    progress = round((sum(evaluation.status == "clasificada" for evaluation in evaluations) / len(evaluations)) * 100) if evaluations else 0
    record_event("report_viewed", user_id=current_user.id, entity_type="student_report", entity_id=str(student.id), detail=student.name)
    return render_template("reports/detail.html", student=student, evaluations=evaluations, recommendations=recommendations, progress=progress)
