"""Abstracción de proveedores IA para chat y clasificación académica."""

from __future__ import annotations

import json
from dataclasses import asdict, dataclass
from typing import Iterable, Protocol
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

from fm_server import FMServerError, FMServerManager


class AIProviderError(RuntimeError):
    """Error común para que las rutas no dependan de un proveedor concreto."""

    pass


@dataclass(frozen=True)
class ProviderStatus:
    """Estado técnico seguro para mostrar disponibilidad sin credenciales."""

    provider: str
    available: bool
    model: str
    processing_location: str
    access_mode: str
    managed_by_app: bool
    detail: str
    error: str | None = None

    def as_dict(self) -> dict:
        """Serializa el estado para las respuestas JSON del panel web."""

        return asdict(self)


class ChatProvider(Protocol):
    """Contrato mínimo requerido por cualquier proveedor de conversación."""

    name: str

    def ensure_ready(self) -> ProviderStatus:
        """Prepara el proveedor y devuelve su estado de disponibilidad."""

        ...

    def status(self) -> ProviderStatus:
        """Devuelve información técnica segura para la interfaz."""

        ...

    def stream_chat(self, messages: list[dict[str, str]]) -> Iterable[bytes]:
        """Produce fragmentos SSE de una respuesta conversacional."""

        ...

    def complete_chat(self, messages: list[dict[str, str]]) -> str:
        """Obtiene una respuesta completa para procesos no interactivos."""

        ...

    def shutdown(self) -> None:
        """Libera recursos locales o finaliza conexiones mantenidas."""

        ...


class FoundationModelsProvider:
    """Implementa chat local mediante el endpoint compatible de ``fm serve``."""

    name = "foundation_models"

    def __init__(
        self,
        manager: FMServerManager,
        model: str = "system",
        processing_location: str = "device",
        access_mode: str = "local",
        request_timeout: float = 180,
    ) -> None:
        """Configura el administrador local y los parámetros del modelo."""

        self.manager = manager
        self.model = model
        self.processing_location = processing_location
        self.access_mode = access_mode
        self.request_timeout = request_timeout

    def ensure_ready(self) -> ProviderStatus:
        """Inicia FM si hace falta y devuelve su estado listo para usar."""

        try:
            self.manager.ensure_started()
        except FMServerError as error:
            raise AIProviderError(str(error)) from error
        return self.status()

    def status(self) -> ProviderStatus:
        """Consulta la salud local y la transforma al contrato común."""

        health = self.manager.health()
        return ProviderStatus(
            provider=self.name,
            available=health["available"],
            model=self.model,
            processing_location=self.processing_location,
            access_mode=self.access_mode,
            managed_by_app=self.manager.owns_process,
            detail=health["status"],
            error=health["error"],
        )

    def stream_chat(self, messages: list[dict[str, str]]) -> Iterable[bytes]:
        """Envía mensajes y retransmite fragmentos SSE de Foundation Models."""

        self.ensure_ready()
        payload = {
            "model": self.model,
            "stream": True,
            "messages": messages,
        }
        upstream_request = Request(
            f"{self.manager.base_url}/v1/chat/completions",
            data=json.dumps(payload).encode("utf-8"),
            headers={
                "Content-Type": "application/json",
                "Accept": "text/event-stream",
            },
            method="POST",
        )

        try:
            with urlopen(upstream_request, timeout=self.request_timeout) as upstream:
                while line := upstream.readline():
                    yield line
        except HTTPError as error:
            raise AIProviderError(
                f"Foundation Models respondió HTTP {error.code}."
            ) from error
        except (URLError, TimeoutError) as error:
            raise AIProviderError(
                f"Se perdió la conexión con Foundation Models: {error}."
            ) from error

    def complete_chat(self, messages: list[dict[str, str]]) -> str:
        """Solicita una respuesta completa para clasificación diagnóstica."""

        self.ensure_ready()
        payload = {
            "model": self.model,
            "stream": False,
            "messages": messages,
        }
        upstream_request = Request(
            f"{self.manager.base_url}/v1/chat/completions",
            data=json.dumps(payload).encode("utf-8"),
            headers={"Content-Type": "application/json"},
            method="POST",
        )

        try:
            with urlopen(upstream_request, timeout=self.request_timeout) as upstream:
                body = json.load(upstream)
        except HTTPError as error:
            raise AIProviderError(
                f"Foundation Models respondió HTTP {error.code}."
            ) from error
        except (URLError, TimeoutError, json.JSONDecodeError) as error:
            raise AIProviderError(
                f"No se pudo obtener una respuesta completa de Foundation Models: {error}."
            ) from error

        try:
            return body["choices"][0]["message"]["content"]
        except (KeyError, IndexError, TypeError) as error:
            raise AIProviderError("Foundation Models devolvió una respuesta sin contenido utilizable.") from error

    def shutdown(self) -> None:
        """Detiene el servidor local si fue iniciado por el proveedor."""

        self.manager.stop()


class NVIDIAProvider:
    """Cliente OpenAI-compatible para NVIDIA NIM con credencial por entorno."""

    name = "nvidia"

    def __init__(
        self,
        api_key: str | None,
        model: str = "meta/llama-3.1-8b-instruct",
        base_url: str = "https://integrate.api.nvidia.com/v1",
        request_timeout: float = 180,
    ) -> None:
        """Configura credencial, modelo y endpoint remoto de NVIDIA."""

        self.api_key = api_key
        self.model = model
        self.base_url = base_url.rstrip("/")
        self.request_timeout = request_timeout

    def status(self) -> ProviderStatus:
        """Expone configuración sin hacer una llamada remota en cada consulta de UI."""

        configured = bool(self.api_key)
        return ProviderStatus(
            provider=self.name,
            available=configured,
            model=self.model,
            processing_location="remote",
            access_mode="remote",
            managed_by_app=False,
            detail="API key configurada" if configured else "API key no configurada",
        )

    def ensure_ready(self) -> ProviderStatus:
        """Valida que exista una credencial antes de consumir el servicio."""

        if not self.api_key:
            raise AIProviderError("NVIDIA_API_KEY no está configurada.")
        return self.status()

    def _request(self, messages: list[dict[str, str]], stream: bool) -> Request:
        """Construye la solicitud compatible con el endpoint de chat de NVIDIA."""

        payload = {"model": self.model, "stream": stream, "messages": messages}
        if stream:
            # NVIDIA puede devolver el uso real de tokens en el último evento
            # SSE cuando esta opción está activa.
            payload["stream_options"] = {"include_usage": True}
        return Request(
            f"{self.base_url}/chat/completions",
            data=json.dumps(payload).encode("utf-8"),
            headers={
                "Authorization": f"Bearer {self.api_key}",
                "Content-Type": "application/json",
                "Accept": "text/event-stream" if stream else "application/json",
            },
            method="POST",
        )

    def stream_chat(self, messages: list[dict[str, str]]) -> Iterable[bytes]:
        """Entrega los fragmentos SSE del modelo NVIDIA al navegador."""

        self.ensure_ready()
        try:
            with urlopen(self._request(messages, stream=True), timeout=self.request_timeout) as upstream:
                while line := upstream.readline():
                    yield line
        except HTTPError as error:
            raise AIProviderError(f"NVIDIA respondió HTTP {error.code}.") from error
        except (URLError, TimeoutError) as error:
            raise AIProviderError(f"No se pudo conectar con NVIDIA: {error}.") from error

    def complete_chat(self, messages: list[dict[str, str]]) -> str:
        """Obtiene una respuesta completa para clasificación diagnóstica."""

        self.ensure_ready()
        try:
            with urlopen(self._request(messages, stream=False), timeout=self.request_timeout) as upstream:
                body = json.load(upstream)
        except HTTPError as error:
            raise AIProviderError(f"NVIDIA respondió HTTP {error.code}.") from error
        except (URLError, TimeoutError, json.JSONDecodeError) as error:
            raise AIProviderError(f"No se pudo obtener respuesta completa de NVIDIA: {error}.") from error

        try:
            return body["choices"][0]["message"]["content"]
        except (KeyError, IndexError, TypeError) as error:
            raise AIProviderError("NVIDIA devolvió una respuesta sin contenido utilizable.") from error

    def shutdown(self) -> None:
        """No hay proceso local que detener para NVIDIA remoto."""


class FallbackChatProvider:
    """Usa NVIDIA como principal y Foundation Models como respaldo automático."""

    name = "nvidia_with_foundation_fallback"

    def __init__(self, primary: ChatProvider, fallback: ChatProvider) -> None:
        """Recibe proveedor principal remoto y proveedor local de respaldo."""

        self.primary = primary
        self.fallback = fallback
        self.active_provider = primary

    def status(self) -> ProviderStatus:
        """Muestra NVIDIA si está configurado; si no, informa el fallback local."""

        primary_status = self.primary.status()
        if primary_status.available:
            return ProviderStatus(
                provider=self.name,
                available=True,
                model=primary_status.model,
                processing_location="remote",
                access_mode="remote",
                managed_by_app=False,
                detail="NVIDIA principal; Foundation Models disponible como fallback",
            )
        fallback_status = self.fallback.status()
        return ProviderStatus(
            provider=self.name,
            available=fallback_status.available,
            model=fallback_status.model,
            processing_location=fallback_status.processing_location,
            access_mode=fallback_status.access_mode,
            managed_by_app=fallback_status.managed_by_app,
            detail="NVIDIA no configurado; usando Foundation Models como fallback",
            error=fallback_status.error,
        )

    def ensure_ready(self) -> ProviderStatus:
        """Prueba primero el proveedor principal y activa el local si falla."""

        try:
            self.active_provider = self.primary
            return self.primary.ensure_ready()
        except AIProviderError:
            self.active_provider = self.fallback
            return self.fallback.ensure_ready()

    def stream_chat(self, messages: list[dict[str, str]]) -> Iterable[bytes]:
        """Hace fallback solo si NVIDIA falla antes de entregar contenido."""

        emitted = False
        try:
            self.active_provider = self.primary
            for chunk in self.primary.stream_chat(messages):
                emitted = True
                yield chunk
        except AIProviderError:
            if emitted:
                raise
            self.active_provider = self.fallback
            yield from self.fallback.stream_chat(messages)

    def complete_chat(self, messages: list[dict[str, str]]) -> str:
        """Usa NVIDIA para diagnóstico y cambia a FM si la llamada falla."""

        try:
            self.active_provider = self.primary
            return self.primary.complete_chat(messages)
        except AIProviderError:
            self.active_provider = self.fallback
            return self.fallback.complete_chat(messages)

    def shutdown(self) -> None:
        """Libera el fallback local; el proveedor remoto no mantiene procesos."""

        self.primary.shutdown()
        self.fallback.shutdown()
