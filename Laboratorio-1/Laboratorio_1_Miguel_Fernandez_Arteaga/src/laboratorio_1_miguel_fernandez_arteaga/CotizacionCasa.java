/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio_1_miguel_fernandez_arteaga;

import java.util.Scanner;

/**
 *
 * @author migue
 */
public class CotizacionCasa {

    /**
     * @param args the command line arguments
     */
    /*
        Defina un algoritmo que calcule la cotización de una compra de casa según los siguientes datos:
        i. Información personal de la persona interesada (cedula, nombre, teléfono)
        ii. El valor de venta de la propiedad
        iii. Descuento por pago de contado: un 10% de rebajo del valor de la propiedad
        iv. Financiación a corto plazo: valor de la propiedad entre 24 meses para
        calcular la cuota mensual, cada cuota mensual tiene un recargo de 4%  del valor de esta
        v. Financiación a largo plazo: valor de la propiedad entre 84 meses para
        calcular la cuota mensual, cada cuota tiene un recargo de 2% del valor
        de esta.
        vi. Mostrar en pantalla los datos anteriores
    */
    public static void main(String[] args) {
        
        //Variables
        long cedula,telefono;
        double valorPropiedad,pagoDeContado,financiacionCortoPlazo,cuotaMensual,
                financiacionCortoPlazoTotal,financiacionLargoPlazo,
                cuotaMensualLargo,financiacionLargoPlazoTotal;
        String nombre;
        Scanner in = new Scanner(System.in);
       
        //Constantes
        final double DESCUENTO10PORC = 0.10;
        final double CUOTACORTOPLAZO = 0.04;
        final double CUOTALARGOPLAZO = 0.02;
        
        
        //Entrada        
        //i. Información personal de la persona interesada (cedula, nombre, teléfono)
        System.out.print("Introduzca el nombre del propietario: ");
        nombre = in.next();
        
        
        System.out.print("Introduzca el numero de cedula del propietario: ");
        cedula = in.nextLong();
        
        System.out.print("Introduzca el numero de telefono del propietario: ");
        telefono = in.nextLong();
         
        //ii. El valor de venta de la propiedad
        System.out.print("Introduzca el valor de la propiedad: ");
        valorPropiedad = in.nextDouble();
        
        in.close();
        
        //Procesos
        //iii. Descuento por pago de contado: un 10% de rebajo del valor de la propiedad        
        pagoDeContado = valorPropiedad - (valorPropiedad * DESCUENTO10PORC);
        
        /* iv. Financiación a corto plazo: 
                valor de la propiedad entre 24 meses para
                calcular la cuota mensual, cada cuota mensual 
                tiene un recargo de 4%  del valor de esta
        */
        financiacionCortoPlazo =  valorPropiedad / 24;
        cuotaMensual = financiacionCortoPlazo + (financiacionCortoPlazo * CUOTACORTOPLAZO);
        financiacionCortoPlazoTotal = cuotaMensual * 24;
        
        /*v. Financiación a largo plazo: 
                valor de la propiedad entre 84 meses paracalcular la cuota 
                mensual, cada cuota tiene un recargo de 2% del valor de esta.       
        */
        financiacionLargoPlazo = valorPropiedad / 84;
        cuotaMensualLargo = financiacionLargoPlazo + (financiacionLargoPlazo * CUOTALARGOPLAZO);
        financiacionLargoPlazoTotal = cuotaMensualLargo * 84;
        //Salidas
        //vi. Mostrar en pantalla los datos anteriores    
        System.out.println("Informacion del propietario: ");
        System.out.println("Nombre: : " + nombre + "\n" 
                + "Cedula: "+ cedula + "\n" 
                + "Numero de telefono: " + telefono);
        // Uso de un metodo dentro del Objeto String, el cual formatea el valor y evita la notacion cientifica.
        System.out.println("La propiedad tiene un valor inicial de: " + String.format("%.2f",valorPropiedad));
        
        System.out.println("Pago de contado, con un descuento del 10%, Total a pagar: " + String.format("%.2f",pagoDeContado));
        
        System.out.println("Financiacion a Corto Plazo (24 meses): ");
        System.out.println("Valor de l cuota mensual: " + String.format("%.2f",financiacionCortoPlazo));
        System.out.println("Valor de la cuota mensual con recargo: "+ String.format("%.2f",cuotaMensual));
        System.out.println("Valor total del pago con financiamiento: " + String.format("%.2f", financiacionCortoPlazoTotal));
        
        
        System.out.println("Financiacion a Largo Plazo (84 meses): ");
        System.out.println("Valor de l cuota mensual: "+String.format("%.2f",financiacionLargoPlazo));
        System.out.println("Valor de la cuota mensual con recargo: " + String.format("%.2f",cuotaMensualLargo));
        System.out.println("Valor total del pago con financiamiento: " + String.format("%.2f",financiacionLargoPlazoTotal));        
    }
    
}
