/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsRectangulo implements iMetodos {
    private double largo;
    private double ancho;

   public clsRectangulo() {
    }
   
   public clsRectangulo(double largo, double ancho) {
        this.largo = largo;
        this.ancho = ancho;
    }

   public double getLargo() {
        return largo;
    }

   public void setLargo(double largo) {
        this.largo = largo;
    }

   public double getAncho() {
        return ancho;
    }

   public void setAncho(double ancho) {
        this.ancho = ancho;
    }   
    
    @Override
    public double area() {
        return this.largo*this.ancho;
    }

   @Override
    public double perimetro() {
        return this.largo*2+this.ancho*2;
    }
}
