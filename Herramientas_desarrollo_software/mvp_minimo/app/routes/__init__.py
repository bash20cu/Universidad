# Archivo: __init__.py
# Propósito: Proporciona decoradores de autorización compartidos por las rutas.
# Responsabilidades: Comprueba autenticación y roles antes de permitir el acceso a una vista Flask.
# Dependencias: Flask y Flask-Login.
# Entradas y salidas: Recibe roles y una vista; devuelve un decorador que produce 403 cuando corresponde.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Decoradores de autorización compartidos por las rutas Flask."""

from functools import wraps

from flask import abort
from flask_login import current_user


def roles_required(*roles: str):
    """Restringe una vista a usuarios autenticados con alguno de los roles."""

    def decorator(view):
        """Aplica la validación de rol a la función de vista recibida."""

        @wraps(view)
        def wrapped(*args, **kwargs):
            """Devuelve 403 si la sesión no tiene permisos suficientes."""

            if not current_user.is_authenticated or current_user.role not in roles:
                abort(403)
            return view(*args, **kwargs)

        return wrapped

    return decorator
