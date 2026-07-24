from __future__ import annotations

from datetime import datetime, timezone

from flask import Blueprint, current_app, flash, redirect, render_template, session, url_for
from flask_login import current_user, login_user, logout_user
from sqlalchemy.exc import IntegrityError

from app.extensions import db
from app.forms import LoginForm, RegistrationForm, TwoFactorForm
from app.models import TwoFactorCode, User
from app.services.audit import record_event
from app.services.email import EmailDeliveryError, send_two_factor_code
from app.services.two_factor import TwoFactorError, issue_code, verify_code


bp = Blueprint("auth", __name__, url_prefix="/auth")


@bp.route("/register", methods=["GET", "POST"])
def register():
    """Crea una cuenta estudiantil sin permitir elevación de privilegios."""

    if current_user.is_authenticated:
        return redirect(url_for("main.dashboard"))

    form = RegistrationForm()
    if form.validate_on_submit():
        username = form.username.data.strip()
        email = form.email.data.strip().lower()
        if User.query.filter((User.username == username) | (User.email == email)).first():
            flash("El usuario o correo ya están registrados.", "warning")
            return render_template("auth/register.html", form=form), 409

        user = User(username=username, email=email, role="estudiante", active=True)
        user.set_password(form.password.data)
        db.session.add(user)
        try:
            db.session.flush()
            record_event(
                "user_registered",
                user_id=user.id,
                entity_type="User",
                entity_id=str(user.id),
                detail="Autorregistro de cuenta estudiantil",
            )
        except IntegrityError:
            db.session.rollback()
            flash("No se pudo crear la cuenta porque el usuario o correo ya existe.", "warning")
            return render_template("auth/register.html", form=form), 409

        db.session.commit()
        flash("Cuenta creada. Ya puedes iniciar sesión y confirmar tu correo con 2FA.", "success")
        return redirect(url_for("auth.login"))

    return render_template("auth/register.html", form=form)


@bp.route("/login", methods=["GET", "POST"])
def login():
    if current_user.is_authenticated:
        return redirect(url_for("main.dashboard"))
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(username=form.username.data.strip()).first()
        if user is None or not user.check_password(form.password.data) or not user.active:
            record_event("login_failed", detail="Credenciales inválidas")
            flash("Usuario o contraseña incorrectos.", "danger")
            return render_template("auth/login.html", form=form), 401

        challenge, code = issue_code(user)
        try:
            send_two_factor_code(user.email, code)
        except EmailDeliveryError as error:
            current_app.logger.error("%s", error)
            flash("No fue posible enviar el código. Intenta nuevamente.", "danger")
            return render_template("auth/login.html", form=form), 503

        session.clear()
        session["pending_2fa_id"] = challenge.id
        record_event("password_verified", user_id=user.id)
        flash("Enviamos un código de 6 dígitos a tu correo.", "info")
        return redirect(url_for("auth.verify_2fa"))
    return render_template("auth/login.html", form=form)


@bp.route("/verify", methods=["GET", "POST"])
def verify_2fa():
    challenge_id = session.get("pending_2fa_id")
    challenge = db.session.get(TwoFactorCode, challenge_id) if challenge_id else None
    if challenge is None:
        flash("Inicia sesión nuevamente.", "warning")
        return redirect(url_for("auth.login"))

    form = TwoFactorForm()
    if form.validate_on_submit():
        try:
            verify_code(challenge, form.code.data)
        except TwoFactorError as error:
            record_event("two_factor_failed", user_id=challenge.user_id, detail=str(error))
            flash(str(error), "danger")
            return render_template("auth/verify.html", form=form), 400

        user = db.session.get(User, challenge.user_id)
        user.last_login_at = datetime.now(timezone.utc)
        db.session.commit()
        session.pop("pending_2fa_id", None)
        login_user(user)
        record_event("login_success", user_id=user.id)
        return redirect(url_for("main.dashboard"))
    return render_template("auth/verify.html", form=form)


@bp.post("/logout")
def logout():
    user_id = current_user.id if current_user.is_authenticated else None
    logout_user()
    session.clear()
    if user_id:
        record_event("logout", user_id=user_id)
    return redirect(url_for("auth.login"))
