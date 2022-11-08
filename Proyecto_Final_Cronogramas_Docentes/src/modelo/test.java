/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        clsDocente miguel;
        miguel = new clsDocente("119200708407", "64524289", "Miguel Fernandez", "Calle Fallas");
       
        System.out.println(miguel.getCedula());
        System.out.println(miguel.getDireccion());
        System.out.println(miguel.getTelefono());

        miguel.setCedula("200119708407");
        System.out.println(miguel.getCedula());
    }
    
}
