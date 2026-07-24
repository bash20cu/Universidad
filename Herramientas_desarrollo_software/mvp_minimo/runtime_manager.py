"""Administración local de los procesos que componen TutorIA.

Este módulo no conoce widgets ni detalles de PySide6. Su responsabilidad es
iniciar, observar y detener Foundation Models y Flask de forma comprobable.
Separar esta lógica permite probarla y evita mezclar control de procesos con
la interfaz académica del sistema web.
"""

from __future__ import annotations

import os
import signal
import subprocess
import threading
import time
from collections.abc import Callable
from dataclasses import dataclass
from pathlib import Path
from urllib.error import URLError
from urllib.request import urlopen


LogCallback = Callable[[str], None]


@dataclass(frozen=True)
class ServiceStatus:
    """Representa el estado visible de un servicio local."""

    name: str
    running: bool
    detail: str
    pid: int | None = None
    owned: bool = False


class RuntimeManager:
    """Coordina Foundation Models y Flask durante una sesión de TutorIA."""

    def __init__(
        self,
        project_dir: Path,
        python_bin: str | None = None,
        fm_command: str = "/usr/bin/fm",
        fm_host: str = "127.0.0.1",
        fm_port: int = 1976,
        app_host: str = "127.0.0.1",
        app_port: int = 5050,
        on_log: LogCallback | None = None,
    ) -> None:
        self.project_dir = project_dir
        self.python_bin = python_bin or str(project_dir / ".venv" / "bin" / "python")
        self.fm_command = fm_command
        self.fm_host = fm_host
        self.fm_port = fm_port
        self.app_host = app_host
        self.app_port = app_port
        self.on_log = on_log or (lambda _message: None)
        self.fm_process: subprocess.Popen[str] | None = None
        self.fm_external_pid: int | None = None
        self.app_process: subprocess.Popen[str] | None = None
        self._reader_threads: list[threading.Thread] = []

    @property
    def fm_url(self) -> str:
        """Devuelve la URL local de salud de Foundation Models."""

        return f"http://{self.fm_host}:{self.fm_port}/health"

    @property
    def app_url(self) -> str:
        """Devuelve la URL principal de TutorIA."""

        return f"http://{self.app_host}:{self.app_port}"

    def _log(self, message: str) -> None:
        """Envía un mensaje a la interfaz sin imponer una implementación gráfica."""

        self.on_log(message)

    def _health(self, url: str) -> dict | None:
        """Consulta un endpoint JSON sin bloquear indefinidamente la interfaz."""

        try:
            with urlopen(url, timeout=1.5) as response:
                import json

                return json.load(response)
        except (OSError, URLError, TimeoutError, ValueError):
            return None

    def fm_health(self) -> dict | None:
        """Consulta el estado actual de `fm serve`."""

        return self._health(self.fm_url)

    def app_health(self) -> dict | None:
        """Consulta el endpoint público de estado del proveedor de TutorIA."""

        return self._health(f"{self.app_url}/chat/api/status")

    def _read_output(self, process: subprocess.Popen[str], service: str) -> None:
        """Reenvía stdout/stderr de un proceso a la consola de la aplicación."""

        if process.stdout is None:
            return
        for line in process.stdout:
            line = line.rstrip()
            if line:
                self._log(f"[{service}] {line}")

    def _watch_output(self, process: subprocess.Popen[str], service: str) -> None:
        thread = threading.Thread(target=self._read_output, args=(process, service), daemon=True)
        thread.start()
        self._reader_threads.append(thread)

    def start_fm(self) -> None:
        """Inicia Foundation Models o adopta un servidor compatible existente."""

        if self.fm_health() is not None:
            self.fm_external_pid = self._find_fm_pid()
            if self.fm_external_pid:
                self._log(f"Foundation Models ya estaba activo; se adoptó el PID {self.fm_external_pid}.")
            else:
                self._log("Foundation Models ya estaba activo, pero no se pudo identificar su PID.")
            return

        self.fm_process = subprocess.Popen(
            [self.fm_command, "serve", "--host", self.fm_host, "--port", str(self.fm_port)],
            cwd=self.project_dir,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            bufsize=1,
            start_new_session=True,
        )
        self._watch_output(self.fm_process, "FM")
        self._wait_for(self.fm_health, "Foundation Models")

    def _find_fm_pid(self) -> int | None:
        """Identifica solo un proceso `fm serve` que escuche el puerto configurado."""

        try:
            result = subprocess.run(
                ["/usr/sbin/lsof", "-tiTCP:" + str(self.fm_port), "-sTCP:LISTEN"],
                check=False,
                capture_output=True,
                text=True,
            )
            for raw_pid in result.stdout.splitlines():
                if not raw_pid.strip().isdigit():
                    continue
                pid = int(raw_pid.strip())
                command = subprocess.run(["/bin/ps", "-p", str(pid), "-o", "command="], capture_output=True, text=True, check=False).stdout
                if "fm serve" in command:
                    return pid
        except OSError:
            return None
        return None

    def start_app(self) -> None:
        """Inicia Flask usando el entorno virtual del MVP."""

        if self.app_health() is not None:
            self._log("TutorIA ya estaba activo en el puerto configurado.")
            return

        if not Path(self.python_bin).exists():
            raise FileNotFoundError(f"No existe el Python del entorno virtual: {self.python_bin}")

        environment = os.environ.copy()
        environment.update({
            "APP_HOST": self.app_host,
            "APP_PORT": str(self.app_port),
            "FM_HOST": self.fm_host,
            "FM_PORT": str(self.fm_port),
            "AUTO_OPEN_BROWSER": "0",
        })
        self.app_process = subprocess.Popen(
            [self.python_bin, "run.py"],
            cwd=self.project_dir,
            env=environment,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            text=True,
            bufsize=1,
            start_new_session=True,
        )
        self._watch_output(self.app_process, "APP")

    def start_all(self) -> None:
        """Inicia FM y Flask en orden, validando primero la dependencia IA."""

        self.start_fm()
        self.start_app()

    def _wait_for(self, health_check: Callable[[], dict | None], name: str, timeout: float = 20) -> None:
        """Espera hasta que un servicio responda o informa el bloqueo claramente."""

        deadline = time.monotonic() + timeout
        while time.monotonic() < deadline:
            if health_check() is not None:
                self._log(f"{name} está disponible.")
                return
            time.sleep(0.25)
        raise TimeoutError(f"{name} no respondió dentro de {timeout:g} segundos.")

    def stop_process(self, process: subprocess.Popen[str] | None, name: str) -> None:
        """Detiene un proceso propio junto con su grupo para evitar huérfanos."""

        if process is None or process.poll() is not None:
            return
        try:
            os.killpg(process.pid, signal.SIGTERM)
            process.wait(timeout=5)
        except (ProcessLookupError, subprocess.TimeoutExpired):
            if process.poll() is None:
                os.killpg(process.pid, signal.SIGKILL)
                process.wait(timeout=2)
        finally:
            self._log(f"{name} detenido.")

    def stop_external_fm(self) -> None:
        """Detiene únicamente el `fm serve` externo que el panel identificó."""

        if self.fm_external_pid is None:
            return
        try:
            os.kill(self.fm_external_pid, signal.SIGTERM)
            self._log(f"Foundation Models externo detenido (PID {self.fm_external_pid}).")
        except ProcessLookupError:
            self._log("El proceso externo de Foundation Models ya no estaba activo.")
        finally:
            self.fm_external_pid = None

    def stop_all(self) -> None:
        """Detiene Flask y Foundation Models si fueron controlados por esta sesión."""

        self.stop_process(self.app_process, "TutorIA")
        self.stop_process(self.fm_process, "Foundation Models")
        self.stop_external_fm()
        self.app_process = None
        self.fm_process = None

    def statuses(self) -> tuple[ServiceStatus, ServiceStatus]:
        """Devuelve los estados listos para pintar en la interfaz."""

        fm = self.fm_health()
        app = self.app_health()
        return (
            ServiceStatus("Foundation Models", fm is not None, "Disponible" if fm else "Apagado", self.fm_process.pid if self.fm_process else self.fm_external_pid, self.fm_process is not None or self.fm_external_pid is not None),
            ServiceStatus("TutorIA Flask", app is not None, "Disponible" if app else "Apagado", self.app_process.pid if self.app_process else None, self.app_process is not None),
        )
