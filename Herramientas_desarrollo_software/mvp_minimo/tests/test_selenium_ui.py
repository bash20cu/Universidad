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
from selenium.webdriver.support.ui import Select
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


# PRUEBAS FUNCIONALES END-TO-END: validan la experiencia completa en navegador
# remoto, desde la pantalla pública hasta los paneles por rol y las evidencias.
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


def submit_form(browser):
    """Envía el formulario visible centrando el botón para evitar solapamientos."""

    button = browser.find_element(By.NAME, "submit")
    browser.execute_script(
        "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();",
        button,
    )


def submit_form_bypassing_html_validation(browser):
    """Envía datos incompletos al servidor para probar validaciones Flask-WTF."""

    browser.execute_script(
        "HTMLFormElement.prototype.submit.call(document.querySelector('main form'));"
    )


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
    capture(browser, f"{prefix}_02_totp_form", ("input[name='code']",))
    # Genera el código después de capturar la pantalla para reducir el riesgo
    # de que expire durante una ejecución lenta del navegador remoto.
    code.send_keys(pyotp.TOTP(secret).now())
    browser.find_element(By.NAME, "submit").click()
    WebDriverWait(browser, 15).until(condition.url_contains("/dashboard"))


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


def test_teacher_crud_validation_and_filtering(browser, prepare_ui_database):
    """Prueba validación, alta, edición y filtro de contenidos desde el navegador."""

    login_with_totp(browser, "docente", "Docente123!", prepare_ui_database["docente"], "20_docente_crud")

    browser.get(f"{BASE_URL}/contents/new")
    wait_for(browser, (By.NAME, "title"))
    submit_form_bypassing_html_validation(browser)
    assert browser.find_elements(By.CSS_SELECTOR, ".field-error")
    capture(browser, "21_contenido_validacion_incompleta")

    browser.find_element(By.NAME, "title").send_keys("Selenium: índices y rendimiento")
    browser.find_element(By.NAME, "topic").send_keys("Bases de datos")
    Select(browser.find_element(By.NAME, "level")).select_by_value("intermedio")
    browser.find_element(By.NAME, "competency").send_keys("Analizar el uso de índices")
    browser.find_element(By.NAME, "description").send_keys("Recurso creado durante la prueba funcional.")
    Select(browser.find_element(By.NAME, "material_type")).select_by_value("ejercicio")
    submit_form(browser)
    wait_for(browser, (By.CSS_SELECTOR, "[data-content-row]"))
    assert "Selenium: índices y rendimiento" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "22_contenido_creado")

    row = browser.find_element(By.XPATH, "//tr[@data-content-row][.//*[contains(normalize-space(), 'Selenium: índices y rendimiento')]]")
    row.find_element(By.LINK_TEXT, "Editar").click()
    wait_for(browser, (By.NAME, "title")).clear()
    browser.find_element(By.NAME, "title").send_keys("Selenium: índices optimizados")
    submit_form(browser)
    wait_for(browser, (By.CSS_SELECTOR, "[data-content-row]"))
    assert "Selenium: índices optimizados" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "23_contenido_editado")

    browser.find_element(By.ID, "content-search").send_keys("índices optimizados")
    WebDriverWait(browser, 5).until(
        lambda page: page.find_element(By.ID, "result-count").text.startswith("1")
    )
    assert browser.find_element(By.ID, "filtered-empty").get_attribute("hidden") is not None
    capture(browser, "24_filtro_contenidos")


def test_teacher_question_and_evaluation_validation(browser, prepare_ui_database):
    """Prueba el banco de preguntas y el rechazo de una evaluación incompleta."""

    login_with_totp(browser, "docente", "Docente123!", prepare_ui_database["docente"], "25_docente_diagnostico")

    browser.get(f"{BASE_URL}/diagnostics/questions/new")
    wait_for(browser, (By.NAME, "topic"))
    submit_form_bypassing_html_validation(browser)
    assert browser.find_elements(By.CSS_SELECTOR, ".field-error")
    capture(browser, "26_pregunta_validacion_incompleta")

    browser.find_element(By.NAME, "topic").send_keys("SQL")
    browser.find_element(By.NAME, "prompt").send_keys("¿Qué ventaja ofrece un índice en una consulta?")
    browser.find_element(By.NAME, "expected_competency").send_keys("Relacionar índices y rendimiento")
    submit_form(browser)
    wait_for(browser, (By.TAG_NAME, "h1"))
    assert "¿Qué ventaja ofrece un índice en una consulta?" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "27_pregunta_creada")

    browser.get(f"{BASE_URL}/diagnostics/new")
    wait_for(browser, (By.NAME, "student_id"))
    submit_form_bypassing_html_validation(browser)
    assert "Responde todas las preguntas" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "28_evaluacion_validacion_incompleta")


def test_admin_user_creation_and_audit(browser, prepare_ui_database):
    """Prueba alta de usuario administrador y trazabilidad en la bitácora."""

    login_with_totp(browser, "admin", "Administrador123!", prepare_ui_database["admin"], "29_admin_usuarios")
    browser.get(f"{BASE_URL}/users/new")
    wait_for(browser, (By.NAME, "username"))
    submit_form_bypassing_html_validation(browser)
    assert browser.find_elements(By.CSS_SELECTOR, ".field-error") or "obligatoria" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "30_usuario_validacion_incompleta")

    browser.find_element(By.NAME, "username").send_keys("selenium_docente")
    browser.find_element(By.NAME, "email").send_keys("selenium.docente@example.com")
    browser.find_element(By.NAME, "password").send_keys("Selenium123!")
    Select(browser.find_element(By.NAME, "role")).select_by_value("docente")
    submit_form(browser)
    WebDriverWait(browser, 15).until(condition.url_contains("/users"))
    assert "selenium_docente" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "31_usuario_creado")

    browser.get(f"{BASE_URL}/users/audit")
    assert "user_created" in browser.find_element(By.TAG_NAME, "body").text
    capture(browser, "32_auditoria_usuario_creado")


def test_student_authorization_and_chat_error_state(browser, prepare_ui_database):
    """Comprueba permisos de estudiante y degradación visible del chat sin proveedor."""

    login_with_totp(browser, "estudiante", "Estudiante123!", prepare_ui_database["estudiante"], "33_estudiante_permisos")
    browser.get(f"{BASE_URL}/users")
    restricted_page = browser.find_element(By.TAG_NAME, "body").text
    assert "Usuarios y permisos" not in restricted_page
    assert "Bitácora" not in restricted_page
    capture(browser, "34_acceso_estudiante_denegado")

    browser.get(f"{BASE_URL}/chat")
    wait_for(browser, (By.ID, "prompt"))
    suggestion = browser.find_element(By.CSS_SELECTOR, "[data-prompt]")
    suggestion.click()
    assert browser.find_element(By.ID, "prompt").get_attribute("value")
    assert browser.find_element(By.ID, "character-count").text.endswith(" / 4000")
    capture(browser, "35_sugerencia_chat")

    browser.find_element(By.ID, "send-button").click()
    WebDriverWait(browser, 20).until(
        condition.presence_of_element_located((By.CSS_SELECTOR, ".assistant-message:not(:first-child)"))
    )
    assert browser.find_elements(By.CSS_SELECTOR, ".error-message, .assistant-message:not(:first-child)")
    capture(browser, "36_chat_respuesta_o_error")


def test_mobile_layout_has_no_horizontal_overflow(browser, prepare_ui_database):
    """Comprueba la adaptación móvil de la interfaz pública y del chat."""

    browser.set_window_size(390, 844)
    browser.get(f"{BASE_URL}/help")
    wait_for(browser, (By.TAG_NAME, "h1"))
    overflow = browser.execute_script("return document.documentElement.scrollWidth - document.documentElement.clientWidth;")
    assert overflow <= 1
    capture(browser, "37_ayuda_movil")

    browser.get(f"{BASE_URL}/auth/login")
    overflow = browser.execute_script("return document.documentElement.scrollWidth - document.documentElement.clientWidth;")
    assert overflow <= 1
    capture(browser, "38_login_movil")


def test_invalid_totp_and_duplicate_registration_are_visible(browser, prepare_ui_database):
    """Comprueba errores de autenticación y duplicados directamente en la UI."""

    browser.get(f"{BASE_URL}/auth/login")
    wait_for(browser, (By.NAME, "username")).send_keys("admin")
    browser.find_element(By.NAME, "password").send_keys("Administrador123!")
    submit_form(browser)
    wait_for(browser, (By.NAME, "code")).send_keys("000000")
    submit_form(browser)
    assert "no es válido" in wait_for(browser, (By.TAG_NAME, "body")).text
    capture(browser, "39_totp_invalido")

    browser.get(f"{BASE_URL}/auth/register")
    wait_for(browser, (By.NAME, "username")).send_keys("admin")
    browser.find_element(By.NAME, "email").send_keys("duplicado@example.com")
    browser.find_element(By.NAME, "password").send_keys("Selenium123!")
    browser.find_element(By.NAME, "confirm_password").send_keys("Selenium123!")
    submit_form(browser)
    assert "registrados" in wait_for(browser, (By.TAG_NAME, "body")).text
    capture(browser, "40_registro_duplicado")
