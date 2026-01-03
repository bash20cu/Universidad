package analisis1martes;

public class Esfera extends Circulo implements iArea,iVolumen{
    
    
    Esfera(int x, int y, double radio) {
        super(x, y, radio);
    }
    
    @Override
    public double area() {
        return 4 * Math.PI * Math.pow(radio, 2);
    }
    
    @Override
    public double volumen() {
        return (4f/3f) * Math.PI * Math.pow(radio, 3);
    }
    
    @Override
    public void nombre() {
        System.out.println("Soy una esfera");
    }
    
}
