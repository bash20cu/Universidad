/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsCentrosFormacion {
    private int codigoCentro; 
    private String nombreCentr; 
    private String direccionCentro;
    private String telefonoCentro;

    public clsCentrosFormacion(int codigoCentro, String nombreCentr, String direccionCentro, String telefonoCentro) {
        this.codigoCentro = codigoCentro;
        this.nombreCentr = nombreCentr;
        this.direccionCentro = direccionCentro;
        this.telefonoCentro = telefonoCentro;
    }

    public int getCodigoCentro() {
        return codigoCentro;
    }

    public void setCodigoCentro(int codigoCentro) {
        this.codigoCentro = codigoCentro;
    }

    public String getNombreCentr() {
        return nombreCentr;
    }

    public void setNombreCentr(String nombreCentr) {
        this.nombreCentr = nombreCentr;
    }

    public String getDireccionCentro() {
        return direccionCentro;
    }

    public void setDireccionCentro(String direccionCentro) {
        this.direccionCentro = direccionCentro;
    }

    public String getTelefonoCentro() {
        return telefonoCentro;
    }

    public void setTelefonoCentro(String telefonoCentro) {
        this.telefonoCentro = telefonoCentro;
    }
    
}
