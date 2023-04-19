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


public class clsCronograma extends clsMetodos implements Serializable{
    
    private int totalHoras = 0;
    private int vacaciones;
    
    clsDocentes docente = new clsDocentes();
    
    private clsModulos modulo = new clsModulos();
    private clsPrograma programa =  new clsPrograma();
    
    private ArrayList<clsModulos> modulos = new ArrayList<>();
    private ArrayList<clsPrograma> programas = new ArrayList<>();
        

    public clsCronograma() {
    }

    public clsCronograma(int vacaciones) {
        this.vacaciones = vacaciones;
    }
    
        
    public clsCronograma(clsDocentes docente, ArrayList<clsModulos> modulos, 
            ArrayList<clsPrograma> programas) {        
        this.programas = programas;
        this.modulos = modulos;
        this.docente = docente;
        this.setTotalHoras(docente.getTotalHoras());
    }
    
    
    public clsModulos getModulo() {
        return modulo;
    }

    public void setModulo(clsModulos modulo) {
        this.modulo = modulo;
    }

    public clsPrograma getPrograma() {
        return programa;
    }

    public void setPrograma(clsPrograma programa) {
        this.programa = programa;
    }
    
    
    
    
    public int getVacaciones() {
        return vacaciones;
    }

    public void setVacaciones(int vacaciones) {
        this.vacaciones = vacaciones;
    }
        
    public int getTotalHoras() {
        return totalHoras;
    }

    private void setTotalHoras(int totalHoras) {
        if (totalHoras >= 2080) {
            System.out.println("Total de horas excedido");
             //this.totalHoras = this.totalHoras;
        }else{
            this.totalHoras = totalHoras;
        }     
    }

    public clsDocentes getDocente() {
        return docente;
    }

    public void setDocente(clsDocentes docente) {
        this.docente = docente;
    }

    public ArrayList<clsModulos> getModulos() {        
        return modulos;
    }
    

    public void setModulos(ArrayList<clsModulos> modulos) {
        this.modulos = modulos;
    }

    public ArrayList<clsPrograma> getProgramas() {
        return programas;
    }

    public void setProgramas(ArrayList<clsPrograma> programas) {
        this.programas = programas;
    }

    @Override
    public int guardar() {
         FileOutputStream ficheroSalida = null;
        try {
            File bd = validarArchivo("cronogramas");

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
         File bd = this.validarArchivo("cronogramas");
        ArrayList<Object> cronogramas = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("docentes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd);

            for (Object cronograma : cronogramas) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsCronograma crono = (clsCronograma) cronograma;
                if (crono.getDocente().getNombre().compareToIgnoreCase(this.docente.getNombre()) == 0) {
                    objetoSalida.writeObject(this);
                } else {
                    objetoSalida.writeObject(cronograma);
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
                Logger.getLogger(clsCronograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public int eliminar() {
        File bd = this.validarArchivo("cronogramas");
        ArrayList<Object> cronogramas = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("docentes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;    
        try {
            ficheroSalida = new FileOutputStream(bd, true);
            //objetoSalida = new ObjectOutputStream(ficheroSalida);
            for (Object cronograma : cronogramas) {
                clsCronograma m = (clsCronograma) cronograma;
                if (m.getDocente().getNombre().compareToIgnoreCase(this.docente.getNombre()) != 0) {
                    objetoSalida = new ObjectOutputStream(ficheroSalida);
                    objetoSalida.writeObject(cronograma);
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
                Logger.getLogger(clsCronograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public ArrayList<Object> getRegistros() {
        FileInputStream ficheroEntrada = null;
        ArrayList<Object> cronogramas = new ArrayList<>();
        ObjectInputStream objetoEntrada = null;
        try {
            File bd = this.validarArchivo("cronogramas");
            //abrir el flujo

            ficheroEntrada = new FileInputStream(bd);
            //objetoEntrada = new ObjectInputStream(ficheroEntrada);
            while (ficheroEntrada.available() > 0) {
                objetoEntrada = new ObjectInputStream(ficheroEntrada);
                cronogramas.add(objetoEntrada.readObject());
            }
            if (objetoEntrada != null) {
                objetoEntrada.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsCronograma.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();

        } catch (IOException ex) {
            Logger.getLogger(clsCronograma.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsCronograma.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(clsCronograma.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ficheroEntrada.close();

            } catch (IOException ex) {
                Logger.getLogger(clsCronograma.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cronogramas;
    }

    @Override
    public Object getRegistro(int id) {
             ArrayList<Object> filas = this.getRegistros();
           return filas.get(id);
        }
}
