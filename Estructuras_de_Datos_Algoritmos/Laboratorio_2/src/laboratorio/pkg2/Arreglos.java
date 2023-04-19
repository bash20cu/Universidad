/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package laboratorio.pkg2;

import java.util.ArrayList;

/**
 *
 * @author migue
 */
public class Arreglos {
    
    // Método que devuelve si un Arreglo contiene o no un elemento 
  protected boolean contieneElemento(String[] array, String elemento) {
    for (String s : array) {
      if (s.equals(elemento)) {
        return true;
      }
    }
    return false;
  }
  
  // Método que devuelve el índice de un elemento en un Arreglo 
  protected int indice(String[] array, String elemento) {
    for (int i=0; i<array.length; i++) {
      if (array[i].equals(elemento)) {
        return i;
      }
    }
    return -1;
  }
  
  // Método que devuelve el mínimo de una lista de Arreglos según su longitud 
  protected String[] minimo(ArrayList<String[]> lista) {
    int min = Integer.MAX_VALUE; 
    int index = -1; 
    for (int i=0; i<lista.size(); i++) {
      if (lista.get(i).length < min) {
        min = lista.get(i).length;
        index = i;
          }
    }
    return lista.get(index);
  }
  
  // Método que devuelve el maximo de una lista de Arreglos según su longitud 
  protected String[] maximo(ArrayList<String[]> lista) {
    int max = Integer.MIN_VALUE; 
    int index = 0; 
    for (int i=0; i<lista.size(); i++) {
      if (lista.get(i).length > max) {
        max = lista.get(i).length;
        index = i;
        }
    }
    return lista.get(index);
  }
  
  // Método auxiliar que inserta un elemento al principio de un Arreglo 
  protected String[] InsertarInicio(String[] array, String elemento) {
    String[] ArregloAuxiliar = new String[array.length+1];
    ArregloAuxiliar[0] = elemento;
    for (int i=0; i<array.length; i++) {
      ArregloAuxiliar[i+1] = array[i];
    }
    return ArregloAuxiliar;
  }
  
}
