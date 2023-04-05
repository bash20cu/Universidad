package laboratorio4.aerolineas.vista;

/**
 *
 * @author miguel
 * 
 * Utilizar una lista simplemente enlazada para controlar la lista de pasajeros de una línea aérea. 
 * El programa debe ser controlado por menú y permitir al usuario visualizar los datos de un pasajero 
 * (nombre, cedula y destino), insertar un nodo (siempre al final)y eliminar un pasajero de la lista. 
 * A la lista se accede por dos punteros, una referencia al primer nodo y una al ultimo nodo.
 *  
 */
public class Laboratorio4Aerolineas {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ListaPasajeros listaPasajero = new ListaPasajeros();
        listaPasajero.iniciar();
    }
    
}
