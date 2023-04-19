package lab.vi;


public class Nodo {
    int numeroNodo;
    String nombreCancion;
    String nombreCantante;
    int numeroProcesamiento;
    Nodo siguiente;

    public Nodo(int numeroNodo, String nombreCancion, String nombreCantante) {
        this.numeroNodo = numeroNodo;
        this.nombreCancion = nombreCancion;
        this.nombreCantante = nombreCantante;
        this.numeroProcesamiento = fibonacci(numeroNodo);
        this.siguiente = null;
    }
    
    /**
     * Método que calcula el n-ésimo término de la serie de Fibonacci de manera recursiva.
     * La serie de Fibonacci es una secuencia de números en la que cada número es la suma de los dos números anteriores.
     * El primer y segundo término son 0 y 1 respectivamente.
     * @param n el término que se desea calcular de la serie de Fibonacci.
     * @return el valor del n-ésimo término de la serie de Fibonacci.
     */
    private int fibonacci(int n) {
        if (n <= 1) {
            return n;
        } else {
            return fibonacci(n-1) + fibonacci(n-2);
        }
    }
}
