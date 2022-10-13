/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytsemana5;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author migue
 */
public class listaedades {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
Scanner in = new Scanner(System.in);
        Map<String,Integer> mascotas=new HashMap<String,Integer>();
        String nombre;
        int edad;
        //agregar un elemento al diccionario
        System.out.println("Nombre de la mascota:");
        nombre=in.next();
        System.out.println("Edad de la mascota:");
        edad=in.nextInt();
        mascotas.put(nombre,edad);
        
        mascotas.put("Fofo", 10);
        mascotas.put("Momo", 12);
        mascotas.put("Titan", 12);
        
        for (Object m : mascotas.keySet()) {
            System.out.println(m + " tiene " + mascotas.get(m) + " años");
        }
        
        mascotas.put("Fofo", 155); //modifica el valor de la clave existente
        
        for (Object m : mascotas.keySet()) {
            System.out.println(m + " tiene " + mascotas.get(m) + " años");
        }
        
        mascotas.remove("Fofo");
        for (Object m : mascotas.keySet()) {
            System.out.println(m + " tiene " + mascotas.get(m) + " años");
        }
        System.out.println("---------------------------------");
        for (String m : mascotas.keySet()) {
            String txtbuscar="Momo";
            if(txtbuscar.compareToIgnoreCase(m)==0){
                System.out.println(m + " tiene " + mascotas.get(m) + " años");
            }
            
        }
        
    }
    
}
