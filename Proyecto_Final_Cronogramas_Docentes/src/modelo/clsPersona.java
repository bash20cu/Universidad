package modelo;

public class clsPersona {

    private String cedula;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;

    public clsPersona() {
    }

    public clsPersona(String cedula, String Nombre, String Apellidos, String Telefono, String Direccion) {
        this.cedula = cedula;
        this.nombre = Nombre;
        this.apellidos = Apellidos;
        this.telefono = Telefono;
        this.direccion = Direccion;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Nombre) {
        this.nombre = Nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String Apellidos) {
        this.apellidos = Apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String Telefono) {
        this.telefono = Telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String Direccion) {
        this.direccion = Direccion;
    }
    
    public String getPersona(){
        String persona = "Nombre: " +getNombre()+ "\n"+
                "Apellidos: "+getApellidos()+"\n"+
                "Telefono: "+getTelefono()+"\n"+
                "Direccion: "+getDireccion()+"\n"+
                "Cedula: "+getCedula();        
        return persona;
    }

}
