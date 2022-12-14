
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

public class clsPrograma  extends clsMetodos implements Serializable{
    private static final long serialVersionUID = 6529685098267777690L; //determina la version de la clase 
    clsCentroFormacion centroFormacion = new clsCentroFormacion();
    //clsHorario horario = new clsHorario();
    ArrayList<clsModulos> modulos = new ArrayList<>();
    private String referencia;
    private String sector;
    private String codigo;
    private String grupo;
    private int anno;
   

    public  clsPrograma(){}
    
    public clsPrograma(String codigo, String sector, String grupo, int anno) {
        this.codigo = codigo;
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
    }
    
    public clsPrograma(clsModulos modulo,clsCentroFormacion centroFormacion, String codigo, String sector, String grupo, int anno) {
        this.codigo = codigo;
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
        this.modulos.add(modulo);
        this.centroFormacion = centroFormacion;
    }
    
    
    
    public clsPrograma(clsCentroFormacion centroFormacion,clsHorario horario, String codigo, String sector, String grupo, int anno) {
        this.centroFormacion = centroFormacion;
        this.codigo = codigo;
        this.sector = sector;
        this.grupo = grupo;
        this.anno = anno;
        //this.horario = horario;
    }

    public String getReferencia() {
        return referencia;
    }
    

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public ArrayList<clsModulos> getModulos() {
        return modulos;
    }

    public void setModulos(ArrayList<clsModulos> modulos) {
        this.modulos = modulos;
    }
    
    public void setModulos(clsModulos modulo){
        this.modulos.add(modulo);
    }
    

    public clsCentroFormacion getCentroFormacion() {     
        return centroFormacion;
    }

    public void setCentroFormacion(clsCentroFormacion centroFormacion) {
        this.centroFormacion = centroFormacion;
    }

   public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }
/*
    public clsHorario getHorario() {
        return horario;
    }

    public void setHorario(clsHorario horario) {
        this.horario = horario;
    }
*/
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

 @Override
    public int guardar() {
       FileOutputStream ficheroSalida = null;
        try {
            File bd = validarArchivo("programas");

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
       File bd = this.validarArchivo("programas");
        ArrayList<Object> programas = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("modulos");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        try {
            ficheroSalida = new FileOutputStream(bd);

            for (Object programa : programas) {
                objetoSalida = new ObjectOutputStream(ficheroSalida);
                clsPrograma m = (clsPrograma) programa;
                
                System.out.println(m.getCodigo());
                
                if (m.getCodigo().compareToIgnoreCase(this.codigo) == 0) {
                    objetoSalida.writeObject(this);
                } else {
                    objetoSalida.writeObject(programa);
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
                Logger.getLogger(clsPrograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public int eliminar() {
        File bd = this.validarArchivo("programas");
        ArrayList<Object> programas = this.getRegistros();
        //borrar el contenido del archivo
        bd.delete();
        //vuelvo a crear el archivo
        //bd= this.validarArchivo("docentes");
        ObjectOutputStream objetoSalida = null;
        FileOutputStream ficheroSalida = null;
        
        System.out.println(this.codigo);
        
        try {
            ficheroSalida = new FileOutputStream(bd, true);
            //objetoSalida = new ObjectOutputStream(ficheroSalida);
            for (Object programa : programas) {
                clsPrograma m = (clsPrograma) programa;
                if (m.getCodigo().compareToIgnoreCase(this.codigo) != 0) {
                    objetoSalida = new ObjectOutputStream(ficheroSalida);
                    objetoSalida.writeObject(programa);
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
                Logger.getLogger(clsPrograma.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 1;
    }

    @Override
    public ArrayList<Object> getRegistros() {
        FileInputStream ficheroEntrada = null;
        ArrayList<Object> programas = new ArrayList<>();
        ObjectInputStream objetoEntrada = null;
        try {
            File bd = this.validarArchivo("programas");
            //abrir el flujo

            ficheroEntrada = new FileInputStream(bd);
            //objetoEntrada = new ObjectInputStream(ficheroEntrada);
            while (ficheroEntrada.available() > 0) {
                objetoEntrada = new ObjectInputStream(ficheroEntrada);
                programas.add(objetoEntrada.readObject());
            }
            if (objetoEntrada != null) {
                objetoEntrada.close();
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(clsPrograma.class
                    .getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<>();

        } catch (IOException ex) {
            Logger.getLogger(clsPrograma.class
                    .getName()).log(Level.SEVERE, null, ex);
            return null;

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsPrograma.class
                    .getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(clsPrograma.class
                    .getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                ficheroEntrada.close();
            } catch (IOException ex) {
                Logger.getLogger(clsPrograma.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return programas;
    }

    @Override
    public Object getRegistro(int id) {
         ArrayList<Object> filas = this.getRegistros();
       return filas.get(id);
    }
     
}
