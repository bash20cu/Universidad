/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import java.util.Scanner;
import modelo.clsCirculo;
import modelo.clsCuadrado;
import modelo.clsRectangulo;

/**
 *
 * @author migue
 */
public class calculos {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        clsCirculo ci=new clsCirculo(10);
        clsCuadrado cu=new clsCuadrado(5);
        clsRectangulo re=new clsRectangulo(2, 4);
        int opc;
        Scanner in=new Scanner(System.in);
        
        do {            
            System.out.println("Seleccione la Figura:");
            System.out.println("1-Circulo");
            System.out.println("2-Cuadrado");
            System.out.println("3-Rectangulo");
            System.out.println("4-Salir");
            opc=in.nextInt();
            
            switch (opc) {
                case 1:
                        System.out.println("Circulo");
                        System.out.println("Area: " + ci.area());
                        System.out.println("Perimetro: " + ci.perimetro());
                    break;
                case 2:
                        System.out.println("Cuadrado");
                        System.out.println("Area: " + cu.area());
                        System.out.println("Perimetro: " + cu.perimetro());
                    break;
                case 3:
                        System.out.println("Rectangulo");
                        System.out.println("Area: " + re.area());
                        System.out.println("Perimetro: " + re.perimetro());
                    break;
                case 4:
                        
                    break;
                default:
                    System.out.println("Debe ingresar una opción válida");
            }
        } while (opc!=4);               
    }    
}
