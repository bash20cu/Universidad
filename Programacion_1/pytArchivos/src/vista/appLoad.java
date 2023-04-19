/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

/**
 *
 * @author Est_Nautico
 */
public class appLoad {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        mdiPrincipal mdi = new mdiPrincipal();
        mdi.setExtendedState(mdi.MAXIMIZED_BOTH);   
        mdi.setVisible(true);
    }
    
}
