/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vista;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.clsClientes;

/**
 *
 * @author Est_Nautico
 */
public class frmClientes extends javax.swing.JInternalFrame {

    /**
     * Creates new form frmTarjetas
     */
    public frmClientes() {
        initComponents();
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
        this.cargarTabla();
    }

    public void cargarTabla() {
        clsClientes c = new clsClientes();
        ArrayList<Object> clientes = c.getRegistros();
        DefaultTableModel model = (DefaultTableModel) tblClientes.getModel();
        //borrar filas
        model.setRowCount(0);

        for (Object cliente : clientes) {
            clsClientes cl = (clsClientes) cliente;
            //generar la fila
            model.addRow(new Object[]{cl.getCedula(),
                cl.getNombre(),
                cl.getApellidos(),
                cl.getTelefono()});
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panDatosCliente = new javax.swing.JPanel();
        lblCedula = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        lblApellidos = new javax.swing.JLabel();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        panOperaciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnElminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        panOperaciones1 = new javax.swing.JPanel();
        scrScrollPlato1 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();

        setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(153, 153, 255), java.awt.Color.lightGray));
        setClosable(true);
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/logo.png"))); // NOI18N

        panDatosCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 14))); // NOI18N
        panDatosCliente.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblCedula.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblCedula.setText("Cedula");
        panDatosCliente.add(lblCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 30, -1, -1));

        txtCedula.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        panDatosCliente.add(txtCedula, new org.netbeans.lib.awtextra.AbsoluteConstraints(16, 51, 114, -1));

        lblNombre.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblNombre.setText("Nombre");
        panDatosCliente.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, -1, -1));

        txtNombre.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        panDatosCliente.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 114, -1));

        txtApellidos.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        panDatosCliente.add(txtApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 220, -1));

        lblApellidos.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblApellidos.setText("Apellidos");
        panDatosCliente.add(lblApellidos, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, -1, -1));

        lblTelefono.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblTelefono.setText("Telefono");
        panDatosCliente.add(lblTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, -1, -1));

        txtTelefono.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        panDatosCliente.add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 114, -1));

        panOperaciones.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Operaciones", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        btnGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/save.png"))); // NOI18N
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnModificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/edit.png"))); // NOI18N
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnElminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/delete.png"))); // NOI18N
        btnElminar.setFocusCycleRoot(true);
        btnElminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnElminarActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/recursos/cancel.png"))); // NOI18N
        btnCancelar.setFocusCycleRoot(true);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panOperacionesLayout = new javax.swing.GroupLayout(panOperaciones);
        panOperaciones.setLayout(panOperacionesLayout);
        panOperacionesLayout.setHorizontalGroup(
            panOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panOperacionesLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnElminar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panOperacionesLayout.setVerticalGroup(
            panOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnElminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        panOperaciones1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cedula", "Nombre", "Apellidos", "Telefono"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        scrScrollPlato1.setViewportView(tblClientes);

        javax.swing.GroupLayout panOperaciones1Layout = new javax.swing.GroupLayout(panOperaciones1);
        panOperaciones1.setLayout(panOperaciones1Layout);
        panOperaciones1Layout.setHorizontalGroup(
            panOperaciones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOperaciones1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrScrollPlato1, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                .addContainerGap())
        );
        panOperaciones1Layout.setVerticalGroup(
            panOperaciones1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panOperaciones1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrScrollPlato1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panDatosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .addComponent(panOperaciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(27, 27, 27)
                .addComponent(panOperaciones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panOperaciones1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(panDatosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        clsClientes c = new clsClientes(txtCedula.getText(),
                txtNombre.getText(),
                txtApellidos.getText(),
                txtTelefono.getText());
        if (c.guardar() == 1) {
            JOptionPane.showMessageDialog(null, "Cliente Guardado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
            this.cargarTabla();
            CancelarBtn();
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido guardar la información", "Error", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        int opc = JOptionPane.showConfirmDialog(null, "Estas seguro modificar este registro", "Mensaje", JOptionPane.YES_NO_OPTION);

        if (opc == 0) {

            clsClientes c = new clsClientes(txtCedula.getText(),
                    txtNombre.getText(),
                    txtApellidos.getText(),
                    txtTelefono.getText());

            if (c.modificar() == 1) {
                JOptionPane.showMessageDialog(null, "Cliente modificado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                this.cargarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "No se ha podido modificar la información", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            CancelarBtn();
        }

    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnElminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElminarActionPerformed
        int opc = JOptionPane.showConfirmDialog(null, "Estas seguro eliminar este registro", "Mensaje", JOptionPane.YES_NO_OPTION);

        if (opc == 0) {
            clsClientes c = new clsClientes(txtCedula.getText(),
                    txtNombre.getText(),
                    txtApellidos.getText(),
                    txtTelefono.getText());

            if (c.eliminar() == 1) {
                JOptionPane.showMessageDialog(null, "Cliente eliminado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                this.cargarTabla();
            } else {
                JOptionPane.showMessageDialog(null, "No se ha podido modificar la información", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            CancelarBtn();
        }

    }//GEN-LAST:event_btnElminarActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnElminar.setEnabled(true);

        clsClientes c = new clsClientes();
        c = (clsClientes) c.getRegistro(tblClientes.getSelectedRow());
        txtCedula.setText(c.getCedula());
        txtNombre.setText(c.getNombre());
        txtApellidos.setText(c.getApellidos());
        txtTelefono.setText(c.getTelefono());
    }//GEN-LAST:event_tblClientesMouseClicked

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
        limpiarCampos();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void limpiarCampos() {
        txtCedula.setText("");
        txtNombre.setText("");
        txtApellidos.setText("");
        txtTelefono.setText("");
    }

    private void CancelarBtn() {
        limpiarCampos();
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnElminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel lblApellidos;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JPanel panDatosCliente;
    private javax.swing.JPanel panOperaciones;
    private javax.swing.JPanel panOperaciones1;
    private javax.swing.JScrollPane scrScrollPlato1;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
