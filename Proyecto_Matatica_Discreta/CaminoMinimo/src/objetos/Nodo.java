/*
 * Autor: Juan Carlos Arcila Diaz
 * Localidad: Chiclayo-Peru
 * Email:carlos_ad_6@hotmail.com
 * Para Comunidad IncanatoHack.com
 */

package objetos;
import java.util.ArrayList;

public class Nodo{
    private Object dato;
    private ArrayList<Enlace> listaNodoAdyacente;
    private boolean visitado = false;    
    private Circulo circulo;
    private int  izquierda;

    private int longitudCamino; //Para uso de la clase Disjktra
    private Nodo nodoAntecesorDisjktra; //Para uso de la clase Disjktra
    private boolean marca;//Para uso de la clase Disjktra

    public Nodo(){
        this.dato = new Object();
        circulo = null;
        izquierda = 0;
        inicializarParaDisjktra();
    }

    private void inicializarParaDisjktra(){
        longitudCamino = -1;
        nodoAntecesorDisjktra = null;
        marca = false;
    }

    public Nodo(Object dato,Coordenadas coordenada){
        this.dato = dato;
        listaNodoAdyacente = new ArrayList<Enlace>();
        circulo = new Circulo(coordenada);
        circulo.setIzquierda(izquierda);
        inicializarParaDisjktra();
    }

    public void setDato(Object dato){
        this.dato = dato;
    }
    public Object getDato(){
        return this.dato;
    }
    public ArrayList<Enlace> getListaNodoAdyacente(){
        ArrayList<Enlace> listAristaAux = null;
        if(!listaNodoAdyacente.isEmpty()){
            listAristaAux = new ArrayList<Enlace>();
            for(Enlace enlace:listaNodoAdyacente){
                if(enlace.getArista().isHabilitado()){
                    listAristaAux.add(enlace);
                }
            }
        }        
        return listAristaAux;
    }

   public void addNodoAdyacente(Enlace arista){
        listaNodoAdyacente.add(arista);
    }
    public void addNodoAdyacente(Arista via,Nodo nodo){
        this.addNodoAdyacente(new Enlace(via,nodo));
    }
    public void setVisitado(boolean visitado){
        this.visitado = visitado;
    }
    public boolean isVisitado(){
        return visitado;
    }
    
    public Circulo getCirculo() {
        return circulo;
    }

    public void setCirculo(Circulo circulo) {
        this.circulo = circulo;
    }

    public int getLongitudCamino() {
        return longitudCamino;
    }

    public void setLongitudCamino(int longitudCamino) {
        this.longitudCamino = longitudCamino;
    }

    public boolean isMarca() {
        return marca;
    }

    public void setMarca(boolean marca) {
        this.marca = marca;
    }

    public Nodo getNodoAntecesorDisjktra() {
        return nodoAntecesorDisjktra;
    }

    public void setNodoAntecesorDisjktra(Nodo nodoAntecesorDisjktra) {
        this.nodoAntecesorDisjktra = nodoAntecesorDisjktra;
    }
    
}