from __future__ import annotations

import json
import subprocess
import time
from dataclasses import dataclass
from urllib.error import HTTPError, URLError
from urllib.request import urlopen


class FMServerError(RuntimeError):
    pass


@dataclass(frozen=True)
class FMHealth:
    available: bool
    status: str
    models: list[dict]
    error: str | None = None

    def as_dict(self) -> dict:
        return {
            "available": self.available,
            "status": self.status,
            "models": self.models,
            "error": self.error,
        }


class FMServerManager:
    def __init__(
        self,
        command: str = "/usr/bin/fm",
        host: str = "127.0.0.1",
        port: int = 1976,
        start_timeout: float = 20,
    ) -> None:
        self.command = command
        self.host = host
        self.port = port
        self.start_timeout = start_timeout
        self.process: subprocess.Popen | None = None

    @property
    def base_url(self) -> str:
        return f"http://{self.host}:{self.port}"

    @property
    def owns_process(self) -> bool:
        return self.process is not None and self.process.poll() is None

    def health(self) -> dict:
        try:
            with urlopen(f"{self.base_url}/health", timeout=1) as response:
                payload = json.load(response)
            return FMHealth(
                available=True,
                status=payload.get("status", "running"),
                models=payload.get("models", []),
            ).as_dict()
        except (HTTPError, URLError, TimeoutError, json.JSONDecodeError) as error:
            return FMHealth(
                available=False,
                status="offline",
                models=[],
                error=str(error),
            ).as_dict()

    def ensure_started(self) -> dict:
        current_health = self.health()
        if current_health["available"]:
            return current_health

        if self.process is not None and self.process.poll() is None:
            return self._wait_until_healthy()

        try:
            self.process = subprocess.Popen(
                [
                    self.command,
                    "serve",
                    "--host",
                    self.host,
                    "--port",
                    str(self.port),
                ],
                stdout=subprocess.DEVNULL,
                stderr=subprocess.PIPE,
                text=True,
            )
        except OSError as error:
            raise FMServerError(
                f"No se pudo ejecutar {self.command}: {error}"
            ) from error

        return self._wait_until_healthy()

    def _wait_until_healthy(self) -> dict:
        deadline = time.monotonic() + self.start_timeout

        while time.monotonic() < deadline:
            health = self.health()
            if health["available"]:
                return health

            if self.process is not None and self.process.poll() is not None:
                stderr = self.process.stderr.read().strip() if self.process.stderr else ""
                raise FMServerError(
                    "fm serve terminó antes de estar disponible."
                    + (f" Detalle: {stderr}" if stderr else "")
                )
            time.sleep(0.25)

        self.stop()
        raise FMServerError(
            f"fm serve no respondió en {self.base_url} "
            f"después de {self.start_timeout:g} segundos."
        )

    def stop(self) -> None:
        if not self.owns_process:
            return

        self.process.terminate()
        try:
            self.process.wait(timeout=4)
        except subprocess.TimeoutExpired:
            self.process.kill()
            self.process.wait(timeout=2)
        finally:
            self.process = None
