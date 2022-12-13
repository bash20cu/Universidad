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

public class clsModulos extends clsMetodos implements Serializable{
    private static final long serialVersionUID = 6529685098267777690L; //determina la version de la clase
    private String codigo;
    private String nombre;
    private String duracion;
    private String sector;

    public clsModulos() {}
    
    public clsModulos(String codigo, String nombre, String duracion, String sectores) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.duracion = duracion;
        this.sector = sectores;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }   

    @Override
    public int guardar() {
       FileOutputStream ficheroSalida = null;
        try {
            File bd = validarArchivo("modulos");

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
                return 0;
            }
        }
        return 1;
    }

    @Override
    public int modificar() {
       File bd = this.validarArchivo("modulos");
        ArrayList<Object> modulos = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("modulos");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd);

            for (Object modulo : modulos) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsModulos m = (clsModulos) modulo;
                if (m.getCodigo().compareToIgnoreCase(this.codigo) == 0) {
                    objetoSalida.writeObject(this);
                } else {
                    objetoSalida.writeObject(modulo);
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
                Logger.getLogger(clsModulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public int eliminar() {
        File bd = this.validarArchivo("modulos");
        ArrayList<Object> modulos = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("docentes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd, true);
            //objetoSalida = new ObjectOutputStream(ficheroSalida);
            for (Object modulo : modulos) {
                clsModulos m = (clsModulos) modulo;
                if (m.getCodigo().compareToIgnoreCase(this.codigo) != 0) {
                    objetoSalida = new ObjectOutputStream(ficheroSalida);
                    objetoSalida.writeObject(modulo);
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
                Logger.getLogger(clsModulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public ArrayList<Object> getRegistros() {
        FileInputStream ficheroEntrada = null;
        ArrayList<Object> modulos = new ArrayList<>();
        ObjectInputStream objetoEntrada = null;
        try {
            File bd = this.validarArchivo("modulos");
            //abrir el flujo

            ficheroEntrada = new FileInputStream(bd);
            //objetoEntrada = new ObjectInputStream(ficheroEntrada);
            while (ficheroEntrada.available() > 0) {
                objetoEntrada = new ObjectInputStream(ficheroEntrada);
                modulos.add(objetoEntrada.readObject());
            }
            if (objetoEntrada != null) {
                objetoEntrada.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsModulos.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();

        } catch (IOException ex) {
            Logger.getLogger(clsModulos.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsModulos.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(clsModulos.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ficheroEntrada.close();

            } catch (IOException ex) {
                Logger.getLogger(clsModulos.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return modulos;
    }

    @Override
    public Object getRegistro(int id) {
         ArrayList<Object> filas = this.getRegistros();
       return filas.get(id);
    }
    
}
