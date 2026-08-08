# Archivo: auth.py
# Propósito: Gestiona login, registro y configuración del segundo factor.
# Responsabilidades: Valida credenciales, emite desafíos, configura TOTP, crea QR, registra eventos y cierra sesiones.
# Dependencias: Flask, Flask-Login, PyOTP, formularios, modelos y servicios de correo/auditoría.
# Entradas y salidas: Recibe formularios HTTP y sesión; devuelve redirecciones, vistas, mensajes y eventos de seguridad.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Autenticación, autorregistro y segundo factor de TutorIA."""

from __future__ import annotations

from datetime import datetime, timezone
import base64
from io import BytesIO

from flask import Blueprint, current_app, flash, redirect, render_template, session, url_for
from flask_login import current_user, login_required, login_user, logout_user
import qrcode
from sqlalchemy.exc import IntegrityError

from app.extensions import db
from app.forms import LoginForm, RegistrationForm, TotpForm, TwoFactorForm
from app.models import TwoFactorCode, User
from app.services.audit import record_event
from app.services.two_factor import TwoFactorError, generate_totp_secret, issue_code, totp_provisioning_uri, verify_code, verify_totp_code


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

        user = User(
            username=username,
            email=email,
            role="estudiante",
            active=True,
            # El secreto se crea durante el registro para que el estudiante
            # configure su segundo factor antes de utilizar la plataforma.
            totp_secret=generate_totp_secret(),
        )
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
        session.clear()
        session["pending_registration_totp_user_id"] = user.id
        flash("Cuenta creada. Configura ahora tu autenticador TOTP para continuar.", "success")
        return redirect(url_for("auth.register_totp"))

    return render_template("auth/register.html", form=form)


@bp.route("/login", methods=["GET", "POST"])
def login():
    """Comprueba credenciales y envía el desafío 2FA antes de crear sesión."""

    if current_user.is_authenticated:
        return redirect(url_for("main.dashboard"))
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(username=form.username.data.strip()).first()
        if user is None or not user.check_password(form.password.data) or not user.active:
            record_event("login_failed", detail="Credenciales inválidas")
            flash("Usuario o contraseña incorrectos.", "danger")
            return render_template("auth/login.html", form=form), 401

        if user.totp_enabled and user.totp_secret:
            session.clear()
            session["pending_totp_user_id"] = user.id
            record_event("password_verified", user_id=user.id, detail="Pendiente de TOTP")
            flash("Abre tu aplicación autenticadora e ingresa el código de 6 dígitos.", "info")
            return redirect(url_for("auth.verify_totp"))

        challenge, code = issue_code(user)
        # Mientras la cuenta no tenga TOTP configurado, el código se muestra
        # únicamente en la consola para permitir la activación durante la demo.
        current_app.logger.warning("Código TOTP inicial para %s: %s", user.username, code)

        session.clear()
        session["pending_2fa_id"] = challenge.id
        record_event("password_verified", user_id=user.id)
        flash("Código inicial generado en la consola de desarrollo. Ingresa los 6 dígitos para continuar.", "info")
        return redirect(url_for("auth.verify_2fa"))
    return render_template("auth/login.html", form=form)


@bp.route("/verify", methods=["GET", "POST"])
def verify_2fa():
    """Valida el código temporal y completa la sesión autenticada."""

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


@bp.route("/verify-totp", methods=["GET", "POST"])
def verify_totp():
    """Valida el código TOTP y completa el inicio de sesión."""

    user_id = session.get("pending_totp_user_id")
    user = db.session.get(User, user_id) if user_id else None
    if user is None or not user.totp_enabled:
        flash("Inicia sesión nuevamente.", "warning")
        return redirect(url_for("auth.login"))

    form = TotpForm()
    if form.validate_on_submit():
        if not verify_totp_code(user, form.code.data):
            record_event("two_factor_failed", user_id=user.id, detail="Código TOTP inválido")
            flash("El código TOTP no es válido o ya expiró.", "danger")
            return render_template("auth/verify_totp.html", form=form), 400
        user.last_login_at = datetime.now(timezone.utc)
        db.session.commit()
        session.pop("pending_totp_user_id", None)
        login_user(user)
        record_event("login_success", user_id=user.id, detail="Acceso verificado con TOTP")
        return redirect(url_for("main.dashboard"))
    return render_template("auth/verify_totp.html", form=form)


@bp.route("/register/2fa", methods=["GET", "POST"])
def register_totp():
    """Activa TOTP inmediatamente después de crear una cuenta estudiantil."""

    user_id = session.get("pending_registration_totp_user_id")
    user = db.session.get(User, user_id) if user_id else None
    if user is None or not user.totp_secret:
        flash("Completa primero el registro de la cuenta.", "warning")
        return redirect(url_for("auth.register"))

    form = TotpForm()
    if form.validate_on_submit():
        if not verify_totp_code(user, form.code.data):
            flash("El código no coincide. Revisa Google Authenticator e intenta de nuevo.", "danger")
            return render_template("auth/totp_setup.html", form=form, qr_data=_qr_data(user), secret=user.totp_secret, registration=True), 400
        user.totp_enabled = True
        user.last_login_at = datetime.now(timezone.utc)
        db.session.commit()
        session.pop("pending_registration_totp_user_id", None)
        login_user(user)
        record_event("totp_enabled", user_id=user.id, entity_type="user", entity_id=str(user.id), detail="Activado durante el registro")
        record_event("login_success", user_id=user.id, detail="Registro confirmado con TOTP")
        flash("TOTP activado. ¡Bienvenido a TutorIA!", "success")
        return redirect(url_for("main.dashboard"))
    return render_template("auth/totp_setup.html", form=form, qr_data=_qr_data(user), secret=user.totp_secret, registration=True)


@bp.route("/totp/setup", methods=["GET", "POST"])
@login_required
def setup_totp():
    """Genera un QR y activa TOTP después de validar el primer código."""

    user = db.session.get(User, current_user.id)
    if user.totp_enabled:
        flash("El autenticador TOTP ya está activo en tu cuenta.", "info")
        return redirect(url_for("main.dashboard"))
    if not user.totp_secret:
        user.totp_secret = generate_totp_secret()
        db.session.commit()

    form = TotpForm()
    if form.validate_on_submit():
        if not verify_totp_code(user, form.code.data):
            flash("El código no coincide. Revisa el autenticador e intenta de nuevo.", "danger")
            return render_template("auth/totp_setup.html", form=form, qr_data=_qr_data(user), secret=user.totp_secret), 400
        user.totp_enabled = True
        db.session.commit()
        record_event("totp_enabled", user_id=user.id, entity_type="user", entity_id=str(user.id))
        flash("TOTP activado correctamente. En el próximo login usarás tu autenticador.", "success")
        return redirect(url_for("main.dashboard"))
    return render_template("auth/totp_setup.html", form=form, qr_data=_qr_data(user), secret=user.totp_secret)


def _qr_data(user: User) -> str:
    """Convierte la URI TOTP del usuario en una imagen PNG embebida."""

    qr = qrcode.make(totp_provisioning_uri(user, user.totp_secret))
    buffer = BytesIO()
    qr.save(buffer, format="PNG")
    encoded = base64.b64encode(buffer.getvalue()).decode("ascii")
    return f"data:image/png;base64,{encoded}"


@bp.post("/logout")
def logout():
    """Cierra la sesión actual y registra la salida del usuario."""

    user_id = current_user.id if current_user.is_authenticated else None
    logout_user()
    session.clear()
    if user_id:
        record_event("logout", user_id=user_id)
    return redirect(url_for("auth.login"))
