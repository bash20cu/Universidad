"""
agent.py
========
Lógica del agente: construye el prompt del sistema, consulta al modelo de
lenguaje (API remota u Ollama local) y mantiene un historial básico de la
conversación.

El agente NO ejecuta comandos por su cuenta. Cuando el modelo propone un
comando, app.py es quien solicita confirmación humana y luego invoca las
herramientas de tools.py. Esta separación es deliberada: el modelo
*sugiere*, la persona *autoriza*, y el código *valida y ejecuta*.
"""

from __future__ import annotations

from typing import Dict, List

import requests

from config import settings
from security import describe_whitelist


# ---------------------------------------------------------------------------
# Prompt del sistema (ver Seccion 10 del manual)
# ---------------------------------------------------------------------------
SYSTEM_PROMPT = f"""\
Eres un agente de inteligencia artificial de asistencia que opera EXCLUSIVAMENTE
dentro de un laboratorio universitario de Seguridad Informatica, en una maquina
virtual aislada y controlada. Tu proposito es educativo y defensivo.

REGLAS OBLIGATORIAS:
1. Solo realizas tareas defensivas, informativas y educativas.
2. Explica SIEMPRE, antes de proponer un comando, que hace y por que es seguro.
3. No puedes modificar el sistema sin autorizacion explicita de la persona.
4. Toda ejecucion de comando requiere confirmacion humana; tu solo propones.
   Nunca afirmes que un comando ya fue ejecutado ni que la confirmacion es
   innecesaria: solo app.py puede confirmar y ejecutar la propuesta.
5. No ejecutas acciones destructivas (borrar, sobrescribir, formatear, matar
   procesos del sistema).
6. No atacas, escaneas ni interactuas con sistemas externos.
7. No recopilas, lees ni exfiltras credenciales ni secretos.
8. No desactivas ni evades controles de seguridad.
9. No descargas ni ejecutas codigo desconocido.
10. Respetas estrictamente el alcance definido por el profesor.
11. Registras (a traves del sistema) cada accion realizada.
12. Si hay ambiguedad, riesgo o una peticion fuera de alcance, te DETIENES y
    pides aclaracion en lugar de actuar.

Solo puedes proponer comandos de la siguiente lista blanca. Cualquier otra
peticion debe ser rechazada con una explicacion:

{describe_whitelist()}

Cuando propongas un comando para ejecutar, responde en este formato:
  EXPLICACION: <que hace y por que es seguro>
  COMANDO: <comando exacto de la lista blanca>

Si la peticion no requiere ejecutar nada, responde solo con texto explicativo.
Si la peticion es peligrosa o esta fuera de alcance, responde:
  RECHAZADO: <motivo>
"""


class Agent:
    """Agente conversacional con historial básico."""

    def __init__(self) -> None:
        # Historial de mensajes (rol, contenido). Se reenvía al modelo en
        # cada turno porque los modelos no tienen memoria entre llamadas.
        self.history: List[Dict[str, str]] = []

    def reset(self) -> None:
        """Borra el historial de la conversación."""
        self.history.clear()

    def ask(self, user_message: str) -> str:
        """Envía un mensaje al modelo y devuelve su respuesta de texto."""
        self.history.append({"role": "user", "content": user_message})

        if settings.model_backend == "api":
            answer = self._ask_api()
        else:
            answer = self._ask_ollama()

        self.history.append({"role": "assistant", "content": answer})
        return answer

    # ----------------------- Backend: API remota -------------------------
    def _ask_api(self) -> str:
        """Consulta el proveedor remoto configurado y normaliza su respuesta."""
        if self._uses_openai_compatible_api():
            url, headers, payload = self._build_openai_request()
        else:
            url, headers, payload = self._build_anthropic_request()
        try:
            resp = requests.post(url, headers=headers, json=payload, timeout=60)
        except requests.RequestException as exc:
            return f"[ERROR] No se pudo contactar la API: {exc}"

        if resp.status_code == 401:
            return "[ERROR] Autenticacion fallida (401): revise LLM_API_KEY."
        if resp.status_code == 403:
            return "[ERROR] Acceso prohibido (403): permisos insuficientes."
        if resp.status_code == 429:
            return "[ERROR] Limite de solicitudes alcanzado (429): espere e intente de nuevo."
        if resp.status_code >= 400:
            return f"[ERROR] La API respondio {resp.status_code}: {resp.text[:200]}"

        try:
            data = resp.json()
            return self._extract_api_text(data)
        except (ValueError, KeyError) as exc:
            return f"[ERROR] Respuesta de la API no valida: {exc}"

    @staticmethod
    def _uses_openai_compatible_api() -> bool:
        """Indica si el endpoint usa el formato OpenAI Chat Completions."""
        base_url = settings.api_base_url.lower()
        return "openrouter.ai" in base_url or "nvidia.com" in base_url

    def _build_openai_request(self):
        """Construye una solicitud para NVIDIA u OpenRouter."""
        url = f"{settings.api_base_url.rstrip('/')}/chat/completions"
        headers = {
            "Authorization": f"Bearer {settings.api_key}",
            "Content-Type": "application/json",
        }
        payload = {
            "model": settings.api_model,
            "max_tokens": 1024,
            "messages": [
                {"role": "system", "content": SYSTEM_PROMPT},
                *self.history,
            ],
        }
        return url, headers, payload

    def _build_anthropic_request(self):
        """Construye una solicitud para la API de mensajes de Anthropic."""
        url = f"{settings.api_base_url.rstrip('/')}/v1/messages"
        headers = {
            "x-api-key": settings.api_key,
            "anthropic-version": "2023-06-01",
            "content-type": "application/json",
        }
        payload = {
            "model": settings.api_model,
            "max_tokens": 1024,
            "system": SYSTEM_PROMPT,
            "messages": self.history,
        }
        return url, headers, payload

    def _extract_api_text(self, response_data: dict) -> str:
        """Extrae texto tanto de respuestas OpenAI como de Anthropic."""
        if self._uses_openai_compatible_api():
            content = response_data.get("choices", [{}])[0].get("message", {}).get("content", "")
            return content.strip() or "[sin respuesta del modelo]"

        text_parts = [
            block.get("text", "")
            for block in response_data.get("content", [])
            if block.get("type") == "text"
        ]
        return "\n".join(part for part in text_parts if part).strip() or "[sin respuesta del modelo]"

    # ----------------------- Backend: Ollama local ----------------------
    def _ask_ollama(self) -> str:
        """Consulta un modelo local servido por Ollama."""
        url = f"{settings.ollama_base_url}/api/chat"
        # Ollama acepta un mensaje 'system' como primer elemento.
        messages = [{"role": "system", "content": SYSTEM_PROMPT}, *self.history]
        payload = {
            "model": settings.ollama_model,
            "messages": messages,
            "stream": False,
        }
        try:
            resp = requests.post(url, json=payload, timeout=120)
        except requests.RequestException as exc:
            return (
                f"[ERROR] No se pudo contactar Ollama en {settings.ollama_base_url}: "
                f"{exc}. Verifique que el servicio este activo (ollama serve)."
            )

        if resp.status_code >= 400:
            return f"[ERROR] Ollama respondio {resp.status_code}: {resp.text[:200]}"

        try:
            data = resp.json()
            return data.get("message", {}).get("content", "").strip() or "[sin respuesta]"
        except ValueError as exc:
            return f"[ERROR] Respuesta de Ollama no valida: {exc}"
