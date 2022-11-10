/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsEmpleado{
    private String nombre;
    private String apellido1;
    private String apellido2;

    public clsEmpleado(){
    }

    public clsEmpleado(String nombre, String apellido1, String apellido2) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    public String getNombre() {
        System.out.println("el nombre del empleado es: " + this.nombre);
        return nombre;
    }

    public void setNombre(String nombre) {        
        if(nombre.matches("[a-zA-Z]*")){
            System.out.println("Nombre valido");
            this.nombre = nombre;
        }else{
            System.out.println("Nombre no valido: " + nombre);
        }      
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    } 
    
}
