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
public class Pytsemana2 {


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        short docenas = 0;
        double descuento = 0;
        double subtotal = 0;
        
        System.out.println("Ingrese la cantidad de docenas");
        docenas=in.nextShort();
        System.out.println("Ingrese el subtotal");
        subtotal=in.nextDouble();
        
        //calcular
        if (docenas>=3) {
            descuento=subtotal*0.1;
        }
        
        //salidas
        System.out.println("Subtotal: ¢" + subtotal);
        System.out.println("Descuento: ¢" + descuento);
        System.out.println("Total: ¢" + (subtotal - descuento));
    }
    
}
