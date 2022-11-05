/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import javax.swing.JOptionPane;
import modelo.clsCuenta;

/**
 *
 * @author migue
 */
public class frmCuenta extends javax.swing.JFrame {
     clsCuenta cuenta = new clsCuenta();
     
    /**
     * Creates new form jtfCuenta
     */
    public frmCuenta() {
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

        panSaldo = new javax.swing.JPanel();
        lblSaldo = new javax.swing.JLabel();
        lblNumeroCuenta = new javax.swing.JLabel();
        lblSaldoDolar = new javax.swing.JLabel();
        panRetiro = new javax.swing.JPanel();
        lblRetiro = new javax.swing.JLabel();
        txtRetiro = new javax.swing.JTextField();
        btnRetiro = new javax.swing.JToggleButton();
        panDeposito = new javax.swing.JPanel();
        lblDeposito = new javax.swing.JLabel();
        txtDeposito = new javax.swing.JTextField();
        btnDeposito = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CitiCookie");
        setResizable(false);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        panSaldo.setBorder(javax.swing.BorderFactory.createTitledBorder("Saldo de la cuenta"));

        lblSaldo.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSaldo.setText("0.0");

        lblNumeroCuenta.setText("Numero de cuenta: XXX - XXXX - XXXX");

        lblSaldoDolar.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblSaldoDolar.setText("$");

        javax.swing.GroupLayout panSaldoLayout = new javax.swing.GroupLayout(panSaldo);
        panSaldo.setLayout(panSaldoLayout);
        panSaldoLayout.setHorizontalGroup(
            panSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panSaldoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panSaldoLayout.createSequentialGroup()
                        .addComponent(lblSaldoDolar, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(94, Short.MAX_VALUE))
        );
        panSaldoLayout.setVerticalGroup(
            panSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panSaldoLayout.createSequentialGroup()
                .addComponent(lblNumeroCuenta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(panSaldoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSaldoDolar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panRetiro.setBorder(javax.swing.BorderFactory.createTitledBorder("Retiro"));

        lblRetiro.setText("Ingrese el monto a retirar: ");

        txtRetiro.setText("0.0");

        btnRetiro.setText("Realizar Retiro");
        btnRetiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRetiroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panRetiroLayout = new javax.swing.GroupLayout(panRetiro);
        panRetiro.setLayout(panRetiroLayout);
        panRetiroLayout.setHorizontalGroup(
            panRetiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRetiroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panRetiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRetiro)
                    .addGroup(panRetiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtRetiro, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnRetiro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)))
                .addContainerGap(10, Short.MAX_VALUE))
        );
        panRetiroLayout.setVerticalGroup(
            panRetiroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panRetiroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRetiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRetiro)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panDeposito.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposito"));

        lblDeposito.setText("Ingrese monto del deposito:");

        txtDeposito.setText("0.0");

        btnDeposito.setText("Realizar Deposito");
        btnDeposito.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDepositoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panDepositoLayout = new javax.swing.GroupLayout(panDeposito);
        panDeposito.setLayout(panDepositoLayout);
        panDepositoLayout.setHorizontalGroup(
            panDepositoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panDepositoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panDepositoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDeposito)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panDepositoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btnDeposito, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtDeposito, javax.swing.GroupLayout.Alignment.LEADING))))
        );
        panDepositoLayout.setVerticalGroup(
            panDepositoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panDepositoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDeposito)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnDeposito)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnSalir.setText("Salir de la aplicacion");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(33, 33, 33))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSalir)
                .addGap(121, 121, 121))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(panSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panRetiro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panDeposito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(btnSalir)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        cuenta.saldoAleatorio();        
        lblSaldo.setText(cuenta.getCuenta().get("12"));       
    }//GEN-LAST:event_formComponentShown

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed
    
    private void btnRetiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRetiroActionPerformed
        // TODO add your handling code here:
        //Validacion que sea siempre un Numero. Capturamos el error
        try {
            //tomamos el saldo, almacenado en el Label, y lo formateamos para un double.
            double saldo = Double.parseDouble(lblSaldo.getText().replace(",", "."));
            double retiro = Double.parseDouble(txtRetiro.getText().replace(",", "."));                       
            txtRetiro.setText("0.0");
            btnRetiro.requestFocus();
            lblSaldo.setText(cuenta.retiro(saldo, retiro));
           
            //En caso que no sea un numero, advertimos al cliente que solo numeros estan permitidos
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Solo numeros estan permitidos", "Mensaje", JOptionPane.ERROR_MESSAGE);
            btnRetiro.requestFocus();
            txtRetiro.setText("0.0");
        }
        
        
              

    }//GEN-LAST:event_btnRetiroActionPerformed

    private void btnDepositoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDepositoActionPerformed
        // TODO add your handling code here:
        try {
            double saldo = Double.parseDouble(lblSaldo.getText().replace(",", "."));       
            double deposito = Double.parseDouble(txtDeposito.getText().replace(",", "."));
            lblSaldo.setText(cuenta.deposito(saldo, deposito));
            btnDeposito.requestFocus();
            txtDeposito.setText("0.0");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Solo numeros estan permitidos", "Mensaje", JOptionPane.ERROR_MESSAGE);
            btnDeposito.requestFocus();
            txtDeposito.setText("0.0");
        }     
        
    }//GEN-LAST:event_btnDepositoActionPerformed

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
            java.util.logging.Logger.getLogger(frmCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmCuenta.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmCuenta().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDeposito;
    private javax.swing.JToggleButton btnRetiro;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel lblDeposito;
    private javax.swing.JLabel lblNumeroCuenta;
    private javax.swing.JLabel lblRetiro;
    private javax.swing.JLabel lblSaldo;
    private javax.swing.JLabel lblSaldoDolar;
    private javax.swing.JPanel panDeposito;
    private javax.swing.JPanel panRetiro;
    private javax.swing.JPanel panSaldo;
    private javax.swing.JTextField txtDeposito;
    private javax.swing.JTextField txtRetiro;
    // End of variables declaration//GEN-END:variables
}
