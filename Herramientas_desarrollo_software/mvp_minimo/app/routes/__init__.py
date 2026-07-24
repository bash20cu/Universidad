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
