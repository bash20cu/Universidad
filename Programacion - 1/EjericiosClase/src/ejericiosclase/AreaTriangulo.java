/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ejericiosclase;
import java.util.Scanner;
/**
 *
 * @author migue daniel
 */
public class AreaTriangulo {  
    
    public void Area(){        
        
        float base = 0;
        float altura = 0;
        float area = 0;
        
        System.out.println("Area de un triangulo \n");  
        
        System.out.print("Escriba la base del triangulo: ");        
        base = validar(base);
        
               
        
        while (base < 0 )
        {
            System.out.println(" La base del triangulo es incorrecta ");
            System.out.println("Solo numeros fraccionarios positivos estan permitidos");
            System.out.print("Intente de nuevo: ");
            base = validar(base);           
        }
        
        System.out.print("Escriba la altura del triangulo: ");
        altura = validar(altura); 
        
        while (altura < 0 )
        {
            System.out.print(" La altura del triangulo es incorrecta ");
            System.out.println("Solo numeros fraccionarios positivos estan permitidos");
            System.out.print("Intente de nuevo: ");
            altura = validar(altura);
        }            
        
        area = base * altura /2;        
        System.out.print("El area del triangulo es: " + area +"\n");       
    }

    
    private float validar(float numero) {
        Scanner entrada = new Scanner(System.in);
        
        try
        {
            numero = entrada.nextFloat();
        }
        
        catch(Exception e)
        {
            System.out.println("Error: "+ e +" -- "+ e.getMessage());
            return numero = -1;
        }
        
        if ( numero == 0)
        {
            System.out.println("WTF  ¯\\_(ツ)_/¯ ");
            numero = -1;
        }
        
        return numero;
    }
    
}
