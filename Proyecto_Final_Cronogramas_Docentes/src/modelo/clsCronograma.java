package modelo;

import java.util.ArrayList;


public class clsCronograma {
    
    private int totalHoras = 0;
    private int vacaciones;
    clsDocentes docente = new clsDocentes();
    private ArrayList<clsModulos> modulos = new ArrayList<>();
    private ArrayList<clsPrograma> programas = new ArrayList<>();
        

    public clsCronograma() {
    }

    public clsCronograma(clsDocentes docente, ArrayList<clsModulos> modulos, 
            ArrayList<clsPrograma> programas) {        
        this.programas = programas;
        this.modulos = modulos;
        this.docente = docente;
        setTotalHoras(docente.getTotalHoras());
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
        System.out.println("---- ---- Modulos: ----- ----- ----");
        this.modulos.forEach((e) -> {
               //System.out.println(e.getModulo());
        });  
        return modulos;
    }

    public void setModulos(ArrayList<clsModulos> modulos) {
        this.modulos = modulos;
    }

    public ArrayList<clsPrograma> getProgramas() {
        System.out.println("---- ---- Programas: ----- ----- ----");
        this.programas.forEach((e) -> {
           // System.out.println(e.getPrograma());            
        });
        return programas;
    }

    public void setProgramas(ArrayList<clsPrograma> programas) {
        this.programas = programas;
    }
      
}
