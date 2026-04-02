# Proyecto de Inteligencia Artificial

Aplicacion academica para clasificar imagenes de tres clases (`cats`, `dogs`, `panda`) usando una CNN basica en PyTorch. El repositorio incluye scripts por fase, artefactos generados y un dashboard web en Flask para inspeccionar metricas, lanzar procesos e inferir imagenes nuevas.

## Estructura

- `core/`: logica principal de fases, carga de manifest, modelo e inferencia.
- `phase2_artifacts/`: manifest del dataset preparado.
- `phase3_artifacts/`: checkpoints y metricas del entrenamiento.
- `phase4_artifacts/`: evaluacion final, matriz de confusion y reporte por clase.
- `templates/`: interfaz web del dashboard Flask.
- `docs/`: material del curso, bitacoras e insumos del informe.

## Requisitos

- Python 3.10 o superior
- Windows, macOS o Linux
- Dataset local con imagenes etiquetables como `cats`, `dogs` y `panda`

Dependencias principales:

- `torch`
- `numpy`
- `Pillow`
- `Flask`
- `rich`

## Instalacion

```powershell
cd D:\GitHub\Universidad\Inteligencia_Artificial
python -m venv .venv
.\.venv\Scripts\activate
python -m pip install -r requirements.txt
```

## Flujo recomendado

### Fase 1: inspeccion del dataset

```powershell
.\.venv\Scripts\python.exe -m core.phase1 --dataset-root gatos_perros_pandas
.\.venv\Scripts\python.exe -m core.phase1 --dataset-root gatos_perros_pandas --gpu-test --matmul-size 512
```

### Fase 2: preparacion y split

```powershell
.\.venv\Scripts\python.exe -m core.phase2 --dataset-root gatos_perros_pandas --output-dir phase2_artifacts --dedupe-mode sha1 --verify-dataloaders --batch-size 16 --debug
```

### Fase 3: entrenamiento

```powershell
.\.venv\Scripts\python.exe -m core.phase3_train --manifest-csv phase2_artifacts/dataset_split.csv --output-dir phase3_artifacts --epochs 5 --batch-size 32 --image-size 128 --seed 42 --debug
```

### Fase 3: inferencia de una imagen

```powershell
.\.venv\Scripts\python.exe -m core.phase3_infer --image-path gatos_perros_pandas\animals\cats\cats_00001.jpg --checkpoint-path phase3_artifacts\best_checkpoint.pt --image-size 128
```

### Fase 3: evidencia automatica

```powershell
.\.venv\Scripts\python.exe -m core.phase3_evidence --manifest-csv phase2_artifacts/dataset_split.csv --checkpoint-path phase3_artifacts\best_checkpoint.pt --output-dir phase3_artifacts --split test --samples-per-class 1 --image-size 128
```

### Fase 4: evaluacion

```powershell
.\.venv\Scripts\python.exe -m core.phase4_evaluate --manifest-csv phase2_artifacts/dataset_split.csv --checkpoint-path phase3_artifacts\best_checkpoint.pt --output-dir phase4_artifacts --split test --image-size 128
```

## Dashboard web

```powershell
.\.venv\Scripts\python.exe app.py
```

Luego abre `http://127.0.0.1:5000/dashboard`.

El dashboard permite:

- ver resumen del dataset y del entrenamiento
- consultar metricas y graficos
- generar evidencia de clasificacion
- correr las fases en segundo plano
- subir una imagen para inferencia

## Artefactos esperados

- `phase2_artifacts/dataset_split.csv`
- `phase3_artifacts/best_checkpoint.pt`
- `phase3_artifacts/metrics_history.json`
- `phase4_artifacts/metrics_test.json`
- `phase4_artifacts/confusion_matrix_test.csv`
- `phase4_artifacts/classification_report_test.csv`

## Notas tecnicas

- La fase 2 deduplica por contenido en modo `sha1`, no solo por nombre.
- La fase 3 usa semilla para `random`, `numpy` y PyTorch para mejorar reproducibilidad.
- La app web resuelve checkpoints relativos respecto a la carpeta del proyecto.
- Si no hay backend GPU disponible, el proyecto funciona en CPU.
