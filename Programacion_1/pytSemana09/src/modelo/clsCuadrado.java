/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsCuadrado implements iMetodos{
    private double lado;

   public clsCuadrado() {
    }

   public clsCuadrado(double lado) {
        this.lado = lado;
    }

   public double getLado() {
        return lado;
    }

   public void setLado(double lado) {
        this.lado = lado;
    }

   @Override
    public double area() {
        return this.lado * this.lado;
    }

   @Override
    public double perimetro() {
        return this.lado+this.lado+this.lado+this.lado;
    }
}
