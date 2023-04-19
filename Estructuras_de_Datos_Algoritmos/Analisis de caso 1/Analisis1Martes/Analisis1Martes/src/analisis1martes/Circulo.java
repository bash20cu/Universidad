package analisis1martes;

public class Circulo extends Punto implements iArea{
    
    protected double radio;

    public Circulo() {
    }
    
       
    Circulo(int x, int y, double radio) {
        this.radio = radio;
    }

    public double getRadio() {
        return radio;
    }

    public void setRadio(double radio) {
        this.radio = radio;
    }
    
    @Override
    public double area() {
        return Math.PI * Math.pow(radio, 2);
    }
    
    @Override
    public void nombre() {
        System.out.println("Soy un circulo");
    }
    
}
