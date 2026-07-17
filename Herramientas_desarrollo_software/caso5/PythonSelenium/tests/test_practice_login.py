from pathlib import Path

import pytest
from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as condition
from selenium.webdriver.support.ui import WebDriverWait


GRID_ENDPOINT = "http://localhost:4444/wd/hub"
LOGIN_PAGE = "https://practicetestautomation.com/practice-test-login/"
OUTPUT_DIR = Path(__file__).resolve().parents[1] / "evidencias"


@pytest.fixture(scope="session", autouse=True)
def prepare_evidence_folder():
    OUTPUT_DIR.mkdir(exist_ok=True)


@pytest.fixture()
def browser():
    settings = Options()
    settings.add_argument("--window-size=1366,900")

    session = webdriver.Remote(
        command_executor=GRID_ENDPOINT,
        options=settings,
    )

    yield session

    session.quit()


def fill_login_form(browser, username, password):
    wait = WebDriverWait(browser, 15)
    browser.get(LOGIN_PAGE)

    username_box = wait.until(
        condition.visibility_of_element_located((By.ID, "username"))
    )
    password_box = browser.find_element(By.ID, "password")

    username_box.clear()
    username_box.send_keys(username)

    password_box.clear()
    password_box.send_keys(password)

    return browser.find_element(By.ID, "submit")


def test_usuario_valido_accede_al_area_privada(browser):
    wait = WebDriverWait(browser, 15)

    submit_button = fill_login_form(
        browser,
        username="student",
        password="Password123",
    )

    browser.save_screenshot(str(OUTPUT_DIR / "01_formulario_login_valido.png"))
    submit_button.click()

    success_message = wait.until(
        condition.visibility_of_element_located((By.TAG_NAME, "h1"))
    )
    logout_button = wait.until(
        condition.element_to_be_clickable((By.LINK_TEXT, "Log out"))
    )

    assert "logged in successfully" in success_message.text.lower()
    assert "logged-in-successfully" in browser.current_url
    assert logout_button.is_displayed()

    browser.save_screenshot(str(OUTPUT_DIR / "02_login_valido_confirmado.png"))
    (OUTPUT_DIR / "resultado_login_valido.html").write_text(
        browser.page_source,
        encoding="utf-8",
    )


def test_credenciales_invalidas_muestran_alerta(browser):
    wait = WebDriverWait(browser, 15)

    submit_button = fill_login_form(
        browser,
        username="usuario_prueba",
        password="clave_equivocada",
    )

    browser.save_screenshot(str(OUTPUT_DIR / "03_formulario_login_invalido.png"))
    submit_button.click()

    alert_text = wait.until(
        condition.visibility_of_element_located((By.ID, "error"))
    )

    assert "username is invalid" in alert_text.text.lower()
    assert "practice-test-login" in browser.current_url

    browser.save_screenshot(str(OUTPUT_DIR / "04_login_invalido_alerta.png"))
