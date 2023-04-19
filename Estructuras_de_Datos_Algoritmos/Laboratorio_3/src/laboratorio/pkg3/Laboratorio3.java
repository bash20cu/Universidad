package laboratorio.pkg3;

import static laboratorio.pkg3.OrdenamientoPorCubetas.imprimirArreglo;
import static laboratorio.pkg3.OrdenamientoPorCubetas.ordenarCubetas;

/**
 * @author migue
 * @reference https://programandoandosite.wordpress.com/2016/01/19/ordenamientos-por-cubetas/
 * @reference https://es.stackoverflow.com/questions/7836/c%c3%b3mo-funciona-el-algoritmo-de-quicksort
 * @reference https://parzibyte.me/blog/2019/12/26/quicksort-java-ordenar-arreglos/
 */
public class Laboratorio3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {     
       
        // Arreglo desordenado    
       System.out.println("Arreglo desordenado:");
       //Escoja los numeros a ordenar
       int[] desordenado= {97,3,100,2005};
       imprimirArreglo(desordenado);
       
       // Arreglo ordenado     
       int[] ordenado=ordenarCubetas(desordenado);
       System.out.println("Arreglo ordenado:");
       imprimirArreglo(ordenado);
        
    }
    
}
