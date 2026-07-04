from __future__ import annotations

import secrets
from datetime import timedelta, timezone

from app.extensions import db
from app.models import TwoFactorCode, User, utcnow


class TwoFactorError(ValueError):
    pass


def issue_code(user: User, lifetime_minutes: int = 10) -> tuple[TwoFactorCode, str]:
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
