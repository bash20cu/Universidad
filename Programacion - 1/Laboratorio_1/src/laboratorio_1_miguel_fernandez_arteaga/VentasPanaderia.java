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
public class VentasPanaderia {

    /**
     * @param args the command line arguments
     */
    /*
      Ejercicio #: Una panadería desea llevar un control sobre sus ventas, los
    productos con su precio se muestran a continuación:
    i. Baguette ¢600
    ii. Manita ¢500
    iii. Bollo pan dulce ¢900
    iv. Bollo pan salado ¢800
    v. Bollo relleno ¢1500
    vi. Se desea conocer el total recaudado por la venta de cada producto.
    vii. Se desea conocer el total recaudado de ventas.
    */
    
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        
        //Variables
        int Baguette,Manita, pandulce,pansalado,panrelleno,
                TotalBaguette,TotalManita,Totalpandulce,
                Totalpansalado,Totalpanrelleno,TotalVentas;
        
        //Constantes
        final int BAGUETTE = 660;
        final int MANITA = 500;
        final int PANDULCE = 900;
        final int PANSALADO = 800;
        final int PANRELLENO = 1500;
        
        //Entradas
        System.out.println("- Total de ventas - ");
        
        System.out.print("Baguette:");
        Baguette = in.nextInt();
        
        System.out.print("Manita: ");
        Manita = in.nextInt();
        
        System.out.print("Bollo pan dulce: ");
        pandulce = in.nextInt();
        
        System.out.print("Bollo de pan Salado: ");
        pansalado = in.nextInt();
        
        System.out.print("Bollo de pan Relleno: ");
        panrelleno = in.nextInt();
        
        //Procesos
        //vi. Se desea conocer el total recaudado por la venta de cada producto.
        TotalBaguette = Baguette * BAGUETTE;
        TotalManita = Manita * MANITA;
        Totalpandulce = pandulce * PANDULCE;
        Totalpansalado = pansalado * PANSALADO;
        Totalpanrelleno = panrelleno * PANRELLENO;
        TotalVentas = TotalBaguette + TotalManita + Totalpandulce +
                Totalpansalado + Totalpanrelleno;
        
        //Salidas
        // vii. Se desea conocer el total recaudado de ventas.
        System.out.println("El importe del total de Baguette vendido es: " + TotalBaguette);
        System.out.println("El importe del total de Manita vendido es: " + TotalManita);
        System.out.println("El importe del total de Pan Dulce vendido es: " + Totalpandulce);
        System.out.println("El importe del total de Pan Salado vendido es: " + Totalpansalado);
        System.out.println("El importe del total de Pan Relleno vendido es: " + Totalpanrelleno);
        System.out.println("El importe del total vendido es: " + TotalVentas);
    }
    
}
