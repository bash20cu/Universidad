package laboratorio.pkg3;

/**
 * @author migue
 * 
 */
public class OrdenamientoPorCubetas extends Ordenamiento{

    // Método que ordena un arreglo unidimensional usando cubetas
    public static int[] ordenarCubetas(int[] numeros) {
        
        // Obtener el número de elementos a ordenar
        int cantidadElementos = numeros.length;
        
        // Crear el arreglo bidimensional de cubetas con 10 filas y cantidadElementos columnas
        int[][] cubetas = new int[10][cantidadElementos];
        
        // Obtener el número máximo de dígitos de los elementos a ordenar
        int maxDigitos = contarDigitos(maximo(numeros));
        
        // Recorrer cada posición de los dígitos desde las unidades hasta el máximo
        for (int d = 0; d < maxDigitos; d++) {
           
            // Recorrer el arreglo unidimensional y asignar cada elemento a una cubeta según su valor en la posición actual del dígito
            for (int i = 0; i < cantidadElementos; i++) {
                
                // Obtener el valor del elemento
                int valor = numeros[i];
                
                // Obtener el valor del dígito en la posición actual
                int digito = obtenerDigito(valor, d);                
                
                // Insertar el elemento en la primera posición vacía de la cubeta correspondiente al valor del dígito
                for (int j = 0; j < cantidadElementos; j++) { 
                    if (cubetas[digito][j] == 0) {
                        cubetas[digito][j] = valor;
                        break;
                    }
                }
            }
            
            // Recorrer las cubetas en orden ascendente y copiar los valores de vuelta al arreglo original
            int k = 0; // Índice para el arreglo original
            System.out.print("Progreso de ordenamiento en el vector numeros: ");
            for (int i = 0; i < 10; i++) {                  
                for (int j = 0; j < cantidadElementos; j++) {                  
                    if (cubetas[i][j] != 0) {
                        numeros[k] = cubetas[i][j];                        
                        System.out.print(" "+numeros[k]+" ");
                        k++;
                        // Vaciar la cubeta para la siguiente iteración
                        cubetas[i][j] = 0;
                    }                  
                }
            }
            System.out.println("\n");
        }
        // Devolver el arreglo ordenado
        return numeros;
    }  
}