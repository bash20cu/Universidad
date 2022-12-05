package objetos;

public class Arista{
    private int idArista;
    private String nombreArista;
    private int distancia;//Distancia entre dos puntos
    private LineaQuebrada lineaQuebrada;//Ésta es la via representada graficamente    
    private boolean habilitado;

    public Arista(){
        // Inicializando variables de instancia
        this(-1,"");
    }
    public Arista(int idArista){
        // Inicializando variables de instancia
        this(-1,"");
    }
    public Arista(int idArista,String nombreArista){
        // Inicializando variables de instancia
        this(-1,"",1);
    }
    public Arista(int idArista,String nombreArista,int peso){
        // Inicializando variables de instancia
        this.idArista = idArista;
        this.nombreArista = nombreArista;
        this.distancia = peso;
        lineaQuebrada = null;
        habilitado = true;
    }
    public void setIdArista(int idArista){
        this.idArista = idArista;
    }
    public int getIdArista(){
        return idArista;
    }
    public void setNombreArista(String nombreVia){
        this.nombreArista = nombreVia;
    }
    public String getNombreArista(){
        return nombreArista;
    }
    public void setPeso(int peso){
        this.distancia = peso;
    }
    public int getPeso(){
        return distancia;
    }
    public void setLineaQuebrada(LineaQuebrada lineaQuebrada){
        this.lineaQuebrada = lineaQuebrada;
        if(lineaQuebrada != null){
            this.lineaQuebrada.setLongitud(distancia);
        }
    }
    public LineaQuebrada getLineaQuebrada(){
        return lineaQuebrada;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}
