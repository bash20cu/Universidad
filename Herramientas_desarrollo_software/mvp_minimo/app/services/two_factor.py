# Archivo: two_factor.py
# Propósito: Emite y verifica desafíos TOTP y códigos temporales.
# Responsabilidades: Genera secretos, crea URI QR, valida ventanas de tiempo y aplica expiración, intentos y uso único.
# Dependencias: PyOTP, secrets, datetime, SQLAlchemy y modelos de usuario.
# Entradas y salidas: Recibe usuario/código; devuelve booleanos, URI, desafíos persistidos o TwoFactorError.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Emisión y verificación segura de códigos de segundo factor."""

from __future__ import annotations

import secrets
from datetime import timedelta, timezone

import pyotp

from app.extensions import db
from app.models import TwoFactorCode, User, utcnow


class TwoFactorError(ValueError):
    """Error controlado para códigos usados, vencidos o incorrectos."""


def generate_totp_secret() -> str:
    """Genera el secreto base32 que se vincula a una app autenticadora."""

    return pyotp.random_base32()


def totp_provisioning_uri(user: User, secret: str) -> str:
    """Construye la URI estándar que será convertida en código QR."""

    return pyotp.TOTP(secret).provisioning_uri(
        name=user.email,
        issuer_name="TutorIA",
    )


def verify_totp_code(user: User, code: str) -> bool:
    """Comprueba el código actual con una tolerancia de una ventana temporal."""

    if not user.totp_secret:
        return False
    return pyotp.TOTP(user.totp_secret).verify(code, valid_window=1)

    pass


def issue_code(user: User, lifetime_minutes: int = 10) -> tuple[TwoFactorCode, str]:
    """Genera un código aleatorio, lo guarda como hash y define su vencimiento."""

    code = f"{secrets.randbelow(1_000_000):06d}"
    challenge = TwoFactorCode(
        user_id=user.id,
        expires_at=utcnow() + timedelta(minutes=lifetime_minutes),
    )
    challenge.set_code(code)
    db.session.add(challenge)
    db.session.commit()
    return challenge, code


def verify_code(challenge: TwoFactorCode, code: str, max_attempts: int = 3) -> None:
    """Valida un desafío y lo marca como usado al primer acierto."""

    now = utcnow()
    expires_at = challenge.expires_at
    if expires_at.tzinfo is None:
        expires_at = expires_at.replace(tzinfo=timezone.utc)
    if challenge.used_at is not None:
        raise TwoFactorError("El código ya fue utilizado.")
    if challenge.attempts >= max_attempts:
        raise TwoFactorError("Se alcanzó el máximo de intentos.")
    if now >= expires_at:
        raise TwoFactorError("El código expiró.")

    if not challenge.check_code(code):
        challenge.attempts += 1
        db.session.commit()
        raise TwoFactorError("El código no es válido.")

    challenge.used_at = now
    db.session.commit()
