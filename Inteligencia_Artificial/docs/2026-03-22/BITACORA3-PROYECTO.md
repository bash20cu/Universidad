# BITACORA 3 - PROYECTO

**Curso:** Inteligencia Artificial Aplicada  
**Proyecto:** Software que identifique objetos basicos en imagenes mediante redes neuronales  
**Equipo:** 2  
**Fecha de entrega del avance:** 27/03/2026  
**Avance correspondiente:** Requerimiento C y Bitacora 3

## Integrantes

- Miguel Alejandro Fernandez Arteaga
- Jasser Rigoberto Reyes Salazar

## Objetivo del avance

Documentar el trabajo realizado para cumplir con el Requerimiento C del proyecto, demostrando que el sistema clasifica tres tipos de objetos en imagenes: `cats`, `dogs` y `panda`, a partir del modelo entrenado en la fase anterior.

## Distribucion de tareas

### Miguel Alejandro Fernandez Arteaga

- Verificar la configuracion de las clases del sistema.
- Ejecutar pruebas de inferencia con imagenes de entrada.
- Validar la salida del modelo y la prediccion final.
- Consolidar la evidencia tecnica del flujo de clasificacion.

### Jasser Rigoberto Reyes Salazar

- Revisar la documentacion del avance.
- Redactar la descripcion formal del Requerimiento C para el informe.
- Verificar que la evidencia muestre las tres clases requeridas.
- Preparar la bitacora para entrega.

## Trabajo realizado

### Implementacion tecnica verificada

- Se confirmo que el sistema trabaja con tres clases objetivo: `cats`, `dogs` y `panda`.
- Se verifico el uso del checkpoint entrenado para ejecutar inferencia sobre imagenes nuevas.
- Se reviso el script de inferencia encargado de cargar la imagen, preprocesarla y devolver la clase predicha.
- Se comprobo la integracion del flujo de prediccion dentro de la aplicacion Flask.

### Evidencia del Requerimiento C

- Definicion de clases:
  - Archivo: `core/constants.py`
  - Clases definidas: `cats`, `dogs`, `panda`
- Script de inferencia:
  - Archivo: `core/phase3_infer.py`
  - Salida: clase predicha y probabilidades por clase
- Integracion web:
  - Archivo: `app.py`
  - Endpoint de prediccion: `/api/predict-upload`
- Artefacto del modelo:
  - Archivo: `phase3_artifacts/best_checkpoint.pt`

## Resultados obtenidos

El sistema cuenta con un flujo funcional para clasificar imagenes en tres tipos de objetos. La estructura actual del proyecto permite utilizar el modelo entrenado para recibir una imagen de entrada y devolver la etiqueta mas probable entre `cats`, `dogs` y `panda`.

Como evidencia automatica del avance, se ejecuto el proceso `core.phase3_evidence` sobre el conjunto `test`, tomando una muestra por clase. Los resultados obtenidos fueron:

| Clase real | Prediccion | Confidence | Correcta |
|---|---|---:|---|
| cats | dogs | 0.4827 | No |
| dogs | dogs | 0.5035 | Si |
| panda | panda | 0.7792 | Si |

Artefactos generados:

- `phase3_artifacts/classification_examples_test.json`
- `phase3_artifacts/classification_examples_test.csv`

Resumen del avance:

- Se verificaron las tres clases del proyecto.
- Se dejo validado el flujo de clasificacion por inferencia.
- El sistema genera una salida interpretable mediante prediccion y probabilidades.
- El Requerimiento C queda sustentado tecnicamente con codigo y artefactos del repositorio.

## Estado del avance

**Estado general:** Completado

**Conclusiones del avance:**

- El proyecto ya clasifica tres tipos de objetos, en concordancia con el enunciado del equipo 2.
- Existe coherencia entre las clases definidas, el entrenamiento previo y el proceso de inferencia.
- El avance deja lista la base tecnica para la siguiente etapa de evaluacion con metricas.

## Bloqueos y observaciones

- Se recomienda agregar capturas o ejemplos concretos de predicciones al documento formal para fortalecer la evidencia del avance.
- Conviene normalizar rutas de artefactos para mejorar portabilidad entre entornos.
- La version final debe adaptarse al formato oficial del curso y llevar las firmas correspondientes.
- En la muestra de evidencia, la clase `cats` presento confusion con `dogs`, lo cual debe comentarse en resultados y limitaciones tecnicas.

## Proximos pasos

- Incorporar el Requerimiento C en el informe final.
- Adjuntar evidencia visual de clasificacion para las tres clases.
- Continuar con el Requerimiento D y la documentacion de metricas.

## Firmas

- Miguel Alejandro Fernandez Arteaga: ______________________
- Jasser Rigoberto Reyes Salazar: ______________________
