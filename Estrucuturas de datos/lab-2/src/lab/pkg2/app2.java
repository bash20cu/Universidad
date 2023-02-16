/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab.pkg2;

/**
 *
 * @author migue
 */
public class app2 {
    public long f(int n){
        if(n==0 || n==1){
            return 1;
        }else{
            System.out.println(n);
            return 3*f(n-2)+2*f(n-1);
        }
    }
}
