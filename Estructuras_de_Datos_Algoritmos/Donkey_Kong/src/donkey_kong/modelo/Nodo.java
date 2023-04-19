package donkey_kong.modelo;

/**
 * Clase que representa un nodo de una lista enlazada.
 *
 * @param <T> tipo de dato que almacenará el nodo.
 */
public class Nodo<T> {

    private T dato;
    private Nodo<T> siguiente;

    /**
     * Constructor de la clase Nodo.
     *
     * @param dato objeto que se almacenará en el nodo.
     */
    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    /**
     * Obtiene el objeto almacenado en el nodo.
     *
     * @return objeto almacenado en el nodo.
     */
    public T getDato() {
        return dato;
    }

    /**
     * Establece el objeto que se almacenará en el nodo.
     *
     * @param dato objeto que se almacenará en el nodo.
     */
    public void setDato(T dato) {
        this.dato = dato;
    }

    /**
     * Obtiene el siguiente nodo en la lista enlazada.
     *
     * @return siguiente nodo en la lista enlazada.
     */
    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    /**
     * Establece el siguiente nodo en la lista enlazada.
     *
     * @param siguiente siguiente nodo en la lista enlazada.
     */
    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }
}
