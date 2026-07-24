"""Administración de cuentas reservada al rol administrador."""

from flask import Blueprint, flash, redirect, render_template, url_for
from flask_login import current_user, login_required

from app.extensions import db
from app.forms import UserForm
from app.models import AuditLog, User
from app.routes import roles_required
from app.services.audit import record_event


bp = Blueprint("users", __name__, url_prefix="/users")


@bp.get("")
@login_required
@roles_required("administrador")
def index():
    """Lista cuentas para que el administrador pueda supervisar el acceso."""

    users = User.query.order_by(User.username.asc()).all()
    return render_template("users/index.html", users=users)


@bp.get("/audit")
@login_required
@roles_required("administrador")
def audit():
    """Muestra al administrador las acciones registradas por el sistema."""

    entries = AuditLog.query.order_by(AuditLog.created_at.desc()).limit(200).all()
    return render_template("users/audit.html", entries=entries)


@bp.route("/new", methods=["GET", "POST"])
@login_required
@roles_required("administrador")
def create():
    """Registra una cuenta con contraseña almacenada mediante hash."""

    form = UserForm()
    if form.validate_on_submit():
        if not form.password.data:
            form.password.errors.append("La contraseña es obligatoria para un usuario nuevo.")
            return render_template("users/form.html", form=form, title="Nuevo usuario", require_password=True), 400
        if User.query.filter((User.username == form.username.data.strip()) | (User.email == form.email.data.strip())).first():
            flash("El usuario o correo ya existe.", "danger")
            return render_template("users/form.html", form=form, title="Nuevo usuario"), 409
        user = User(username=form.username.data.strip(), email=form.email.data.strip(), role=form.role.data, active=form.active.data == "1")
        user.set_password(form.password.data)
        db.session.add(user)
        db.session.commit()
        record_event("user_created", user_id=current_user.id, entity_type="user", entity_id=str(user.id), detail=user.username)
        flash("Usuario creado correctamente.", "success")
        return redirect(url_for("users.index"))
    return render_template("users/form.html", form=form, title="Nuevo usuario", require_password=True)


@bp.route("/<int:user_id>/edit", methods=["GET", "POST"])
@login_required
@roles_required("administrador")
def edit(user_id: int):
    """Actualiza datos de cuenta y permite cambiar la contraseña opcionalmente."""

    user = db.get_or_404(User, user_id)
    form = UserForm(obj=user)
    form.active.data = "1" if user.active else "0"
    if form.validate_on_submit():
        duplicate = User.query.filter(User.id != user.id).filter(
            (User.username == form.username.data.strip()) | (User.email == form.email.data.strip())
        ).first()
        if duplicate:
            flash("El usuario o correo ya existe.", "danger")
            return render_template("users/form.html", form=form, title="Editar usuario"), 409
        user.username = form.username.data.strip()
        user.email = form.email.data.strip()
        user.role = form.role.data
        user.active = form.active.data == "1"
        if user.id == current_user.id and not user.active:
            flash("No puedes desactivar la cuenta con la que estás trabajando.", "warning")
            return render_template("users/form.html", form=form, title="Editar usuario", user=user), 409
        if form.password.data:
            user.set_password(form.password.data)
        db.session.commit()
        record_event("user_updated", user_id=current_user.id, entity_type="user", entity_id=str(user.id), detail=user.username)
        flash("Usuario actualizado correctamente.", "success")
        return redirect(url_for("users.index"))
    return render_template("users/form.html", form=form, title="Editar usuario", user=user)
