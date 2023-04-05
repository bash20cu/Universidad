package laboratorio4.aerolineas.vista;

import java.util.Scanner;
import laboratorio4.aerolineas.controlador.Lista;
import laboratorio4.aerolineas.modelo.Pasajero;

/**
 *
 * @author miguel
 */
public class ListaPasajeros {

    /**
     * Metodo para Iniciar la lista de pasajeros
     * contiene el menu de operaciones a realizar 
     * y la logica del programa.
     */
    public void iniciar() {
        Lista lista = new Lista();
        Scanner entrada = new Scanner(System.in);
        String nombre, cedula, destino;
        int opcion = 0;
        while (opcion != 5) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Agregar pasajero");
            System.out.println("2. Mostrar lista de pasajeros");
            System.out.println("3. Buscar un pasajero");
            System.out.println("4. Eliminar un pasajero");
            System.out.println("5. Salir");
            opcion = entrada.nextInt();
            entrada.nextLine(); // Limpiar el buffer de entrada
            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el nombre del pasajero:");
                    nombre = entrada.nextLine();
                    System.out.println("Ingrese la cédula del pasajero:");
                    cedula = entrada.nextLine();
                    System.out.println("Ingrese el destino del pasajero:");
                    destino = entrada.nextLine();
                    Pasajero pasajero = new Pasajero(nombre, cedula, destino);
                    lista.insertar_final(pasajero);
                    break;
                case 2:
                    System.out.println("Lista de pasajeros:");
                    lista.mostrar_todos();
                    break;
                case 3:
                    if (lista.esta_vacia()) {
                        lista.info_esta_vacia();
                    } else {
                        System.out.println("Buscar un pasajero:");
                        System.out.print("Ingrese la cédula del pasajero:");
                        cedula = entrada.nextLine();
                        lista.mostrar_pasajero(cedula);
                    }
                    break;
                case 4:
                    if (lista.esta_vacia()) {
                        lista.info_esta_vacia();
                    } else {
                        System.out.println("Eliminar un pasajero:");
                        System.out.print("Ingrese la cédula del pasajero:");
                        cedula = entrada.nextLine();
                        lista.eliminar_pasajero(cedula);
                    }
                    break;
                case 5:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción inválida");
                    break;
            }
        }
        entrada.close();
    }
}
