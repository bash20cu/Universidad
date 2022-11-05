/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

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
    //metodo para crear tarjetas y los pines,
    //Solo caracteres numericos String, estan permitidos.
    public void cuentaPin(){
       cuenta.put("12", "12");
       cuenta.put("34", "34");
       cuenta.put("56", "56");
       cuenta.put("78", "78");
       cuenta.put("910", "910");
    }
    //metodo para genear un saldo aleatorio cada inicio de sesion.
    public void saldoAleatorio(){   
        Double saldoAleatorio;            
        saldoAleatorio = Math.random() * (1-100+1) + 100;  
        String format = String.format("%.2f", saldoAleatorio);
        cuenta.put("12", format);  
    }
    //Metodo para depositar en la cuenta 
    public String depositoCuenta (Double saldo, Double deposito){
        //Validacion en caso que el monto a depositar que sea no menor o igual que 0.
        if (deposito <= 0) {
            JOptionPane.showMessageDialog(null, "El deposito no puede ser numero negativo o igual a 0", "Mensaje", JOptionPane.ERROR_MESSAGE);
            return saldo.toString();
        }else{                
            Double saldoNuevo;
            saldoNuevo = saldo + deposito;
            String format = String.format("%.2f", saldoNuevo);                
            JOptionPane.showMessageDialog(null, "Se a depositado el monto exitosamente", "Deposito Exitoso", JOptionPane.INFORMATION_MESSAGE);
            return format;
        } 
    }
    //Metodo para hacer retiso de la cuenta.
    public String retiroCuenta (Double saldo, Double retiro){
        //Validacion en caso que el monto a retirar que no sea menor o igual que 0.
        if (retiro <= 0) { 
            JOptionPane.showMessageDialog(null, "El retiro no puede ser numero negativo o igual a 0", "Mensaje", JOptionPane.ERROR_MESSAGE);
            return saldo.toString();
            //Validacion en caso que SALDO sea menor que 0.
        }else if(saldo <= 0 ) {               
            JOptionPane.showMessageDialog(null, "Saldo insuficiente para realizar la operación", "Mensaje", JOptionPane.ERROR_MESSAGE);
            return saldo.toString();
        }else{
            Double saldoNuevo;
            saldoNuevo = saldo - retiro;
            
            //Validamos en caso que el saldo quede negativo, advirtiendo al cliente.
            if(saldoNuevo <= 0){
                JOptionPane.showMessageDialog(null, "Saldo insuficiente para realizar la operación", "Mensaje", JOptionPane.ERROR_MESSAGE);
                    return saldo.toString();
            }else{
                String format = String.format("%.2f", saldoNuevo);
                JOptionPane.showMessageDialog(null, "Se a retirado el monto exitosamente", "Retiro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                return format;
            }           
        }
    }
}
