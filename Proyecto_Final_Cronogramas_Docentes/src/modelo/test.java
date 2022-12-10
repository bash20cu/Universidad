/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package modelo;

import java.util.ArrayList;

/**
 *
 * @author migue
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        ArrayList<clsModulos> modulos = new ArrayList<clsModulos>();
        ArrayList<clsPrograma> programas = new ArrayList<clsPrograma>();
        
        
        clsCurso excelAvanzado = new clsCurso("Excel avanzado", "ene-2023",
                "may-2023", 10);
        
        clsCurso InglesConversacional = new clsCurso("InglesConversacional", "ene-2023",
                "may-2023", 24);
        
          
          
        clsDocente docente1 = new clsDocente(excelAvanzado, "mixta", 
                "001", "docente 1", "docente2 - docente 3", 
                "001", "San jose");
        
        docente1.setCursos(InglesConversacional);
        
        clsCentroFormacion uia = new clsCentroFormacion("uia-001", "UIA",
                "San Jose", "0001-0002");
        clsCentroFormacion fidelitas = new clsCentroFormacion("fidelitas-001", "Fidelitas",
                "San jose", "00-10001");
        
        clsHorario horarioDocente1 = new clsHorario(8, "Activo", docente1);

        clsPrograma programa1  =  new clsPrograma(fidelitas, horarioDocente1,
                "prog-001", "Idiomas", "A2", 2020);
        
         clsPrograma programa2  =  new clsPrograma(uia, horarioDocente1,
                 "Prog-002", "Tecnologia","A1", 2022);
        
        clsModulos modulo1 = new clsModulos("0012", "primer-cuatrimestre",
                "120 Horas", "Tecnologias");
        
        clsModulos modulo3 = new clsModulos("0012", "segundo-cuatrimestre",
                "120 Horas", "Idiomas");
        
        programas.add(programa1);
        programas.add(programa2);
        modulos.add(modulo1);
        modulos.add(modulo3);
        
        clsCronograma cronograma1 = new clsCronograma(docente1, modulos, programas);

        
        cronograma1.setProgramas(programas);
        
        System.out.println("------------Cronograma ---------");
        System.out.println(cronograma1.getDatos());
            
        
        /*
        System.out.println(" ---- ---- Docente: ----- ----- ----");
        System.out.println(cronograma1.docente.getPersona());
        System.out.println(" ---- ---- Cursos: ----- ----- ----");
        System.out.println(cronograma1.docente.getCursos());
        System.out.println(" ---- ---- Modulos ----- ----- ----");
        System.out.println("Modulos: "+"\n"+cronograma1.getModulos()+"\n");
        System.out.println(" ---- ---- Programas ----- ----- ----");
        System.out.println("Programas: "+"\n"+cronograma1.getProgramas()+"\n");      
*/
    }    
}
