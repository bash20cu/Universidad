package laboratorio4.aerolineas.modelo;

/**
 *
 * @author miguel
 * Clase especializada en Pasajero
 */
public class Pasajero {

    //Variables
    //Nombre, cedula,destino del pasajero
    private String nombre, cedula, destino;

 /**
  * Constructor 
  * @param nombre, recibe una cadena de texto con el nombre
  * @param cedula, recibe una cadena de texto con la cedula
  * @param destino,  recibe una cadena de texto con el destino
  */
    public Pasajero(String nombre, String cedula, String destino) {
        this.nombre = nombre;
        this.cedula = cedula;
        this.destino = destino;
    }

    //Metodos
    
    /**
     * Metodo para obtener el nombre
     * @return nombre , una cadena de texto con el nombre;
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Metodo para obtener la cedula
     * @return cedula, una cadena de texto con la cedula;
     */
    public String getCedula() {
        return cedula;
    }
    
    /**
     * Metodo para obtener el destino
     * @return destino, una cadena de texo con el destino
     */
    public String getDestino() {
        return destino;
    }
    
    /**
     * Metodo para guardar el nombre
     * @param nombre, recibe una cadena de texto con el nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Metodo para guardar la cedula
     * @param cedula , recibe una cadena de texto con la cedula
     */
    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    /**
     * Metodo para guardar el destino
     * @param destino , recibe una cadena de texto con el destino
     */
    public void setDestino(String destino) {
        this.destino = destino;
    }
    

}
