/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio_1_miguel_fernandez_arteaga;

import java.util.Scanner;

/**
 *
 * @author miguel
 */
public class ServicioTaxi {

    /**
     * @param args the command line arguments
     */
    /* Ejercicio #1:
        Desarrolle un algoritmo que calcule el costo total de un servicio
        de taxi según los siguientes datos:
        i. Primer kilómetro tiene un valor de ¢660
        ii. Cada kilómetro adicional tiene un costo de ¢615
        iii. Tarifa espera tiene un costo de ¢3750 por hora
        iv. Tarifa tiempo de demora tiene un costo de ¢6140
        v. Muestre los datos desglosados y el total a pagar en pantalla
    */
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        //Variables
        int TotalKm,TotalEspera,TotalDemora,importeRecorrido,importeEspera,importeDemora;
        int MontoTotal = 0;
        
        //Constantes 
         //i. Primer kilómetro tiene un valor de ¢660  
        final int VALORPRIMERKM = 660;
        
        //ii. Cada kilómetro adicional tiene un costo de ¢615
        final int VALORKM = 615;
        
        //iii. Tarifa espera tiene un costo de ¢3750 por hora
        final int VALORESPERA = 3750;
        //iv. Tarifa tiempo de demora tiene un costo de ¢6140 
        final int VALORDEMORA = 6140;
        
                
        //Entradas 
        System.out.print("Ingrese el total de kilometros recorridos: ");
        TotalKm = in.nextInt();
        
        
        
        System.out.print("Si tuvo espera, ingrese el total (Solo se contabilizan horas): ");
        TotalEspera = in.nextInt();
        
        System.out.print("Si tuvo demora, ingrese el total (Solo se contabilizan horas): ");
        TotalDemora = in.nextInt();
        
        in.close();
        
        //Procesos
        //i. Primer kilómetro tiene un valor de ¢660        
        //ii. Cada kilómetro adicional tiene un costo de ¢615
        importeRecorrido = ((TotalKm - 1) * VALORKM) + VALORPRIMERKM;
        
        //iii. Tarifa espera tiene un costo de ¢3750 por hora
        importeEspera = TotalEspera * VALORESPERA;
        
        //iv. Tarifa tiempo de demora tiene un costo de ¢6140       
        importeDemora = TotalDemora * VALORDEMORA;
        
        //importe total
        MontoTotal = importeRecorrido + importeEspera + importeDemora;
        
        //Salidas
        System.out.println("Total de Kilometros recorridos: " + TotalKm);
        System.out.println("Total de horas en espera: " + TotalEspera);
        System.out.println("Total de horas en demora: " + TotalDemora);
        System.out.println("- Importe del primer Kilometro: $660 -");
        System.out.println("Total de importe del recorrido : " + importeRecorrido);
        System.out.println("Importe adicional de horas en espera: $" + importeEspera);
        System.out.println("Importe adicional de horas en demora: $" + importeDemora);
        
        
        //v. Muestre los datos desglosados y el total a pagar en pantalla
        System.out.println("Importe Total a pagar : $" + MontoTotal );  
    }
    
}
