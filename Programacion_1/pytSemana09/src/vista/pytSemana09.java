/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package vista;

import java.util.Scanner;
import modelo.clsEstudiante;
import modelo.clsNotas;

/**
 *
 * @author migue
 */
public class pytSemana09 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner in = new Scanner(System.in);
        clsEstudiante e = new clsEstudiante();
        double nota1, nota2, nota3, nota4;
        String trimestre, seccion;
        int anio, opc = 0;

        System.out.println("Digite el carnet");
        e.setCarnet(in.next());
        System.out.println("Digite el nombre");
        e.setNombre(in.next());
        System.out.println("Digite el Primer Apellido");
        e.setApellido1(in.next());
        System.out.println("Digite el Segundo Apellido");
        e.setApellido2(in.next());
        System.out.println("Digite el Telefono");
        e.setTelefono(in.next());
        System.out.println("Digite el Encargado");
        e.setEncargado(in.next());
        do {
            System.out.println("Digite la nota 1");
            nota1 = in.nextDouble();
            System.out.println("Digite la nota 2");
            nota2 = in.nextDouble();
            System.out.println("Digite la nota 3");
            nota3 = in.nextDouble();
            System.out.println("Digite la nota 4");
            nota4 = (in.nextDouble());
            System.out.println("Digite la sección");
            seccion = in.next();
            System.out.println("Digite el trimestre");
            trimestre = in.next();
            System.out.println("Digite el año");
            anio = in.nextInt();
            e.notas.add(new clsNotas(nota1, nota2, nota3, nota4, trimestre, anio, seccion));            
            System.out.println("Desea agregar mas notas 1-si | 2-No");
            opc=in.nextInt();
        } while (opc != 2);



       System.out.println("Estudiante: " + e.getNombreCompleto());
        for (clsNotas n :e.notas) {
             System.out.println("Año: " + n.getAnio());
            System.out.println("Promedio: " + n.promedio());
        }
    }
    
}
