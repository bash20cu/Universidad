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
public class clsTarjetas extends clsMetodos implements Serializable {

    private static final long serialVersionUID = 6529685098267777690L; //determina la version de la clase
    private String cedula;
    private int numeroCuenta;
    private double montoLimite;
    private double saldo;

    public clsTarjetas() {
    }

    public clsTarjetas(String cedula, int numeroCuenta, double montoLimite, double saldo) {
        this.cedula = cedula;
        this.numeroCuenta = numeroCuenta;
        this.montoLimite = montoLimite;
        this.saldo = saldo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public int getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(int numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public double getMontoLimite() {
        return montoLimite;
    }

    public void setMontoLimite(double montoLimite) {
        this.montoLimite = montoLimite;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public int guardar() {
        FileOutputStream ficheroSalida = null;
        try {
            File bd = this.validarArchivo("tarjetas");

            //abrir el flujo de escritura
            ficheroSalida = new FileOutputStream(bd, true);
            ObjectOutputStream objetoSalida = new ObjectOutputStream(ficheroSalida);

            //escribimos en el archivo
            objetoSalida.writeObject(this);

        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            System.out.println(ex);
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
    public int modificar() {
        File bd = this.validarArchivo("tarjetas");

        ArrayList<Object> tarjetas = this.getRegistros();

        //borrar archivos
        bd.delete();
        //Volver a crear el archivo
        bd = this.validarArchivo("tarjetas");

        ObjectOutputStream objetoSalida = null;

        try {
            //Abriendo el flujo de datos
            FileOutputStream ficheroSalida = new FileOutputStream(bd);

            for (Object tarjeta : tarjetas) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsTarjetas c = (clsTarjetas) tarjeta;

                if (c.getCedula().compareToIgnoreCase(this.cedula) == 0) {
                    objetoSalida.writeObject(this);
                } else {
                    objetoSalida.writeObject(tarjeta);
                }
            }

        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            return 0;
        }
        return 1;
    }

    @Override
    public int eliminar() {
        File bd = this.validarArchivo("tarjetas");

        ArrayList<Object> tarjetas = this.getRegistros();

        //borrar archivos
        bd.delete();
        bd = this.validarArchivo("tarjetas");
        ObjectOutputStream objetoSalida = null;

        try {
            //Abriendo el flujo de datos
            FileOutputStream ficheroSalida = new FileOutputStream(bd);

            for (Object tarjeta : tarjetas) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsTarjetas c = (clsTarjetas) tarjeta;

                if (c.getCedula().compareToIgnoreCase(this.cedula) != 0) {
                    objetoSalida.writeObject(this);
                }
            }

        } catch (FileNotFoundException ex) {
            return 0;
        } catch (IOException ex) {
            return 0;
        }
        return 1;
    }

    @Override
    public ArrayList<Object> getRegistros() {
        FileInputStream ficheroEntrada = null;
        ArrayList<Object> tarjetas = new ArrayList<Object>();
        try {
            File bd = this.validarArchivo("tarjetas");
            //abrir el flujo
            ObjectInputStream objetoEntrada = null;
            ficheroEntrada = new FileInputStream(bd);

            while (ficheroEntrada.available() > 0) {

                objetoEntrada = new ObjectInputStream(ficheroEntrada);

                tarjetas.add(objetoEntrada.readObject());
            }

        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            System.out.println(ex);
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
        return tarjetas;
    }

    @Override
    public Object getRegistro(int id) {
        ArrayList<Object> filas = this.getRegistros();
        return filas.get(id);
    }

}
