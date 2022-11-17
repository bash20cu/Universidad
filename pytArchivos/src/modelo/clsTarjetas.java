/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author Est_Nautico
 */
public class clsTarjetas extends clsMetodos{
    //private static final long serialVersionUID=6529685098267777690L; //determina la version de la clase
    private String cedula;
    private int numeroCuenta;
    private double montoLimite;
    private double saldo;

    public clsTarjetas() {
    }

    public clsTarjetas(String cedula, int numeroCuenta, double montoLimite, double saldo) {
        this.cedula = cedula;
        this.numeroCuenta = numeroCuenta;
        this.montoLimite = montoLimite;
        this.saldo = saldo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getMontoLimite() {
        return montoLimite;
    }

    public void setMontoLimite(double montoLimite) {
        this.montoLimite = montoLimite;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public int guardar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int modificar(Object dato) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminar(int cedula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Object> getRegistros() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRegistro(int cedula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
