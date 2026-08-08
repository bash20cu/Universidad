# Archivo: fm_server.py
# Propósito: Administra el ciclo de vida de un servidor local fm serve.
# Responsabilidades: Consulta salud, inicia bajo demanda, espera disponibilidad y detiene únicamente procesos propios.
# Dependencias: __future__, json, subprocess, time, dataclasses, urllib
# Entradas y salidas: Recibe comando, host y puerto; devuelve estados o FMServerError y controla un proceso local.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Cliente y ciclo de vida del servidor local ``fm serve``."""

from __future__ import annotations

import json
import subprocess
import time
from dataclasses import dataclass
from urllib.error import HTTPError, URLError
from urllib.request import urlopen


class FMServerError(RuntimeError):
    """Error de arranque, salud o cierre del servidor Foundation Models."""

    pass


@dataclass(frozen=True)
class FMHealth:
    """Respuesta normalizada del endpoint de salud de Foundation Models."""

    available: bool
    status: str
    models: list[dict]
    error: str | None = None

    def as_dict(self) -> dict:
        """Convierte el estado en un diccionario para Flask y la UI."""

        return {
            "available": self.available,
            "status": self.status,
            "models": self.models,
            "error": self.error,
        }


class FMServerManager:
    """Inicia FM bajo demanda y evita cerrar procesos que no administra."""

    def __init__(
        self,
        command: str = "/usr/bin/fm",
        host: str = "127.0.0.1",
        port: int = 1976,
        start_timeout: float = 20,
    ) -> None:
        """Configura comando, dirección y límite de espera para FM."""

        self.command = command
        self.host = host
        self.port = port
        self.start_timeout = start_timeout
        self.process: subprocess.Popen | None = None

    @property
    def base_url(self) -> str:
        """Devuelve la URL base del servicio local."""

        return f"http://{self.host}:{self.port}"

    @property
    def owns_process(self) -> bool:
        """Indica si esta instancia mantiene un proceso FM activo."""

        return self.process is not None and self.process.poll() is None

    def health(self) -> dict:
        """Consulta salud y modelos disponibles sin propagar fallos de red."""

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
        """Reutiliza FM si está activo o lo inicia y espera su disponibilidad."""

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
        """Espera con límite de tiempo hasta que FM responda correctamente."""

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
        """Detiene solo el proceso FM iniciado por este administrador."""

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
