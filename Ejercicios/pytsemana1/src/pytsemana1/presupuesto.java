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

/*Ejercicio 33:
    En un hospital existen 3 áreas: Urgencias, Pediatría y Traumatología. El
    presupuesto anual del hospital se reparte de la siguiente manera:
    a. Área Presupuesto
    b. Urgencias 37%
    c. Pediatría 42%
    d. Traumatología 21%
    e. Diseñe un algoritmo que calcule y muestre la cantidad de dinero que
        recibirá cada área a partir del presupuesto del hospital

*/

public class presupuesto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        final double PEMERGECIAS = 0.37;
        final double PPEDIATRIA = 0.42;
        final double PTRAUMATOLOGIA = 0.21;
        
        double presupuesto, emergencias, pediatria,traumatologia;
        
        //entradas
        System.out.print("Ingrese el Presupuesto: ");
        presupuesto = in.nextDouble();
        
        //procesos
        emergencias = presupuesto*PEMERGECIAS;
        pediatria = presupuesto*PPEDIATRIA;
        traumatologia = presupuesto*PTRAUMATOLOGIA;
        
        System.out.println("Presupuesto para Emergencias: $ " +emergencias);
        System.out.println("Presupuesto para Pediatria: $ " +pediatria);
        System.out.println("Presupuesto para Traumatologia: $ " +traumatologia);      
        
    }
    
}
