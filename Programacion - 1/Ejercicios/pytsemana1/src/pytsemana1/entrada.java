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
public class entrada {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Scanner in = new Scanner(System.in);
     
        int num1,num2;
        
        System.out.print("Ingrese el primer numero: ");
                        
        num1 = in.nextInt();
        System.out.print("\n");
        System.out.print("Ingrese el segundo numero: ");
        num2 = in.nextInt();
        System.out.print("\n");
                
        System.out.println("Suma: " + (num1+num2));
        System.out.println("Resta: " + (num1-num2));
        System.out.println("Multiplicación: " + (num1*num2));
        System.out.println("División: " + (num1/num2));
        
    }
    
}
