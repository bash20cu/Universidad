/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytsemana2;

import java.util.Scanner;

/**
 *
 * @author migue
 */
public class IfAnidado {

    /**
     * @param args the command line arguments
     */
        public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            short nota;

            System.out.println("Digite la nota:");
             nota = in.nextShort();

            if (nota >= 90) {
                 System.out.println("Calificación: A");
             } else if (nota >= 80 && nota <= 89) {
                 System.out.println("Calificación: B");
             } else if (nota >= 70 && nota <= 79) {
                 System.out.println("Calificación: C");
             }else if (nota >= 60 && nota <= 69) {
                 System.out.println("Calificación: D");
             }else{
                 System.out.println("Calificación: F");
             }
    }
    
}
