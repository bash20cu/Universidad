/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsEmpleadoPorHoras extends clsEmpleado implements iSueldoPorHoras,iTiempoExtra,iPorcentajeVentas{
    private int horasTrabajadas;  
    private int totalVentas;
    private double tasaxhoras;
    private double salarioSemanal;
    private double comision;
    
    public clsEmpleadoPorHoras() {
    }
    
    //Empleado X Horas
    public clsEmpleadoPorHoras(int horasTrabajadas, double tasaxhoras, 
            String empleadoID, String nombre, String apellido1, String apellido2, 
            String telefono) {
        super(empleadoID, nombre, apellido1, apellido2, telefono);
        this.horasTrabajadas = horasTrabajadas;
        this.tasaxhoras = tasaxhoras;
    }
    
    //Empleado X Horas sin tiempo extra que recibe comision
    public clsEmpleadoPorHoras(int horasTrabajadas, int totalVentas, 
            double tasaxhoras, double comision, String empleadoID, 
            String nombre, String apellido1, String apellido2, String telefono) {
        super(empleadoID, nombre, apellido1, apellido2, telefono);
        this.horasTrabajadas = horasTrabajadas;
        this.totalVentas = totalVentas;
        this.tasaxhoras = tasaxhoras;
        this.comision = comision;       
    }
    
   

    //Getters and Setters
    public int getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(int horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public int getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(int totalVentas) {
        this.totalVentas = totalVentas;
    }

    public double getTasaxhoras() {
        return tasaxhoras;
    }

    public void setTasaxhoras(double tasaxhoras) {
        this.tasaxhoras = tasaxhoras;
    }

    public double getSalarioSemanal() {
        return salarioSemanal;
    }

    public void setSalarioSemanal(double salarioSemanal) {
        this.salarioSemanal = salarioSemanal;
    }

    public double getComision() {
        return comision;
    }

    public void setComision(double comision) {
        this.comision = comision;
    }

    
    //Metodos abstractos implementados    
        
    @Override
    public double importeNomina() {
        
        setSalarioSemanal(sueldoxhoras());
        System.out.println("--|Horas trabajadas: " + getHorasTrabajadas());
        System.out.println("--|Salario semanal  : "  + getSalarioSemanal());
        
        //Si tiene horas extras se pagan.
        if(getHorasTrabajadas() > 40){
            setSalarioSemanal(sueldoxhoras() + tiempoExtra());
            System.out.println("---|Salario semanal + Horas extras : "  + getSalarioSemanal());            
        }
        
        //Si tiene ventas, o derechos a comision se pagan.
        if(getTotalVentas() > 0){
            setSalarioSemanal(getSalarioSemanal() + porcentajeComision());
            System.out.println("---|Salario semanal + comision : " + getSalarioSemanal());
        }
        
        System.out.print("-> Salario semanal total: ");
        return (getSalarioSemanal());        
    }

    @Override
    public double sueldoxhoras() {        
        if(getHorasTrabajadas() <= 40 ){
           return (getTasaxhoras()) * getHorasTrabajadas();
        }else{
            return (40 * getTasaxhoras());            
        }
    }        
    
    //Metodo para determinar el tiempo extra
    @Override
    public double tiempoExtra() {                
        System.out.println("--| Horas trabajadas extra: " + (getHorasTrabajadas() - 40)  + " Sueldo horas extra: "+ (getTasaxhoras() * 1.5));
        return ((getHorasTrabajadas() - 40)* (getTasaxhoras() * 1.5));        
    }
    
    //Metodo para determinar la comision a pagar
    @Override
    public double porcentajeComision() {
        System.out.println("--| Comision: " + getComision() + " Total de ventas: " + getTotalVentas());
        System.out.println("--| Total de comision: " + (getComision() * getTotalVentas()));
        return (getComision() * getTotalVentas());                            
    }
}
