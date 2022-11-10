/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsProfesor extends clsPersona{
    private String cedula;
    private String email;
    
    public clsProfesor() {
    }

   public clsProfesor(String cedula, String email, String nombre, String apellido1, String apellido2, String telefono) {
        super(nombre, apellido1, apellido2, telefono);
        this.cedula = cedula;
        this.email = email;
    }

   public String getCedula() {
        return cedula;
    }

   public void setCedula(String cedula) {
        this.cedula = cedula;
    }

   public String getEmail() {
        return email;
    }

   public void setEmail(String email) {
        this.email = email;
    }    
}
