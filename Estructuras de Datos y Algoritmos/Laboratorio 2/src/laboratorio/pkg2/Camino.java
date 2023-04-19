/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package laboratorio.pkg2;

import java.util.ArrayList;

/**
 *
 * @author miguel fernandez Si x o y no están en la región, devolver una lista
 vacía []. Si x e y son el mismo pueblo, devolver una lista con solo x [x]. Si
 hay una conexión directa entre x e y, devolver una lista con x e y [x,y]. Si
 no hay una conexión directa entre x e y, buscar todos los pueblos pueblo que
 tengan una conexión directa con x y que no hayan sido PueblosVisitados antes. Para
 cada pueblo,llamar recursivamente a la función camino con los parámetros (g,pueblo,y) y
 añadir x al principio de la lista resultante. Devolver la lista más corta de
 todas las posibles.
 */
public class Camino extends Arreglos {

    // Lista de pueblos PueblosVisitados (inicialmente solo PuebloX)
    ArrayList<String> PueblosVisitados = new ArrayList<String>();

    // Lista de posibles caminos desde PuebloX hasta PuebloY
    ArrayList<String[]> PosiblesCaminos = new ArrayList<String[]>();

    //Distancia recorrida
    int distancia = 0;
   
            
    // Método que devuelve un array con el camino entre dos pueblos PuebloX e PuebloY
    public String[] camino(String[] MatrizPueblos, int[][] MatrizCaminos, String PuebloX, String PuebloY, boolean min) {        
        
        // Casos base
        // Si PuebloX o PuebloY no están en la región
        if (!contieneElemento(MatrizPueblos, PuebloX) || !contieneElemento(MatrizPueblos, PuebloY)){
           
            // Devolver array vacío 
            System.out.println("Uno de los pueblos no esta en la Region declarada en la matriz");
            return new String[0];             
        } 
        else if (PuebloX.equals(PuebloY)){ 
            
            // Si PuebloX y PuebloY son el mismo pueblo
            String[] resultado = new String[1];            
            
            // Devolver array con solo PuebloX
            resultado[0] = PuebloX; 
            System.out.print("Ya estas en el pueblo: ");
            return resultado;            
        } 
        else if (MatrizCaminos[indice(MatrizPueblos, PuebloX)][indice(MatrizPueblos, PuebloY)] > 0){
            
            // Si hay conexión directa entre PuebloX e PuebloY
            String[] resultado = new String[2];
            PueblosVisitados.add(PuebloX);
            
            //Acumular distancia recorrida 
            distancia += (MatrizCaminos[indice(MatrizPueblos, PuebloX)][indice(MatrizPueblos, PuebloY)]);
            
            // Devolver array con PuebloX e PuebloY
            resultado[0] = PuebloX; 
            resultado[1] = PuebloY;
            System.out.println("Existe Conexion directa entre los pueblos: " + PuebloX + PuebloY);            
            return resultado;           
        }   
        else { 
            // Caso recursivo
            // Lista de pueblos PueblosVisitados (inicialmente solo PuebloX)
            PueblosVisitados.add(PuebloX);            
           
            // Para cada pueblo en la región
            for (String pueblo : MatrizPueblos){
                
                // Si hay conexión directa entre PuebloX y PuebloY, pueblo no ha sido visitado
                if (MatrizCaminos[indice(MatrizPueblos, PuebloX)][indice(MatrizPueblos, pueblo)]
                        > 0 && !PueblosVisitados.contains(pueblo)){
                    
                    System.out.println("Existe una coneccion directa entre los pueblos: "+pueblo+" "+PuebloY);

                    // Añadir pueblo a la lista de PueblosVisitados
                    PueblosVisitados.add(pueblo);
                    
                    //Acumular distancia recorrida 
                    distancia += (MatrizCaminos[indice(MatrizPueblos, PuebloX)][indice(MatrizPueblos, pueblo)]);

                    // Llamar recursivamente a camino con (MatrizPueblos,MatrizCaminos,pueblo,PuebloY)
                    String[] CaminoAuxiliar = camino(MatrizPueblos, MatrizCaminos, pueblo, PuebloY, min);                    
                    
                    // Insertar PuebloX al principio del resultado 
                    CaminoAuxiliar = InsertarInicio(CaminoAuxiliar, PuebloX);

                    // Añadir el CaminoAuxiliar a la lista de PosiblesCaminos
                    PosiblesCaminos.add(CaminoAuxiliar);
                } 
                else{
                   //System.out.println("No existe coneccion directa entre los pueblos : " + pueblo + " " + PuebloY);
                }                
            }
            
            
            // Si hay algún candidato
            if (!PosiblesCaminos.isEmpty()){ 
                System.out.println("Distancia Recorrida por el Algortimo: " + distancia + " Km");
                if (!min)
                    // Devolver el candidato más largo según su longitud
                    return maximo(PosiblesCaminos);
                
                else                   
                // Devolver el candidato más corto según su longitud
                return minimo(PosiblesCaminos);
            } 
            // Si no hay ningún candidato
            else{  
                // Devolver array vacío 
                return new String[0]; 
            }
        }       
        
    }    
}