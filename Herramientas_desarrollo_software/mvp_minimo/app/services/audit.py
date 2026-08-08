# Archivo: audit.py
# Propósito: Centraliza el registro de eventos de auditoría.
# Responsabilidades: Captura acción, usuario, entidad, detalle e IP y persiste un registro trazable.
# Dependencias: Flask request, SQLAlchemy y modelo AuditLog.
# Entradas y salidas: Recibe datos del evento; no devuelve una respuesta HTTP y deja el evento en la base.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Servicio centralizado para registrar acciones de auditoría."""

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
    """Guarda una acción con usuario, entidad, detalle y dirección IP."""

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
