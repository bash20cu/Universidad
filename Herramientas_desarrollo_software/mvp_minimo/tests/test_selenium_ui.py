# Archivo: test_selenium_ui.py
# Propósito: Ejecuta pruebas visuales remotas de la interfaz con Selenium Grid.
# Responsabilidades: Recorre páginas públicas, registro, TOTP, portal estudiantil, paneles por rol y captura evidencias sanitizadas.
# Dependencias: pytest, Selenium, Chrome remoto, PyOTP y proveedor controlado.
# Entradas y salidas: Lee variables de Selenium; navega el sitio y genera PNG/HTML en docs/evidencias.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Pruebas UI remotas de TutorIA usando Selenium Grid y Chrome."""

from __future__ import annotations

import json
import os
from pathlib import Path

import pyotp
import pytest
from selenium import webdriver
from selenium.common.exceptions import WebDriverException
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as condition
from selenium.webdriver.support.ui import WebDriverWait

from ai_provider import ProviderStatus
from app import create_app, seed_database
from app.extensions import db
from app.models import User
from app.services.two_factor import generate_totp_secret


GRID_ENDPOINT = os.getenv("SELENIUM_REMOTE_URL", "http://localhost:4444/wd/hub")
BASE_URL = os.getenv("SELENIUM_BASE_URL", "http://host.docker.internal:5050")
DATABASE_URL = os.getenv("DATABASE_URL", "sqlite:////private/tmp/tutoria_selenium.db")
EVIDENCE_DIR = Path(__file__).resolve().parents[1] / "docs" / "evidencias" / "selenium_2026-08-07"

if os.getenv("RUN_SELENIUM") != "1":
    pytest.skip("Las pruebas UI requieren RUN_SELENIUM=1 y un Selenium Grid activo.", allow_module_level=True)


class UITestProvider:
    """Proveedor estable para validar la interfaz sin llamar a un servicio IA."""

    name = "ui_test_provider"

    def status(self):
        """Devuelve un estado disponible para las pantallas de la aplicación."""

        return ProviderStatus(
            provider=self.name,
            available=True,
            model="ui-test",
            processing_location="local",
            access_mode="local",
            managed_by_app=False,
            detail="Proveedor controlado para pruebas de interfaz.",
        )

    def ensure_ready(self):
        """Confirma disponibilidad sin iniciar procesos externos."""

        return self.status()

    def stream_chat(self, _messages):
        """Entrega una respuesta mínima para el flujo de chat si se prueba."""

        yield b'data: {"choices":[{"delta":{"content":"Respuesta de prueba"}}]}\n\n'
        yield b"data: [DONE]\n\n"

    def complete_chat(self, _messages):
        """Entrega una clasificación válida para pruebas futuras de UI."""

        return json.dumps(
            {
                "level": "basico",
                "explanation": "Resultado generado por el proveedor de prueba.",
                "strengths": ["Participa en la evaluación"],
                "improvement_areas": ["Continuar practicando"],
            }
        )

    def shutdown(self):
        """No tiene procesos externos que cerrar."""


@pytest.fixture(scope="session", autouse=True)
def prepare_ui_database():
    """Prepara la base aislada y cuentas TOTP conocidas para las pruebas."""

    app = create_app(
        {"TESTING": True, "DATABASE_URL": DATABASE_URL, "WTF_CSRF_ENABLED": True},
        provider=UITestProvider(),
    )
    with app.app_context():
        db.drop_all()
        db.create_all()
        seed_database()
        secrets = {}
        for username in ("admin", "docente", "estudiante"):
            user = User.query.filter_by(username=username).one()
            user.totp_secret = generate_totp_secret()
            user.totp_enabled = True
            secrets[username] = user.totp_secret
        db.session.commit()
    EVIDENCE_DIR.mkdir(parents=True, exist_ok=True)
    (EVIDENCE_DIR / "cuentas_prueba.json").write_text(
        json.dumps({"usuarios": list(secrets), "base_url": BASE_URL}, indent=2),
        encoding="utf-8",
    )
    return secrets


@pytest.fixture()
def browser():
    """Abre Chrome remoto con una resolución reproducible."""

    options = Options()
    options.add_argument("--window-size=1440,1000")
    try:
        session = webdriver.Remote(command_executor=GRID_ENDPOINT, options=options)
    except WebDriverException as error:
        pytest.skip(f"Selenium Grid no disponible en {GRID_ENDPOINT}: {error}")
    yield session
    session.quit()


def wait_for(browser, locator):
    """Espera un elemento visible para evitar dependencias en tiempos fijos."""

    return WebDriverWait(browser, 15).until(condition.visibility_of_element_located(locator))


def capture(browser, name: str, redact_selectors=()):
    """Guarda captura y HTML, ocultando secretos antes de persistir evidencia."""

    redactions = []
    if redact_selectors:
        redactions = browser.execute_script(
            """
            const redactions = [];
            for (const selector of arguments[0]) {
                document.querySelectorAll(selector).forEach((element, index) => {
                    redactions.push({
                        selector,
                        index,
                        visibility: element.style.visibility,
                        src: element.getAttribute('src'),
                        text: element.textContent,
                    });
                    element.style.visibility = 'hidden';
                    if (element.tagName === 'IMG') element.removeAttribute('src');
                    if (element.tagName === 'CODE') element.textContent = '[REDACTADO]';
                });
            }
            return redactions;
            """,
            list(redact_selectors),
        )

    browser.save_screenshot(str(EVIDENCE_DIR / f"{name}.png"))
    (EVIDENCE_DIR / f"{name}.html").write_text(browser.page_source, encoding="utf-8")

    if redactions:
        browser.execute_script(
            """
            for (const redaction of arguments[0]) {
                const element = document.querySelectorAll(redaction.selector)[redaction.index];
                if (!element) continue;
                element.style.visibility = redaction.visibility || '';
                if (redaction.src !== null) element.setAttribute('src', redaction.src);
                if (redaction.text !== null) element.textContent = redaction.text;
            }
            """,
            redactions,
        )


def login_with_totp(browser, username: str, password: str, secret: str, prefix: str):
    """Completa primer y segundo factor usando el secreto de la cuenta de prueba."""

    browser.get(f"{BASE_URL}/auth/login")
    wait_for(browser, (By.NAME, "username")).send_keys(username)
    browser.find_element(By.NAME, "password").send_keys(password)
    capture(browser, f"{prefix}_01_login_form")
    submit = browser.find_element(By.NAME, "submit")
    browser.execute_script("arguments[0].scrollIntoView({block: 'center'});", submit)
    submit.click()
    code = wait_for(browser, (By.NAME, "code"))
    code.send_keys(pyotp.TOTP(secret).now())
    capture(browser, f"{prefix}_02_totp_form", ("input[name='code']",))
    browser.find_element(By.NAME, "submit").click()
    wait_for(browser, (By.TAG_NAME, "h1"))


def test_public_pages_and_help_have_expected_content(browser, prepare_ui_database):
    """Valida inicio y ayuda pública con evidencia visual."""

    browser.get(f"{BASE_URL}/")
    assert "TutorIA" in browser.title
    capture(browser, "01_inicio_publico")

    browser.get(f"{BASE_URL}/help")
    wait_for(browser, (By.TAG_NAME, "h1"))
    page_text = browser.find_element(By.TAG_NAME, "body").text
    assert "Aprende a usar TutorIA" in page_text
    assert "Estudiante" in page_text
    assert "Docente" in page_text
    assert "Administrador" in page_text
    assert "NVIDIA NIM" in page_text
    capture(browser, "02_ayuda_roles")


def test_student_registration_profile_evaluation_and_progress(browser, prepare_ui_database):
    """Cubre el recorrido principal de un estudiante con capturas por etapa."""

    browser.get(f"{BASE_URL}/auth/register")
    wait_for(browser, (By.NAME, "username")).send_keys("selenium_student")
    browser.find_element(By.NAME, "email").send_keys("selenium.student@example.com")
    browser.find_element(By.NAME, "password").send_keys("Selenium123!")
    browser.find_element(By.NAME, "confirm_password").send_keys("Selenium123!")
    capture(browser, "03_registro_formulario")
    browser.find_element(By.NAME, "submit").click()

    secret = wait_for(browser, (By.TAG_NAME, "code")).text
    capture(browser, "04_configuracion_qr_totp", ("img[alt*='QR']", "code"))
    browser.find_element(By.NAME, "code").send_keys(pyotp.TOTP(secret).now())
    browser.find_element(By.NAME, "submit").click()
    wait_for(browser, (By.TAG_NAME, "h1"))
    capture(browser, "05_aula_inicial")

    browser.get(f"{BASE_URL}/student/profile")
    wait_for(browser, (By.NAME, "name")).send_keys("Estudiante Selenium")
    browser.find_element(By.NAME, "age").send_keys("20")
    browser.find_element(By.NAME, "school").send_keys("Universidad Demo")
    browser.find_element(By.NAME, "interest_area").send_keys("Bases de datos")
    capture(browser, "06_perfil_estudiante")
    browser.find_element(By.NAME, "submit").click()
    WebDriverWait(browser, 15).until(condition.url_contains("/student/dashboard"))

    browser.get(f"{BASE_URL}/student/diagnostic/new")
    wait_for(browser, (By.TAG_NAME, "h1"))
    answers = [
        "Una clave primaria identifica de forma unica una fila.",
        "Normalizar reduce la repeticion de datos.",
        "Un indice acelera consultas frecuentes.",
    ]
    for field, answer in zip(browser.find_elements(By.CSS_SELECTOR, "textarea[name^='answer_']"), answers):
        field.send_keys(answer)
    capture(browser, "07_evaluacion_respuestas")
    submit = browser.find_element(By.NAME, "submit")
    browser.execute_script(
        "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();",
        submit,
    )
    wait_for(browser, (By.TAG_NAME, "h1"))
    assert "Mi progreso" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "08_progreso_evaluacion_enviada")


def test_role_panels_and_invalid_login(browser, prepare_ui_database):
    """Comprueba paneles diferenciados y rechazo de credenciales inválidas."""

    browser.get(f"{BASE_URL}/auth/login")
    browser.find_element(By.NAME, "username").send_keys("usuario_invalido")
    browser.find_element(By.NAME, "password").send_keys("clave_incorrecta")
    browser.find_element(By.NAME, "submit").click()
    assert "Usuario o contraseña incorrectos." in wait_for(browser, (By.CSS_SELECTOR, ".alert-danger")).text
    capture(browser, "09_login_invalido")

    login_with_totp(browser, "docente", "Docente123!", prepare_ui_database["docente"], "10_docente")
    browser.get(f"{BASE_URL}/students")
    assert "Estudiantes" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "11_panel_docente_estudiantes")

    browser.get(f"{BASE_URL}/chat")
    assert "TutorIA" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "14_tutor_ia_chat")
    for path, expected, name in (
        ("/contents", "Biblioteca", "15_biblioteca_contenidos"),
        ("/diagnostics", "Diagnósticos", "16_banco_diagnosticos"),
        ("/recommendations", "Recomendaciones", "17_recomendaciones"),
        ("/reports", "Reportes", "18_reportes"),
    ):
        browser.get(f"{BASE_URL}{path}")
        assert expected in browser.find_element(By.TAG_NAME, "body").text
        capture(browser, name)
    browser.find_element(By.CSS_SELECTOR, "form[action*='/auth/logout'] button").click()

    login_with_totp(browser, "admin", "Administrador123!", prepare_ui_database["admin"], "12_admin")
    browser.get(f"{BASE_URL}/users")
    assert "Usuarios" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "13_panel_admin_usuarios")
    browser.get(f"{BASE_URL}/users/audit")
    assert "Bitácora" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "19_bitacora_auditoria")
