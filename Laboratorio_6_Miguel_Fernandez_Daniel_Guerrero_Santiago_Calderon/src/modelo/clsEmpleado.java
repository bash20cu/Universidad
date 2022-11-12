/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public abstract class clsEmpleado extends clsPersona{
    private String empleadoID;    
    public clsEmpleado() {
        super();
        empleadoID = "Vacio";
    }

    public clsEmpleado(String empleadoID, String nombre, String apellido1, String apellido2, String telefono, String direccion) {
        super(nombre, apellido1, apellido2, telefono, direccion);
        this.empleadoID = empleadoID;
    }

    public String getEmpleadoID() {
        return empleadoID;
    }
    
    public void setEmpleadoID(String empleadoID) {
        this.empleadoID = empleadoID;
    }
    
    abstract public double importeNomina ();   
    
}
