from __future__ import annotations

import json
from dataclasses import dataclass
from typing import Any

from ai_provider import AIProviderError, ChatProvider
from app.models import DiagnosticEvaluation


VALID_LEVELS = {"basico", "intermedio", "avanzado"}
SYSTEM_PROMPT = (
    "Eres TutorIA, un evaluador académico. Analiza respuestas diagnósticas "
    "y responde únicamente JSON válido en español, sin Markdown."
)


@dataclass(frozen=True)
class DiagnosticClassification:
    level: str
    explanation: str
    strengths: list[str]
    improvement_areas: list[str]


def build_diagnostic_prompt(evaluation: DiagnosticEvaluation) -> str:
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
                "Usa explicación breve y listas concretas. No incluyas campos adicionales."
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

    return parse_classification(raw_content)


def format_classification_explanation(classification: DiagnosticClassification) -> str:
    strengths = "; ".join(classification.strengths)
    opportunities = "; ".join(classification.improvement_areas)
    return (
        f"{classification.explanation}\n\n"
        f"Fortalezas: {strengths}.\n"
        f"Oportunidades de mejora: {opportunities}."
    )
