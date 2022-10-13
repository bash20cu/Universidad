/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytsemana5;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author migue
 */
public class listaArreglos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> nombres = new ArrayList<String>();
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



   static void guardarNombres(ArrayList<String> nombres, Scanner in) {
        System.out.println("Digite el nombre a guardar:");
        nombres.add(in.next());
    }



   static void imprimirNombres(ArrayList<String> nombres) {
        int i=1;
        
        if(!nombres.isEmpty()){
            for (String nombre : nombres) {
                System.out.println("Nombre #" + i + " : " + nombre);
                i++;
            }
        }else{
             System.out.println("No hay nombres registrados");
        }
        
    }



   static void modificarNombres(ArrayList<String> nombres, String nombre, Scanner in) {
        int i =  nombres.indexOf(nombre);
        if (i!=-1) {
            System.out.println("Digite nuevo Nombre");
            String updNombre=in.next();
            nombres.set(i, updNombre);
            System.out.println("Nombre actualizado");
        } else {
            System.out.println("Nombre: " + nombre + " no registrado");
        }
    }



   static void eliminarNombres(ArrayList<String> nombres, String nombre, Scanner in) {
       int i =  nombres.indexOf(nombre);
        if (i!=-1) {
            nombres.remove(i);
            System.out.println("Nombre eliminado");
        } else {
            System.out.println("Nombre: " + nombre + " no registrado");
        }
    }
    
}
