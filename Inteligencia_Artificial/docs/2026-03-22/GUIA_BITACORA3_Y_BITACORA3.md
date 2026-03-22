# Guia para la Bitacora 3 y Base Editable

## Que debe cubrir la Bitacora 3

Segun el cronograma del curso, el avance 3 corresponde a:

- `Requerimiento C`
- `Bitacora 3`

Para el equipo 2, el Requerimiento C es:

`Clasifique 3 tipos de objetos`

Por eso la Bitacora 3 debe documentar el trabajo realizado para dejar evidencia de que el sistema clasifica:

- `cats`
- `dogs`
- `panda`

## Que conviene incluir

- fecha del avance;
- integrantes;
- objetivo del avance;
- tareas asignadas por integrante;
- trabajo realizado;
- evidencia tecnica;
- resultados observados;
- estado del avance;
- bloqueos o notas;
- proximos pasos;
- firmas.

## Evidencias tecnicas recomendadas

- `core/constants.py`: clases definidas.
- `core/phase3_infer.py`: inferencia por imagen.
- `app.py`: endpoint de prediccion.
- `phase3_artifacts/best_checkpoint.pt`: checkpoint del modelo.
- capturas o pruebas de clasificacion sobre una imagen de cada clase.

## Enfoque de redaccion sugerido

La Bitacora 3 no debe describir solo que "el modelo existe". Debe explicar especificamente que:

- el sistema fue preparado para trabajar con 3 clases;
- la inferencia devuelve una clase predicha;
- se probaron las clases requeridas por el proyecto;
- el avance deja listo el sistema para luego medir metricas.

## Plantilla resumida

1. Encabezado del proyecto y avance.
2. Objetivo del avance.
3. Distribucion de tareas.
4. Trabajo realizado.
5. Evidencias.
6. Resultados.
7. Estado del avance.
8. Bloqueos.
9. Proximos pasos.
10. Firmas.

---

# Bitacora 3 Lista para Adaptar

Puedes copiar este contenido a un documento formal o usarlo como version base.

## BITACORA 3 - PROYECTO

**Curso:** Inteligencia Artificial Aplicada  
**Proyecto:** Software que identifique objetos basicos en imagenes mediante redes neuronales  
**Equipo:** 2  
**Fecha de entrega del avance:** 27/03/2026  
**Avance correspondiente:** Requerimiento C y Bitacora 3

## Integrantes

- Miguel Alejandro Fernandez Arteaga
- Jasser Rigoberto Reyes Salazar

## Objetivo del avance

Documentar el trabajo realizado para cumplir con el Requerimiento C del proyecto, enfocado en demostrar que el sistema clasifica tres tipos de objetos en imagenes: `cats`, `dogs` y `panda`, utilizando el modelo entrenado en la fase anterior.

## Distribucion de tareas

### Miguel Alejandro Fernandez Arteaga

- Verificar que las clases del sistema quedaran definidas correctamente.
- Probar la inferencia del modelo entrenado con imagenes de entrada.
- Validar que la salida del sistema devolviera una clase predicha y probabilidades por clase.
- Consolidar evidencia tecnica del flujo de clasificacion.

### Jasser Rigoberto Reyes Salazar

- Revisar la documentacion del avance.
- Organizar la descripcion formal del Requerimiento C para el informe.
- Verificar que la evidencia del proyecto demostrara la clasificacion de las tres clases requeridas.
- Preparar la estructura de la Bitacora 3 para entrega.

## Trabajo realizado

### Implementacion tecnica verificada

- Se confirmo que el proyecto trabaja con tres clases definidas en el dominio del problema: `cats`, `dogs` y `panda`.
- Se verifico que el modelo entrenado puede utilizarse en inferencia mediante un script dedicado.
- Se valido que el sistema recibe una imagen, la preprocesa, ejecuta la prediccion y devuelve la clase con mayor probabilidad.
- Se confirmo que la aplicacion Flask tambien integra el flujo de prediccion mediante carga de imagen.

### Evidencia del Requerimiento C

- Definicion de clases del proyecto:
  - Archivo: `core/constants.py`
  - Clases configuradas: `cats`, `dogs`, `panda`
- Script de inferencia:
  - Archivo: `core/phase3_infer.py`
  - Funcion: cargar imagen, aplicar preprocesamiento y devolver clase predicha
- Integracion en interfaz:
  - Archivo: `app.py`
  - Endpoint: `/api/predict-upload`
- Artefacto utilizado para clasificacion:
  - Archivo: `phase3_artifacts/best_checkpoint.pt`

## Resultados obtenidos

El proyecto cuenta con un flujo funcional de clasificacion de imagenes para tres tipos de objetos. La configuracion actual del sistema permite identificar las clases `cats`, `dogs` y `panda`, tanto desde script como desde la interfaz web.

Resumen del avance:

- Se verificaron las tres clases objetivo del proyecto.
- Se dejo listo el proceso de inferencia del modelo entrenado.
- El sistema devuelve una prediccion final y probabilidades por clase.
- El requerimiento queda sustentado tecnicamente mediante codigo y artefactos del repositorio.

## Estado del avance

**Estado general:** Completado

**Conclusiones del avance:**

- El Requerimiento C queda cubierto tecnicamente porque el sistema ya clasifica tres tipos de objetos.
- La evidencia del proyecto demuestra coherencia entre dataset, clases configuradas, modelo entrenado e inferencia.
- Este avance deja preparada la base para el siguiente paso, correspondiente a la evaluacion automatica mediante metricas.

## Bloqueos y observaciones

- Hace falta incorporar capturas o ejemplos de prediccion dentro del documento formal para fortalecer la evidencia visual del avance.
- Conviene mantener rutas portables en los artefactos para evitar dependencia de un entorno especifico.
- La version final debe adaptarse al formato oficial solicitado por el curso y contener las firmas del equipo.

## Proximos pasos

- Integrar el texto del Requerimiento C en el informe final.
- Agregar evidencia visual de predicciones sobre las tres clases.
- Continuar con el Requerimiento D, correspondiente a metricas de evaluacion.

## Firmas

- Miguel Alejandro Fernandez Arteaga: ______________________
- Jasser Rigoberto Reyes Salazar: ______________________
