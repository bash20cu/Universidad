/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.clsEmpleadoSemanal;
import modelo.clsEmpleadoPorHoras;

/**
 *
 * @author migue
 */
public class laboratorio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        short opc = 1; //Opcion para cerrar el ciclo.
        List<clsEmpleadoSemanal> nominaSemanal = new ArrayList<>();
        List<clsEmpleadoPorHoras> nominaPorHoras = new ArrayList<>();
        Scanner in =  new Scanner(System.in);
        while (opc != 4) {
            System.out.print("Menu: Digite (1) Introducir empleado semanal, (2) Introducir empleado por horas,(3) Imprimir Nomina, (4) Salir : ");
            opc = in.nextShort();            
            switch(opc){
                case 1:
                    empleadoSemanal(nominaSemanal, in);
                    //introducirDatos(nominaSemanal,nominaPorHoras, in);
                    break;
                case 2:
                    empleadoXhoras(nominaPorHoras, in);
                    //introducirDatos(nominaSemanal,nominaPorHoras, in);
                    break; 
                case 3:                    
                    imprimirNomina(nominaSemanal,nominaPorHoras);
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

    private static void imprimirNomina(List<clsEmpleadoSemanal> nominaSemanal, List<clsEmpleadoPorHoras> nominaPorHoras) {
       System.out.println("Nomina Semanal Mostrando datos");        
        nominaSemanal.forEach(empleado -> {
            System.out.println("");
            System.out.println(empleado.nombreCompleto());
            System.out.println(empleado.importeNomina());
        });        
        
        nominaPorHoras.forEach(empleado -> {
            System.out.println(empleado.nombreCompleto());
            System.out.println(empleado.importeNomina());
        });
    }


    private static void empleadoSemanal(List<clsEmpleadoSemanal> nominaSemanal, Scanner in) {
        System.out.println("-- Ingresando datos de empleados semanal: --  ");        
        System.out.println("Digite: (1) - Empleado sin comision, (2) - Empleado con comision");
        clsEmpleadoSemanal empleado = new clsEmpleadoSemanal();
        int opcComision = in.nextInt();
        switch (opcComision) {
            case 1:
                System.out.print("Codigo del empleado: ");
                empleado.setEmpleadoID(in.next());
                System.out.print("Nombre del empleado: ");
                empleado.setNombre( in.next());
                System.out.print("Primer apellido del empleado: ");
                empleado.setApellido1(in.next());            
                System.out.print("Segundo apellido del empleado: ");
                empleado.setApellido2(in.next());            
                System.out.print("Telefono del empleado: ");
                empleado.setTelefono(in.next());
                nominaSemanal.add(empleado);
                break;
            case 2:
                System.out.print("Codigo del empleado: ");
                empleado.setEmpleadoID(in.next());
                System.out.print("Nombre del empleado: ");
                empleado.setNombre( in.next());
                System.out.print("Primer apellido del empleado: ");
                empleado.setApellido1(in.next());            
                System.out.print("Segundo apellido del empleado: ");
                empleado.setApellido2(in.next());            
                System.out.print("Telefono del empleado: ");
                empleado.setTelefono(in.next());
                System.out.print("Tasa comision: ");
                empleado.setComision(in.nextDouble());
                System.out.print("Total de ventas: ");
                empleado.setTotalVentas(in.nextInt());
                nominaSemanal.add(empleado);
                break;
            default:
                System.out.println("Opcion no valida");
        }        
        
    }

    private static void empleadoXhoras(List<clsEmpleadoPorHoras> nominaPorHoras, Scanner in) {
        System.out.println("-- Ingresando datos de empleados por horas: --  ");        
        System.out.println("Digite: (1) - Empleado sin comision, (2) - Empleado con comision");
        clsEmpleadoPorHoras empleado = new clsEmpleadoPorHoras();
        int opcComision = in.nextInt();
        switch (opcComision) {
            case 1:
                System.out.print("Codigo del empleado: ");
                empleado.setEmpleadoID(in.next());
                System.out.print("Nombre del empleado: ");
                empleado.setNombre( in.next());
                System.out.print("Primer apellido del empleado: ");
                empleado.setApellido1(in.next());            
                System.out.print("Segundo apellido del empleado: ");
                empleado.setApellido2(in.next());            
                System.out.print("Telefono del empleado: ");
                empleado.setTelefono(in.next());
                System.out.print("Cantidad de horas trabajadas durante la semana: ");
                empleado.setHorasTrabajadas(in.nextInt());
                System.out.print("Tasa de pago por horas: ");
                empleado.setTasaxhoras(in.nextDouble());
                nominaPorHoras.add(empleado);
                break;
            case 2:
                System.out.print("Codigo del empleado: ");
                empleado.setEmpleadoID(in.next());
                System.out.print("Nombre del empleado: ");
                empleado.setNombre( in.next());
                System.out.print("Primer apellido del empleado: ");
                empleado.setApellido1(in.next());            
                System.out.print("Segundo apellido del empleado: ");
                empleado.setApellido2(in.next());            
                System.out.print("Telefono del empleado: ");
                empleado.setTelefono(in.next());
                System.out.print("Cantidad de horas trabajadas durante la semana: ");
                empleado.setHorasTrabajadas(in.nextInt());
                System.out.print("Tasa de pago por horas: ");
                empleado.setTasaxhoras(in.nextDouble());               
                System.out.print("Total de ventas: ");
                empleado.setTotalVentas(in.nextInt());
                System.out.print("Tasa comision: ");
                empleado.setComision(in.nextDouble());
                nominaPorHoras.add(empleado);
                break;
            default:
                System.out.println("Opcion no valida");
        }
    }
  

}