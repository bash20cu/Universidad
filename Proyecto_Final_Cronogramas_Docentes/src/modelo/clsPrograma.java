
package modelo;

public class clsPrograma {
    clsCentroFormacion centroFormacion = new clsCentroFormacion();
    clsDocente docente = new clsDocente();
    private String referencia;
    private String codigo;
    private String sector;
    private String grupo;
    private int anno;
    private String horario;
    private int horasPorDia;
    private String estado;

    public  clsPrograma(){}
    
    public clsPrograma(String referencia, String codigo, String sector, String grupo, int anno) {
        this.referencia = referencia;
        this.codigo = codigo;
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
    }
    
    
    public clsPrograma(clsCentroFormacion centroFormacion, String codigo, String sector, String grupo, int anno) {
        this.centroFormacion = centroFormacion;
        this.referencia = centroFormacion.getCodigo();
        this.codigo = codigo;
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
    }
    

    public clsCentroFormacion getCentroFormacion() {
        System.out.println(centroFormacion.getDatos());
        return centroFormacion;
    }

    public void setCentroFormacion(clsCentroFormacion centroFormacion) {
        this.centroFormacion = centroFormacion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    
    
    
    
    
}
