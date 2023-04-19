package lab.vi;

import java.util.Scanner;

public class ListaReproducion {

    public void start() {
        Scanner scanner = new Scanner(System.in);
        ListaEnlazada lista = new ListaEnlazada();
        int opcion = 0;
        do {
            System.out.println("\n Menu de opciones:");
            System.out.println("1. Insertar nodo");
            System.out.println("2. Eliminar nodo");
            System.out.println("3. Mostrar lista");
            System.out.println("4. Crea una lista Ramdom");
            System.out.println("5. Salir");
            System.out.println("Ingrese una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el nombre de la canción: ");
                    String nombreCancion = scanner.next();
                    System.out.print("Ingrese el nombre del cantante: ");
                    String nombreCantante = scanner.next();
                    lista.insertar(nombreCancion, nombreCantante);
                    //lista.insertarNodo(nombreCancion, nombreCantante);
                    System.out.println("Nodo insertado con éxito");
                    lista.mostrarLista();
                    break;
                case 2:
                    if (lista.listaVacia()) {
                        System.out.println("La lista esta vacia");
                        System.out.println("Puede agregar nuevos elementos, con la opcion 1");
                        System.out.println("Puede generar una list aleatoria, con la opcion 4\n");
                    } else {
                        System.out.println("Ingrese el número de nodo a eliminar: ");
                        int numNodoEliminar = scanner.nextInt();
                        lista.borrar(numNodoEliminar);
                        System.out.println("Nodo eliminado con éxito");                        
                    }
                    break;
                case 3:
                    if (lista.listaVacia()) {
                        System.out.println("La lista esta vacia");
                        System.out.println("Puede agregar nuevos elementos, con la opcion 1");
                        System.out.println("Puede generar una list aleatoria, con la opcion 4\n");
                    } else {
                        System.out.println("Lista de reproducción:");
                        lista.mostrarLista();
                    }
                    break;
                case 4:
                    lista.listaRandom();
                    System.out.println("\n");
                    break;

                case 5:
                    System.out.println("Adiós");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 5);
        scanner.close();
    }

}
