from flask import request

from app.extensions import db
from app.models import AuditLog


def record_event(
    action: str,
    *,
    user_id: int | None = None,
    entity_type: str | None = None,
    entity_id: str | None = None,
    detail: str | None = None,
) -> None:
    forwarded = request.headers.get("X-Forwarded-For", "")
    ip_address = forwarded.split(",", 1)[0].strip() or request.remote_addr
    db.session.add(
        AuditLog(
            user_id=user_id,
            action=action,
            entity_type=entity_type,
            entity_id=entity_id,
            detail=detail,
            ip_address=ip_address,
        )
    )
    db.session.commit()

