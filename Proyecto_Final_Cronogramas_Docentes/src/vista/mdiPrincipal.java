package vista;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import modelo.clsUsuarios;

public class mdiPrincipal extends javax.swing.JFrame {

    /**
     * Creates new form mdiPrincipal
     */
    public mdiPrincipal() {
        initComponents();      
        this.fmuAdministracion.setVisible(false);
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
        fmuCronograma = new javax.swing.JMenu();
        mniDocentes = new javax.swing.JMenuItem();
        mniModulos = new javax.swing.JMenuItem();
        mniProgramas = new javax.swing.JMenuItem();
        mniCronograma = new javax.swing.JMenuItem();
        mniSalir = new javax.swing.JMenuItem();
        fmuAdministracion = new javax.swing.JMenu();
        mniUsuarios = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        fmuCronograma.setMnemonic('f');
        fmuCronograma.setText("Cronograma");

        mniDocentes.setMnemonic('o');
        mniDocentes.setText("Docentes");
        fmuCronograma.add(mniDocentes);

        mniModulos.setMnemonic('s');
        mniModulos.setText("Modulos");
        fmuCronograma.add(mniModulos);

        mniProgramas.setMnemonic('a');
        mniProgramas.setText("Programas");
        mniProgramas.setDisplayedMnemonicIndex(5);
        fmuCronograma.add(mniProgramas);

        mniCronograma.setMnemonic('x');
        mniCronograma.setText("Cronogramas");
        mniCronograma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniCronogramaActionPerformed(evt);
            }
        });
        fmuCronograma.add(mniCronograma);

        mniSalir.setMnemonic('a');
        mniSalir.setText("Salir");
        fmuCronograma.add(mniSalir);

        mnbMenu.add(fmuCronograma);

        fmuAdministracion.setMnemonic('f');
        fmuAdministracion.setText("Administracion");

        mniUsuarios.setMnemonic('o');
        mniUsuarios.setText("Usuarios");
        mniUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mniUsuariosActionPerformed(evt);
            }
        });
        fmuAdministracion.add(mniUsuarios);

        mnbMenu.add(fmuAdministracion);

        setJMenuBar(mnbMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1012, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 636, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mniCronogramaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniCronogramaActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mniCronogramaActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Bienvenido "+this.getTitle(), "Informacion no valida", JOptionPane.ERROR_MESSAGE);
        validUsuario(this.getTitle());
    }//GEN-LAST:event_formWindowOpened

    private void mniUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mniUsuariosActionPerformed
        // TODO add your handling code here:
        frmUsuarios u = new frmUsuarios();
        dskPanel.add(u);
        u.setVisible(true);
    }//GEN-LAST:event_mniUsuariosActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(mdiPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
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

    private void validUsuario(String title) {        
      
        clsUsuarios c = new clsUsuarios();
        ArrayList<Object> usr = c.getRegistros();
         for (Object user : usr)
            {
                clsUsuarios u = (clsUsuarios) user;
                //System.out.println(u.getUsuario());
                if(u.getUsuario().contentEquals(title)){
                    
                    if(u.isAdmin()){
                        this.fmuAdministracion.setVisible(true);
                    }
                }
            }
        if(title.contentEquals("admin")){
            System.out.println(title);
             this.fmuAdministracion.setVisible(true);
        }
    }
        
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane dskPanel;
    private javax.swing.JMenu fmuAdministracion;
    private javax.swing.JMenu fmuCronograma;
    private javax.swing.JMenuBar mnbMenu;
    private javax.swing.JMenuItem mniCronograma;
    private javax.swing.JMenuItem mniDocentes;
    private javax.swing.JMenuItem mniModulos;
    private javax.swing.JMenuItem mniProgramas;
    private javax.swing.JMenuItem mniSalir;
    private javax.swing.JMenuItem mniUsuarios;
    // End of variables declaration//GEN-END:variables



}
