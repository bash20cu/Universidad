
package modelo;


public class clsHorario {
   private int horasDia;
   clsDocente docente = new clsDocente();
   private String estado;

    public clsHorario() {
    }

    public clsHorario(int horasDia, String estado, clsDocente docente) {
        this.horasDia = horasDia;
        this.estado = estado;
        this.docente = docente;
    }

    public int getHorasDia() {
        return horasDia;
    }

    public void setHorasDia(int horasDia) {
        this.horasDia = horasDia;
    }

    public clsDocente getDocente() {
        return docente;
    }

    public void setDocente(clsDocente docente) {
        this.docente = docente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDatosHorario(){
        String Horario = "\n"+
                "Horas por dia: " + getHorasDia() + "\n "+
                "Docente: " + getDocente().getPersona()+ "\n "+
                "Estado: " + getEstado()+"\n ";  
        return Horario;
    }
}
