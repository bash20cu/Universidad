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
        Map<String,Float> carrito = new HashMap<String,Float>();
        
        
        
        do {            
           menuPrincipal();          
           opc = in.nextInt();
            switch (opc) {
                case 1:
                    AdministrarSupermercado(productos,in);
                    break;
                case 2:
                    compras(productos,carrito, in);
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
    static void menuCompras(){
        System.out.println("Opciones Validas");
        System.out.println("1 - Agregar Producto a la lista ");
        System.out.println("2 - Pagar ");
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
        productos.put(nombreProducto.toUpperCase(), precioProducto);
        
        System.out.println("--- Producto insertado correctamente ---");
        System.out.println("Que desea hacer? ");        
    }
    
    //Metodo para modificar el Producto del inventario
    static void modificarProducto(Map<String,Float> productos,Scanner in){
        // System.out.println("Modificando productos");     
        
        //Validar que la lista no este vacia
        if(productos.isEmpty()){
            consultarProductos(productos);
        }else {
            //Lectura de los nuevos valores del producto
            consultarProductos(productos);
            System.out.println("Ingrese el nombre del producto que desea modificar");
            String productoModificado = in.next();
            productoModificado = productoModificado.toUpperCase();
            
            //Verificar si el producto existe dentro de la lista 
            for (String m : productos.keySet()) {                
                
                if(productoModificado.compareToIgnoreCase(m)==0){
                    System.out.println("Ingrese el nuevo precio: ");
                    float precioModificado = in.nextFloat();
                    productos.put(productoModificado, precioModificado);
                    System.out.println("--- Producto " + productoModificado + " Modificado Correctamente ---");
                    consultarProductos(productos);
                }else{
                    System.out.println("Producto no encontrado");
                    System.out.println("Consulte la lista nuevamente");
                }
            }
        }                      
    }
    
    //Metodo para Eliminar el producto del inventario
    static void eliminarProducto(Map<String,Float> productos,Scanner in){
        System.out.println("Eliminando productos");
        if(productos.isEmpty()){
             System.out.println("--- No hay productos para eliminar ---");
        }else{
          consultarProductos(productos);
          System.out.println("Ingrese el nombre del producto que desea eliminar");
          String productoEliminado = in.next();
          productoEliminado = productoEliminado.toUpperCase();          
          productos.remove(productoEliminado);
            System.out.println(" --- Producto " + productoEliminado + " eliminado exitosamente ---");
        }
    }
    
    //Metodo para Consultar total de inventario
    static void consultarProductos(Map<String,Float> productos){
        if(productos.isEmpty()){
            System.out.println("--- Inventario Vacio ---");
            System.out.println("Que desea hacer? ");
        }else{
            System.out.println("#"+" => Producto  "+"=> Precio");
            int i = 1;
            for (Object m : productos.keySet()) {
                System.out.println(" ("+ i + ") "+ m + " " + productos.get(m));
                i++;
            }
        }        
    }
    
    /*
    Metodo para Simular la compra del supermercado
    */
   static void compras(Map<String,Float> productos,Map<String,Float> carrito, Scanner in){
        System.out.println("Bienvenido al carrito de compras");
        System.out.println("Que desea hacer? ");  
        int opcCompras;
        do {            
           menuCompras();   
           opcCompras = in.nextInt();
            switch (opcCompras) {
                case 1:
                    agregarProducto(productos,carrito, in);
                    break;
                case 2:
                    pagar(carrito);
                    break;
                case 3:
                    System.out.println("--- Saliendo del carrito --- ");
                    break;
                default:
                    System.out.println("Opción Invalida");
            }
        } while (opcCompras != 2);
   }
   
   //Metodo para agregar productos a la lista de compras
   static void agregarProducto(Map<String,Float> productos,Map<String,Float> carrito, Scanner in){
       System.out.println("Estas agregando productos");
       if(productos.isEmpty()){
           System.out.println("No hay productos para comprar");
       }else{
           consultarProductos(productos);
           
           System.out.println("Digite el producto a comprar:");
           String producto = in.next();           
           
           producto = producto.toUpperCase();
           
           //Validamos que el producto este en el inventario
           for (String m : productos.keySet()) {
               if(producto.compareToIgnoreCase(m)==0){
                   System.out.println("Digite cantidad a agregar: ");
                   int cantidad = in.nextInt();
                   carrito.put(m, (productos.get(m)*cantidad));
               }else{
                   System.out.println("Producto no encontrado");
               }
           }
           
       }
   }
   
   //Metodo para pagar 
   static void pagar(Map<String,Float> carrito){        
        float importe = 0;
        final double IMPUESTO = 0.13;
        System.out.println("#"+" => Producto  "+"=> Precio");
            int i = 1;
            for (Object m : carrito.keySet()) {               
                System.out.println(" ("+ i + ") "+ m + " " + carrito.get(m));
                importe = importe + carrito.get(m);
                i++;
            }
            System.out.println("Subtotal: " + importe);
            System.out.println("Impuesto total: " + (importe*IMPUESTO));
            System.out.println("Total a pagar: " + (importe+(importe*IMPUESTO)));       
   }
    
}
