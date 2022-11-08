/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class clsModulos {
    private int codigoModulos;
    private int duracionModulos;
    private String nombreModulos;
    private String sectorModulos;

    public clsModulos(int codigoModulos, int duracionModulos, String nombreModulos, String sectorModulos) {
        this.codigoModulos = codigoModulos;
        this.duracionModulos = duracionModulos;
        this.nombreModulos = nombreModulos;
    }

    public int getCodigoModulos() {
        return codigoModulos;
    }

    public void setCodigoModulos(int codigoModulos) {
        this.codigoModulos = codigoModulos;
    }

    public int getDuracionModulos() {
        return duracionModulos;
    }

    public void setDuracionModulos(int duracionModulos) {
        this.duracionModulos = duracionModulos;
    }

    public String getNombreModulos() {
        return nombreModulos;
    }

    public void setNombreModulos(String nombreModulos) {
        this.nombreModulos = nombreModulos;
    }

    public String getSectorModulos() {
        return sectorModulos;
    }

    public void setSectorModulos(String sectorModulos) {
        this.sectorModulos = sectorModulos;
    }
    
    
    
}
