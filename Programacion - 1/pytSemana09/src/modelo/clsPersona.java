/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsPersona {
    private String nombre;
    private String apellido1;
    private String apellido2;   
    private String telefono;



   public clsPersona() {
    }

   public clsPersona(String nombre, String apellido1, String apellido2, String telefono) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.telefono = telefono;
    }

   public String getNombre() {
        return nombre;
    }

   public void setNombre(String nombre) {
        this.nombre = nombre;
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

   public String getTelefono() {
        return telefono;
    }

   public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
     public String getNombreCompleto(){
        return this.nombre + " " + this.apellido1 + " " +this.apellido2;
    }   
    
}
