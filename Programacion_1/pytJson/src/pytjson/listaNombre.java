/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytjson;

import java.util.ArrayList;
import org.json.simple.JSONObject;

/**
 *
 * @author miguel
 */
public class listaNombre {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //instanciar un objeto JSON
        ArrayList<JSONObject> lista = new  ArrayList<JSONObject>();
        JSONObject json;
        
        //guardar datos en el json
        
        for (int i = 0; i < 10; i++) {
            json=new JSONObject();
            json.put("cedula", i);
            json.put("nombre", "Juan Perez Sosa");
            json.put("activo", false);
            
            lista.add(json);
        }
        
        for (JSONObject persona : lista) {
            System.out.println(persona);
        }
    }
    
}
