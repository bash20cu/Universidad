from __future__ import annotations

import json
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

from flask import current_app


class EmailDeliveryError(RuntimeError):
    pass


def send_two_factor_code(recipient: str, code: str) -> None:
    if current_app.config["TWO_FACTOR_DELIVERY"] == "console":
        current_app.logger.warning("Código 2FA de desarrollo para %s: %s", recipient, code)
        return

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
        headers={"Authorization": f"Bearer {api_key}", "Content-Type": "application/json"},
        method="POST",
    )
    try:
        with urlopen(request, timeout=10) as response:
            if response.status >= 300:
                raise EmailDeliveryError(f"Resend respondió HTTP {response.status}.")
    except (HTTPError, URLError, TimeoutError) as error:
        raise EmailDeliveryError(f"No se pudo enviar el código 2FA: {error}") from error

