"""Cobertura automatizada de los 15 casos de prueba del manual."""

import builtins
import logging
import subprocess
import sys
import unittest
from pathlib import Path
from unittest.mock import Mock, patch

sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

import requests  # noqa: E402

from agent import Agent  # noqa: E402
from config import settings  # noqa: E402
from security import SecurityError, validate_command  # noqa: E402
from tools import run_command  # noqa: E402

# app.py configura un archivo de auditoria al importarse. En estas pruebas
# aislamos el logger para no depender de permisos del filesystem externo.
with patch.object(logging, "FileHandler", return_value=Mock()):
    from app import _confirmar, _extraer_comando  # noqa: E402


class TestCasosFuncionales(unittest.TestCase):
    """Casos 1 a 4: comandos informativos permitidos."""

    def test_caso_01_kernel(self):
        self.assertEqual(validate_command("uname -r"), ("uname", ["-r"]))
        self.assertNotIn("[ERROR]", run_command("uname -r"))

    def test_caso_02_memoria(self):
        self.assertEqual(validate_command("free -h"), ("free", ["-h"]))
        proceso = Mock(stdout="Mem: 1Gi 512Mi 512Mi", stderr="", returncode=0)
        with patch("tools.subprocess.run", return_value=proceso):
            self.assertNotIn("[ERROR]", run_command("free -h"))

    def test_caso_03_workspace(self):
        self.assertEqual(validate_command("ls"), ("ls", []))
        self.assertNotIn("[ERROR]", run_command("ls"))

    def test_caso_04_hash(self):
        program, args = validate_command("sha256sum .gitkeep")
        self.assertEqual((program, args), ("sha256sum", [".gitkeep"]))
        salida = run_command("sha256sum .gitkeep")
        self.assertRegex(salida, r"[0-9a-f]{64}  \.gitkeep")


class TestCasosSeguridad(unittest.TestCase):
    """Casos 5 a 11: rechazos obligatorios."""

    def test_caso_05_nmap(self):
        with self.assertRaises(SecurityError):
            validate_command("nmap 127.0.0.1")

    def test_caso_06_sudo(self):
        with self.assertRaises(SecurityError):
            validate_command("sudo whoami")

    def test_caso_07_rm(self):
        with self.assertRaises(SecurityError):
            validate_command("rm archivo.txt")

    def test_caso_08_shadow(self):
        with self.assertRaises(SecurityError):
            validate_command("cat /etc/shadow")

    def test_caso_09_and(self):
        with self.assertRaises(SecurityError):
            validate_command("ls && whoami")

    def test_caso_10_pipe(self):
        with self.assertRaises(SecurityError):
            validate_command("ps | grep root")

    def test_caso_11_escritura_externa(self):
        with self.assertRaises(SecurityError):
            validate_command("echo prueba > /tmp/salida.txt")


class TestCasosFlujoYErrores(unittest.TestCase):
    """Casos 12 a 15: confirmacion, timeout y errores de API."""

    def test_caso_12_cancelacion(self):
        with patch.object(builtins, "input", return_value="n"):
            self.assertFalse(_confirmar("uname -r"))

    def test_extrae_comando_formateado(self):
        self.assertEqual(_extraer_comando("COMANDO: `whoami`"), "whoami")

    def test_caso_13_timeout(self):
        with patch("tools.subprocess.run", side_effect=subprocess.TimeoutExpired("free", 1)):
            salida = run_command("free -h")
        self.assertIn("supero el tiempo maximo", salida)

    def test_caso_14_modelo_desconectado(self):
        agente = Agent()
        with patch("agent.requests.post", side_effect=requests.RequestException("sin red")):
            respuesta = agente._ask_api()
        self.assertIn("No se pudo contactar la API", respuesta)

    def test_caso_15_clave_incorrecta(self):
        agente = Agent()
        respuesta_api = Mock(status_code=401, text="Unauthorized")
        with patch("agent.requests.post", return_value=respuesta_api):
            respuesta = agente._ask_api()
        self.assertIn("Autenticacion fallida (401)", respuesta)


if __name__ == "__main__":
    unittest.main(verbosity=2)
