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
import javax.swing.JOptionPane;

/**
 *
 * @author Est_Nautico
 */
public class clsClientes extends clsMetodos implements Serializable {

    private static final long serialVersionUID = 6529685098267777690L; //determina la version de la clase
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
        FileOutputStream ficheroSalida = null;
        ObjectOutputStream objetoSalida = null;
        try {
            File bd = this.validarArchivo("clientes");
            //abrir el flujo de escritura
            ficheroSalida = new FileOutputStream(bd, true);
            objetoSalida = new ObjectOutputStream(ficheroSalida);

            //escribimos en el archivo
            objetoSalida.writeObject(this);
        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            return 0;
        } finally {
            try {
                objetoSalida.close();
                ficheroSalida.close();
            } catch (IOException ex) {
                Logger.getLogger(clsClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public int modificar() {
        File bd = this.validarArchivo("clientes");
        ArrayList<Object> clientes = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("clientes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd);

            for (Object cliente : clientes) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsClientes c = (clsClientes) cliente;
                if (c.getCedula().compareToIgnoreCase(this.cedula) == 0) {
                    objetoSalida.writeObject(this);
                } else {
                    objetoSalida.writeObject(cliente);
                }

            }
        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            return 0;
        } finally {
            try {
                objetoSalida.close();
                ficheroSalida.close();
            } catch (IOException ex) {
                Logger.getLogger(clsClientes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return 1;
    }

    @Override
    public int eliminar() {
        File bd = this.validarArchivo("clientes");
        ArrayList<Object> clientes = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("clientes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd, true);
            //objetoSalida = new ObjectOutputStream(ficheroSalida);
            for (Object cliente : clientes) {
                clsClientes c = (clsClientes) cliente;
                if (c.getCedula().compareToIgnoreCase(this.cedula) != 0) {
                    objetoSalida = new ObjectOutputStream(ficheroSalida);
                    objetoSalida.writeObject(cliente);

                }

            }
            objetoSalida.close();
        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            return 0;
        } finally {
            try {
                //objetoSalida.close();
                ficheroSalida.close();
            } catch (IOException ex) {
                Logger.getLogger(clsClientes.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return 1;
    }

    @Override
    public ArrayList<Object> getRegistros() {
        FileInputStream ficheroEntrada = null;
        ArrayList<Object> clientes = new ArrayList<Object>();
        ObjectInputStream objetoEntrada = null;
        try {
            File bd = this.validarArchivo("clientes");
            //abrir el flujo

            ficheroEntrada = new FileInputStream(bd);
            //objetoEntrada = new ObjectInputStream(ficheroEntrada);
            while (ficheroEntrada.available() > 0) {
                objetoEntrada = new ObjectInputStream(ficheroEntrada);
                clientes.add(objetoEntrada.readObject());
            }
            if (objetoEntrada != null) {
                objetoEntrada.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsClientes.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<Object>();

        } catch (IOException ex) {
            Logger.getLogger(clsClientes.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsClientes.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(clsClientes.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ficheroEntrada.close();

            } catch (IOException ex) {
                Logger.getLogger(clsClientes.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return clientes;
    }

    @Override
    public Object getRegistro(int id) {
        ArrayList<Object> filas = this.getRegistros();
        return filas.get(id);
    }

}
