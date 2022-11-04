/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author migue
 */
public class clsLogin {
    private Map<String,Integer> Tarjeta=new HashMap<String,Integer>();
    

    public clsLogin() {
       Tarjeta.put("12", 12);
       Tarjeta.put("34", 34);
       Tarjeta.put("56", 56);
    }

    public Map<String, Integer> getTarjeta() {
        return Tarjeta;
    }

    public void setTarjeta(Map<String, Integer> Tarjeta) {
        this.Tarjeta = Tarjeta;
    }   
    
    //metodos personaliados
    

   
     
}
