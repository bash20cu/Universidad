/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lab.pkg2;

/**
 *
 * @author migue
 */
public class Lab2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        app1 appCiclo = new app1();
        System.out.println(appCiclo.loge());
        System.out.println(appCiclo.logeRecursive(2,3.0, 1.0, 5.0));
        
        app2 app2Recursiva = new app2();
        System.out.println(app2Recursiva.f(5));
        
        
    }
    
}
