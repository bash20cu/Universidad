package lab.vi;

import java.util.Random;

public class ListaEnlazada {

    Nodo cabeza;

    public ListaEnlazada() {
        cabeza = null;
    }

    /**
     * Método que busca validar si la lista esta vacia
     *
     * @return true,false si la lista esta vacia
     */
    public boolean listaVacia() {
        if (cabeza == null) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Método insertar: se crea un nuevo nodo con los parámetros dados y se
     * inserta al final de la lista.Si la cabeza de la lista es nula, entonces
     * el nuevo nodo se convierte en la cabeza de la lista.De lo contrario, se
     * recorre la lista enlazada hasta llegar al último nodo y se agrega el
     * nuevo nodo a continuación.
     *
     * @param nombreCancion, nombre de la cancion
     * @param nombreCantante, nombre del cantante
     */
    public void insertar(String nombreCancion, String nombreCantante) {
        int numeroNodo = generarNumeroConsecutivo();
        Nodo nuevoNodo = new Nodo(numeroNodo, nombreCancion, nombreCantante);
        if (listaVacia()) {
            cabeza = nuevoNodo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevoNodo;
        }       
    }

    /**
     * Método borrar: si la cabeza de la lista es nula, entonces no hay nodos
     * para borrar.De lo contrario, si el número de nodo que se pasa como
     * parámetro es el número de la cabeza de la lista, se elimina la cabeza. De
     * lo contrario, se recorre la lista enlazada hasta llegar al nodo anterior
     * al nodo que se va a borrar, y se actualizan los punteros de los nodos
     * apropiados.
     *
     * @param numeroNodo, numero del nodo a borrar
     */
    public void borrar(int numeroNodo) {
        if (listaVacia()) {
            return;
        }
        if (cabeza.numeroNodo == numeroNodo) {
            cabeza = cabeza.siguiente;
            organizarLista();
            return;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null && actual.siguiente.numeroNodo != numeroNodo) {
            actual = actual.siguiente;
        }
        if (actual.siguiente == null) {
            organizarLista();
            return;
        }
        actual.siguiente = actual.siguiente.siguiente;
        mostrarLista();
    }

    /**
     * Genera un número consecutivo para el siguiente nodo en la lista. El
     * número consecutivo es el número del último nodo en la lista más uno. Si
     * la lista está vacía, devuelve 1.
     *
     * @return el número consecutivo para el siguiente nodo en la lista.
     */
    public int generarNumeroConsecutivo() {
        // Inicializar el número máximo de nodo como 0.
        int maxNumeroNodo = 0;
        // Iniciar un puntero al primer nodo de la lista.
        Nodo actual = cabeza;
        // Iterar sobre todos los nodos en la lista para encontrar el número máximo de nodo.
        while (actual != null) {
            // Si el número del nodo actual es mayor que el número máximo encontrado hasta el momento,
            // actualizar el número máximo.
            if (actual.numeroNodo > maxNumeroNodo) {
                maxNumeroNodo = actual.numeroNodo;
            }
            // Mover el puntero al siguiente nodo.
            actual = actual.siguiente;
        }
        // El número consecutivo para el siguiente nodo en la lista es el número máximo encontrado más uno.
        return maxNumeroNodo + 1;
    }

    /**
     * Método que verifica si un número de nodo existe en la lista.
     *
     * @param numeroNodo El número de nodo a buscar.
     * @return true si el número de nodo existe en la lista, false en caso
     * contrario.
     */
    public boolean existeNumero(int numeroNodo) {
        Nodo actual = cabeza;
        while (actual != null) {
            if (actual.numeroNodo == numeroNodo) {
                return true;
            }
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Este método organiza la lista y le asigna números consecutivos a cada
     * nodo, empezando por 1. Recorre la lista y asigna el número de nodo
     * correspondiente a cada nodo de la lista. Después de organizar la lista,
     * muestra el contenido de la lista.
     */
    private void organizarLista() {
        // Verifica si la lista está vacía
        if (listaVacia()) {
            return;
        }
        // Inicia el contador para asignar números consecutivos a los nodos
        int numeroNodo = 1;
        Nodo actual = cabeza;
        // Recorre la lista y asigna el número de nodo correspondiente a cada nodo de la lista
        while (actual != null) {
            actual.numeroNodo = numeroNodo;
            numeroNodo++;
            actual = actual.siguiente;
        }
        // Muestra el contenido de la lista después de organizarla y asignar los números consecutivos
        mostrarLista();
    }

    /**
     * Método usado para iterar sobre la lista y mostrar los elementos
     */
    public void mostrarLista() {
        System.out.println("+-------------------------------------------------------------------------+");
        System.out.println("| Número de nodo | Nombre de canción | Nombre de cantante | Procesamiento |");
        System.out.println("+-------------------------------------------------------------------------+");
        Nodo actual = cabeza;
        while (actual != null) {
            /**
             * Cada % indica que se debe sustituir por un valor. Los diferentes
             * caracteres que siguen al % indican el tipo de valor que se espera
             * y el formato que se usará para imprimirlo. %15d indica que se
             * debe imprimir un número entero (d) y que debe ocupar al menos 15
             * espacios en la tabla. %19s indica que se debe imprimir una cadena
             * de caracteres (s) y que debe ocupar al menos 19 espacios en la
             * tabla. %20s indica que se debe imprimir otra cadena de caracteres
             * (s) y que debe ocupar al menos 20 espacios en la tabla. %15d
             * indica que se debe imprimir otro número entero (d) y que debe
             * ocupar al menos 15 espacios en la tabla.
             */
            System.out.printf("|%15d|%19s|%20s|%15d |\n", actual.numeroNodo, actual.nombreCancion, actual.nombreCantante, actual.numeroProcesamiento);
            actual = actual.siguiente;
        }
        System.out.println("+-------------------------------------------------------------------------+");
    }

    /**
     * Método para generar un lista ramdom, solo para testeo
     */
    public void listaRandom() {
        // Se crea una nueva lista enlazada
        ListaEnlazada lista = new ListaEnlazada();

        // Arreglo de nombres de canciones a elegir de manera aleatoria
        String[] canciones = {
            "Pareja Del Año",
            "La Toxica",
            "Bandido",
            "Volando",
            "Mala Influencia",
            "La Botella",
            "Canción Bonita",
            "El Makinon",
            "La Curiosidad",
            "Dime Si Tú"
        };

        // Se crea un objeto Random para generar números aleatorios
        Random rand = new Random();

        // Se inserta un número aleatorio de elementos (10 en este caso) en la lista
        for (int i = 0; i < 10; i++) {
            // Se elige aleatoriamente un índice del arreglo de canciones
            int randomIndex = rand.nextInt(canciones.length);
            // Se obtiene el nombre de la canción correspondiente al índice aleatorio
            String cancion = canciones[randomIndex];
            // Se crea el nombre del cantante concatenando la cadena "Cantante" y el índice actual
            String cantante = "Cantante " + i;
            // Se inserta el elemento en la lista con la canción y cantante generados aleatoriamente
            insertar(cancion, cantante);
        }

        // Se muestra la lista generada aleatoriamente
        mostrarLista();
    }

}
