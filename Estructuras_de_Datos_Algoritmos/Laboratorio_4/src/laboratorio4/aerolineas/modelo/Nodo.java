package laboratorio4.aerolineas.modelo;

/**
 *
 * @author miguel
 * Clase Nodo, especializada en el nodo de la lista.
 */
public class Nodo {
    //Variables
    //Objeto que contiene los datos de Pasajero
    private Pasajero pasajero;
    //Puntero al siguiente nodo
    private Nodo siguiente;
    
    /**Constructor
    @param pasajero, Objeto que contiene la informacion del pasajero,
        [String - Nombre,String - Cedula,String - Destino]
     */
    public Nodo(Pasajero pasajero) {
        this.pasajero = pasajero;
        //inicializamos el nodo apuntando a Nulo.
        //este no tiene ningún nodo siguiente en la lista aun.
        this.siguiente = null;
    }
    
    //Metodos
    //Metodo para obtener el pasajero
    public Pasajero getPasajero() {
        return this.pasajero;
    }
    
    //metodo para saber el siguiente nodo
    public Nodo getSiguiente() {
        return siguiente;
    }
    //Metodo para guardar el siguiente nodo.
    public void setSiguiente(Nodo siguiente) {
        this.siguiente = siguiente;
    }
}
