/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/MDIApplication.java to edit this template
 */
package vista;

/**
 *
 * @author migue
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
        flmCatalogo = new javax.swing.JMenu();
        mntUsuarios = new javax.swing.JMenuItem();
        mntProductos = new javax.swing.JMenuItem();
        mntEmpleados = new javax.swing.JMenuItem();
        mntSalir = new javax.swing.JMenuItem();
        flmReportes = new javax.swing.JMenu();
        mntCalculadora = new javax.swing.JMenuItem();
        mntCut = new javax.swing.JMenuItem();
        mntCopy = new javax.swing.JMenuItem();
        mntPaste = new javax.swing.JMenuItem();
        mntDelete = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        flmCatalogo.setMnemonic('f');
        flmCatalogo.setText("Catalogos");

        mntUsuarios.setMnemonic('o');
        mntUsuarios.setText("Usuarios");
        mntUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mntUsuariosActionPerformed(evt);
            }
        });
        flmCatalogo.add(mntUsuarios);

        mntProductos.setMnemonic('s');
        mntProductos.setText("Productos");
        flmCatalogo.add(mntProductos);

        mntEmpleados.setMnemonic('a');
        mntEmpleados.setText("Empleados");
        flmCatalogo.add(mntEmpleados);

        mntSalir.setMnemonic('x');
        mntSalir.setText("Salir");
        mntSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mntSalirActionPerformed(evt);
            }
        });
        flmCatalogo.add(mntSalir);

        mnbMenu.add(flmCatalogo);

        flmReportes.setMnemonic('e');
        flmReportes.setText("Reportes");

        mntCalculadora.setMnemonic('t');
        mntCalculadora.setText("Calculadora");
        mntCalculadora.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mntCalculadoraActionPerformed(evt);
            }
        });
        flmReportes.add(mntCalculadora);

        mntCut.setMnemonic('t');
        mntCut.setText("Cut");
        mntCut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mntCutActionPerformed(evt);
            }
        });
        flmReportes.add(mntCut);

        mntCopy.setMnemonic('y');
        mntCopy.setText("Copy");
        flmReportes.add(mntCopy);

        mntPaste.setMnemonic('p');
        mntPaste.setText("Paste");
        flmReportes.add(mntPaste);

        mntDelete.setMnemonic('d');
        mntDelete.setText("Delete");
        flmReportes.add(mntDelete);

        mnbMenu.add(flmReportes);

        setJMenuBar(mnbMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(dskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(dskPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 719, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mntSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mntSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mntSalirActionPerformed

    private void mntUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mntUsuariosActionPerformed
        // TODO add your handling code here:
        
        //Instanciar JinternalFrame
       // jtfUsuarios vUsuarios = new jtfUsuarios();
        
        //agregar el jinternal al desktop panel
       // dskPanel.add(vUsuarios);
        
        //mostrar el desktop panel
       // vUsuarios.setVisible(true);
        
    }//GEN-LAST:event_mntUsuariosActionPerformed

    private void mntCutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mntCutActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mntCutActionPerformed

    private void mntCalculadoraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mntCalculadoraActionPerformed
        // TODO add your handling code here:
         //Instanciar JinternalFrame
        //jtfCalculadora vCalculadora = new jtfCalculadora();
        
        //agregar el jinternal al desktop panel
        //dskPanel.add(vCalculadora);
        
        //mostrar el desktop panel
        //vCalculadora.setVisible(true);
    }//GEN-LAST:event_mntCalculadoraActionPerformed

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
    private javax.swing.JMenu flmCatalogo;
    private javax.swing.JMenu flmReportes;
    private javax.swing.JMenuBar mnbMenu;
    private javax.swing.JMenuItem mntCalculadora;
    private javax.swing.JMenuItem mntCopy;
    private javax.swing.JMenuItem mntCut;
    private javax.swing.JMenuItem mntDelete;
    private javax.swing.JMenuItem mntEmpleados;
    private javax.swing.JMenuItem mntPaste;
    private javax.swing.JMenuItem mntProductos;
    private javax.swing.JMenuItem mntSalir;
    private javax.swing.JMenuItem mntUsuarios;
    // End of variables declaration//GEN-END:variables

}
