package modelo;

public class clsModulos {
    private String codigo;
    private String nombre;
    private String duracion;
    private String sector;

    public clsModulos() {}
    
    public clsModulos(String codigo, String nombre, String duracion, String sectores) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.duracion = duracion;
        this.sector = sectores;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getModulo(){
        String modulo = "\n"+
                "Nombre del modulo: "+getNombre()+"\n"+
                "Codigo del modulo: "+getCodigo()+"\n"+
                "Duracion del modulo: "+getDuracion()+"\n"+
                "Sector del modulo: "+getSector()+"\n";
        return modulo;
    }
    
}
