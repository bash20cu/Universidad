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

public class clsDocentes extends clsMetodos implements iTipoDocente, Serializable{
    
    private static final long serialVersionUID = 6529685098267777690L; //determina la version de la clase
    private String cedula;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String direccion;
    private ArrayList<clsCurso> cursos = new ArrayList<>();
    private int totalHoras = 0;
    private String jornada;

    public clsDocentes() {
    }

    public clsDocentes(String cedula, String nombre, String apellidos, String telefono, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    

    public clsDocentes(clsCurso cursos, String jornada, String cedula, String nombre,
            String apellidos, String telefono, String direccion) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.direccion = direccion;
        this.cursos.add(cursos);
    }

    public ArrayList<clsCurso> getCursos() {
        this.cursos.forEach((e) -> {
        System.out.println(e.getDatosCursos());
        this.totalHoras += e.getTotalHoras();
        });
        System.out.println("Total de horas: "+this.getTotalHoras()+"\n");
        return cursos;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    

    public void setCursos(clsCurso curso){
        this.cursos.add(curso);
    }
    public void setCursos(ArrayList<clsCurso> cursos) {
        this.cursos = cursos;
    }
      
    
    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    } 

    public int getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(int totalHoras) {
        this.totalHoras += totalHoras;
    }  

    @Override
    public boolean docenteDiurno() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean docenteMixto() {
        throw new UnsupportedOperationException("Not supported yet.");
    }    

    @Override
    public int guardar() {
        FileOutputStream ficheroSalida = null;
        try {
            File bd = validarArchivo("docentes");

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
       File bd = this.validarArchivo("docentes");
        ArrayList<Object> docentes = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("docentes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd);

            for (Object docente : docentes) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsDocentes d = (clsDocentes) docente;
                if (d.getCedula().compareToIgnoreCase(this.cedula) == 0) {
                    objetoSalida.writeObject(this);
                } else {
                    objetoSalida.writeObject(docente);
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
                Logger.getLogger(clsDocentes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public int eliminar() {
        File bd = this.validarArchivo("docentes");
        ArrayList<Object> docentes = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("docentes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd, true);
            //objetoSalida = new ObjectOutputStream(ficheroSalida);
            for (Object docente : docentes) {
                clsDocentes c = (clsDocentes) docente;
                if (c.getCedula().compareToIgnoreCase(this.cedula) != 0) {
                    objetoSalida = new ObjectOutputStream(ficheroSalida);
                    objetoSalida.writeObject(docente);
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
                Logger.getLogger(clsDocentes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public ArrayList<Object> getRegistros() {
       FileInputStream ficheroEntrada = null;
        ArrayList<Object> docentes = new ArrayList<>();
        ObjectInputStream objetoEntrada = null;
        try {
            File bd = this.validarArchivo("docentes");
            //abrir el flujo

            ficheroEntrada = new FileInputStream(bd);
            //objetoEntrada = new ObjectInputStream(ficheroEntrada);
            while (ficheroEntrada.available() > 0) {
                objetoEntrada = new ObjectInputStream(ficheroEntrada);
                docentes.add(objetoEntrada.readObject());
            }
            if (objetoEntrada != null) {
                objetoEntrada.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsDocentes.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();

        } catch (IOException ex) {
            Logger.getLogger(clsDocentes.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsDocentes.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(clsDocentes.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ficheroEntrada.close();

            } catch (IOException ex) {
                Logger.getLogger(clsDocentes.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return docentes;
    }

    @Override
    public Object getRegistro(int id) {
         ArrayList<Object> filas = this.getRegistros();
       return filas.get(id);
    }
}