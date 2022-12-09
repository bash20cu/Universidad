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
        clsCentroFormacion uia = new clsCentroFormacion("aa1", "UIA",
                "San Jose", "0001-0002");
        
        //clsPrograma programa1  =  new clsPrograma(uia,
          //      "0001", "aa10", "Idiomas", "2B", 2020);
          
        clsPrograma programa1  =  new clsPrograma(uia.getCodigo(), "aa10", "Idiomas", "2B", 2020);
        //System.out.println(programa1.getCentroFormacion());
        
        System.out.println(programa1.getReferencia());
       
        
    }
    
}
