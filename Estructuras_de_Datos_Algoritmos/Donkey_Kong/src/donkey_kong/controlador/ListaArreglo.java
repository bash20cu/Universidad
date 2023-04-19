package donkey_kong.controlador;

import donkey_kong.modelo.Nodo;
import java.util.Iterator;

/**
 *
 * Clase que representa una lista enlazada simple implementada con arreglo de
 * nodos.
 *
 * @param <T> Tipo de dato que contiene la lista.
 */
public class ListaArreglo<T> implements Iterable<T> {

    private Nodo<T> cabeza; //Referencia al primer nodo de la lista.
    private int tamano; //Cantidad de elementos en la lista.

    /**
     * Constructor que inicializa la lista vacía.
     */
    public ListaArreglo() {
        cabeza = null;
        tamano = 0;
    }

    /**
     *
     * Retorna la cantidad de elementos en la lista.
     *
     * @return Cantidad de elementos en la lista.
     */
    public int getTamano() {
        return tamano;
    }

    /**
     *
     * Retorna la referencia al primer nodo de la lista.
     *
     * @return Referencia al primer nodo de la lista.
     */
    public Nodo<T> getCabeza() {
        return cabeza;
    }

    /**
     *
     * Verifica si la lista está vacía.
     *
     * @return True si la lista está vacía, false de lo contrario.
     */
    public boolean estaVacia() {
        return cabeza == null;
    }

    /**
     *
     * Agrega un elemento al inicio de la lista.
     *
     * @param elemento Elemento a agregar.
     */
    public void agregarAlInicio(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        nuevoNodo.setSiguiente(cabeza);
        cabeza = nuevoNodo;
        tamano++;
    }

    /**
     *
     * Elimina el primer elemento de la lista.
     *
     * @return El elemento eliminado, o null si la lista está vacía.
     */
    public T eliminarDelInicio() {
        if (cabeza == null) {
            return null;
        }
        Nodo<T> nodoEliminado = cabeza;
        cabeza = cabeza.getSiguiente();
        nodoEliminado.setSiguiente(null);
        tamano--;
        return nodoEliminado.getDato();
    }

    /**
     *
     * Elimina el nodo que contiene el elemento especificado de la lista, si
     * existe. Si el elemento se encuentra en la cabeza de la lista, se elimina
     * la cabeza y se ajusta la nueva cabeza. Si el elemento se encuentra en
     * cualquier otra posición de la lista, se elimina el nodo y se ajustan los
     * enlaces de los nodos adyacentes. Si el elemento no se encuentra en la
     * lista, no se realiza ninguna acción.
     *
     * @param elemento el elemento a eliminar de la lista
     */
    public void eliminar(T elemento) {
        if (cabeza == null) {
            return;
        }
        if (cabeza.getDato().equals(elemento)) {
            cabeza = cabeza.getSiguiente();
            tamano--;
            return;
        }
        Nodo<T> anterior = cabeza;
        Nodo<T> actual = cabeza.getSiguiente();
        while (actual != null && !actual.getDato().equals(elemento)) {
            anterior = actual;
            actual = actual.getSiguiente();
        }
        if (actual != null) {
            anterior.setSiguiente(actual.getSiguiente());
            actual.setSiguiente(null);
            tamano--;
        }
    }

    /**
     *
     * Busca un elemento en la lista y devuelve true si se encuentra y false en
     * caso contrario.
     *
     * @param elemento el elemento a buscar en la lista.
     * @return true si se encuentra el elemento, false en caso contrario.
     */
    public boolean buscar(T elemento) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.getDato().equals(elemento)) {
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Retorna el elemento de la lista en el índice especificado.
     *
     * @param indice índice del elemento a obtener.
     * @return el elemento en el índice especificado, o null si el índice es
     * inválido.
     */
    public T obtener(int indice) {
        if (indice < 0 || indice >= tamano) {
            return null;
        }
        Nodo<T> actual = cabeza;
        for (int i = 0; i < indice; i++) {
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    /**
     *
     * Agrega un elemento al final de la lista. Si la lista está vacía, el
     * elemento se agrega al inicio.
     *
     * @param elemento el elemento a agregar.
     */
    public void agregarAlFinal(T elemento) {
        if (cabeza == null) {
            agregarAlInicio(elemento);
            return;
        }
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        Nodo<T> actual = cabeza;
        while (actual.getSiguiente() != null) {
            actual = actual.getSiguiente();
        }
        actual.setSiguiente(nuevoNodo);
        tamano++;
    }

    /**
     * Este es un método sobrescrito de la interfaz Iterable que devuelve un
     * Iterador para los elementos en la instancia ListaArreglo. Crea y devuelve
     * una nueva instancia de IteradorListaArreglo, que es una implementación de
     * iterador para ListaArreglo. La instancia IteradorListaArreglo se
     * construye con una referencia a la instancia ListaArreglo en la que se
     * llama este método. La clase IteradorListaArreglo en sí no se muestra en
     * este fragmento de código, pero presumiblemente implementa la interfaz
     * Iterator y proporciona una implementación para los métodos hasNext() y
     * next().
     *
     * @return Un iterador para los elementos de la lista.
     */
    @Override
    public Iterator<T> iterator() {
        return new IteradorListaArreglo<T>(this);
    }
}
