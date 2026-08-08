# Archivo: extensions.py
# Propósito: Declara las extensiones compartidas de Flask.
# Responsabilidades: Centraliza SQLAlchemy, Flask-Login y protección CSRF para que la factoría y las rutas usen instancias únicas.
# Dependencias: Flask-SQLAlchemy, Flask-Login y Flask-WTF.
# Entradas y salidas: No recibe entradas directas; exporta las extensiones inicializadas sin aplicación asociada.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Extensiones compartidas de Flask y SQLAlchemy para TutorIA."""

from flask_login import LoginManager
from flask_sqlalchemy import SQLAlchemy
from flask_wtf import CSRFProtect


db = SQLAlchemy()
login_manager = LoginManager()
csrf = CSRFProtect()
