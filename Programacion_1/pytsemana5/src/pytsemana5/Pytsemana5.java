/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytsemana5;

import java.util.Scanner;

/**
 *
 * @author migue
 */
public class Pytsemana5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String nombres[] = new String[5];
        String txtBuscar;
        int opc;

        do {
            menu();
            opc = in.nextInt();
            switch (opc) {
                case 1:
                    guardarNombres(nombres, in);
                    break;
                case 2:
                    System.out.println("Digite el nombre a modificar");
                    txtBuscar = in.next();
                    modificarNombres(nombres, txtBuscar, in);
                    break;
                case 3:
                    System.out.println("Digite el nombre a eliminar");
                    txtBuscar = in.next();
                    eliminarNombres(nombres, txtBuscar, in);
                    break;
                case 4:
                    imprimirNombres(nombres);
                    break;
            }
       } while (opc != 5);
    }
    
    static void menu() {
        System.out.println("1-Guardar Nombres");
        System.out.println("2-Modificar Nombre");
        System.out.println("3-Eliminar Nombres");
        System.out.println("4-Imprimir Nombres");
        System.out.println("5-Salir");
    }



    static void guardarNombres(String[] nombres, Scanner in) {
        for (int i = 0; i < nombres.length; i++) {
            System.out.println("Digite el nombre #" + (i + 1));
            nombres[i] = in.next();
        }
    }



    static void imprimirNombres(String[] nombres) {
        for (int i = 0; i < nombres.length; i++) {
            System.out.println("Nombre #" + (i + 1) + " : " + nombres[i]);
        }
    }



    static void modificarNombres(String[] nombres, String nombre, Scanner in) {
        boolean enc = false;
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].compareToIgnoreCase(nombre) == 0) {
                System.out.println("Digite el nuevo nombre");
                nombres[i] = in.next();
                enc = true;
                break;
            }
        }
        if (enc) {
            System.out.println("Nombre actualizado");
        } else {
            System.out.println("Nombre: " + nombre + " no registrado");
        }
    }
    
    static void eliminarNombres(String[] nombres, String nombre, Scanner in) {
        boolean enc = false;
        for (int i = 0; i < nombres.length; i++) {
            if (nombres[i].compareToIgnoreCase(nombre) == 0) {
                nombres[i] = "";
                enc = true;
                break;
            }
        }
        if (enc) {
            System.out.println("Nombre Eliminado");
        } else {
            System.out.println("Nombre: " + nombre + " no registrado");
        }
    }    
}
