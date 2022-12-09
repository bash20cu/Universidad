package modelo;

public class clsCentroFormacion {

    private String codigo;
    private String nombre;
    private String direccion;
    private String telefono;

    public clsCentroFormacion() {
    }

    public clsCentroFormacion(String codigo, String nombre, String direccion, String telefono) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public String getDatos(){
        String datos = "Codigo: " + getCodigo() + "\n "+
                " Nombre Centro Formacion: " + getNombre()+ "\n "+
                " Direccion Centro Formacion: " + getDireccion()+"\n "+
                " Telefono centro formacion: " + getTelefono();  
        return datos;
    }
}
