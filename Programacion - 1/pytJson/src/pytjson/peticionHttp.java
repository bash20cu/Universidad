/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pytjson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author miguel
 */
public class peticionHttp {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        JSONParser parser = new JSONParser();
        JSONObject data = new JSONObject();
        //Object json;

        String url = "http://api.openweathermap.org/data/2.5/weather?q=puntarenas,cr&units=metric&lang=es&appid=5db604a191948ce24beb4f4a13299bcc";

        try {
            //String res= peticionHttpGet(url);
            data = (JSONObject) parser.parse(peticionHttpGet(url));

        } catch (ParseException ex) {
            System.out.println("Resultado no es un objeto JSON válido");
        }

        System.out.println("Latitud: " + ((JSONObject) data.get("coord")).get("lat"));
        System.out.println("Longitud: " + ((JSONObject) data.get("coord")).get("lon"));
        
        
        System.out.println("Latitud: " + ((JSONObject) data.get("coord")).get("lat"));
        System.out.println("Longitud: " + ((JSONObject) data.get("coord")).get("lon"));
        System.out.println("Clima: " + ((JSONObject)((JSONArray)data.get("weather")).get(0)).get("description"));
        System.out.println("Time Zone: " + (data.get("timezone")));
         
    }

    public static String peticionHttpGet(String urlPeticion) {
        //objetos necearios para la respuesta
        StringBuilder resultado = new StringBuilder();
        //crear un objeto URL
        URL url = null;
        String linea;
        try {
            //crear un objeto URL
            url = new URL(urlPeticion);
        } catch (MalformedURLException ex) {
            System.out.println("Error en la Url ingresada");
        }

        try {
            //abrir la conexión
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            conexion.setRequestMethod("GET");
            //abrir el buffer de lectura
            BufferedReader rd = new BufferedReader(new InputStreamReader(conexion.getInputStream()));
            //recorrer el rd para rear la cadena de resultado
            while ((linea = rd.readLine()) != null) {
                resultado.append(linea);
            }
            //cerrar el buffered
            rd.close();
        } catch (IOException ex) {
            System.out.println("Error en la conexión");
        }
        return resultado.toString();
    }

}
