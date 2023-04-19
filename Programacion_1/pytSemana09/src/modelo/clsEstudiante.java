/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author migue
 */
public class clsEstudiante extends clsPersona{
    private String carnet;
    private String encargado;
    public ArrayList<clsNotas> notas;

    public clsEstudiante(String carnet, String encargado, ArrayList<clsNotas> notas) {
        this.carnet = carnet;
        this.encargado = encargado;
        this.notas = notas;
    }

    public clsEstudiante(String carnet, String encargado, ArrayList<clsNotas> notas, String nombre, String apellido1, String apellido2, String telefono) {
        super(nombre, apellido1, apellido2, telefono);
        this.carnet = carnet;
        this.encargado = encargado;
        this.notas = notas;
    }

    public clsEstudiante() {
        this.notas = new ArrayList<clsNotas>();
    }

    public String getCarnet() {
        return carnet;
    }

    public void setCarnet(String carnet) {
        this.carnet = carnet;
    }

    public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }
    
    
    
}
