# Laboratorio 10: Optimización y Performance (Índices)

En un documento .sql escrito en MS SQL Server y utilizando la herramienta SSMS, se tiene que
documentar cada una de las instrucciones DQL que se solicitan a continuación:

1. Hacer una consulta DQL que busque por nombre, apellido1 y apellido2. Debe devolver
   como resultado la cédula, nombre, apellido1, apellido2.
2. Analizar la consulta en términos de rendimiento y contestar si es eficiente o no y del por
   qué.
3. Construir un índice llamado ix_nombre que use la columna nombre, otro índice llamado
   ix_apellido1 que use la columna apellido1, otro índice llamado ix_apellido2 que use la
   columna apellido2.
4. Analice el plan de ejecución con base en las métricas vistas en clase.
5. Cambie en algo si se elimina el índice ix_apellido2. Explique su repuesta.
6. Elimine los índices ix_nombre, ix_apellido1, ix_apellido2.
   Universidad Internacional de las Américas
   1
7. Construir un índice llamado ix_nombrecompleto que use las columnas nombre,
   apellido1, y apellido2.
8. Analice el plan de ejecución con base en las métricas vistas en clase.
9. ¿Cuál de los dos escenarios (punto 4 y punto 7) es mejor en términos de rendimiento?
10. ¿Cuál es la diferencia entre un índice agrupado y uno no agrupado?


## 🛠 Tecnologías
![MicrosoftSQLServer](https://img.shields.io/badge/Microsoft%20SQL%20Server-CC2927?style=for-the-badge&logo=microsoft%20sql%20server&logoColor=white)
