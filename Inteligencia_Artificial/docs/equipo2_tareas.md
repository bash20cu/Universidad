# Equipo 2 - Tareas del Proyecto (SOF-31)

Integrantes: Miguel, Jasser

## Tema asignado (extraído del PDF)
**Software que identifique objetos básicos en imágenes mediante redes neuronales.**

## Requerimientos obligatorios
A. Diseñe un software que procese imágenes.
B. Implemente una red neuronal básica (Deep Learning).
C. Clasifique 3 tipos de objetos.
D. Genere 3 métricas de precisión.
E. Documente 5 limitaciones técnicas.

## Distribución sugerida del trabajo
- Miguel: A y C (pipeline de imágenes + clasificación de 3 tipos de objetos).
- Miguel: B (implementación y entrenamiento de la red neuronal básica).
- Jasser: D y E (métricas de precisión + documentación de limitaciones técnicas).

## Entregable final del equipo
- Un software funcional que procese imágenes y clasifique 3 tipos de objetos con una red neuronal básica.
- Reporte con 3 métricas de precisión y 5 limitaciones técnicas documentadas.

## Plan de accion (ejecucion incremental)
1. Fase 1 - Preparacion del entorno y validacion del dataset
- Crear entorno virtual y archivo `requirements.txt`.
- Ejecutar `image_phase1_check.py` para:
  - contar imagenes totales y por clase (`cats`, `dogs`, `panda`);
  - validar lectura de una muestra de imagenes;
  - preprocesar muestra (resize + normalizacion);
  - probar backend de computo (CPU/CUDA/DirectML).
- Criterio de salida: script funcional, conteos correctos y prueba basica completada.

2. Fase 2 - Pipeline de preprocesamiento para entrenamiento
- Limpiar estructura de carpetas si hay duplicados.
- Construir carga de datos reproducible (split train/val/test).
- Normalizar y aplicar aumentos basicos.
- Criterio de salida: dataloaders listos para entrenamiento.

3. Fase 3 - Red neuronal basica (Deep Learning)
- Implementar CNN base para 3 clases.
- Entrenar con hiperparametros iniciales y guardar checkpoints.
- Criterio de salida: modelo entrenado y reproducible.

4. Fase 4 - Metricas y evaluacion
- Generar `accuracy`, `precision`, `recall` y `f1-score`.
- Incluir matriz de confusion y reporte por clase.
- Criterio de salida: metricas automaticas y comparables.

5. Fase 5 - Documentacion y limitaciones tecnicas
- Documentar 5 limitaciones reales del enfoque/modelo/dataset.
- Registrar bitacoras por avance segun cronograma del curso.
- Criterio de salida: entregable tecnico completo para e-campus.

## Estructura tecnica (Screaming Architecture)
- `core/`: logica de dominio y pipeline (etiquetado, manifest, modelo, estadisticas).
- `core.phase1`, `core.phase2`, `core.phase3_train`, `core.phase3_infer`, `core.phase4_evaluate`: scripts CLI por fase.
- `app.py`: capa de presentacion (Flask) para dashboard e inferencia.
- `templates/`: vistas HTML basicas para mostrar estado del proyecto.

## Fase 4 - Estado implementado
- Script: `core.phase4_evaluate`.
- Metricas globales: `accuracy`, `macro_precision`, `macro_recall`, `macro_f1`, `weighted_precision`, `weighted_recall`, `weighted_f1`.
- Reporte por clase: precision, recall, f1, support para `cats`, `dogs`, `panda`.
- Matriz de confusion: exportada a CSV.
- Artefactos de salida esperados en `phase4_artifacts/`:
  - `metrics_test.json`
  - `confusion_matrix_test.csv`
  - `classification_report_test.csv`

## Fase 5 - Limitaciones tecnicas (5)
1. Generalizacion limitada a 3 clases cerradas
- El modelo solo fue entrenado con `cats`, `dogs`, `panda`; para clases fuera de distribucion (ej. reptiles) fuerza una etiqueta incorrecta.

2. Arquitectura CNN basica
- La red es intencionalmente simple; puede quedar corta ante variaciones fuertes de iluminacion, pose, fondo o escala.

3. Dataset con estructura duplicada y limpieza heuristica
- El dataset original contenia duplicados y estructura redundante; aunque se aplico deduplicacion por hash, sigue existiendo riesgo de sesgo residual.

4. Evaluacion sin calibracion de confianza
- El sistema reporta probabilidades softmax, pero no tiene calibracion formal ni umbral de rechazo robusto para "desconocido".

5. Backend DirectML con fallback parcial a CPU
- En GPU AMD (DirectML) algunas operaciones pueden caer a CPU, afectando tiempos y consistencia de rendimiento entre entornos.

## Bitacora de avances (plantilla base)
- Fecha:
- Integrante:
- Tarea asignada:
- Tarea ejecutada:
- Evidencia (archivo/comando/salida):
- Estado: Completado / Parcial / Bloqueado
- Bloqueos y acciones siguientes:
