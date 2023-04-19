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
public class UsoWhileEncuesta {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        short opc=0, edad;
        int cMenor=0, cMayor=0;
        



       while (opc!=2) {            
            System.out.println("Digite la edad de la persona:");
            edad=in.nextShort();
            
            if (edad>=18) {
                cMayor++;
            } else {
                cMenor++; // cMenor=cMenor+1
            }
            
            System.out.println("¿Desea agregar otra edad? 1-Si / 2-No");
            opc=in.nextShort();
        }
        
        System.out.println("Cantidad de Encuestados: " + (cMayor+cMenor));
        System.out.println("Cantidad de menores: " + cMenor);
        System.out.println("Cantidad de mayores: " + cMayor);
        System.out.println("Porcentaje de menores: " + cMenor*100/(cMayor+cMenor) +"%");
        System.out.println("Porcentaje de mamyores: " + cMayor*100/(cMayor+cMenor)+"%");
    }
    
}
