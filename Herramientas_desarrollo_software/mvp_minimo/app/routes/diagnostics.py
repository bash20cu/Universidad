from flask import Blueprint, current_app, flash, redirect, render_template, request, url_for
from flask_login import current_user, login_required

from ai_provider import AIProviderError

from app.extensions import db
from app.forms import DiagnosticEvaluationForm
from app.models import DiagnosticAnswer, DiagnosticEvaluation, DiagnosticQuestion, Student
from app.routes import roles_required
from app.services.audit import record_event
from app.services.diagnostic import classify_evaluation, format_classification_explanation


bp = Blueprint("diagnostics", __name__, url_prefix="/diagnostics")


def _active_questions():
    return DiagnosticQuestion.query.filter_by(active=True).order_by(DiagnosticQuestion.topic.asc(), DiagnosticQuestion.id.asc()).all()


def _prepare_form(form: DiagnosticEvaluationForm) -> None:
    students = Student.query.order_by(Student.name.asc()).all()
    form.student_id.choices = [(student.id, f"{student.name} - {student.interest_area}") for student in students]


@bp.get("")
@login_required
@roles_required("administrador", "docente")
def index():
    evaluations = DiagnosticEvaluation.query.order_by(DiagnosticEvaluation.created_at.desc()).all()
    status_totals = {
        "pendiente_ia": sum(evaluation.status == "pendiente_ia" for evaluation in evaluations),
        "clasificada": sum(evaluation.status == "clasificada" for evaluation in evaluations),
    }
    return render_template("diagnostics/index.html", evaluations=evaluations, status_totals=status_totals)


@bp.route("/new", methods=["GET", "POST"])
@login_required
@roles_required("administrador", "docente")
def create():
    form = DiagnosticEvaluationForm()
    _prepare_form(form)
    questions = _active_questions()

    if not form.student_id.choices:
        flash("Primero registra al menos un estudiante.", "warning")
        return redirect(url_for("students.create"))
    if not questions:
        flash("No hay preguntas diagnósticas activas.", "warning")
        return redirect(url_for("diagnostics.index"))

    if form.validate_on_submit():
        answers_by_question = {}
        missing_questions = []
        for question in questions:
            value = request.form.get(f"answer_{question.id}", "").strip()
            if not value:
                missing_questions.append(question.id)
            answers_by_question[question.id] = value

        if missing_questions:
            flash("Responde todas las preguntas antes de guardar la evaluación.", "danger")
            return render_template("diagnostics/form.html", form=form, questions=questions), 400

        evaluation = DiagnosticEvaluation(
            student_id=form.student_id.data,
            status="pendiente_ia",
            explanation="Evaluación registrada. Pendiente de clasificación por IA.",
        )
        db.session.add(evaluation)
        db.session.flush()

        # Cada respuesta queda asociada a la evaluación y a la pregunta original para preservar evidencia académica.
        for question in questions:
            db.session.add(
                DiagnosticAnswer(
                    evaluation_id=evaluation.id,
                    question_id=question.id,
                    answer=answers_by_question[question.id],
                )
            )

        db.session.commit()
        record_event(
            "diagnostic_created",
            user_id=current_user.id,
            entity_type="diagnostic_evaluation",
            entity_id=str(evaluation.id),
            detail=f"Estudiante {evaluation.student.name}",
        )
        flash("Evaluación diagnóstica guardada. Queda pendiente la clasificación con IA.", "success")
        return redirect(url_for("diagnostics.detail", evaluation_id=evaluation.id))

    return render_template("diagnostics/form.html", form=form, questions=questions)


@bp.get("/<int:evaluation_id>")
@login_required
@roles_required("administrador", "docente")
def detail(evaluation_id: int):
    evaluation = db.get_or_404(DiagnosticEvaluation, evaluation_id)
    answers = DiagnosticAnswer.query.filter_by(evaluation_id=evaluation.id).order_by(DiagnosticAnswer.id.asc()).all()
    return render_template("diagnostics/detail.html", evaluation=evaluation, answers=answers)


@bp.post("/<int:evaluation_id>/classify")
@login_required
@roles_required("administrador", "docente")
def classify(evaluation_id: int):
    evaluation = db.get_or_404(DiagnosticEvaluation, evaluation_id)
    if evaluation.status == "clasificada":
        flash("La evaluación ya fue clasificada.", "info")
        return redirect(url_for("diagnostics.detail", evaluation_id=evaluation.id))

    provider = current_app.extensions["ai_provider"]
    try:
        classification = classify_evaluation(evaluation, provider)
    except (AIProviderError, ValueError) as error:
        record_event(
            "diagnostic_classification_failed",
            user_id=current_user.id,
            entity_type="diagnostic_evaluation",
            entity_id=str(evaluation.id),
            detail=str(error),
        )
        flash(f"No fue posible clasificar la evaluación: {error}", "danger")
        return redirect(url_for("diagnostics.detail", evaluation_id=evaluation.id))

    # La clasificación actualiza tanto la evaluación como el nivel asignado del estudiante.
    evaluation.classified_level = classification.level
    evaluation.explanation = format_classification_explanation(classification)
    evaluation.status = "clasificada"
    evaluation.student.assigned_level = classification.level
    db.session.commit()

    record_event(
        "diagnostic_classified",
        user_id=current_user.id,
        entity_type="diagnostic_evaluation",
        entity_id=str(evaluation.id),
        detail=f"Nivel {classification.level} para {evaluation.student.name}",
    )
    flash("Evaluación clasificada correctamente con IA.", "success")
    return redirect(url_for("diagnostics.detail", evaluation_id=evaluation.id))
