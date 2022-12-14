/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsEmpleadoSemanal extends clsEmpleado implements iPorcentajeVentas {

    //Atributos
    private double salarioSemanal = 250; // salario semanal fijo 250
    private double comision;
    private int totalVentas;

    public clsEmpleadoSemanal() {
        super();
    }

    //empleados asalariados que reciben un salario semanal fijo, 
    //sin importar el número de horas trabajadas;
    public clsEmpleadoSemanal(String empleadoID, String nombre, String apellido1,
            String apellido2, String telefono, String direccion) {
        super(empleadoID, nombre, apellido1, apellido2, telefono);
    }

    //Empleados asalariados por comision que reciben un salario base
    //más un porcentaje de sus ventas.
    public clsEmpleadoSemanal(double comision, int totalVentas,
            String empleadoID, String nombre, String apellido1, String apellido2,
            String telefono, String direccion) {
        super(empleadoID, nombre, apellido1, apellido2, telefono);
        this.salarioSemanal = 250;
        this.comision = comision;
        this.totalVentas = totalVentas;
    }

    public void setSalarioSemanal(double salarioSemanal) {
        this.salarioSemanal = salarioSemanal;
    }

    public double getSalarioSemanal() {
        return salarioSemanal;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    public int getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(int totalVentas) {
        this.totalVentas = totalVentas;
    }

    //Metodo para determinar el importe en la nomina
    @Override
    public double importeNomina() {
        if (getComision() > 0) {
            setSalarioSemanal(getSalarioSemanal() + porcentajeComision());
            System.out.println("Salario semanal + Comision : " + salarioSemanal);
        } else {
            System.out.println("Importe en nomina : " + salarioSemanal);
        }
        System.out.println("Total a pagar: " + getSalarioSemanal());
        return (getSalarioSemanal()); //Salario Semanal
    }

    //Metodo para determinar la comision a pagar
    @Override
    public double porcentajeComision() {
        System.out.println("Total de comision: " + (getComision() * getTotalVentas()));
        return (getComision() * getTotalVentas());
    }
}
