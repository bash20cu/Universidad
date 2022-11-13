/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.regex.Pattern;

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
        this.nombre = nombre.toUpperCase();
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1.toUpperCase();
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2.toUpperCase();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    //Metodos personalizados   
    

    //Metodo para devolver el nombre completo
    public String nombreCompleto(){
        return (this.nombre +" " + this.apellido1 +" " + this.apellido2); 
    }
    
    //Validacion de datos
    private boolean validacion(String atributo,String dato){ 
        String NAME_REGEX;
        Pattern NAME_PATTERN;
        
        switch (atributo) {
            case "nombre","apellido1","apellido2":
                NAME_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
                NAME_PATTERN = Pattern.compile(NAME_REGEX);

                if(NAME_PATTERN.matcher(dato).matches()){                    
                    //this.nombre = dato;
                    if(atributo.equals(nombre)){
                        System.out.println("Nombre valido");
                        this.nombre = nombre;
                    }else if(atributo.equals(apellido1)){
                        this.apellido1 = apellido1;
                    }else{
                        this.apellido2 = apellido2;
                    }
                    return true;
                }else{
                    System.out.println("Nombre no valido");
                    return false;
                }
                case "telefono":
                NAME_REGEX = "^(?:(?:(?:0?[13578]|1[02])(\\/|-|\\.)31)\\1|" +
                            "(?:(?:0?[1,3-9]|1[0-2])(\\/|-|\\.)(?:29|30)\\2))" +
                            "(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:0?2(\\/|-|\\.)29\\3" +
                            "(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|" +
                            "[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|" +
                            "^(?:(?:0?[1-9])|(?:1[0-2]))(\\/|-|\\.)(?:0?[1-9]|1\\d|" +
                            "2[0-8])\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$";;
                NAME_PATTERN = Pattern.compile(NAME_REGEX);

                if(NAME_PATTERN.matcher(dato).matches()){
                    System.out.println("Nombre valido");
                    this.telefono = dato;
                    return true;
                }else{
                    System.out.println("Nombre no valido");
                    return false;
                }
            default:
                throw new AssertionError();
        }              
    }
    
}
