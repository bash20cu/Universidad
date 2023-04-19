package laboratorio.pkg3;

/**
 *
 * @author migue
 */
public class Ordenamiento {  
    
    // Método que obtiene el valor de un dígito en una posición determinada de un número entero
    protected static int obtenerDigito(int numero, int posicion) {
        System.out.println("Fila: "+posicion+" Numero : "+numero+", Columna: "+(numero / (int) Math.pow(10, posicion)) % 10);
        return (numero / (int) Math.pow(10, posicion)) % 10;
    }
    
    // Método que cuenta el número de dígitos de un número entero
    protected static int contarDigitos(int numero) {
        System.out.println("Cantidad de digitos del numero mayor es: "+String.valueOf(numero).length()+"\n");
        return String.valueOf(numero).length();        
    }
    
     // Método que obtiene el máximo valor de un arreglo unidimensional
    protected static int maximo(int[] arr) {
        int maximo = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > maximo) {
                maximo = arr[i];
            }
        }
        System.out.println("El numero mayor es: "+maximo);
        return maximo;
    }
    
    //Metodo para imprimir un arreglo
    protected static void imprimirArreglo(int[]valores){
        
      System.out.print("[");
      //Iteramos en el arreglo recibido, para mostrar cada valor
      for(int x=0;x<valores.length;x++){
         System.out.print(valores[x]);
         if(x!=valores.length-1){
           System.out.print(",");
         }else{
           System.out.print("]");
         } 
      }      
      System.out.println();
   } 
    
    
}
