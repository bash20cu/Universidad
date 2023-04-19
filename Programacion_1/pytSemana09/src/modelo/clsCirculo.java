/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsCirculo  implements iMetodos{
     private double radio;

   public clsCirculo() {
    }

   public clsCirculo(double radio) {
        this.radio = radio;
    }

   public double getRadio() {
        return radio;
    }

   public void setRadio(double radio) {
        this.radio = radio;
    }
       
    
    @Override
    public double area() {
        return Math.PI * Math.pow(radio, 2);
   }

   @Override
    public double perimetro() {
        return 2*Math.PI*radio;
    }
}
