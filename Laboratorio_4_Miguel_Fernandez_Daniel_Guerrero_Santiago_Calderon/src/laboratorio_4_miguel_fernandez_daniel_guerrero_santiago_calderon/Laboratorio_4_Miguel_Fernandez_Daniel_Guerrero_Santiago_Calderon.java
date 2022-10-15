/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package laboratorio_4_miguel_fernandez_daniel_guerrero_santiago_calderon;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author migue
 */
/*Ejercicio #1: 
Super Mercado Feliz lo ha contratado para desarrollar una 
 aplicación que administra(inserte, modifique, elimine y consulte) los 
    productos(nombre y precio), 
    además de un modulo de ventas que simule un carrito de compra, 
    donde el usuario agrega productos y cantidad a comprar, 
    el usuario ingresa productos hasta que desea pagar, mostrando en pantalla el 
    desglose a pagar(importe de cada uno de los productos del carrito, subtotal, 
    impuesto(13%) y total a pagar)
*/
public class Laboratorio_4_Miguel_Fernandez_Daniel_Guerrero_Santiago_Calderon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int opc;
        Map<String,Float> productos = new HashMap<String,Float>();
        
        do {            
           menuPrincipal();          
           opc = in.nextInt();
            switch (opc) {
                case 1:
                    AdministrarSupermercado(productos,in);
                    break;
                case 2:
                    
                    break;    
                case 3:
                    in.close();
                    System.out.println("----- Saliendo del programa -----"); 
                    break;
                default:
                    System.out.println("Opción Invalida");
            }
        } while (opc != 3);
    }
    
   /*
    Diferentes menues 
    */
    static void menuPrincipal() {
       System.out.println("Digite: 1 - Administrar, 2 - Comprar, 3 - Salir :  ");
    }
    static void menuAdministrar(){
       System.out.println("Digite: 1 - Inserter, 2 - Modificar, 3 - Eliminar, 4 - Consultar, 5 - Salir :");
    }
    
    
    /*
    Metodo para arministrar el supermercado
    Tiene CRUD
    */
    static void AdministrarSupermercado(Map<String,Float> productos,Scanner in){
        System.out.println("Estas Administrando los productos del inventario");
        System.out.println("Que desea hacer? ");  
        int opcAdmin;
        do {            
           menuAdministrar();   
           opcAdmin = in.nextInt();
            switch (opcAdmin) {
                case 1:
                    
                    break;
                case 2:
                    
                    break;    
                case 5:
                    System.out.println("----- Saliendo del Administrador -----"); 
                    break;
                default:
                    System.out.println("Opción Invalida");
            }
        } while (opcAdmin != 5);
    }
    
    static void insertarProducto(Map<String,Float> productos,Scanner in){
        System.out.print("Nombre del producto: ");
        String nombreProducto = in.next();
        System.out.println("Precio del Producto: ");
        float precioProducto = in.nextFloat();        
        productos.put(nombreProducto, precioProducto);
        
        System.out.println("Producto insertado correctamente");
        
    }
    /*
    Metodo para Simular la compra del supermercado
    */
   
    
}
