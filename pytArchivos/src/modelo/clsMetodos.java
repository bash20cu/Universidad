/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Est_Nautico
 */
public abstract class clsMetodos {
    
    
   public File  validarArchivo(String archivo){
       //archivo a utilizar 
       //src\\archivos\\bdplatos.dat
       File bd=new File("src\\archivos\\"+ archivo +".dat");       
       //verificar si existe el archivo
       if (!bd.exists()) {
           try {
               //si no existe lo creo
               bd.createNewFile();
           } catch (IOException ex) {
               System.err.println("Error al abrir el archivo");
           }catch (Exception ex){
                System.err.println("Error al abrir el archivo");
           }
       }
       return bd;
    }
    
    public abstract int guardar();
    public abstract int modificar(Object dato);
    public abstract int eliminar(int cedula);
    public abstract ArrayList<Object> getRegistros();
    public abstract Object getRegistro(int cedula);
    
    
}
