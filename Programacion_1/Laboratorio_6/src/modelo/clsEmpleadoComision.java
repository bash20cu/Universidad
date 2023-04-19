/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsEmpleadoComision extends clsEmpleado implements iPorcentajeVentas {

    private double comision;
    private int totalVentas;
    private double salarioSemanal;

    public clsEmpleadoComision() {
        super();
    }

    public clsEmpleadoComision(double comision, int totalVentas) {
        this.comision = comision;
        this.totalVentas = totalVentas;
    }

    public clsEmpleadoComision(double comision, int totalVentas, String empleadoID, String nombre, String apellido1, String apellido2, String telefono) {
        super(empleadoID, nombre, apellido1, apellido2, telefono);
        this.comision = comision;
        this.totalVentas = totalVentas;
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

    public double getSalarioSemanal() {
        return salarioSemanal;
    }

    public void setSalarioSemanal(double salarioSemanal) {
        this.salarioSemanal = salarioSemanal;
    }

    @Override
    public double importeNomina() {
        if (getComision() > 0) {
            setSalarioSemanal(getSalarioSemanal() + porcentajeComision());
            System.out.println("Salario semanal + Comision : " + salarioSemanal);
        } else {
            System.out.println("Importe en nomina : No tuvo ventas");
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
