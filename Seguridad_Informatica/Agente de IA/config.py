"""
config.py
=========
Carga y centraliza la configuración del agente de IA de laboratorio.

Toda la configuración sensible (claves API) se lee desde variables de
entorno cargadas por python-dotenv a partir de un archivo `.env` que
NUNCA debe subirse a un repositorio (ver .gitignore).

Este módulo no contiene secretos en texto plano. Si una variable
requerida no existe, el agente debe negarse a arrancar con un mensaje
claro en lugar de continuar en un estado inseguro.
"""

from __future__ import annotations

import os
from dataclasses import dataclass, field
from pathlib import Path

from dotenv import load_dotenv

# Carga las variables definidas en .env hacia el entorno del proceso.
# override=False evita pisar variables ya definidas por el sistema.
load_dotenv(override=False)


# Raíz del proyecto (carpeta que contiene este archivo).
BASE_DIR = Path(__file__).resolve().parent

# Directorio de trabajo restringido. El agente SOLO puede leer archivos
# dentro de esta carpeta. Se resuelve de forma absoluta para poder
# compararlo de manera segura más adelante.
WORKSPACE_DIR = (BASE_DIR / "workspace").resolve()

# Directorio de registros de auditoría.
LOGS_DIR = (BASE_DIR / "logs").resolve()


@dataclass(frozen=True)
class Settings:
    """Configuración inmutable del agente."""

    # --- Selección de backend del modelo ---
    # "api"   -> usar un proveedor remoto vía API.
    # "local" -> usar un modelo local servido por Ollama.
    model_backend: str = os.getenv("MODEL_BACKEND", "local").strip().lower()

    # --- Opción A: API remota ---
    api_key: str = os.getenv("LLM_API_KEY", "").strip()
    api_base_url: str = os.getenv(
        "LLM_API_BASE_URL", "https://api.anthropic.com"
    ).strip()
    api_model: str = os.getenv("LLM_API_MODEL", "claude-haiku-4-5").strip()

    # --- Opción B: Ollama local ---
    ollama_base_url: str = os.getenv(
        "OLLAMA_BASE_URL", "http://localhost:11434"
    ).strip()
    ollama_model: str = os.getenv("OLLAMA_MODEL", "llama3.2:3b").strip()

    # --- Límites de seguridad para la ejecución de comandos ---
    # Tiempo máximo (segundos) que puede correr un comando antes de
    # cancelarse por timeout.
    command_timeout: int = int(os.getenv("COMMAND_TIMEOUT", "15"))

    # Máximo de caracteres de salida que se muestran/registran. Evita
    # que un comando inunde la terminal o los logs.
    max_output_chars: int = int(os.getenv("MAX_OUTPUT_CHARS", "4000"))

    # Rutas derivadas (no provienen de variables de entorno).
    workspace_dir: Path = field(default=WORKSPACE_DIR)
    logs_dir: Path = field(default=LOGS_DIR)

    def validate(self) -> None:
        """Valida la configuración y crea los directorios necesarios.

        Lanza RuntimeError si la configuración es incoherente, para que
        el agente se niegue a arrancar en un estado inseguro.
        """
        if self.model_backend not in ("api", "local"):
            raise RuntimeError(
                f"MODEL_BACKEND invalido: '{self.model_backend}'. "
                "Use 'api' o 'local'."
            )

        if self.model_backend == "api" and not self.api_key:
            raise RuntimeError(
                "MODEL_BACKEND=api pero LLM_API_KEY esta vacia. "
                "Defina la clave en el archivo .env."
            )

        if self.command_timeout <= 0:
            raise RuntimeError("COMMAND_TIMEOUT debe ser mayor que cero.")

        if self.max_output_chars <= 0:
            raise RuntimeError("MAX_OUTPUT_CHARS debe ser mayor que cero.")

        # Crea las carpetas de trabajo y de logs si no existen.
        self.workspace_dir.mkdir(parents=True, exist_ok=True)
        self.logs_dir.mkdir(parents=True, exist_ok=True)


# Instancia única utilizada por el resto de la aplicación.
settings = Settings()
