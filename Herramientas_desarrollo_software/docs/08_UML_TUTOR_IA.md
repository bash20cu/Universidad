# UML de TutorIA

Documento de apoyo para el informe académico. Los diagramas están expresados en
Mermaid para poder versionarlos junto con el código y exportarlos a imagen o PDF.

## Diagrama de casos de uso

```mermaid
flowchart LR
    estudiante[Estudiante]
    docente[Docente]
    admin[Administrador]
    ia[Proveedor IA\nNVIDIA / Foundation Models]

    estudiante --> registro((Registrarse y activar TOTP))
    estudiante --> perfil((Completar perfil))
    estudiante --> progreso((Consultar progreso))
    estudiante --> chat((Conversar con TutorIA))
    docente --> estudiantes((Gestionar estudiantes))
    docente --> preguntas((Administrar preguntas))
    docente --> contenidos((Gestionar contenidos))
    docente --> diagnostico((Registrar diagnóstico))
    docente --> reportes((Consultar reportes))
    admin --> usuarios((Gestionar usuarios y roles))
    admin --> bitacora((Consultar bitácora))
    admin --> docentes((Ejecutar funciones docentes))
    diagnostico --> ia
    chat --> ia
```

## Diagrama de clases simplificado

```mermaid
classDiagram
    class User {
      +id
      +username
      +email
      +role
      +totp_enabled
      +set_password()
      +check_password()
    }
    class Student {
      +name
      +age
      +school
      +interest_area
      +assigned_level
    }
    class DiagnosticQuestion {
      +topic
      +prompt
      +expected_competency
      +active
    }
    class DiagnosticEvaluation {
      +status
      +classified_level
      +ai_provider
      +ai_model
      +classified_at
    }
    class DiagnosticAnswer { +answer }
    class EducationalContent {
      +title
      +topic
      +level
      +competency
      +material_type
      +resource_url
      +status
    }
    class ContentRecommendation { +reason }
    class AuditLog { +action +detail +ip_address }

    User "1" -- "0..1" Student
    Student "1" -- "0..*" DiagnosticEvaluation
    DiagnosticEvaluation "1" -- "1..*" DiagnosticAnswer
    DiagnosticAnswer "*" -- "1" DiagnosticQuestion
    Student "1" -- "0..*" ContentRecommendation
    EducationalContent "1" -- "0..*" ContentRecommendation
    User "1" -- "0..*" AuditLog
```

## Diagrama de secuencia: registro y TOTP

```mermaid
sequenceDiagram
    actor Estudiante
    participant Web as Flask/Jinja2
    participant DB as SQLite/SQLAlchemy
    participant Auth as Google Authenticator

    Estudiante->>Web: Envía registro
    Web->>DB: Crea User y secreto TOTP
    Web-->>Estudiante: Muestra QR de aprovisionamiento
    Estudiante->>Auth: Escanea QR
    Auth-->>Estudiante: Genera código de 6 dígitos
    Estudiante->>Web: Envía código
    Web->>DB: Activa totp_enabled
    Web-->>Estudiante: Acceso al dashboard
```

## Diagrama de secuencia: diagnóstico con IA

```mermaid
sequenceDiagram
    actor Docente
    participant Web as Flask
    participant DB as SQLite
    participant IA as NVIDIA o Foundation Models

    Docente->>Web: Selecciona estudiante y responde preguntas
    Web->>DB: Guarda evaluación y respuestas
    Docente->>Web: Solicita clasificación
    Web->>IA: Envía prompt estructurado
    IA-->>Web: Devuelve JSON con nivel y explicación
    Web->>Web: Valida contrato y evidencia mínima
    Web->>DB: Guarda nivel, proveedor, modelo y fecha
    Web-->>Docente: Muestra resultado y recomendaciones
```

