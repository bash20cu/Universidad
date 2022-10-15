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
        System.out.println("Opciones Validas");
        System.out.println("1 - Administrar ");
        System.out.println("2 - Comprar ");
        System.out.println("3 - Salir ");
    }
    static void menuAdministrar(){
        System.out.println("Opciones Validas");
        System.out.println("1 - Insertar Nuevo producto ");
        System.out.println("2 - Modificar Productos ");
        System.out.println("3 - Eliminar Productos ");
        System.out.println("4 - Consultar Productos ");
        System.out.println("5 - Salir ");
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
                    insertarProducto(productos, in);
                    break;
                case 2:
                    modificarProducto(productos, in);
                    break;
                case 3:
                    eliminarProducto(productos, in);
                    break;
                case 4:
                    consultarProductos(productos);
                    break;                    
                case 5:
                    System.out.println("----- Saliendo del Administrador -----"); 
                    break;
                default:
                    System.out.println("Opción Invalida");
            }
        } while (opcAdmin != 5);
    }
    /*
     Metodos para el CRUD del inventario
    */
    
    //Metodo para Insertar el producto al inventario
    static void insertarProducto(Map<String,Float> productos,Scanner in){
        System.out.print("Nombre del producto: ");
        String nombreProducto = in.next();
        System.out.print("Precio del Producto: ");
        float precioProducto = in.nextFloat();        
        productos.put(nombreProducto, precioProducto);
        
        System.out.println("--- Producto insertado correctamente ---");
        System.out.println("Que desea hacer? ");        
    }
    
    //Metodo para modificar el Producto del inventario
    static void modificarProducto(Map<String,Float> productos,Scanner in){
        System.out.println("Modificando productos");
    }
    
    //Metodo para Eliminar el producto del inventario
    static void eliminarProducto(Map<String,Float> productos,Scanner in){
        System.out.println("eliminando productos");
    }
    
    //Metodo para Consultar total de inventario
    static void consultarProductos(Map<String,Float> productos){
        for (Object m : productos.keySet()) {
            System.out.println(m);
        }
    }
    
    /*
    Metodo para Simular la compra del supermercado
    */
   
    
}
