/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author migue
 */
public class CaminosMinimos {

    // Metodo para determinar todos los caminos FLOYD
    public String algoritmoFloyd(long[][] mAdy) {
        int vertices = mAdy.length;
        long matrizAdyacencia[][] = mAdy;
        String caminos[][] = new String[vertices][vertices];
        String caminosAuxiliares[][] = new String[vertices][vertices];
        String caminosRecorridos = "", cadena = "", caminitos = "";
        int i, j, k;
        float temporal1, temporal2, temporal3, temporal4, minimo;

        //Inicializando las matrices caminos y caminos Auxiliares
        for (i = 0; i < vertices; i++) {
            for (j = 0; j < vertices; j++) {
                caminos[i][j] = "";
                caminosAuxiliares[i][j] = "";

            }
        }
        for (k = 0; k < vertices; k++) {
            for (j = 0; j < vertices; j++) {
                temporal1 = matrizAdyacencia[i][j];
                temporal2 = matrizAdyacencia[i][k];
                temporal3 = matrizAdyacencia[k][j];
                temporal4 = temporal2 + temporal3;
                //encontrar minimo
                minimo = Math.min(temporal1, temporal4);
                if (temporal1 != temporal4) {
                    if (minimo == temporal4) {
                        caminosRecorridos = "";
                        caminosAuxiliares[i][j] = k + "";
                        caminos[i][j] = caminosR(i, k, caminosAuxiliares, caminosRecorridos) + (k+1);
                    }
                }
                matrizAdyacencia[i][j] = (long) minimo;
            }
        }
        //Agregando el camino minimo a cadena
        for (i = 0; i < vertices; i++) {
            for (j = 0; j < vertices; j++) {
                cadena = cadena+"["+matrizAdyacencia[i][j]+"]";

            }
            cadena = cadena+"\n";
        }
        // 
        for (i = 0; i < vertices; i++) {
            for (j = 0; j < vertices; j++) {
              

            }
            
        }
    }
}
