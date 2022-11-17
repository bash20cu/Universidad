/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Est_Nautico
 */
public abstract class clsMetodos {
    
    
   /* public File  validarArchivo(){
        
    }*/
    
    public abstract int guardar(Object dato);
    public abstract int modificar(Object dato);
    public abstract int eliminar(int cedula);
    public abstract ArrayList<Object> getRegistros();
    public abstract Object getRegistro(int cedula);
    
}
