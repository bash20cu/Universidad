/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytsemana5;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author migue
 */
public class encuesta {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<String> encuesta=new ArrayList<String>();
        int opc,agua=0,coca=0;
        
        do {            
            System.out.println("Que bebida prefiere: 1-Agua|2-Coca Cola");
            encuesta.add(in.next());            
            
            System.out.println("Desea agregar otra encuesta 1-Si|2-No");
            opc=in.nextInt();
        } while (opc!=2);
        
        for (String e : encuesta) {
            if(e.compareTo("1")==0){
                agua++;
            }else{
                coca++;
            }
        }
        
        System.out.println("Porcentaje de Agua: " + (agua*100/encuesta.size()));
        System.out.println("Porcentaje de Coca cola: " + (coca*100/encuesta.size()));
    }
    
}
