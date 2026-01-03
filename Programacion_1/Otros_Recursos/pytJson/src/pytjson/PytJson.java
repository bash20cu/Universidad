/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytjson;

import org.json.simple.JSONObject;

/**
 *
 * @author miguel
 */
public class PytJson {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        JSONObject json = new JSONObject();
        
        //guardar datos en el json
        json.put("cedula", 123);
        json.put("nombre","Juan Perez Sosa");
        json.put("activo",true);
        
        System.out.println(json);
        
        json.replace("activo", false);
        System.out.println(json);        
    }
    
}
