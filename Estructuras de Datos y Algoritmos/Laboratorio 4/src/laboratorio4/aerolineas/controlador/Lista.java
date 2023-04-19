package laboratorio4.aerolineas.controlador;

import laboratorio4.aerolineas.modelo.Nodo;
import laboratorio4.aerolineas.modelo.Pasajero;

/**
 *
 * @author miguel
 *
 * Clase especializada en las Operaciones de la lista
 */
public class Lista {

    //Variables
    private Nodo primer_Nodo;
    private Nodo ultimo_Nodo;

    /**
     * Constructor
     */
    public Lista() {
        //Inicializamos en nulo los punteros
        primer_Nodo = null;
        ultimo_Nodo = null;
    }

    //Metodos
    /**
     * Metodo para determinar si la lista esta vacia
     *
     * @return primer_Nodo, retorna nulo, si esta vacia.
     */
    public boolean esta_vacia() {
        return primer_Nodo == null;
    }

    /**
     * Metodo para insertar al final de la lista
     *
     * @param pasajero
     */
    public void insertar_final(Pasajero pasajero) {
        //Instanciamos el nuevo nodo 
        Nodo nuevo = new Nodo(pasajero);

        //validamos si la lista esta vacia usando el metodo esta_vacia
        if (esta_vacia()) {
            //guardamos el nodo en la lista, como primer nodo
            primer_Nodo = nuevo;
        } else {
            //colocamos el nodo en el ultimo.
            ultimo_Nodo.setSiguiente(nuevo);
        }
        ultimo_Nodo = nuevo;
    }

    /**
     * Metodo para eliminar al nodo
     *
     * @param cedula
     * @return true, en caso de encontrar al pasajero y borrarlo, sino false.
     */
    public boolean eliminar_pasajero(String cedula) {
        //Inicializamos los auxiliares.
        Nodo actual = primer_Nodo;
        Nodo anterior = null;

        /**
         * Revisamos la lista , hasta encontrar al nodo donde la cedula
         * coincide, En caso verdadero, borramos el nodo, En caso falso,
         * retornamos un False.
         */
        while (actual != null) {
            if (actual.getPasajero().getCedula().equals(cedula)) {
                if (anterior == null) {
                    primer_Nodo = actual.getSiguiente();
                } else {
                    anterior.setSiguiente(actual.getSiguiente());
                }
                if (actual == ultimo_Nodo) {
                    ultimo_Nodo = anterior;
                }
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Metodo para imprimir en pantalla la informacion del pasajero,
     *
     * @param cedula , siendo cedula el parametro de comparacion
     */
    public void mostrar_pasajero(String cedula) {
        Nodo nodoActual = primer_Nodo;
        if (esta_vacia()) {
            info_esta_vacia();
        } else {
            while (nodoActual != null) {
                Pasajero pasajeroActual = nodoActual.getPasajero();
                if (pasajeroActual.getCedula().equals(cedula)) {
                    System.out.println("***************************************");
                    System.out.println("Mostrando datos del pasajero: ");
                    System.out.println("Nombre: " + pasajeroActual.getNombre());
                    System.out.println("Cedula: " + pasajeroActual.getCedula());
                    System.out.println("Destino: " + pasajeroActual.getDestino());
                    System.out.println("***************************************");
                    return;
                }
                nodoActual = nodoActual.getSiguiente();
            }
            System.out.println("No se encontró el pasajero con cédula " + cedula);
        }
    }

    /**
     * Metodo para imprimir en pantalla la informacion de todos los pasajeros,
     *
     */
    public void mostrar_todos() {
        Nodo nodoActual = primer_Nodo;
        int cantidad = 1;
        if (esta_vacia()) {
            info_esta_vacia();
        } else {
            System.out.println("***************************************");
            while (nodoActual != null) {
                System.out.println("======== Pasajero numero: " + cantidad + " ======== ");
                Pasajero pasajeroActual = nodoActual.getPasajero();
                System.out.println("Nombre: " + pasajeroActual.getNombre());
                System.out.println("Cedula: " + pasajeroActual.getCedula());
                System.out.println("Destino: " + pasajeroActual.getDestino());
                System.out.println("\n");
                cantidad++;
                nodoActual = nodoActual.getSiguiente();
            }
            System.out.println("***************************************");
        }
    }

    /**
     * Metodo para imprimir una alerta en caso que la lista este vacia
     */
    public void info_esta_vacia() {
        System.out.println("===== lista vacia =====");
        System.out.println("===== inserte pasajeros =====");
        System.out.println("\n");
    }

}
