package modelo;

public class clsCurso {

    private String curso;
    private String fechaInicio;
    private String fechaFin;
    private int totalHoras;

    public clsCurso() {
    }

    public clsCurso(String cursos, String fechaInicio, String fechaFin, int totalHoras) {
        this.curso = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalHoras = totalHoras;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String cursos) {
        this.curso = cursos;
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

    public String getDatosCursos() {
        String curso = "Nombre del curso: " + getCurso() + "\n"
                + "Fecha de Inicio del curso: " + getFechaInicio() + "\n"
                + "Fecha Final del curso: " + getFechaFin() + "\n"
                + "Total de horas: " + getTotalHoras() + "\n";
        return curso;
    }

}
