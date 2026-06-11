from __future__ import annotations

import json
from dataclasses import asdict, dataclass
from typing import Iterable, Protocol
from urllib.error import HTTPError, URLError
from urllib.request import Request, urlopen

from fm_server import FMServerError, FMServerManager


class AIProviderError(RuntimeError):
    pass


@dataclass(frozen=True)
class ProviderStatus:
    provider: str
    available: bool
    model: str
    processing_location: str
    access_mode: str
    managed_by_app: bool
    detail: str
    error: str | None = None

    def as_dict(self) -> dict:
        return asdict(self)


class ChatProvider(Protocol):
    name: str

    def ensure_ready(self) -> ProviderStatus:
        ...

    def status(self) -> ProviderStatus:
        ...

    def stream_chat(self, messages: list[dict[str, str]]) -> Iterable[bytes]:
        ...

    def shutdown(self) -> None:
        ...


class FoundationModelsProvider:
    name = "foundation_models"

    def __init__(
        self,
        manager: FMServerManager,
        model: str = "system",
        processing_location: str = "device",
        access_mode: str = "local",
        request_timeout: float = 180,
    ) -> None:
        self.manager = manager
        self.model = model
        self.processing_location = processing_location
        self.access_mode = access_mode
        self.request_timeout = request_timeout

    def ensure_ready(self) -> ProviderStatus:
        try:
            self.manager.ensure_started()
        except FMServerError as error:
            raise AIProviderError(str(error)) from error
        return self.status()

    def status(self) -> ProviderStatus:
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

    def shutdown(self) -> None:
        self.manager.stop()
