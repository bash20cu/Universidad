# Estudiante de Ingeniería de Software
# en la Universidad Internacional de las Américas Costa Rica.

<!--START_SECTION:badges-->
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
![JavaScript](https://img.shields.io/badge/javascript-%23323330.svg?style=for-the-badge&logo=javascript&logoColor=%23F7DF1E)
![jQuery](https://img.shields.io/badge/jquery-%230769AD.svg?style=for-the-badge&logo=jquery&logoColor=white)
![NetBeans IDE](https://img.shields.io/badge/NetBeansIDE-1B6AC6.svg?style=for-the-badge&logo=apache-netbeans-ide&logoColor=white)
![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)

![GitHub language count](https://img.shields.io/github/languages/count/bash20cu/Universidad?style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/bash20cu/Universidad?style=for-the-badge)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/bash20cu/Universidad?style=for-the-badge)
![GitHub last commit](https://img.shields.io/github/last-commit/bash20cu/Universidad?style=for-the-badge)

[![LinkedIn](https://img.shields.io/badge/linkedin-%230077B5.svg?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/miguel1990/)
[![Portfolio](https://img.shields.io/badge/Portfolio-%23000000.svg?style=for-the-badge&logo=firefox&logoColor=#FF7139)](https://bash20cu.github.io/Portfolio/)
<!--END_SECTION:badges-->

## Estrucuturas de datos y Algoritmos - Java

### Laboratorios:
#### Laboratorio 3  [archivos](https://github.com/bash20cu/Universidad/tree/main/Estructuras_de_Datos_Algoritmos/Laboratorio_3)
#### ORDENAMIENTO POR CUBETAS

    Un ordenamiento por cubetas empieza con un arreglo unidimensional  de enteros positivos a ordenar, 
    y un arreglo bidimensional de enteros con filas cuyos índices  se encuentran entre el 0 y el 9, 
    y columnas con índices entre el 0 y el n – 1, en donde n es el numero de valores a ordenar. 
    A cada fila del arreglo bidimensional se le conoce como cubeta. 
    Escriba un programa que contenga un método llamado ordenarCubetas, el cual tome un arreglo entero
     como argumento y se ejecute de la siguiente manera:

    1. Que coloque cada valor del arreglo unidimensional en una fila del arreglo en cubetas, con base 
    en el valor del digito de las unidades. Por ejemplo, 97 se coloca en la fila 7, 3 se coloca en la 
    fila 3 y 100 se coloca en la fila 0. A este procedimiento se le conoce como un “paso de distribución”
    
    2. Que itere a través del arreglo en cubetas fila por fila, y copie los valores de vuelta al arreglo 
    original. A este procedimiento se le conoce como un “paso de recolección”. El nuevo orden de los valores 
    anteriores en el arreglo unidimensional es 100, 3 y 97.
    
    3. Que repita este proceso para cada posición de los dígitos subsiguientes (decenas, cientos, miles, etcétera).
     En la segunda pasada (digito de decenas), el 100 se coloca en la fila 0, el 3 en la fila 0 
     (porque 3 no tiene dígitos de decenas) y el 97 se coloca en la fila 9. Después del paso de recolección,
      el orden de los valores en el arreglo unidimensional es 100, 3 y 97. En la tercera pasada (digito de centenas),
       el 100 se coloca en la fila 1, el 3 se coloca en la fila 0 y el 97 en la fila 0 (después del 3). 
       Después de este último paso de recolección, el arreglo original queda ordenado.
    
    Al finalizar el programa se debe imprimir el vector original y el vector ordenado.