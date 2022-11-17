/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Est_Nautico
 */
public class clsClientes extends clsMetodos implements Serializable{
    private static final long serialVersionUID=6529685098267777690L; //determina la version de la clase
    private String cedula;
    private String nombre;
    private String apellidos;
    private String telefono;

    public clsClientes() {
    }

    public clsClientes(String cedula, String nombre, String apellidos, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public int guardar() {
        FileOutputStream ficheroSalida= null;
        try {
            File bd= this.validarArchivo("clientes");
            //abrir el flujo de escritura
            ficheroSalida = new FileOutputStream(bd,true);
            ObjectOutputStream objetoSalida=new ObjectOutputStream(ficheroSalida);
            //escribimos en el archivo
            objetoSalida.writeObject(this);
        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
             return 0;
        } finally {
            try {
                ficheroSalida.close();
            } catch (IOException ex) {
                Logger.getLogger(clsClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public int modificar(Object dato) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int eliminar(int cedula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Object> getRegistros() {
        FileInputStream ficheroEntrada=null;
        ArrayList<Object> clientes=new ArrayList<Object>();
        try {
            File bd=this.validarArchivo("clientes");            
            //abrir el flujo
            ObjectInputStream objetoEntrada=null;
            ficheroEntrada = new FileInputStream(bd);
            
            while (ficheroEntrada.available()>0) { 
             
                objetoEntrada= new ObjectInputStream(ficheroEntrada);

                clientes.add(objetoEntrada.readObject());
            }
            
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsClientes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ficheroEntrada.close();
            } catch (IOException ex) {
                Logger.getLogger(clsClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     return clientes;          
    }

    @Override
    public Object getRegistro(int cedula) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
