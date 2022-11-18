/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio_2_miguel_fernandez_arteaga;

import java.util.Scanner;

/**
 *
 * @author migue
 */
public class Ejercicio1 {

    /**
     * @param args the command line arguments
     */
    /*
    Ejercicio #1: 
    Se requiere una aplicación que reciba el resultado de una prueba de 
    una lista desconocida de estudiantes, en esta lista junto a cada nombre hay un A
    escrito, si el estudiante aprobó el examen, o un B si lo reprobó. El programa debe 
    analizar los resultados del examen de la siguiente manera:
    
    a. Introducir cada resultado de la prueba (es decir, un A o un B). Mostrar el 
    mensaje “Ingrese el resultado del estudiante #” en la pantalla, cada vez que 
    el programa solicite otro resultado de la prueba.
    
    b. Si se ingresa un valor que no sea A o B, se debe mostrar el mensaje “Dato 
    Invalido” y volver a pedir la nota de ese estudiante, no puede avanzar si no 
    se ingresa una nota válida.
    
    c. Después de ingresar una nota valida, debe preguntar si desea agregar una 
    nueva o no.
    
    d. Llevar el número de resultados de la prueba, de cada tipo.
    
    e. Al finalizar el ingreso de notas, debe mostrar un resumen de los resultados 
    de la prueba, indicando el número de estudiantes, el número que aprobaron y 
    el número de estudiantes que reprobaron, así como el porcentaje de 
    estudiantes aprobados y reprobados.
    
    f. Al final debe preguntar si se desea ingresar otra lista de estudiantes, de ser 
    así se debe repetir todo el proceso anterior, de lo contrario se finaliza la 
    aplicación.
    */
    public static void main(String[] args) {
        //Lectura de datos.
        Scanner in = new Scanner(System.in);
        
        //Variables
        short opc = 1; //Opcion para cerrar el ciclo.
        int numEstudiante = 1;// numero de cada estudiante
        int nuevoEstudiante = 0;
        String resultadoExamen; // Nota Finalde cada Examen.
        int notaA = 0; // Resultados Notas
        int notaB = 0; // Resultados Notas
        
        
        //Lista de estudiantes
        while (opc != 3) {
            System.out.print("Menu: Digite (1) Registro de Notas, (3) Salir : ");
            opc = in.nextShort();
            
            // Validacion del Menu
            switch (opc) {
                case 1:                        
                    // Validamos que sea la letra A o B.
                    while (opc != 2) { 
                        System.out.println("Ingrese el resultado del estudiante: "+ numEstudiante);
                        resultadoExamen = in.next().toUpperCase(); // Leemos el resultado y convertimos a Mayusculas 
                    switch (resultadoExamen) {
                        case "A":
                        case "B":
                            System.out.println("Nota valida, Insertada en Registro");
                            
                            if(resultadoExamen.equals("A")){
                                notaA++;
                            }else{
                                notaB++;
                            }                            
                            System.out.println("Desea Agregar una nueva nota?: (1)-Si, (2)-No ");
                            nuevoEstudiante = in.nextInt();
                            
                            //Agregar nuevo estudiante
                            if(nuevoEstudiante == 2 ){
                                System.out.println("----- Saliendo del registro de notas -----");                               
                                opc = 2;                                
                                System.out.println("Total de estudiantes examinados: " + numEstudiante);
                                System.out.println("Total de estudiantes aprobados: "+notaA);
                                System.out.println("Porcentaje de estudiantes aprobados: "+ ((notaA*100)/numEstudiante) + "%");
                                System.out.println("Total de estudiantes desaprobados: "+notaB);
                                System.out.println("Porcentaje de estudiantes desaprobados: "+ ((notaB*100)/numEstudiante + "%"));
                                numEstudiante = 0;
                                System.out.println("Puede ingresar otro registro de notas o salir del programa");
                            }
                            numEstudiante++;
                            break;                        
                        default:
                            System.out.println("Nota Invalida");
                            break;
                        }
                    }
                break;                    
                case 3:
                    System.out.println("----- Saliendo del programa -----"); 
                    break;
                default:
                    System.out.println("Opción Invalida");
            }                        
        }
        in.close();
               
    }
    
}
