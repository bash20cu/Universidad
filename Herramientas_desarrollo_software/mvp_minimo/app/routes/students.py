from flask import Blueprint, abort, flash, redirect, render_template, url_for
from flask_login import current_user, login_required

from app.extensions import db
from app.forms import StudentForm
from app.models import Student
from app.routes import roles_required
from app.services.audit import record_event


bp = Blueprint("students", __name__, url_prefix="/students")


@bp.get("")
@login_required
@roles_required("administrador", "docente")
def index():
    students = Student.query.order_by(Student.name.asc()).all()
    level_totals = {
        level: sum(student.assigned_level == level for student in students)
        for level in ("basico", "intermedio", "avanzado")
    }
    return render_template("students/index.html", students=students, level_totals=level_totals)


@bp.route("/new", methods=["GET", "POST"])
@login_required
@roles_required("administrador", "docente")
def create():
    form = StudentForm()
    if form.validate_on_submit():
        student = Student(
            name=form.name.data.strip(), age=form.age.data,
            school=form.school.data.strip(), interest_area=form.interest_area.data.strip(),
            assigned_level=form.assigned_level.data,
        )
        db.session.add(student)
        db.session.commit()
        record_event("student_created", user_id=current_user.id, entity_type="student", entity_id=str(student.id), detail=student.name)
        flash("Estudiante registrado correctamente.", "success")
        return redirect(url_for("students.index"))
    return render_template("students/form.html", form=form, title="Nuevo estudiante")


@bp.route("/<int:student_id>/edit", methods=["GET", "POST"])
@login_required
@roles_required("administrador", "docente")
def edit(student_id: int):
    student = db.get_or_404(Student, student_id)
    form = StudentForm(obj=student)
    if form.validate_on_submit():
        form.populate_obj(student)
        student.name = student.name.strip()
        student.school = student.school.strip()
        student.interest_area = student.interest_area.strip()
        db.session.commit()
        record_event("student_updated", user_id=current_user.id, entity_type="student", entity_id=str(student.id), detail=student.name)
        flash("Estudiante actualizado.", "success")
        return redirect(url_for("students.index"))
    return render_template("students/form.html", form=form, title="Editar estudiante", student=student)


@bp.post("/<int:student_id>/delete")
@login_required
@roles_required("administrador")
def delete(student_id: int):
    student = db.get_or_404(Student, student_id)
    if student.evaluations:
        abort(409, description="No se puede eliminar un estudiante con evaluaciones.")
    student_name = student.name
    db.session.delete(student)
    db.session.commit()
    record_event("student_deleted", user_id=current_user.id, entity_type="student", entity_id=str(student_id), detail=student_name)
    flash("Estudiante eliminado.", "success")
    return redirect(url_for("students.index"))
