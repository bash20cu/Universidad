/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio_3_miguel_fernandez_arteaga;

import java.util.Scanner;

/**
 *
 * @author migue
 */
public class Laboratorio_3_Miguel_Fernandez_Arteaga {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        String frase = "";
        short opc = 1; //Opcion para cerrar el ciclo.
        Scanner in = new Scanner(System.in);
        // Menu 
        while (opc != 4) {
            System.out.print("Menu: Digite (1) Formato Capital, (2) Calular IVA, (3) Numero Aleatorio, (4) Salir : ");
            opc = in.nextShort();            
            switch(opc){
                case 1:
                    FormatoCapital(frase);
                    break;
                case 2:
                    CalcularIva();
                    break;
                case 3:
                    NumeroAleatorio();
                    break;
                case 4:
                    in.close();
                    System.out.println("----- Saliendo del programa -----"); 
                    break;
                default:
                    System.out.println("Opción Invalida");
                    break;
            }
        }  
    }
    
    /*Ejercicio #1:
    Defina una función que reciba un nombre completo y lo retorne en 
    formato capital, por ejemplo, recibe el nombre MarIA mendez SOSA y debe 
    retornar Maria Mendez Sosa
    */
    
    static void FormatoCapital(String frase){
        System.out.println("--- Metodo para darle formato al primer caracter en Mayusculas --- ");
        Scanner in = new Scanner(System.in);
        
        System.out.print("Introduzca el nombre completo: ");
        frase = in.nextLine();
        
    
        String[] nombreArreglo = frase.split(" ");
                
        for (int i = 0; i < nombreArreglo.length; i++) {
            nombreArreglo[i].toLowerCase(); 
            nombreArreglo[i] = nombreArreglo[i].substring(0,1).toUpperCase()+nombreArreglo[i].substring(1); 
            System.out.println(nombreArreglo[i]); 
        }
    }
    
    /*Ejercicio #2:
    Realice una función que permita calcular cualquier IVA de costa rica, 
    tome en cuenta que los porcentajes vigentes para costa rica son exonerados, 1%, 
    2%, 4% y 13% del subtotal.
    */
    
    static void CalcularIva(){
        System.out.println("--- Metodo para Calcular el IVA de un Subtotal --- ");
        Scanner in = new Scanner(System.in);
        short opcMetodo;
        double montoTotal;
        final double unoPorc = 0.01; 
        final double dosPorc = 0.02; 
        final double tresPorc = 0.03; 
        final double cuatroPorc = 0.04; 
        final double trecePorc = 0.13; 
        System.out.print("Ingrese Monto total: ");
        montoTotal = in.nextDouble();
        System.out.println("Que IVA desea calcular?: ");
        System.out.println("(1) - 1%, (2) - 2%, (3) - 3%, (4) - 4% , (5) - 13%, (6) - Exonerado");
        opcMetodo = in.nextShort();
        switch (opcMetodo) {
            case 1:
                System.out.println("IVA 1%: " + (montoTotal*unoPorc));
                montoTotal = montoTotal + (montoTotal*unoPorc);
                System.out.println("Subtotal a pagar: " + montoTotal);
                break;
            case 2:
                System.out.println("IVA 2%: " + (montoTotal*dosPorc));
                montoTotal = montoTotal + (montoTotal*dosPorc);
                System.out.println("Subtotal a pagar: " + montoTotal);
                break;
            case 3:
                System.out.println("IVA 3%: " + (montoTotal*tresPorc));
                montoTotal = montoTotal + (montoTotal*tresPorc);
                System.out.println("Subtotal a pagar: " + montoTotal);
                break;
            case 4:
                System.out.println("IVA 4%: " + (montoTotal*cuatroPorc));
                montoTotal = montoTotal + (montoTotal*cuatroPorc);
                System.out.println("Subtotal a pagar: " + montoTotal);
                break;
            case 5:
                System.out.println("IVA 13%: " + (montoTotal*trecePorc));
                montoTotal = montoTotal + (montoTotal*trecePorc);
                System.out.println("Subtotal a pagar: " + montoTotal);
            case 6:
                System.out.println("IVA Exonerado: " + montoTotal);
                 System.out.println("Subtotal a pagar: " + montoTotal);
            default:
                System.out.println("Opción Invalida");
                break;
        }
    }
    
    /* Ejercicio #3:
    Realice una función que genere un numero aleatorio entre x y y.
    */
    static void NumeroAleatorio(){
        System.out.println("--- Metodo para generar un numero aleatorio --- ");
        int rangoA,rangoB;
        Scanner in = new Scanner(System.in);
        
        System.out.println("Introduzca el rango");
        System.out.print("Desde X igual a: ");
        rangoA = in.nextInt();
        System.out.print("Hasta Y igual a: ");
        rangoB = in.nextInt();
        System.out.println("Numero aleatorio: " + ((int)(Math.random()*(rangoA-rangoB+1)+rangoB)));
    }        
}
