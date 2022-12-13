package vista;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.clsCentroFormacion;


public class frmCentroFormacion extends javax.swing.JInternalFrame {

    /**
     * Creates new form frmCentroFormacion
     */
    public frmCentroFormacion() {
        initComponents();
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
        this.cargarTabla();  
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panTabla = new javax.swing.JPanel();
        scrScroll = new javax.swing.JScrollPane();
        tblModulos = new javax.swing.JTable();
        panFormulario = new javax.swing.JPanel();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblDireccion = new javax.swing.JLabel();
        txtDireccion = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        panOperaciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnElminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);

        panTabla.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabla Centro Formacion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        tblModulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Direccion", "Telefono"
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
        tblModulos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblModulosMouseClicked(evt);
            }
        });
        scrScroll.setViewportView(tblModulos);
        if (tblModulos.getColumnModel().getColumnCount() > 0) {
            tblModulos.getColumnModel().getColumn(0).setResizable(false);
            tblModulos.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout panTablaLayout = new javax.swing.GroupLayout(panTabla);
        panTabla.setLayout(panTablaLayout);
        panTablaLayout.setHorizontalGroup(
            panTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTablaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panTablaLayout.setVerticalGroup(
            panTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panTablaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(scrScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(131, 131, 131))
        );

        panFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario Centro Formacion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        lblCodigo.setText("Codigo");

        lblNombre.setText("Nombre");

        lblDireccion.setText("Direccion");

        lblTelefono.setText("Telefono");

        javax.swing.GroupLayout panFormularioLayout = new javax.swing.GroupLayout(panFormulario);
        panFormulario.setLayout(panFormularioLayout);
        panFormularioLayout.setHorizontalGroup(
            panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCodigo)
                    .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtCodigo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(lblDireccion))
                .addGap(18, 18, 18)
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTelefono)
                    .addComponent(txtNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(txtTelefono))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        panFormularioLayout.setVerticalGroup(
            panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panFormularioLayout.createSequentialGroup()
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panFormularioLayout.createSequentialGroup()
                                .addComponent(lblCodigo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panFormularioLayout.createSequentialGroup()
                                .addComponent(lblNombre)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblDireccion)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addComponent(lblTelefono)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(18, Short.MAX_VALUE))
        );

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
                .addContainerGap(27, Short.MAX_VALUE))
        );
        panOperacionesLayout.setVerticalGroup(
            panOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnElminar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnModificar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnCancelar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblModulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblModulosMouseClicked
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnElminar.setEnabled(true);
        txtCodigo.setEnabled(false);

        clsCentroFormacion m = new clsCentroFormacion();
        m = (clsCentroFormacion) m.getRegistro(tblModulos.getSelectedRow());
        txtCodigo.setText(m.getCodigo());
        txtNombre.setText(m.getNombre());
        txtDireccion.setText(m.getDireccion());
        txtTelefono.setText(m.getTelefono());
    }//GEN-LAST:event_tblModulosMouseClicked

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if (this.validar())
        {
            clsCentroFormacion m = new clsCentroFormacion(txtCodigo.getText(), txtNombre.getText(),
                txtDireccion.getText(), txtTelefono.getText());
            if (m.guardar() == 1)
            {
                JOptionPane.showMessageDialog(null, "Centro de formacion Guardado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                this.cargarTabla();
                CancelarBtn();
            } else
            {
                JOptionPane.showMessageDialog(null, "No se ha podido guardar la información", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

        if (this.validar())
        {
            int opc = JOptionPane.showConfirmDialog(null, "Estas seguro modificar este registro", "Mensaje", JOptionPane.YES_NO_OPTION);

            if (opc == 0)
            {

                clsCentroFormacion m = new clsCentroFormacion(txtCodigo.getText(), txtNombre.getText(),
                    txtDireccion.getText(), txtTelefono.getText());
                if (m.modificar() == 1)
                {
                    JOptionPane.showMessageDialog(null, "Centro de formacion  modificado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                    this.cargarTabla();
                } else
                {
                    JOptionPane.showMessageDialog(null, "No se ha podido modificar la información", "Error", JOptionPane.INFORMATION_MESSAGE);
                }
                CancelarBtn();
            }
        }
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnElminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnElminarActionPerformed

        int opc = JOptionPane.showConfirmDialog(null, "Estas seguro eliminar este registro", "Mensaje", JOptionPane.YES_NO_OPTION);

        if (opc == 0)
        {
            clsCentroFormacion m = new clsCentroFormacion(txtCodigo.getText(), txtNombre.getText(),
                txtDireccion.getText(), txtTelefono.getText());

            if (m.eliminar() == 1)
            {
                JOptionPane.showMessageDialog(null, "El centro de formacion a sido eliminado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
                this.cargarTabla();
            } else
            {
                JOptionPane.showMessageDialog(null, "No se ha podido eliminar la información", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            CancelarBtn();
        }
    }//GEN-LAST:event_btnElminarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
        limpiarCampos();
        txtCodigo.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnElminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JPanel panFormulario;
    private javax.swing.JPanel panOperaciones;
    private javax.swing.JPanel panTabla;
    private javax.swing.JScrollPane scrScroll;
    private javax.swing.JTable tblModulos;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables

    private boolean validar() {
         //validar que digite una cedula
        if (txtCodigo.getText().length() == 0 || txtCodigo.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo valido", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtCodigo);
            return false;
        }

        if (txtNombre.getText().length() == 0 || txtNombre.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar el nombre del centro de formacion", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtNombre);
            return false;
        }

        if (txtDireccion.getText().length() == 0 || txtDireccion.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar una direccion valida", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtDireccion);
            return false;
        }

       if (txtTelefono.getText().length() == 0 || txtTelefono.getText().compareTo("") == 0) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el télefono del cliente", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtTelefono);
            return false;
        }
        if (txtTelefono.getText().length() != 9) {
            JOptionPane.showMessageDialog(null, "Debe ingresar 9 digits en el télefono del cliente, ejemplo: 8899-7766 ", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtTelefono);
            return false;
        }
        return true;
    }

    private void cargarTabla() {
        limpiarCampos();
        clsCentroFormacion Mod = new clsCentroFormacion();
        ArrayList<Object> centroFormacion = Mod.getRegistros();
        DefaultTableModel model = (DefaultTableModel) tblModulos.getModel();
        //borrar filas
        model.setRowCount(0);

        for (Object centro : centroFormacion)
        {
            clsCentroFormacion m = (clsCentroFormacion) centro;
            //generar la fila
            model.addRow(new Object[]
            {
                m.getCodigo(),
                m.getNombre(),
                m.getDireccion(),
                m.getTelefono()
            });
        }
    }

    private void CancelarBtn() {
        limpiarCampos();
        txtCodigo.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
    }

   private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
    }

    private void formatoError(Object campo) {
        if (campo instanceof JTextField)
        {
            JTextField txt = (JTextField) campo;
            txt.setForeground(new Color(240, 44, 122));
            txt.setBorder(BorderFactory.createLineBorder(new Color(240, 44, 122)));
            txt.requestFocus();
        }
    }
}
