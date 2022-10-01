/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio_2_miguel_fernandez_arteaga;

import java.util.Scanner;

/**
 *
 * @author migue
 */
public class Ejercicio2 {

    /**
     * @param args the command line arguments
     */
    
    /*   Ejercicio #2:
    Realizar un programa que permita realizar una encuesta para las 
    siguientes elecciones presidenciales en Costa Rica con los candidatos de los 
    partidos tradicionales (PTN. PUNS, PAD). A cada persona se le pregunta: El 
    programa debe tener un menú con las siguientes opciones:
    
    a. Aplicar encuesta: esta opción permite encuestar a una persona y se deben 
    registrar las respuestas a las siguientes preguntas:
        i. Si va a votar, En caso de que la respuesta sea afirmativa, se le 
            preguntará por qué partido Votará.
        ii. Debe llevar un control de la información recolectada en el punto 
            anterior.    
    b. Consultar datos: en esta opción consultara los resultados de la encuesta:
        i. ¿Cuál es el partido que está liderando la encuesta?
        ii. ¿Cuál es % a favor de cada partido, teniendo en cuenta, las personas 
        que si votaran?
        iii. ¿Cuál es % de personas que no votarán?
        iv. ¿Cuál es el % de personas que SI votaran?
    c. Salir: finaliza aplicación

    
    */
    public static void main(String[] args) {
        //Lectura de datos.
        Scanner in = new Scanner(System.in);
        
        //Variables
        int opc = 0; //Opcion del menu
        int voto, votoSi = 0,votoNo = 0,ptn = 0,puns = 0,pad = 0;
        short menu,partido;
        
        
        //Menu
        while (opc != 3) {            
            System.out.print("Menu: Digite (1)-Aplicar encuesta, (2)-Consultar Datos, (3)-Salir : ");
            menu = in.nextShort();
            
            switch (menu) {
                case 1:
                    System.out.println("Votara en las proximas elecciones?, digite: (1)-Si , (2)-No ");
                    voto = in.nextInt();
                    
                    if(!(voto!=1||voto!=2)){
                        System.out.println("Opcion no valida");
                    }
                  
                    if(voto == 1) {
                        votoSi++;
                        System.out.println("Por cual Partido realizara su voto?, digite (1)-PTN, (2)-PUNS, (3)-PAD");
                        partido = in.nextShort();
                        if(partido == 1){
                            ptn++;
                        }
                        if(partido == 2){
                            puns++;
                        }
                        if(partido == 3){
                            pad++;
                        }                                 
                    }
                    if(voto == 2){
                        votoNo++;
                    }
                    in.close();
                   break;
                case 2:
                    System.out.println("El partido que está liderando la encuesta es : ");
                    if(ptn>puns&&ptn>pad){
                        System.out.println("PTN con " + ptn + " Votos a favor");
                    }
                    if(puns>ptn&&puns>pad){
                        System.out.println("PUNS con " + puns + " Votos a favor");
                    }
                    if(pad>ptn&&pad>puns){
                        System.out.println("PAD con " + pad + " Votos a favor");
                    }                    
                    System.out.println("Total de votantes es: "+ (votoSi+votoNo));
                    System.out.println("PTN tiene un total de: " + ((ptn*100)/votoSi) + "% de votos a favor");
                    System.out.println("PUNS tiene un total de: " + ((puns*100)/votoSi) + "% de votos a favor");
                    System.out.println("PAD tiene un total de: " + ((pad*100)/votoSi) + "% de votos a favor");
                    System.out.println("Porciento de personas que no votarán: "+ ((votoNo*100)/(votoNo+votoSi)) + "%");
                    System.out.println("Porcentaje de  de personas que SI votaran: " + ((votoSi*100)/(votoNo+votoSi))+"%");
                    break;
                case 3:
                    System.out.println("----- Saliendo del programa -----");
                    opc = 3;
                    break;
                default:
                    System.out.println("Opción Invalida");
            }
        }
    }
    
}
