/*
 * Autor: Juan Carlos Arcila Diaz
 * Localidad: Chiclayo-Peru
 * Email:carlos_ad_6@hotmail.com
 * Para Comunidad IncanatoHack.com
 */

package objetos;

public class Enlace{
    private Arista arista;
    private Nodo nodo;

    /**
     * Constructor para objetos de la clase Arista
     */
    public Enlace(){
        // inicializando variables de instancia
        this(new Arista(),new Nodo());
    }
    public Enlace(Arista arista,Nodo nodo){
        // inicializando variables de instancia
        this.arista = arista;
        this.nodo = nodo;
    }
    public void setArista(Arista arista){
        this.arista = arista;
    }
    public Arista getArista(){
        return arista;
    }
    public void setNodo(Nodo nodo){
        this.nodo = nodo;
    }
    public Nodo getNodo(){
        return nodo;
    }
}
