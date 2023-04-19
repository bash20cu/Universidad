package donkey_kong.controlador;

import donkey_kong.modelo.Nodo;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Esta clase representa un iterador para la ListaArreglo. Implementa la
 * interfaz Iterator para poder recorrer los elementos de la lista. Contiene una
 * referencia a la ListaArreglo a la que pertenece y a los nodos actual y
 * anterior para recorrer la lista.
 */
public class IteradorListaArreglo<T> implements Iterator<T> {

    private ListaArreglo<T> lista;
    private Nodo<T> actual;
    private Nodo<T> anterior;

    /**
     * Constructor de la clase. Recibe como parámetro una instancia de
     * ListaArreglo y se inicializan los nodos actual y anterior con los valores
     * correspondientes.
     *
     * @param lista La lista de la que se quiere crear el iterador.
     */
    public IteradorListaArreglo(ListaArreglo<T> lista) {
        this.lista = lista;
        this.actual = lista.getCabeza();
        this.anterior = null;
    }

    /**
     * Método que indica si existe un siguiente elemento en la lista.
     *
     * @return true si hay un siguiente elemento, false en caso contrario.
     */
    public boolean hasNext() {
        return actual != null;
    }

    /**
     * Método que devuelve el siguiente elemento de la lista y avanza el cursor
     * al siguiente nodo.
     *
     * @return el siguiente elemento de la lista.
     * @throws NoSuchElementException si no hay más elementos en la lista.
     */
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        T elemento = actual.getDato();
        actual = actual.getSiguiente();
        return elemento;
    }
}
