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
public class mdiPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form mdiPrincipal
     */
    public mdiPrincipal() {
        initComponents();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dskPanel = new javax.swing.JDesktopPane();
        mnbMenu = new javax.swing.JMenuBar();
        fmuCatalogos = new javax.swing.JMenu();
        mniTarjetas = new javax.swing.JMenuItem();
        mniClientes = new javax.swing.JMenuItem();
        mniSalir = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Control de Clientes y tarjetas");

        fmuCatalogos.setMnemonic('f');
        fmuCatalogos.setText("Catalogos");

        mniTarjetas.setMnemonic('s');
        mniTarjetas.setText("Clientes");
        mniTarjetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniTarjetasActionPerformed(evt);
            }
        });
        fmuCatalogos.add(mniTarjetas);

        mniClientes.setMnemonic('o');
        mniClientes.setText("Tarjetas");
        mniClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniClientesActionPerformed(evt);
            }
        });
        fmuCatalogos.add(mniClientes);

        mniSalir.setMnemonic('x');
        mniSalir.setText("Salir");
        mniSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniSalirActionPerformed(evt);
            }
        });
        fmuCatalogos.add(mniSalir);

        mnbMenu.add(fmuCatalogos);

        setJMenuBar(mnbMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mniSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mniSalirActionPerformed

    private void mniClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniClientesActionPerformed
        frmTarjetas v = new frmTarjetas();
        dskPanel.add(v);
        v.setVisible(true);
    }//GEN-LAST:event_mniClientesActionPerformed

    private void mniTarjetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniTarjetasActionPerformed
        frmClientes v = new frmClientes();
        dskPanel.add(v);
        v.setVisible(true);
    }//GEN-LAST:event_mniTarjetasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new mdiPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane dskPanel;
    private javax.swing.JMenu fmuCatalogos;
    private javax.swing.JMenuBar mnbMenu;
    private javax.swing.JMenuItem mniClientes;
    private javax.swing.JMenuItem mniSalir;
    private javax.swing.JMenuItem mniTarjetas;
    // End of variables declaration//GEN-END:variables

}
