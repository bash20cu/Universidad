/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import java.util.Scanner;
import modelo.clsEmpleado;

/**
 *
 * @author migue
 */
public class laboratorio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        
        clsEmpleado miguel = new clsEmpleado();        
        clsEmpleado santiago =  new clsEmpleado("Santiago", "Calderon", "Rueda");
        
        
        System.out.println("Introduzca el nombre: ");
        miguel.setNombre(in.next());

    }
    
}
