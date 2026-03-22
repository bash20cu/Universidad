# Analisis del Informe Actual y Faltantes

## Archivo revisado

- `Informe Inteligencia Artificial Aplicada.docx`

## Estado general

El informe actual tiene una base util para el documento final: portada, introduccion, plan por fases, arquitectura general y una seccion de limitaciones. Sin embargo, todavia no esta alineado de forma completa con la evaluacion del proyecto segun los requerimientos `A, B, C, D y E` del equipo 2.

## Lo que ya esta bien encaminado

- La introduccion define con claridad el objetivo del proyecto: clasificar imagenes en `cats`, `dogs` y `panda`.
- Se explica el trabajo por fases y la arquitectura general del sistema.
- Se documenta la existencia del modelo CNN y de la etapa de evaluacion.
- Ya aparecen las 5 limitaciones tecnicas en forma preliminar.

## Lo que falta o debe mejorarse

### 1. Requerimiento A no esta documentado como requerimiento formal

Hace falta una seccion explicita que demuestre:

- como se carga el dataset;
- que formato se usa;
- como se manejan errores basicos;
- cuantos registros se cargan;
- evidencia del preprocesamiento aplicado.

### 2. Requerimiento B esta mencionado, pero no justificado con suficiente detalle

Hace falta explicar mejor:

- la arquitectura de la CNN;
- las capas principales;
- los hiperparametros usados;
- por que se considero una red neuronal basica y funcional;
- que artefactos genera el entrenamiento.

### 3. Requerimiento C no aparece como seccion propia

Para el equipo 2, el Requerimiento C es:

`Clasifique 3 tipos de objetos`

Esto debe aparecer como una seccion independiente con evidencia clara de que el sistema clasifica:

- `cats`
- `dogs`
- `panda`

Tambien hace falta incluir:

- referencia a las clases definidas;
- evidencia de inferencia o predicciones;
- explicacion del flujo de clasificacion;
- relacion entre dataset, entrenamiento e inferencia.

### 4. Requerimiento D esta adelantado tecnicamente, pero no integrado al informe

El proyecto ya tiene metricas en los artefactos de `phase4_artifacts`, pero el informe todavia no presenta esta informacion como analisis formal.

Hace falta incorporar:

- tabla de metricas globales;
- explicacion de `accuracy`, `precision`, `recall` y `f1`;
- interpretacion de resultados;
- lectura breve de la matriz de confusion;
- conclusion sobre cual clase fue mejor y peor clasificada.

### 5. Requerimiento E existe, pero debe pulirse

Las 5 limitaciones ya estan redactadas de forma preliminar. Aun asi, conviene convertirlas en una seccion mas academica:

- una limitacion por subtitulo;
- explicacion tecnica corta;
- impacto en resultados o despliegue;
- posible mejora futura.

### 6. Faltan secciones tipicas de un informe academico completo

Conviene agregar:

- objetivo general;
- objetivos especificos;
- descripcion del dataset;
- metodologia;
- resultados;
- conclusiones;
- recomendaciones;
- bibliografia o fuentes;
- anexos o evidencias.

### 7. Faltan evidencias visuales o tecnicas

Seria ideal incluir:

- capturas del dashboard;
- salida de inferencia;
- tabla con clases;
- tabla con metricas;
- referencia a archivos generados por el proyecto.

## Foco inmediato para el avance 3

El avance 3 del cronograma corresponde a:

- `Requerimiento C`
- `Bitacora 3`

Por lo tanto, para ese avance lo minimo recomendable es entregar:

- una seccion formal del Requerimiento C;
- evidencia de clasificacion de las 3 clases;
- una bitacora 3 completa;
- referencias a los archivos del proyecto que sustentan el avance.

## Evidencias del repositorio que si apoyan el informe

- `core/constants.py`: define las 3 clases del proyecto.
- `core/phase3_infer.py`: permite clasificar una imagen con el modelo entrenado.
- `app.py`: expone inferencia y ejecucion de fases desde Flask.
- `phase3_artifacts/best_checkpoint.pt`: checkpoint del modelo.
- `phase4_artifacts/metrics_test.json`: resultados de evaluacion.

## Recomendacion de trabajo

1. Completar primero el texto del Requerimiento C.
2. Preparar la `Bitacora 3`.
3. Reorganizar el informe final por requerimientos `A-E`.
4. Agregar resultados numericos y capturas.
5. Cerrar con conclusiones y limitaciones mejor redactadas.
