# Requerimiento C Listo para Informe

## Requerimiento C. Clasifique 3 tipos de objetos

El sistema desarrollado fue configurado para clasificar imagenes en tres categorias definidas por el proyecto: `cats`, `dogs` y `panda`. Estas clases se encuentran integradas en la configuracion central del sistema y son utilizadas tanto en el proceso de entrenamiento como en la fase de inferencia. De esta manera, el modelo mantiene coherencia entre el dataset empleado, las etiquetas del dominio y la salida final mostrada al usuario.

Para cumplir este requerimiento se implemento un flujo de clasificacion que recibe una imagen de entrada, la convierte al formato RGB, la redimensiona al tamano de trabajo y la normaliza antes de enviarla al modelo entrenado. Posteriormente, la red neuronal convolucional genera probabilidades por clase y el sistema selecciona como resultado final la categoria con mayor probabilidad. Este flujo se encuentra disponible tanto mediante script de inferencia como desde la interfaz web del proyecto.

La evidencia tecnica del requerimiento se sustenta en los siguientes componentes del sistema:

- `core/constants.py`: define las clases `cats`, `dogs` y `panda`.
- `core/phase3_infer.py`: ejecuta inferencia sobre una imagen individual.
- `core/phase3_evidence.py`: genera evidencia automatica de clasificacion para las tres clases.
- `app.py`: integra la prediccion desde la aplicacion Flask.
- `phase3_artifacts/best_checkpoint.pt`: checkpoint del modelo entrenado.

Con el fin de dejar una evidencia reproducible del cumplimiento del requerimiento, se ejecuto un proceso automatico de verificacion sobre el conjunto `test`, seleccionando una muestra por cada clase. Los resultados obtenidos fueron los siguientes:

| Clase real | Prediccion | Confidence | Correcta |
|---|---|---:|---|
| cats | dogs | 0.4827 | No |
| dogs | dogs | 0.5035 | Si |
| panda | panda | 0.7792 | Si |

Los artefactos generados por esta prueba fueron:

- `phase3_artifacts/classification_examples_test.json`
- `phase3_artifacts/classification_examples_test.csv`

Los resultados muestran que el sistema efectivamente realiza clasificacion sobre las tres categorias requeridas por el proyecto. En la muestra utilizada, las clases `dogs` y `panda` fueron identificadas correctamente, mientras que una imagen de la clase `cats` fue confundida con `dogs`. Este comportamiento es consistente con el desempeno general observado en la evaluacion posterior del modelo, donde existe mayor dificultad para separar algunas imagenes de gatos y perros debido a similitudes visuales entre ambas clases.

En conclusion, el Requerimiento C se considera cumplido a nivel tecnico, ya que el software clasifica tres tipos de objetos definidos por el proyecto y cuenta con evidencia automatica, reproducible y documentable para sustentar dicho funcionamiento.
