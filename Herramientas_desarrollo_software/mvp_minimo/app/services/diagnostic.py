# Archivo: diagnostic.py
# Propósito: Construye y valida la clasificación académica con IA.
# Responsabilidades: Genera prompts JSON, valida niveles y listas, aplica evidencia mínima y formatea explicaciones.
# Dependencias: __future__, json, dataclasses, typing, ai_provider, app
# Entradas y salidas: Recibe una evaluación y proveedor; devuelve clasificación validada o errores controlados.
# Autoría: Miguel Alejandro Fernández Arteaga y Roberto José Rojas García
# Copyright académico: © 2026 Miguel Alejandro Fernández Arteaga y Roberto José Rojas García.
"""Reglas de evaluación, validación y clasificación académica con IA."""

from __future__ import annotations

import json
from dataclasses import dataclass, replace
from typing import Any

from ai_provider import AIProviderError, ChatProvider
from app.models import DiagnosticEvaluation


VALID_LEVELS = {"basico", "intermedio", "avanzado"}
SYSTEM_PROMPT = (
    "Eres TutorIA, un evaluador académico. Analiza respuestas diagnósticas "
    "y responde únicamente JSON válido en español, sin Markdown. "
    "No supongas conocimientos que no estén demostrados en las respuestas. "
    "Respuestas cortas, incoherentes o que expresen desconocimiento deben ser nivel basico."
)
MINIMUM_EVIDENCE_LENGTH = 20


@dataclass(frozen=True)
class DiagnosticClassification:
    """Resultado estructurado que la IA debe devolver para un diagnóstico."""

    level: str
    explanation: str
    strengths: list[str]
    improvement_areas: list[str]


def build_diagnostic_prompt(evaluation: DiagnosticEvaluation) -> str:
    """Construye el prompt JSON con el contexto y respuestas del estudiante."""

    answers = []
    for answer in evaluation.answers:
        answers.append(
            {
                "tema": answer.question.topic,
                "pregunta": answer.question.prompt,
                "competencia_esperada": answer.question.expected_competency,
                "respuesta_estudiante": answer.answer,
            }
        )

    # El prompt exige un contrato JSON para poder validar antes de guardar en la base de datos.
    return json.dumps(
        {
            "instrucciones": (
                "Clasifica el nivel del estudiante como basico, intermedio o avanzado. "
                "Usa explicación breve y listas concretas. No incluyas campos adicionales. "
                "No seas generoso: no infieras respuestas correctas que el estudiante no escribió. "
                "Para intermedio o avanzado exige evidencia clara en la mayoría de respuestas."
            ),
            "formato_respuesta": {
                "level": "basico|intermedio|avanzado",
                "explanation": "texto breve",
                "strengths": ["fortaleza 1", "fortaleza 2"],
                "improvement_areas": ["oportunidad 1", "oportunidad 2"],
            },
            "estudiante": {
                "nombre": evaluation.student.name,
                "edad": evaluation.student.age,
                "centro_educativo": evaluation.student.school,
                "area_interes": evaluation.student.interest_area,
                "nivel_actual": evaluation.student.assigned_level,
            },
            "respuestas": answers,
        },
        ensure_ascii=False,
    )


def parse_classification(raw_content: str) -> DiagnosticClassification:
    """Valida el JSON de la IA antes de permitir que llegue a la base de datos."""

    try:
        payload: Any = json.loads(raw_content)
    except json.JSONDecodeError as error:
        raise ValueError("La IA no devolvió JSON válido para la clasificación.") from error

    if not isinstance(payload, dict):
        raise ValueError("La clasificación debe ser un objeto JSON.")

    level = str(payload.get("level", "")).strip().lower()
    explanation = str(payload.get("explanation", "")).strip()
    strengths = payload.get("strengths")
    improvement_areas = payload.get("improvement_areas")

    if level not in VALID_LEVELS:
        raise ValueError("La clasificación debe usar nivel basico, intermedio o avanzado.")
    if not explanation:
        raise ValueError("La clasificación debe incluir una explicación.")
    if not isinstance(strengths, list) or not all(isinstance(item, str) and item.strip() for item in strengths):
        raise ValueError("La clasificación debe incluir fortalezas como lista de texto.")
    if not isinstance(improvement_areas, list) or not all(isinstance(item, str) and item.strip() for item in improvement_areas):
        raise ValueError("La clasificación debe incluir oportunidades como lista de texto.")

    return DiagnosticClassification(
        level=level,
        explanation=explanation,
        strengths=[item.strip() for item in strengths],
        improvement_areas=[item.strip() for item in improvement_areas],
    )


def classify_evaluation(evaluation: DiagnosticEvaluation, provider: ChatProvider) -> DiagnosticClassification:
    """Solicita clasificación al proveedor y aplica reglas de evidencia mínima."""

    if not evaluation.answers:
        raise ValueError("La evaluación no tiene respuestas para clasificar.")

    try:
        provider.ensure_ready()
        raw_content = provider.complete_chat(
            [
                {"role": "system", "content": SYSTEM_PROMPT},
                {"role": "user", "content": build_diagnostic_prompt(evaluation)},
            ]
        )
    except AIProviderError:
        raise

    classification = parse_classification(raw_content)
    # Regla de seguridad académica: la IA no puede compensar respuestas sin evidencia.
    insufficient_evidence = any(
        len(answer.answer.strip()) < MINIMUM_EVIDENCE_LENGTH for answer in evaluation.answers
    )
    if insufficient_evidence and classification.level != "basico":
        return replace(
            classification,
            level="basico",
            explanation=(
                "La evidencia escrita es insuficiente para asignar un nivel superior. "
                + classification.explanation
            ),
        )
    return classification


def format_classification_explanation(classification: DiagnosticClassification) -> str:
    """Convierte el resultado estructurado en explicación para docentes."""

    strengths = "; ".join(classification.strengths)
    opportunities = "; ".join(classification.improvement_areas)
    return (
        f"{classification.explanation}\n\n"
        f"Fortalezas: {strengths}.\n"
        f"Oportunidades de mejora: {opportunities}."
    )
