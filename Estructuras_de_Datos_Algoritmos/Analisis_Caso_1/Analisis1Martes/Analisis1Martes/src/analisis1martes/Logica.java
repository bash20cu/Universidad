package analisis1martes;

public class Logica {
    
    Punto[] listafiguras = new Punto[5];
    
    Logica() {
        inicializar();
    }
    
    private void inicializar() {
        int numero;
        for(int i = 0; i < listafiguras.length; i++) {
            numero = (int) (1 + Math.random() * 3);
            switch(numero) {
                case 1:
                    listafiguras[i] = new Punto();
                    break;
                case 2:
                    listafiguras[i] = new Circulo(10, 15, 20);
                    break;
                case 3:
                    listafiguras[i] = new Esfera(10, 15, 20);
                    break;
            }
        }
        
        
        Circulo circulo = new Circulo(5, 6, 2);
        
        System.out.println(circulo.area());
        
        Esfera esfera = new Esfera(5, 10, 3);
        
        System.out.println(esfera.volumen());
    }
    
    public void programa() {
        for(int i = 0; i < listafiguras.length; i++)
            listafiguras[i].nombre();
    }
    
}
