"""CRUD del repositorio de recursos educativos del tutor."""

from flask import Blueprint, abort, flash, redirect, render_template, url_for
from flask_login import current_user, login_required

from app.extensions import db
from app.forms import EducationalContentForm
from app.models import EducationalContent
from app.routes import roles_required
from app.services.audit import record_event


bp = Blueprint("contents", __name__, url_prefix="/contents")


@bp.get("")
@login_required
@roles_required("administrador", "docente")
def index():
    """Lista contenidos ordenados por nivel, tema y título."""

    contents = EducationalContent.query.order_by(
        EducationalContent.level.asc(), EducationalContent.topic.asc(), EducationalContent.title.asc()
    ).all()
    level_totals = {
        level: sum(content.level == level for content in contents)
        for level in ("basico", "intermedio", "avanzado")
    }
    topics = sorted({content.topic for content in contents})
    return render_template(
        "contents/index.html",
        contents=contents,
        level_totals=level_totals,
        topics=topics,
    )


@bp.route("/new", methods=["GET", "POST"])
@login_required
@roles_required("administrador", "docente")
def create():
    """Registra un recurso educativo validado por el formulario."""

    form = EducationalContentForm()
    if form.validate_on_submit():
        content = EducationalContent(
            title=form.title.data.strip(),
            topic=form.topic.data.strip(),
            level=form.level.data,
            competency=form.competency.data.strip(),
            description=form.description.data.strip(),
            material_type=form.material_type.data or "lectura",
            resource_url=(form.resource_url.data or "").strip() or None,
            status=form.status.data or "activo",
        )
        db.session.add(content)
        db.session.commit()
        record_event(
            "content_created",
            user_id=current_user.id,
            entity_type="educational_content",
            entity_id=str(content.id),
            detail=content.title,
        )
        flash("Contenido educativo registrado correctamente.", "success")
        return redirect(url_for("contents.index"))
    return render_template("contents/form.html", form=form, title="Nuevo contenido")


@bp.route("/<int:content_id>/edit", methods=["GET", "POST"])
@login_required
@roles_required("administrador", "docente")
def edit(content_id: int):
    """Edita un recurso sin perder su clasificación académica."""

    content = db.get_or_404(EducationalContent, content_id)
    form = EducationalContentForm(obj=content)
    if form.validate_on_submit():
        form.populate_obj(content)
        # Normaliza espacios de los campos textuales antes de guardar en SQLite.
        content.title = content.title.strip()
        content.topic = content.topic.strip()
        content.competency = content.competency.strip()
        content.description = content.description.strip()
        content.resource_url = (content.resource_url or "").strip() or None
        db.session.commit()
        record_event(
            "content_updated",
            user_id=current_user.id,
            entity_type="educational_content",
            entity_id=str(content.id),
            detail=content.title,
        )
        flash("Contenido educativo actualizado.", "success")
        return redirect(url_for("contents.index"))
    return render_template("contents/form.html", form=form, title="Editar contenido", content=content)


@bp.post("/<int:content_id>/delete")
@login_required
@roles_required("administrador")
def delete(content_id: int):
    """Elimina un recurso solo cuando no tiene recomendaciones asociadas."""

    content = db.get_or_404(EducationalContent, content_id)
    # Conserva la trazabilidad académica: un recurso ya recomendado no se elimina
    # mientras exista evidencia que lo vincule a una ruta de aprendizaje.
    if content.recommendations:
        abort(409, description="No se puede eliminar un contenido que ya fue recomendado.")
    content_title = content.title
    db.session.delete(content)
    db.session.commit()
    record_event(
        "content_deleted",
        user_id=current_user.id,
        entity_type="educational_content",
        entity_id=str(content_id),
        detail=content_title,
    )
    flash("Contenido educativo eliminado.", "success")
    return redirect(url_for("contents.index"))
