# Caso 5 - Automatizacion QA con Python y Selenium

Este proyecto contiene una prueba tecnica de automatizacion web usando Python, pytest y Selenium Grid.

Sitio utilizado: https://practicetestautomation.com/practice-test-login/

## Requisitos

- Docker de Selenium en ejecucion.
- Selenium Grid disponible en `http://localhost:4444`.
- Python 3.10 o superior.

## Instalacion

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

## Ejecucion

```bash
pytest -v
```

## Evidencias

Las capturas y el HTML generado por las pruebas se guardan en:

```text
evidencias/
```

Escenarios incluidos:

- Login valido con el usuario `student`.
- Login invalido con credenciales incorrectas.
