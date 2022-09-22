/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytsemana1;

import java.util.Scanner;

/**
 *
 * @author migue
 */

/* Ejercicio 8
    Una tablilla PVC de cielo raso tienen un costo de ¢3000, y cubre 1m2, realice
    un algoritmo que calcule la cantidad de piezas y el costo a invertir para
    colocar este tipo de cielorraso en una casa de X m2.
*/
public class cieloraso {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final int PRECIO = 3000;
        double metros, total;

        //entrada
        System.out.print("Introduzca los metros del cielo raso: ");
        Scanner in = new Scanner(System.in);
        metros = in.nextDouble();
        
        //procesos 
        total = metros * PRECIO;
        
        //salidas
        System.out.println("Numero de piezas: " + metros);
        System.out.println("Total a pagar: " +total);
    
    }
    
}
