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
public class UsoSwitch {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Scanner in = new Scanner(System.in);
        short opc=0;
        int n1=0,n2=0;
        float res=0;
        String nombre="";
        System.out.println("Digite el numero 1");
        n1=in.nextInt();
        System.out.println("Digite el numero 2");
        n2=in.nextInt();
        
        System.out.println("----- Menu de Opciones -----");
        System.out.println("1) +");
        System.out.println("2) -");
        System.out.println("3) *");
        System.out.println("4) /");
        opc=in.nextShort();
        
        switch (opc) {
            case 1:
                res=n1+n2;
                break;
            case 2:
                res=n1-n2;
                break;
            case 3:
                res=n1*n2;
                break;
            case 4:
                res=n1/n2;
                break;
            default:
                  System.out.println("Opción Invalida");
        }
        System.out.println("Resultado: "  + res); 
    }
    
}
