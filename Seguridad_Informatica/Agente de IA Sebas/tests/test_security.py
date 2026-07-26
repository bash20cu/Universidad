"""
tests/test_security.py
=======================
Pruebas unitarias del núcleo de seguridad. Verifican que:
  * Los comandos de la lista blanca se aceptan.
  * Los comandos no autorizados se rechazan.
  * Los operadores de shell prohibidos se detectan.
  * Las rutas fuera de workspace se rechazan.

Ejecutar con:  python -m unittest discover -s tests -v
(o:           python -m unittest discover -s tests)
"""

import sys
import unittest
from pathlib import Path

# Permite importar los módulos del proyecto (carpeta padre).
sys.path.insert(0, str(Path(__file__).resolve().parent.parent))

from security import SecurityError, validate_command  # noqa: E402


class TestListaBlanca(unittest.TestCase):
    def test_comando_permitido(self):
        prog, args = validate_command("uname -a")
        self.assertEqual(prog, "uname")
        self.assertEqual(args, ["-a"])

    def test_archivo_del_workspace_permitido(self):
        prog, args = validate_command("sha256sum evidencia.txt")
        self.assertEqual(prog, "sha256sum")
        self.assertEqual(args, ["evidencia.txt"])

    def test_comando_no_autorizado(self):
        with self.assertRaises(SecurityError):
            validate_command("nmap 127.0.0.1")

    def test_sudo_rechazado(self):
        with self.assertRaises(SecurityError):
            validate_command("sudo cat archivo")

    def test_rm_rechazado(self):
        with self.assertRaises(SecurityError):
            validate_command("rm archivo.txt")


class TestOperadores(unittest.TestCase):
    def test_punto_y_coma(self):
        with self.assertRaises(SecurityError):
            validate_command("pwd; whoami")

    def test_and_logico(self):
        with self.assertRaises(SecurityError):
            validate_command("ls && whoami")

    def test_tuberia(self):
        with self.assertRaises(SecurityError):
            validate_command("ps | grep root")

    def test_redireccion(self):
        with self.assertRaises(SecurityError):
            validate_command("date > salida.txt")

    def test_escritura_fuera_de_workspace_rechazada(self):
        with self.assertRaises(SecurityError):
            validate_command("echo prueba > /tmp/salida.txt")

    def test_sustitucion(self):
        with self.assertRaises(SecurityError):
            validate_command("echo $(whoami)")


class TestRutas(unittest.TestCase):
    def test_acceso_fuera_de_workspace(self):
        with self.assertRaises(SecurityError):
            validate_command("cat /etc/shadow")

    def test_escape_con_dotdot(self):
        with self.assertRaises(SecurityError):
            validate_command("cat ../config.py")


if __name__ == "__main__":
    unittest.main(verbosity=2)
