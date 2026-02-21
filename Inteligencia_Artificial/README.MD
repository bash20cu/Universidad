Dataset recomendado (3 clases “limpias” y directas)
1) Rock–Paper–Scissors (Piedra/Papel/Tijera)

3 clases exactas (rock, paper, scissors), ideal para comenzar y demostrar resultados rápido.
Por qué sirve: clasificación simple, carpetas por clase, muy usado en ejemplos de CNN.

Alternativas (también 3 clases)
2) Animal Image Dataset: Cats, Dogs, and Foxes (3 clases)

Dataset pequeño (útil si tu compu no es potente).

3) Animal Image Dataset: Dog, Cat and Panda (3 clases)

También directo y claro (perro/gato/panda).

Cómo cumple tu proyecto (A–E)
A) Diseñe un software que procese imágenes

Módulos mínimos del “software”:

Carga de dataset (leer carpetas por clase).

Preprocesado: resize (p.ej. 128×128), normalización [0,1].

Aumento de datos: rotación leve, flips, zoom (para generalizar).

Entrenamiento / Validación / Test con split (p.ej. 70/15/15).

Inferencia: una función predict(image) que devuelva clase + probabilidad.

Reporte: guardar métricas + matriz de confusión.

B) Implemente una red neuronal básica (Deep Learning)

Arquitectura CNN simple (suficiente para “básica”):

Conv(32) → ReLU → MaxPool

Conv(64) → ReLU → MaxPool

Flatten → Dense(128) → Dropout(0.5)

Dense(3) con Softmax

C) Clasifique 3 tipos de objetos

Usa cualquiera de los datasets anteriores:

RPS: {rock, paper, scissors}

Animales: {cat, dog, fox}

Animales: {dog, cat, panda}

D) Genere 3 métricas de precisión

Tres métricas típicas y aceptadas (multiclase):

Accuracy (exactitud global)

Precision (macro) (promedio de precisión por clase)

F1-score (macro) (balance entre precision y recall)

(Opcional para fortalecer el informe: matriz de confusión y recall macro.)

E) Documente 5 limitaciones técnicas (ejemplos listos para tu reporte)

Variación de iluminación/fondo: cambia mucho la apariencia del objeto y baja el desempeño fuera del dataset.

Overfitting si el dataset es pequeño: la red memoriza en vez de generalizar (se mitiga con augmentación y dropout).

Sesgo del dataset / domain shift: si las fotos reales difieren (ángulos/cámaras), el modelo falla en producción.

Requerimientos de cómputo: entrenar CNN en CPU es lento; con GPU mejora, pero no siempre disponible.

Calidad/ruido en datos: imágenes borrosas, recortes malos o etiquetas imperfectas degradan métricas.