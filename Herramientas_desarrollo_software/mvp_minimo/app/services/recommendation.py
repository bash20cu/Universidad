# Archivo: recommendation.py
# Propósito: Calcula recomendaciones educativas explicables.
# Responsabilidades: Prioriza coincidencias de tema y nivel y genera una razón legible para cada recurso.
# Dependencias: Modelos SQLAlchemy EducationalContent y Student.
# Entradas y salidas: Recibe un estudiante y límite opcional; devuelve contenidos ordenados y razones.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Reglas académicas para recomendar recursos sin depender directamente de la IA."""

from __future__ import annotations

from app.models import EducationalContent, Student


LEVEL_ORDER = {"basico": 1, "intermedio": 2, "avanzado": 3}


def recommend_contents(student: Student, limit: int = 5) -> list[EducationalContent]:
    """Devuelve contenidos alineados al nivel y al área de interés del estudiante.

    La regla es deliberadamente explicable para un MVP académico: primero busca
    coincidencias de tema y nivel; si no alcanza el límite, completa con recursos
    del mismo nivel. No inventa contenidos ni delega esta decisión a la IA.
    """

    level = student.assigned_level or "basico"
    interest = (student.interest_area or "").strip().casefold()
    contents = EducationalContent.query.order_by(EducationalContent.title.asc()).all()

    matching_topic = [
        content for content in contents
        if content.level == level and interest and interest in content.topic.casefold()
    ]
    same_level = [content for content in contents if content.level == level and content not in matching_topic]
    return (matching_topic + same_level)[:limit]


def recommendation_reason(student: Student, content: EducationalContent) -> str:
    """Explica en lenguaje claro por qué el recurso coincide con el estudiante."""

    if (student.interest_area or "").strip().casefold() in content.topic.casefold():
        return f"Coincide con el área de interés {student.interest_area} y el nivel {content.level}."
    return f"Refuerza competencias del nivel {content.level} identificado en el diagnóstico."
