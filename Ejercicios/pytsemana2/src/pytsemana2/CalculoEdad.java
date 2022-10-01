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

public class CalculoEdad {

    /**
     * @param args the command line arguments
     */
        public static void main(String[] args) {
         Scanner in = new Scanner(System.in);
         int edad1,edad2,diferencia;
         String msg="";
         
         System.out.println("Digite la edad de la persona 1");
         edad1=in.nextShort();
         System.out.println("Digite la edad de la persona 2");
         edad2=in.nextShort();
         
         //verificar que sean diferenes
         if (edad1 != edad2) {
             if (edad1>edad2) {
                 diferencia = edad1 - edad2;
                 msg="La persona 1 es el mayor";
             } else {
                 diferencia = edad2 - edad1;
                 msg="La persona 2 es el mayor";
             }
             
             System.out.println("La persona con mas edad es: " + msg);
             System.out.println("La diferencia de edad es: " + diferencia + " años");
             
        } else {
             System.out.println("Debe ingresar edades diferentes");
        }
    }
}
