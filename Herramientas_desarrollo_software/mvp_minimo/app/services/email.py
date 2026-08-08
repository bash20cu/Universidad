# Archivo: email.py
# Propósito: Entrega códigos de segundo factor por consola o Resend.
# Responsabilidades: Selecciona canales configurados, construye la solicitud remota y maneja errores de entrega.
# Dependencias: __future__, json, urllib, flask
# Entradas y salidas: Recibe usuario y código; produce envío, salida de desarrollo o error controlado.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Entrega de códigos 2FA mediante consola, Resend o ambos canales."""

from __future__ import annotations

import json
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

from flask import current_app


class EmailDeliveryError(RuntimeError):
    """Error controlado de configuración o comunicación con Resend."""

    pass


def send_two_factor_code(recipient: str, code: str) -> None:
    """Entrega el código por el canal configurado para el entorno.

    ``console`` es útil durante el desarrollo, mientras que
    ``resend_console`` permite demostrar el correo sin perder la visibilidad
    del código en la terminal y en el panel de escritorio.
    """
    delivery = current_app.config["TWO_FACTOR_DELIVERY"].lower()
    if delivery in {"console", "resend_console"}:
        current_app.logger.warning("Código 2FA de desarrollo para %s: %s", recipient, code)

    if delivery == "console":
        return

    if delivery not in {"resend", "resend_console"}:
        raise EmailDeliveryError(
            "TWO_FACTOR_DELIVERY debe ser console, resend o resend_console."
        )

    api_key = current_app.config.get("RESEND_API_KEY")
    if not api_key:
        raise EmailDeliveryError("RESEND_API_KEY no está configurada.")

    payload = {
        "from": current_app.config["RESEND_FROM_EMAIL"],
        "to": [recipient],
        "subject": "Código de acceso a TutorIA",
        "html": (
            "<p>Tu código de verificación es:</p>"
            f"<p style='font-size:24px;font-weight:bold'>{code}</p>"
            "<p>Expira en 10 minutos y permite un máximo de 3 intentos.</p>"
        ),
    }
    request = Request(
        "https://api.resend.com/emails",
        data=json.dumps(payload).encode("utf-8"),
        headers={
            "Authorization": f"Bearer {api_key}",
            "Content-Type": "application/json",
            # Resend bloquea las solicitudes directas que no identifican al
            # cliente mediante User-Agent y responde con el error 1010.
            "User-Agent": "TutorIA-MVP/1.0",
        },
        method="POST",
    )
    try:
        with urlopen(request, timeout=10) as response:
            if response.status >= 300:
                raise EmailDeliveryError(f"Resend respondió HTTP {response.status}.")
    except HTTPError as error:
        # Resend devuelve en el cuerpo la causa concreta del rechazo (por
        # ejemplo, dominio no verificado o destinatario no permitido).
        # Se incluye únicamente ese mensaje, nunca la API key.
        try:
            detail = error.read().decode("utf-8", errors="replace")
        except (OSError, UnicodeError):
            detail = "sin detalle adicional"
        raise EmailDeliveryError(
            f"Resend rechazó el envío (HTTP {error.code}): {detail}"
        ) from error
    except (URLError, TimeoutError) as error:
        raise EmailDeliveryError(f"No se pudo enviar el código 2FA: {error}") from error
