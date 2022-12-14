/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import modelo.clsEmpleadoComision;
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
        List<clsEmpleadoComision> nominaPorComision = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        while (opc != 5) {
            System.out.println("Menu:  Digite");
            System.out.println("(1) Introducir empleado semanal");
            System.out.println("(2) Introducir empleado por horas");
            System.out.println("(3) Introducir empleado por comision");
            System.out.println("(4) Imprimir Nomina");
            System.out.println("(5) Salir");

            opc = in.nextShort();
            switch (opc) {
                case 1:
                    empleadoSemanal(nominaSemanal, in);
                    break;
                case 2:
                    empleadoXhoras(nominaPorHoras, in);
                    break;
                case 3:
                    empleadoXComision(nominaPorComision, in);
                    break;
                case 4:
                    imprimirNomina(nominaSemanal, nominaPorHoras, nominaPorComision);
                    break;
                case 5:
                    in.close();
                    System.out.println("----- Saliendo del programa -----");
                    break;
                default:
                    System.out.println("Opción Invalida");
                    break;
            }
        }
        in.close();
    }

    private static void imprimirNomina(List<clsEmpleadoSemanal> nominaSemanal,
            List<clsEmpleadoPorHoras> nominaPorHoras,
            List<clsEmpleadoComision> nominaPorComision) {

        System.out.println("Nomina Semanal Mostrando datos");
        System.out.println("\n");
        System.out.println(" ---- ---- ---- ---- ---- ---- ");
        System.out.println("Trabajadores con salario semanal Fijo ");
        nominaSemanal.forEach(empleado -> {
            System.out.println(empleado.nombreCompleto());
            System.out.println(empleado.importeNomina());
            System.out.println(" ");
        });

        System.out.println(" ---- ---- ---- ---- ---- ----");
        System.out.println("\n");
        System.out.println(" ---- ---- ---- ---- ---- ----");
        System.out.println("Trabajadores con salario por horas ");
        nominaPorHoras.forEach(empleado -> {
            System.out.println(empleado.nombreCompleto());
            System.out.println(empleado.importeNomina());
            System.out.println(" ");
        });

        System.out.println(" ---- ---- ---- ---- ---- ----");
        System.out.println("\n");
        System.out.println(" ---- ---- ---- -------- ----");
        System.out.println("Trabajadores con salario por comision ");
        nominaPorComision.forEach(empleado -> {
            System.out.println(empleado.nombreCompleto());
            System.out.println(empleado.importeNomina());
            System.out.println(" ");
        });

        System.out.println(" ---- ---- ---- ---- ---- ----");
        System.out.println("Fin de la Nomina");
        System.out.println("\n");
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
                empleado.setNombre(in.next());
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
                empleado.setNombre(in.next());
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
        clsEmpleadoPorHoras empleado = new clsEmpleadoPorHoras();

        System.out.print("Codigo del empleado: ");
        empleado.setEmpleadoID(in.next());
        System.out.print("Nombre del empleado: ");
        empleado.setNombre(in.next());
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
    }

    private static void empleadoXComision(List<clsEmpleadoComision> nominaPorComision, Scanner in) {
        clsEmpleadoComision empleado = new clsEmpleadoComision();
        System.out.print("Codigo del empleado: ");
        empleado.setEmpleadoID(in.next());
        System.out.print("Nombre del empleado: ");
        empleado.setNombre(in.next());
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
        nominaPorComision.add(empleado);
    }

}
