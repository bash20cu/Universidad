/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author miguel
 */
public class clsCronograma {
    private String cursos;
    private String fechaInicio;
    private String fechaFin;
    private int totalHoras;

    public clsCronograma() {
    }

    public clsCronograma(String cursos, String fechaInicio, String fechaFin, int totalHoras) {
        this.cursos = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalHoras = totalHoras;
    }

    public String getCursos() {
        return cursos;
    }

    public void setCursos(String cursos) {
        this.cursos = cursos;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public int getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(int totalHoras) {
        this.totalHoras = totalHoras;
    } 
    
}
