# Documentación técnica de integración de IA para TutorIA

**Alternativas evaluadas:** NVIDIA NIM y Apple Foundation Models  
**Arquitectura aprobada para el MVP:** NVIDIA NIM principal, Foundation Models de respaldo local  
**Proyecto:** TutorIA, Equipo 1  
**Fecha:** 10 de junio de 2026  
**Propósito:** material técnico para revisión con el profesor

> Estado actualizado: la arquitectura NVIDIA + Foundation Models está aprobada
> para el MVP. Las secciones que mencionen Ollama se conservan únicamente como
> alternativa futura de comparación técnica; Ollama no forma parte del flujo
> implementado ni del respaldo activo de esta entrega.

## 1. Objetivo técnico

TutorIA debe utilizar inteligencia artificial para analizar las respuestas de
una evaluación diagnóstica y producir un resultado estructurado:

- nivel: `basico`, `intermedio` o `avanzado`;
- explicación breve;
- fortalezas identificadas;
- oportunidades de mejora;
- temas recomendados;
- indicador informativo de confianza.

La IA no será responsable de autenticar usuarios, guardar información, asignar
permisos ni seleccionar directamente registros de la base de datos. Estas
decisiones permanecen en los servicios de Flask.

## 2. Requisitos de las alternativas

### 2.1. Foundation Models SDK para Python

Requisitos publicados por Apple:

- Mac compatible con Apple Intelligence.
- Apple Intelligence activado.
- macOS 26.0 o posterior.
- Xcode 26.0 o posterior y licencia aceptada.
- Python 3.10 o posterior.
- Paquete `apple-fm-sdk`.

```bash
python3 --version
xcodebuild -version
python3 -m pip install apple-fm-sdk
```

Foundation Models ejecuta desde Python el modelo integrado en Apple
Intelligence. La aplicación debe comprobar la disponibilidad en tiempo de
ejecución; cumplir la versión del sistema no garantiza por sí solo que el
modelo esté disponible.

### 2.2. Ollama

Ollama está disponible para macOS, Windows y Linux. Para usarlo localmente se
requiere instalar la aplicación, descargar un modelo y mantener activo el
servicio.

```bash
ollama --version
ollama pull gemma3:4b
ollama serve
```

La API local se publica por defecto en:

```text
http://localhost:11434/api
```

El modelo concreto debe fijarse antes de las pruebas. `gemma3:4b` aparece aquí
solo como ejemplo de un modelo relativamente pequeño; la selección final
dependerá de la memoria y rendimiento de la computadora de demostración.

## 3. Pruebas mínimas de conectividad

### 3.1. Verificar Foundation Models

```python
import apple_fm_sdk as fm


model = fm.SystemLanguageModel()
available, reason = model.is_available()

print(
    {
        "provider": "foundation_models",
        "available": available,
        "reason": reason,
    }
)
```

Resultado esperado cuando está disponible:

```text
{'provider': 'foundation_models', 'available': True, 'reason': None}
```

La razón devuelta debe registrarse cuando `available` sea `False`. No debe
intentarse generar una respuesta antes de esta comprobación.

### 3.2. Verificar Ollama

```bash
curl http://localhost:11434/api/tags
```

También puede verificarse desde Python:

```python
import requests


def ollama_is_available() -> tuple[bool, str | None]:
    try:
        response = requests.get(
            "http://localhost:11434/api/tags",
            timeout=2,
        )
        response.raise_for_status()
        return True, None
    except requests.RequestException as error:
        return False, str(error)
```

## 4. Uso básico de las API

### 4.1. Respuesta simple con Foundation Models

```python
import asyncio
import apple_fm_sdk as fm


async def main() -> None:
    model = fm.SystemLanguageModel()
    available, reason = model.is_available()

    if not available:
        raise RuntimeError(f"Foundation Models no disponible: {reason}")

    session = fm.LanguageModelSession(
        model=model,
        instructions=(
            "Eres un tutor educativo. Responde de forma breve, clara "
            "y apropiada para el nivel del estudiante."
        ),
    )

    response = await session.respond(
        prompt="Explica qué es una llave primaria en una base de datos."
    )
    print(response)


asyncio.run(main())
```

### 4.2. Respuesta simple con Ollama

```bash
curl http://localhost:11434/api/chat \
  -H "Content-Type: application/json" \
  -d '{
    "model": "gemma3:4b",
    "stream": false,
    "messages": [
      {
        "role": "system",
        "content": "Eres un tutor educativo. Responde de forma breve y clara."
      },
      {
        "role": "user",
        "content": "Explica qué es una llave primaria en una base de datos."
      }
    ]
  }'
```

La respuesta se obtiene en `message.content`. Ollama también devuelve métricas
como duración total, duración de carga y cantidad de tokens evaluados.

## 5. Contrato de diagnóstico de TutorIA

La misma estructura lógica debe utilizarse con ambos proveedores:

```python
from dataclasses import dataclass, field
from typing import Literal, Protocol


Level = Literal["basico", "intermedio", "avanzado"]


@dataclass
class DiagnosticRequest:
    student_id: int
    topic: str
    questions_and_answers: list[dict[str, str]]


@dataclass
class DiagnosticResult:
    level: Level
    explanation: str
    strengths: list[str] = field(default_factory=list)
    improvement_areas: list[str] = field(default_factory=list)
    recommended_topics: list[str] = field(default_factory=list)
    confidence: float = 0.0
    provider: str = ""


class AIProvider(Protocol):
    def is_available(self) -> tuple[bool, str | None]:
        ...

    async def classify(
        self,
        request: DiagnosticRequest,
    ) -> DiagnosticResult:
        ...
```

Este contrato desacopla `EvaluationService` del SDK de Apple y de la API de
Ollama.

## 6. Diagnóstico estructurado con Foundation Models

El SDK de Apple permite generación guiada mediante `@generable`. Las guías
restringen valores, rangos y cantidades.

```python
from typing import List
import apple_fm_sdk as fm


@fm.generable("Resultado de una evaluación diagnóstica")
class FoundationDiagnostic:
    level: str = fm.guide(
        "Nivel académico observado",
        anyOf=["basico", "intermedio", "avanzado"],
    )
    explanation: str = fm.guide(
        "Justificación breve basada solamente en las respuestas"
    )
    strengths: List[str] = fm.guide(
        "Tres fortalezas concretas",
        count=3,
    )
    improvement_areas: List[str] = fm.guide(
        "Tres oportunidades de mejora",
        count=3,
    )
    recommended_topics: List[str] = fm.guide(
        "Tres temas que conviene estudiar",
        count=3,
    )
    confidence: float = fm.guide(
        "Confianza informativa entre cero y uno",
        range=(0.0, 1.0),
    )
```

Implementación del proveedor:

```python
import json
import apple_fm_sdk as fm


class FoundationModelsProvider:
    name = "foundation_models"

    def __init__(self) -> None:
        self.model = fm.SystemLanguageModel()

    def is_available(self) -> tuple[bool, str | None]:
        return self.model.is_available()

    async def classify(
        self,
        request: DiagnosticRequest,
    ) -> DiagnosticResult:
        available, reason = self.is_available()
        if not available:
            raise RuntimeError(
                f"Foundation Models no disponible: {reason}"
            )

        session = fm.LanguageModelSession(
            model=self.model,
            instructions=(
                "Analiza una evaluación diagnóstica educativa. "
                "Usa solo las preguntas y respuestas proporcionadas. "
                "No inventes datos personales, notas ni contenidos."
            ),
        )

        prompt = json.dumps(
            {
                "tema": request.topic,
                "respuestas": request.questions_and_answers,
                "criterio": (
                    "Básico: reconoce conceptos aislados. "
                    "Intermedio: relaciona y aplica conceptos. "
                    "Avanzado: justifica, compara y resuelve casos."
                ),
            },
            ensure_ascii=False,
        )

        generated = await session.respond(
            prompt=prompt,
            generating=FoundationDiagnostic,
        )

        return DiagnosticResult(
            level=generated.level,
            explanation=generated.explanation,
            strengths=list(generated.strengths),
            improvement_areas=list(generated.improvement_areas),
            recommended_topics=list(generated.recommended_topics),
            confidence=float(generated.confidence),
            provider=self.name,
        )
```

## 7. Diagnóstico estructurado con Ollama

Ollama acepta un esquema JSON en el campo `format`. Para TutorIA se recomienda
crear el esquema con Pydantic y validar nuevamente el JSON recibido.

```python
from typing import Literal
from pydantic import BaseModel, Field


class OllamaDiagnostic(BaseModel):
    level: Literal["basico", "intermedio", "avanzado"]
    explanation: str = Field(min_length=10, max_length=500)
    strengths: list[str] = Field(default_factory=list, max_length=3)
    improvement_areas: list[str] = Field(
        default_factory=list,
        max_length=3,
    )
    recommended_topics: list[str] = Field(
        default_factory=list,
        max_length=3,
    )
    confidence: float = Field(ge=0.0, le=1.0)
```

Implementación HTTP:

```python
import json
import requests


class OllamaProvider:
    name = "ollama"

    def __init__(
        self,
        base_url: str = "http://localhost:11434/api",
        model: str = "gemma3:4b",
    ) -> None:
        self.base_url = base_url.rstrip("/")
        self.model = model

    def is_available(self) -> tuple[bool, str | None]:
        try:
            response = requests.get(
                f"{self.base_url}/tags",
                timeout=2,
            )
            response.raise_for_status()
            return True, None
        except requests.RequestException as error:
            return False, str(error)

    async def classify(
        self,
        request: DiagnosticRequest,
    ) -> DiagnosticResult:
        payload = {
            "model": self.model,
            "stream": False,
            "format": OllamaDiagnostic.model_json_schema(),
            "options": {"temperature": 0},
            "messages": [
                {
                    "role": "system",
                    "content": (
                        "Analiza la evaluación diagnóstica. "
                        "Usa solo la evidencia proporcionada y responde "
                        "con el esquema JSON solicitado."
                    ),
                },
                {
                    "role": "user",
                    "content": json.dumps(
                        {
                            "tema": request.topic,
                            "respuestas": (
                                request.questions_and_answers
                            ),
                            "criterio": (
                                "Básico: reconoce conceptos aislados. "
                                "Intermedio: relaciona y aplica conceptos. "
                                "Avanzado: justifica, compara y resuelve casos."
                            ),
                        },
                        ensure_ascii=False,
                    ),
                },
            ],
        }

        response = requests.post(
            f"{self.base_url}/chat",
            json=payload,
            timeout=60,
        )
        response.raise_for_status()

        content = response.json()["message"]["content"]
        generated = OllamaDiagnostic.model_validate_json(content)

        return DiagnosticResult(
            level=generated.level,
            explanation=generated.explanation,
            strengths=generated.strengths,
            improvement_areas=generated.improvement_areas,
            recommended_topics=generated.recommended_topics,
            confidence=generated.confidence,
            provider=self.name,
        )
```

En la implementación final, la llamada HTTP bloqueante debería ejecutarse con
un cliente asíncrono o fuera del event loop. Se muestra `requests` porque hace
el ejemplo más sencillo para la discusión técnica.

## 8. Selección y fallback de proveedores

```python
class AIUnavailableError(RuntimeError):
    pass


class ProviderSelector:
    def __init__(
        self,
        primary: AIProvider,
        fallback: AIProvider,
    ) -> None:
        self.providers = [primary, fallback]

    async def classify(
        self,
        request: DiagnosticRequest,
    ) -> DiagnosticResult:
        errors: list[str] = []

        for provider in self.providers:
            available, reason = provider.is_available()
            if not available:
                errors.append(
                    f"{provider.__class__.__name__}: {reason}"
                )
                continue

            try:
                return await provider.classify(request)
            except Exception as error:
                errors.append(
                    f"{provider.__class__.__name__}: {error}"
                )

        raise AIUnavailableError("; ".join(errors))
```

Política propuesta:

1. Foundation Models es el proveedor principal.
2. Ollama se utiliza si Foundation Models no está disponible o falla.
3. El proveedor utilizado se almacena con la evaluación.
4. Si ambos fallan, la evaluación queda pendiente.
5. No se genera un nivel predeterminado para ocultar el error.

## 9. Servicio de negocio

```python
class EvaluationService:
    def __init__(
        self,
        provider_selector: ProviderSelector,
        evaluation_repository,
        audit_service,
    ) -> None:
        self.provider_selector = provider_selector
        self.evaluation_repository = evaluation_repository
        self.audit_service = audit_service

    async def evaluate(
        self,
        request: DiagnosticRequest,
    ) -> DiagnosticResult:
        result = await self.provider_selector.classify(request)
        validated = validate_diagnostic(result)

        self.evaluation_repository.save_result(
            student_id=request.student_id,
            topic=request.topic,
            level=validated.level,
            explanation=validated.explanation,
            provider=validated.provider,
        )

        self.audit_service.record(
            action="diagnostic_evaluation_completed",
            entity_id=request.student_id,
            metadata={"provider": validated.provider},
        )

        return validated
```

La bitácora no debe almacenar el prompt completo si contiene datos personales.
Debe registrar identificadores, proveedor, fecha, duración y estado.

## 10. Validación independiente del modelo

```python
ALLOWED_LEVELS = {"basico", "intermedio", "avanzado"}


def validate_diagnostic(
    result: DiagnosticResult,
) -> DiagnosticResult:
    if result.level not in ALLOWED_LEVELS:
        raise ValueError("Nivel diagnóstico no permitido")

    if not result.explanation.strip():
        raise ValueError("La explicación es obligatoria")

    result.confidence = max(0.0, min(1.0, result.confidence))
    result.strengths = result.strengths[:3]
    result.improvement_areas = result.improvement_areas[:3]
    result.recommended_topics = result.recommended_topics[:3]
    return result
```

La confianza generada por el modelo es descriptiva. No debe presentarse como
probabilidad estadística comprobada.

## 11. Integración conceptual con Flask

Flask debe depender de `EvaluationService`, no del proveedor:

```python
from flask import Blueprint, jsonify, request
from flask_login import login_required


evaluation_bp = Blueprint(
    "evaluation",
    __name__,
    url_prefix="/evaluations",
)


@evaluation_bp.post("/diagnostic")
@login_required
async def diagnostic():
    data = request.get_json()

    diagnostic_request = DiagnosticRequest(
        student_id=data["student_id"],
        topic=data["topic"],
        questions_and_answers=data["answers"],
    )

    try:
        result = await evaluation_service.evaluate(
            diagnostic_request
        )
        return jsonify(
            {
                "level": result.level,
                "explanation": result.explanation,
                "strengths": result.strengths,
                "improvement_areas": result.improvement_areas,
                "recommended_topics": result.recommended_topics,
                "provider": result.provider,
            }
        )
    except AIUnavailableError:
        return jsonify(
            {
                "error": "AI_UNAVAILABLE",
                "message": (
                    "La evaluación quedó pendiente. "
                    "Intente nuevamente más tarde."
                ),
            }
        ), 503
```

Las vistas asíncronas de Flask requieren instalar Flask con su soporte `async`.
Otra opción para el MVP es conservar rutas síncronas y encapsular la ejecución
asíncrona en una capa adaptadora. La decisión debe mantenerse uniforme en todo
el proyecto.

## 12. Configuración por variables de entorno

```dotenv
AI_PRIMARY_PROVIDER=foundation_models
AI_FALLBACK_PROVIDER=ollama
OLLAMA_BASE_URL=http://localhost:11434/api
OLLAMA_MODEL=gemma3:4b
AI_REQUEST_TIMEOUT_SECONDS=60
AI_STORE_RAW_PROMPTS=false
```

Foundation Models local no requiere una API key. Ollama local tampoco. Ninguna
clave debe escribirse en el código fuente.

## 13. Ejemplo de entrada para la demostración

```json
{
  "student_id": 12,
  "topic": "Fundamentos de bases de datos",
  "answers": [
    {
      "question": "¿Qué función cumple una llave primaria?",
      "answer": "Identifica cada registro y no debería repetirse."
    },
    {
      "question": "¿Qué representa una llave foránea?",
      "answer": "Conecta una tabla con otra usando una referencia."
    },
    {
      "question": "Explique una relación uno a muchos.",
      "answer": "Un pasajero puede tener varias reservas."
    }
  ]
}
```

Respuesta esperada por contrato:

```json
{
  "level": "intermedio",
  "explanation": "El estudiante reconoce las llaves y aplica correctamente una relación uno a muchos.",
  "strengths": [
    "Identifica la función de la llave primaria",
    "Comprende la referencia mediante llave foránea",
    "Aplica una cardinalidad a un ejemplo"
  ],
  "improvement_areas": [
    "Explicar restricciones de integridad",
    "Diferenciar relaciones identificadoras",
    "Profundizar en normalización"
  ],
  "recommended_topics": [
    "Integridad referencial",
    "Cardinalidad y opcionalidad",
    "Primera, segunda y tercera forma normal"
  ],
  "confidence": 0.8,
  "provider": "foundation_models"
}
```

El contenido exacto puede variar porque los modelos generativos no son
deterministas. Lo que debe permanecer estable es el esquema.

## 14. Streaming

Foundation Models ofrece `stream_response()`:

```python
session = fm.LanguageModelSession()

async for chunk in session.stream_response(
    "Explica la integridad referencial en tres pasos."
):
    print(chunk, end="", flush=True)
```

Ollama utiliza streaming por defecto en sus endpoints. Para simplificar la
evaluación estructurada se recomienda `stream: false`. El streaming podría
utilizarse más adelante para explicaciones extensas, pero no es necesario para
el MVP.

## 15. Manejo de errores

Foundation Models documenta, entre otros:

- `AssetsUnavailableError`;
- `ExceededContextWindowSizeError`;
- `GuardrailViolationError`;
- `UnsupportedLanguageOrLocaleError`;
- `DecodingFailureError`;
- `RefusalError`;
- `InvalidGenerationSchemaError`;
- `ToolCallError`.

Ejemplo:

```python
try:
    result = await provider.classify(request)
except fm.GuardrailViolationError:
    logger.warning("Foundation Models rechazó el contenido")
except fm.ExceededContextWindowSizeError:
    logger.warning("La evaluación excedió el contexto permitido")
except fm.FoundationModelsError as error:
    logger.exception("Error de Foundation Models: %s", error)
```

Para Ollama deben controlarse:

- conexión rechazada;
- tiempo de espera;
- modelo no descargado;
- error HTTP;
- JSON incompleto;
- incumplimiento del esquema;
- falta de memoria o cierre del proceso.

## 16. Pruebas propuestas

### Pruebas unitarias

- `ProviderSelector` utiliza FM cuando está disponible.
- Utiliza Ollama cuando FM no está disponible.
- No utiliza Ollama si FM responde correctamente.
- Devuelve `AIUnavailableError` si ambos fallan.
- Rechaza niveles fuera del catálogo.
- Rechaza explicaciones vacías.
- Limita las listas a tres elementos.
- No persiste resultados inválidos.

### Pruebas de integración

- Consultar disponibilidad real de Foundation Models.
- Ejecutar un diagnóstico estructurado con Foundation Models.
- Consultar `/api/tags` de Ollama.
- Ejecutar el mismo diagnóstico con Ollama.
- Verificar que ambos resultados cumplen el contrato.
- Apagar Ollama y comprobar el error controlado.
- Simular FM no disponible y comprobar el fallback.

### Comparación para la exposición

Usar entre 10 y 20 evaluaciones fijas y registrar:

| Métrica | Propósito |
|---|---|
| Proveedor | Identificar el motor utilizado |
| Resultado válido | Comprobar el contrato |
| Nivel esperado/obtenido | Revisar concordancia pedagógica |
| Latencia | Comparar experiencia de uso |
| Error | Analizar disponibilidad |
| Observación docente | Validar la utilidad educativa |

La calidad no debe evaluarse con un solo ejemplo.

## 17. Guion corto para demostrar en clase

1. Mostrar la prueba `is_available()` de Foundation Models.
2. Mostrar `curl http://localhost:11434/api/tags`.
3. Explicar el contrato `AIProvider`.
4. Ejecutar la misma entrada con ambos proveedores.
5. Mostrar que ambos devuelven el mismo esquema.
6. Apagar o simular la indisponibilidad del proveedor principal.
7. Mostrar el fallback y el campo `provider`.
8. Aclarar que SQLite guarda solo el resultado validado.
9. Confirmar que Private Cloud Compute no forma parte del MVP local.

## 18. Preguntas técnicas para aprobación

1. ¿Se acepta `apple-fm-sdk` como dependencia del backend Python?
2. ¿La demostración debe funcionar obligatoriamente fuera de macOS?
3. ¿El respaldo Ollama debe estar implementado o solamente diseñado?
4. ¿Se requiere comparar precisión o basta demostrar integración y estructura?
5. ¿El profesor espera una revisión humana del nivel antes de publicarlo?
6. ¿Se puede registrar el proveedor y métricas de latencia en la bitácora?
7. ¿Se acepta que PCC quede explícitamente fuera del alcance?

## 19. Diferencia entre macOS 26 y macOS 27

- El SDK oficial de Foundation Models para Python declara como requisito
  mínimo macOS 26.
- WWDC26 presenta novedades de la generación de sistemas 27, incluido el
  comando `fm` para terminal y nuevas capacidades del framework.
- El MVP debe basarse únicamente en las funciones comprobadas en la Mac de
  desarrollo.
- No debe afirmarse que una función anunciada para macOS 27 está disponible en
  macOS 26 sin una prueba concreta.

## 20. Private Cloud Compute

Private Cloud Compute usa infraestructura remota de Apple. Aunque Apple
describe protecciones de privacidad y ausencia de API keys para el usuario, no
es inferencia local en el dispositivo.

Para defender el objetivo académico de IA local:

- `SystemLanguageModel` será la opción FM del MVP;
- Ollama utilizará un modelo descargado localmente;
- PCC quedará como alternativa futura y fuera de la demostración principal.

## 21. Referencias oficiales

- Apple, Foundation Models SDK for Python:
  https://apple.github.io/python-apple-fm-sdk/
- Apple, Guided Generation:
  https://apple.github.io/python-apple-fm-sdk/guided_generation.html
- Apple, Streaming Responses:
  https://apple.github.io/python-apple-fm-sdk/streaming.html
- Apple, Tools and function calling:
  https://apple.github.io/python-apple-fm-sdk/tools.html
- Apple, SDK source and examples:
  https://github.com/apple/python-apple-fm-sdk
- Apple, WWDC26: What’s new in the Foundation Models framework:
  https://developer.apple.com/videos/play/wwdc2026/241/
- Apple, WWDC26: Build AI-powered scripts with the fm CLI and Python SDK:
  https://developer.apple.com/videos/play/wwdc2026/334/
- Ollama, API introduction:
  https://docs.ollama.com/api/introduction
- Ollama, Chat endpoint:
  https://docs.ollama.com/api/chat
- Ollama, Structured outputs:
  https://docs.ollama.com/capabilities/structured-outputs
- Ollama, Quickstart:
  https://docs.ollama.com/quickstart
