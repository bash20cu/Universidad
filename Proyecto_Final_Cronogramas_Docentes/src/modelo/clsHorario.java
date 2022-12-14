
package modelo;

import java.io.Serializable;


public class clsHorario implements Serializable {
   private static final long serialVersionUID = 6529685098267777690L; //determina la version de la clase 
   private int horasDia;
   clsDocentes docente = new clsDocentes();
   private String estado;

    public clsHorario() {
    }

    public clsHorario(int horasDia, String estado, clsDocentes docente) {
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

    public clsDocentes getDocente() {
        return docente;
    }

    public void setDocente(clsDocentes docente) {
        this.docente = docente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}
