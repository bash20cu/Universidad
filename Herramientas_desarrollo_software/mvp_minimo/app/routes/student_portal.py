"""Portal privado para que cada estudiante consulte su propio progreso."""

from flask import Blueprint, flash, redirect, render_template, url_for
from flask_login import current_user, login_required

from app.extensions import db
from app.forms import StudentProfileForm
from app.models import ContentRecommendation, DiagnosticEvaluation, Student
from app.services.audit import record_event


bp = Blueprint("student_portal", __name__, url_prefix="/student")


def _require_student() -> Student | None:
    """Obtiene el perfil de la cuenta actual o bloquea roles no estudiantiles."""

    if current_user.role != "estudiante":
        return None
    return Student.query.filter_by(user_id=current_user.id).first()


@bp.get("/dashboard")
@login_required
def dashboard():
    """Muestra únicamente las métricas académicas del estudiante autenticado."""

    student = _require_student()
    if student is None and current_user.role != "estudiante":
        return redirect(url_for("main.dashboard"))
    evaluations = student.evaluations if student else []
    recommendations = ContentRecommendation.query.filter_by(student_id=student.id).count() if student else 0
    return render_template(
        "student/dashboard.html",
        student=student,
        metrics={
            "evaluations": len(evaluations),
            "classified": sum(item.status == "clasificada" for item in evaluations),
            "recommendations": recommendations,
        },
    )


@bp.route("/profile", methods=["GET", "POST"])
@login_required
def profile():
    """Permite completar el perfil académico sin modificar el nivel clasificado."""

    if current_user.role != "estudiante":
        return redirect(url_for("main.dashboard"))
    student = _require_student()
    form = StudentProfileForm(obj=student)
    if form.validate_on_submit():
        if student is None:
            student = Student(user_id=current_user.id, assigned_level="basico")
            db.session.add(student)
        student.name = form.name.data.strip()
        student.age = form.age.data
        student.school = form.school.data.strip()
        student.interest_area = form.interest_area.data.strip()
        db.session.commit()
        record_event(
            "student_profile_updated",
            user_id=current_user.id,
            entity_type="student",
            entity_id=str(student.id),
            detail=student.name,
        )
        flash("Tu perfil académico fue actualizado.", "success")
        return redirect(url_for("student_portal.dashboard"))
    return render_template("student/profile.html", form=form, student=student)


@bp.get("/progress")
@login_required
def progress():
    """Presenta el historial propio de evaluaciones y recomendaciones."""

    student = _require_student()
    if student is None:
        return redirect(url_for("student_portal.profile"))
    evaluations = DiagnosticEvaluation.query.filter_by(student_id=student.id).order_by(DiagnosticEvaluation.created_at.desc()).all()
    recommendations = ContentRecommendation.query.filter_by(student_id=student.id).order_by(ContentRecommendation.created_at.desc()).all()
    record_event("student_progress_viewed", user_id=current_user.id, entity_type="student", entity_id=str(student.id))
    return render_template("student/progress.html", student=student, evaluations=evaluations, recommendations=recommendations)
