/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio.pkg2;

/**
 *
 * @author migue
 */
public class Laboratorio2 {

    /**
     * @param args the command line arguments
     * CAMINOS ENTRE PRUEBLOS
     * Una región esta formada por n pueblos dispersos. Hay conexiones directas entre algunos de ellos,
     * y entre otros no existe conexión, aunque puede haber un camino.
     * Escribir una aplicación que tenga como entrada la matriz que representa las conexiones directas entre pueblos,
     * de tal forma que el elemento M(i, j) de la matriz sea: M(i,j)= {█(0 si no hay conexion directa entre pueblo i y
     *  pueblo j@d   hay conexion entre pueblo i y pueblo j de distancia d.)┤
     * Tenga también como entrada un par de pueblos (x, y). 
     * La aplicación tiene que encontrar un camino entre ambos pueblos utilizando técnicas recursivas. 
     * La salida ha de ser la ruta que ha de seguir para ir de x a y junto a la distancia de la ruta.
     */

    public static void main(String[] args) {
       // TODO code application logic here
               
        String[] MatrizPueblo = {"A", "B", "C", "D", "E"};
        int[][] MatrizCaminos = new int[5][5];
        MatrizCaminos[0][1] = 10; // A-B tiene distancia 10
        MatrizCaminos[1][0] = 10; // B-A tiene distancia 10
        MatrizCaminos[0][3] = 15; // A-D tiene distancia 15
        MatrizCaminos[3][0] = 15; // D-A tiene distancia 15
        MatrizCaminos[3][2] = 15; // D-C tiene distancia 15
        MatrizCaminos[1][2] = 12; // B-C tiene distancia 12
        MatrizCaminos[2][1] = 12; // C-B tiene distancia 12
        MatrizCaminos[2][4] = 8; // C-E tiene distancia 8
        MatrizCaminos[4][2] = 8; // E-C tiene distancia 8
        MatrizCaminos[4][0] = 8; // E-A tiene distancia 8
        
        Camino CaminoPueblos = new Camino();
                
        String[] CaminoAE = CaminoPueblos.camino(MatrizPueblo, MatrizCaminos, "A", "E",false);
                
        for(String elemento : CaminoAE) 
            System.out.print(elemento + " - ");        
    }    
}
