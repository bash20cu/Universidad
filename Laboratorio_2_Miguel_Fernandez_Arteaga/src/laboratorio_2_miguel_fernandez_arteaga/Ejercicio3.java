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
public class Ejercicio3 {

    /**
     * @param args the command line arguments
     */
    
    /*Ejercicio #3:
     Realice una aplicación donde que calcule el total a pagar por el envío 
        de un paquete según la siguiente tabla
    
    Tipo  Prioridad A   Prioridad B   Prioridad C
    Carta ¢2000/gramo   ¢1500/gramo   ¢1000/gramo
    Caja  ¢20000/gramo  ¢15000/gramo  ¢10000/gramo
    
    Por cada paquete calculado, se debe preguntar a usuario si sea agregar otro, de ser 
    afirmativo, se repite el proceso, de lo contrario se finaliza la aplicación.

    */
    public static void main(String[] args) {
        //Lectura de datos.
        Scanner in = new Scanner(System.in);
        
        //Variables
        short opc = 0; //Opcion del menu
        
        int prioridadA, priordadB, prioridadC;
        short menu,paquete,carta,caja,prioridad;
        float peso,precioFinal = 0;
        
        while (opc !=3) {
            System.out.println("Digite (1)-Nuevo paquete, (3)-Salir : ");
            opc = in.nextShort();            
            
            switch (opc) {
                case 1:
                    System.out.print("Digite el peso en gramos: ");
                    peso = in.nextFloat();
                    System.out.println("Escoja el tipo de paquete: (1)-Carta, (2)-Caja");
                    paquete = in.nextShort();                    
                    
                    if(paquete == 1){
                        System.out.println(" --- Carta --- ");
                        
                        System.out.print("Digite la prioridad del paquete: (1)-A , (2)-B, (3)-C : ");
                        prioridad = in.nextShort();                    
                        switch (prioridad) {
                            case 1:
                                precioFinal = 2000/peso;
                                System.out.println(precioFinal);
                                break;
                            case 2:
                                precioFinal = 1500/peso;
                                break;
                            case 3:
                                precioFinal = 1000/peso;
                                break;
                            default:
                                System.out.println("Prioridad invalida");
                                System.out.print("Digite la prioridad del paquete: (1)-A , (2)-B, (3)-C : ");
                                prioridad = in.nextShort();
                            }
                    }
                    
                    if(paquete == 2){
                        System.out.println("--- Caja ---");
                        System.out.print("Digite la prioridad del paquete: (1)-A , (2)-B, (3)-C : ");
                        prioridad = in.nextShort();
                        switch (prioridad) {
                            case 1:
                                precioFinal = 20000/peso;
                                break;
                            case 2:
                                precioFinal = 15000/peso;                                
                                break;
                            case 3:
                                precioFinal = 10000/peso;                                
                                break;
                            default:
                                System.out.println("Prioridad invalida");
                                System.out.print("Digite la prioridad del paquete: (1)-A , (2)-B, (3)-C : ");
                                
                            }
                    }
                    
                     System.out.println("Precio Final del Paquete: "+ precioFinal);
                     System.out.println("--- Desea ingresar otro paquete o salir del programa? --- ");
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
