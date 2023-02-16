/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg2;

/**
 *
 * @author migue
 */
public class app1 {
    
    public double loge(){
        
        double en1, delta, fact;
        int n;
        en1 = fact = delta = 1.0;
        n=1;
        do{
            en1 += delta;
            n++;
            fact *= n;
            delta = 1.0 / fact;
            
        }while(en1 != en1 + delta); //Caso base , condicion para detener el ciclo
        return en1;
    }
    
    public double logeRecursive(int n,double fact,double delta,double en1){
        //caso base "en1 != en1 + delta"  delta es tan pequeño que no afecta al valor de en1
        if(en1 != en1 + delta){
          return en1;  
        }else{
            //Llamada recursiva para aproximar a e.
            //n++;
            //fact *=n;
            //delta = 1.0 / fact;
            return logeRecursive(n++,fact *= n, 1.0/fact, en1 + delta );
        }        
    }
}
