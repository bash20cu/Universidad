package modelo;

import java.util.ArrayList;

public class clsDocente extends clsPersona {

    private ArrayList<clsCurso> cursos = new ArrayList<clsCurso>();
    private int totalHoras = 0;
    private String jornada;

    public clsDocente() {
    }
    

    public clsDocente(String jornada, String cedula, String Nombre,
            String Apellidos, String Telefono, String Direccion) {
        super(cedula, Nombre, Apellidos, Telefono, Direccion);
        this.jornada = jornada;
    }

    public clsDocente(clsCurso cursos, String jornada, String cedula, String Nombre,
            String Apellidos, String Telefono, String Direccion) {
        super(cedula, Nombre, Apellidos, Telefono, Direccion);
        this.jornada = jornada;
        this.cursos.add(cursos);
    }

    public ArrayList<clsCurso> getCursos() {
        this.cursos.forEach((e) -> {
        System.out.println(e.getDatosCursos());
        this.totalHoras += e.getTotalHoras();
        });
        System.out.println("Total de horas: "+this.getTotalHoras()+"\n");
        return cursos;
    }

    public void setCursos(clsCurso curso){
        this.cursos.add(curso);
    }
    public void setCursos(ArrayList<clsCurso> cursos) {
        this.cursos = cursos;
    }
      
    
    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    } 

    public int getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(int totalHoras) {
        this.totalHoras += totalHoras;
    }  
    
}