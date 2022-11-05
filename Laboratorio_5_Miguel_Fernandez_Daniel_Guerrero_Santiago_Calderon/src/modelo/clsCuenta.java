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
public class clsCuenta {
    private Map<String,String> cuenta = new HashMap<>();
    
    
    public clsCuenta() {
       
    }

    public Map<String, String> getCuenta() {
        return cuenta;
    }

    public void setCuenta(Map<String, String> cuenta) {
        this.cuenta = cuenta;
    }
    //metodo para crear un PIN de tarjetas.
    public void cuentaPin(){
       cuenta.put("12", "12");
       cuenta.put("34", "34");
       cuenta.put("56", "56");
    }
    //metodo para genear un saldo aleatorio.
    public void saldoAleatorio(){   
        Double saldoAleatorio;            
        saldoAleatorio = Math.random() * (1-100+1) + 100;  
        String format = String.format("%.2f", saldoAleatorio);
        cuenta.put("12", format);  
    } 
    
}
