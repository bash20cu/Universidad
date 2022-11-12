/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import java.util.Scanner;
import modelo.clsEmpleadoSemanal;
import modelo.clsEmpleadoPorHoras;
import modelo.clsPersona;

/**
 *
 * @author migue
 */
public class laboratorio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in =  new Scanner(System.in);
        //short opc = 1; //Opcion para cerrar el ciclo.
        
        
                
        //empleados asalariados que reciben un salario semanal fijo, 
            //sin importar el número de horas trabajadas;
        //clsEmpleadoSemanal juan = new clsEmpleadoSemanal("001A20", "Juan", "Gonzalez", "Perez", "64524289", "Barrio Jardin");
        
        //Empleados asalariados por comision que reciben un salario base
            //más un porcentaje de sus ventas.
        //clsEmpleadoSemanal juan = new clsEmpleadoSemanal(true, 0.2, 10, "001A20", "Juan", "Gonzalez", "Perez", "64524289", "Barrio Jardin");
        
        //Empleado X Horas        
        //clsEmpleadoPorHoras juan = new clsEmpleadoPorHoras(8, 10, "001A20", "Juan", "Gonzalez", "Perez", "64524289", "Barrio Jardin");
        
        //Empleado X Horas sin tiempo extra que recibe comision
        //clsEmpleadoPorHoras juan = new clsEmpleadoPorHoras(true, 10, 5, 10, 0.2, "001A20", "Juan", "Gonzalez", "Perez", "64524289", "Barrio Jardin");
        
         //Empleados x hora con horas extras y que reciben un porcentaje de sus ventas
         clsEmpleadoPorHoras juan = new clsEmpleadoPorHoras(9, 2, 10, 2, "001A20", "Juan", "Gonzalez", "Perez", "64524289", "Barrio Jardin");

        //visualizacion de datos:
        //System.out.println("Nomina de empleados");
        //Empleado salario Mensual
        //System.out.println("--- Empleados con salario mensual Fijo: ---");
        
        //System.out.println(miguel.nombreCompleto());
        //System.out.println("Salario mensual " + miguel.getSalarioMensual());
        //System.out.println(juan.nombreCompleto());
        //System.out.println(juan.importeNomina()); 
        System.out.println(juan.nombreCompleto());
        System.out.println( juan.importeNomina()); 

    }
    
}

      /**
         * 
         *  
         *  //Menu  
         * while (opc != 3) {
            System.out.print("Menu: Digite (1) Introducir Datos, (2) Imprimir Nomina, (3) Salir : ");
            opc = in.nextShort();            
            switch(opc){
                case 1:
                    
                    break;
                case 2:
                    
                    break;                
                case 3:
                    in.close();
                    System.out.println("----- Saliendo del programa -----"); 
                    break;
                default:
                    System.out.println("Opción Invalida");
                    break;
            }
        }
        
         */