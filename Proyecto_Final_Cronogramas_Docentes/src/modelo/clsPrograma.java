
package modelo;

public class clsPrograma {
    clsCentroFormacion centroFormacion = new clsCentroFormacion();
    clsHorario horario = new clsHorario();
    private String sector;
    private String codigoPrograma;
    private String grupo;
    private int anno;
   

    public  clsPrograma(){}
    
    public clsPrograma(String referencia, String codigo, String sector, String grupo, int anno) {
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
    }
    
    
    public clsPrograma(clsCentroFormacion centroFormacion,clsHorario horario, String codigo, String sector, String grupo, int anno) {
        this.centroFormacion = centroFormacion;
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
        this.horario = horario;
    }
    

    public clsCentroFormacion getCentroFormacion() {
        System.out.println("\n"+centroFormacion.getDatos()+"\n");
        return centroFormacion;
    }

    public void setCentroFormacion(clsCentroFormacion centroFormacion) {
        this.centroFormacion = centroFormacion;
    }

   public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public clsHorario getHorario() {
        return horario;
    }

    public void setHorario(clsHorario horario) {
        this.horario = horario;
    }

    public String getCodigoPrograma() {
        return codigoPrograma;
    }

    public void setCodigoPrograma(String codigoPrograma) {
        this.codigoPrograma = codigoPrograma;
    }
     
}
