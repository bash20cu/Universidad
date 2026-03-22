# BITACORA 2 - PROYECTO

**Curso:** Inteligencia Artificial Aplicada  
**Proyecto:** Software que identifique objetos basicos en imagenes mediante redes neuronales  
**Equipo:** 2  
**Fecha de entrega del avance:** 13/03/2026  
**Avance correspondiente:** Requerimiento B y Bitacora 2

## Integrantes

- Miguel Alejandro Fernandez Arteaga
- Jasser Rigoberto Reyes Salazar

## Objetivo del avance

Documentar el trabajo realizado para cumplir con el Requerimiento B del proyecto: implementar una red neuronal basica funcional para clasificacion de imagenes y dejar evidencia del entrenamiento realizado con el dataset del proyecto.

## Distribucion de tareas

### Miguel Alejandro Fernandez Arteaga

- Implementar la arquitectura de la red neuronal convolucional basica.
- Preparar el script de entrenamiento con parametros configurables.
- Integrar la carga del manifest generado en la fase anterior.
- Guardar checkpoints del modelo y metricas historicas por epoca.
- Conectar la ejecucion del entrenamiento desde la interfaz Flask.

### Jasser Rigoberto Reyes Salazar

- Revisar el avance del requerimiento y validar que la salida del entrenamiento quedara documentada.
- Apoyar en la organizacion de la evidencia tecnica para el entregable.
- Verificar los artefactos generados para incluirlos en la documentacion del proyecto.

## Trabajo realizado

### Implementacion tecnica completada

- Se implemento una CNN basica en `core/model.py`.
- La arquitectura definida utiliza capas convolucionales, activacion ReLU, pooling y una capa final para clasificacion multiclase.
- Se configuro el entrenamiento en `core/phase3_train.py`.
- El entrenamiento permite ajustar parametros basicos como:
  - `epochs`
  - `batch_size`
  - `lr`
  - `weight_decay`
  - `image_size`
  - `num_workers`
  - `seed`
- Se agrego seleccion automatica de dispositivo (`cuda`, `directml` o `cpu`).
- Se guardan artefactos del entrenamiento en `phase3_artifacts/`.
- Se dejo disponible la ejecucion del entrenamiento desde la aplicacion en `app.py`.

### Evidencia del requerimiento B

- Arquitectura base del modelo:
  - Archivo: `core/model.py`
  - Funcion: `build_simple_cnn`
- Script de entrenamiento:
  - Archivo: `core/phase3_train.py`
  - Incluye lectura del manifest, dataloaders, entrenamiento por epocas, validacion y guardado de checkpoints
- Integracion en interfaz:
  - Archivo: `app.py`
  - Tarea expuesta: `phase3_train`
- Historial de entrenamiento generado:
  - Archivo: `phase3_artifacts/metrics_history.json`
- Checkpoints generados:
  - `phase3_artifacts/best_checkpoint.pt`
  - `phase3_artifacts/last_checkpoint.pt`

## Resultados obtenidos

Se cuenta con una red neuronal basica implementada y entrenada con los datos cargados del proyecto. El historial guardado muestra ejecucion por 5 epocas y evidencia que el modelo fue entrenado correctamente.

Resumen de resultados observados:

- Mejor `val_acc`: `0.5778`
- Ultima `train_acc`: `0.5767`
- Dataset de trabajo preparado en manifest para 3 clases:
  - `cats`
  - `dogs`
  - `panda`

## Estado del avance

**Estado general:** Completado

**Conclusiones del avance:**

- El Requerimiento B queda cubierto a nivel tecnico porque el proyecto ya incluye una red neuronal basica funcional.
- Tambien se cumple la parte de ajuste de parametros basicos en al menos un algoritmo, ya que el entrenamiento expone hiperparametros configurables.
- Queda pendiente trasladar este contenido al documento formal en formato solicitado por el curso, junto con las firmas del equipo en la version final.

## Bloqueos y observaciones

- Los artefactos actuales fueron generados originalmente en un entorno Windows, por lo que algunas rutas absolutas almacenadas en los archivos apuntan a `D:\\projects\\...`.
- Esto no impide usar la evidencia para el avance, pero conviene normalizar rutas en una siguiente revision para mejorar portabilidad.
- La bitacora final entregada al docente debe respetar el formato oficial subido al e-campus y llevar las firmas de todos los integrantes.

## Proximos pasos

- Preparar el texto formal del Requerimiento B para el documento escrito.
- Ajustar o regenerar artefactos con rutas portables si el docente pide reproducibilidad local.
- Continuar con el Requerimiento C y la Bitacora 3.

## Firmas

- Miguel Alejandro Fernandez Arteaga: ______________________
- Jasser Rigoberto Reyes Salazar: ______________________
