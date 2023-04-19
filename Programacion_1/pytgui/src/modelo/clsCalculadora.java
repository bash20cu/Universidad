package modelo;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author migue
 */

public class clsCalculadora {
    private int numero1;
    private int numero2;

    public clsCalculadora() {
    }

    
    public clsCalculadora(int numero1, int numero2) {
        this.numero1 = numero1;
        this.numero2 = numero2;
    }

    public int getNumero1() {
        return numero1;
    }

    public int getNumero2() {
        return numero2;
    }

    public void setNumero1(int numero1) {
        this.numero1 = numero1;
    }

    public void setNumero2(int numero2) {
        this.numero2 = numero2;
    }

    //metodos personaliados
    public double Suma(){
        return this.numero1 + this.numero2;
    }
    public double Resta(){
        return this.numero1 - this.numero2;
    }
    public double Multiplicacion(){
        return this.numero1 * this.numero2;
    }
    public double Division(){
        return this.numero1 / this.numero2;
    }
    
}
