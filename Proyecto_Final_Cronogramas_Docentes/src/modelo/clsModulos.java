package modelo;

public class clsModulos {
    private String codigo;
    private String nombre;
    private String duracion;
    private String sectores;

    public clsModulos() {}
    
    public clsModulos(String codigo, String nombre, String duracion, String sectores) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.duracion = duracion;
        this.sectores = sectores;
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

    public String getSectores() {
        return sectores;
    }

    public void setSectores(String sectores) {
        this.sectores = sectores;
    }

    
    
    
}
