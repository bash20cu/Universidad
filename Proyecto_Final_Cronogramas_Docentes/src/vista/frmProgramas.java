package vista;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;
import modelo.clsModulos;
import modelo.clsPrograma;

public class frmProgramas extends javax.swing.JInternalFrame {

    /**
     * Creates new form frmProgramas
     */
    public frmProgramas() {
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

        panFormulario = new javax.swing.JPanel();
        lblSector = new javax.swing.JLabel();
        txtSector = new javax.swing.JTextField();
        lblCodigoPrograma = new javax.swing.JLabel();
        txtCodigoPrograma = new javax.swing.JTextField();
        lblGrupo = new javax.swing.JLabel();
        txtGrupo = new javax.swing.JTextField();
        lblAnno = new javax.swing.JLabel();
        txtAnno = new javax.swing.JTextField();
        cmbCentrosFormacion = new javax.swing.JComboBox<>();
        lblModulo = new javax.swing.JLabel();
        panOperaciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnElminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        panTabla = new javax.swing.JPanel();
        scrScroll = new javax.swing.JScrollPane();
        tblModulos = new javax.swing.JTable();

        setClosable(true);

        panFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario Programas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        lblSector.setText("Sector");

        lblCodigoPrograma.setText("Codigo");

        lblGrupo.setText("Grupo");

        lblAnno.setText("Año");

        lblModulo.setText("Modulos");

        javax.swing.GroupLayout panFormularioLayout = new javax.swing.GroupLayout(panFormulario);
        panFormulario.setLayout(panFormularioLayout);
        panFormularioLayout.setHorizontalGroup(
            panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblSector)
                            .addComponent(txtSector, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)
                            .addComponent(lblGrupo)
                            .addComponent(txtGrupo))
                        .addGap(18, 18, 18)
                        .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblCodigoPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAnno)
                            .addComponent(txtCodigoPrograma, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                            .addComponent(txtAnno)))
                    .addComponent(cmbCentrosFormacion, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModulo))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        panFormularioLayout.setVerticalGroup(
            panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFormularioLayout.createSequentialGroup()
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panFormularioLayout.createSequentialGroup()
                                .addComponent(lblSector)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtSector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panFormularioLayout.createSequentialGroup()
                                .addComponent(lblCodigoPrograma)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtCodigoPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblGrupo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addComponent(lblAnno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtAnno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblModulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbCentrosFormacion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
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

        panTabla.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabla Programas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        tblModulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Codigo", "Nombre", "Duracion", "Sector"
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
                .addComponent(panFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(panTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if (this.validar())
        {
            clsPrograma m = new clsPrograma(txtSector.getText(), txtCodigoPrograma.getText(),
                txtGrupo.getText(), Integer.parseInt(txtAnno.getText()));
            if (m.guardar() == 1)
            {
                JOptionPane.showMessageDialog(null, "Programa Guardado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
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

                clsPrograma m = new clsPrograma(txtSector.getText(), txtCodigoPrograma.getText(),
                    txtGrupo.getText(), Integer.parseInt(txtAnno.getText()));
                if (m.modificar() == 1)
                {
                    JOptionPane.showMessageDialog(null, "Modulo modificado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
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
            clsPrograma m = new clsPrograma(txtSector.getText(), txtCodigoPrograma.getText(),
                txtGrupo.getText(), Integer.parseInt(txtAnno.getText()));

            if (m.eliminar() == 1)
            {
                JOptionPane.showMessageDialog(null, "El programa a sido eliminado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
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
        txtSector.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tblModulosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblModulosMouseClicked
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnElminar.setEnabled(true);
        txtSector.setEnabled(false);

        clsPrograma m = new clsPrograma();
        m = (clsPrograma) m.getRegistro(tblModulos.getSelectedRow());
        txtSector.setText(m.getCodigo());
        txtCodigoPrograma.setText(m.getCodigo());
        txtGrupo.setText(m.getGrupo());
        txtAnno.setText(String.valueOf(m.getAnno()));
    }//GEN-LAST:event_tblModulosMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnElminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cmbCentrosFormacion;
    private javax.swing.JLabel lblAnno;
    private javax.swing.JLabel lblCodigoPrograma;
    private javax.swing.JLabel lblGrupo;
    private javax.swing.JLabel lblModulo;
    private javax.swing.JLabel lblSector;
    private javax.swing.JPanel panFormulario;
    private javax.swing.JPanel panOperaciones;
    private javax.swing.JPanel panTabla;
    private javax.swing.JScrollPane scrScroll;
    private javax.swing.JTable tblModulos;
    private javax.swing.JTextField txtAnno;
    private javax.swing.JTextField txtCodigoPrograma;
    private javax.swing.JTextField txtGrupo;
    private javax.swing.JTextField txtSector;
    // End of variables declaration//GEN-END:variables

    private boolean validar() {
       if (txtCodigoPrograma.getText().length() == 0 || txtCodigoPrograma.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar un codigo valido", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtCodigoPrograma);
            return false;
        }

        if (txtGrupo.getText().length() == 0 || txtGrupo.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar el grupo del programa", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtGrupo);
            return false;
        }

        if (txtAnno.getText().length() == 0 || txtAnno.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar el año", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtAnno);
            return false;
        }

        if (txtSector.getText().length() == 0 || txtSector.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar el sector del modulo", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtSector);
            return false;
        }
        try {
            int x = Integer.parseInt(txtAnno.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el año con solo digitos numéricos, ejemplo 2022", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtAnno);
            return false;
        }
        return true;
    }

    private void cargarTabla() {
        limpiarCampos();
        clsPrograma Mod = new clsPrograma();
        ArrayList<Object> programas = Mod.getRegistros();
        clsModulos Mod1 = new clsModulos();
        ArrayList<Object> modulos = Mod1.getRegistros();
        DefaultTableModel model = (DefaultTableModel) tblModulos.getModel();
        //borrar filas
        model.setRowCount(0);
        
                 
        
        
        for (Object programa : programas)
        {
            clsPrograma m = (clsPrograma) programa;
            //generar la fila
            model.addRow(new Object[]
            {
                m.getCodigo(),
                m.getSector(),
                m.getGrupo(),
                m.getAnno(),
            });    
        }
        //rellenar la lista de modulos disponibles    
        for (Object modulo : modulos)
        {
            clsModulos m1 = (clsModulos) modulo;
            //generar la fila
            cmbCentrosFormacion.addItem(m1.getNombre());
        }
        
    }

    private void CancelarBtn() {
        limpiarCampos();
        txtCodigoPrograma.setEnabled(true);
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
    }

    private void limpiarCampos() {
        txtCodigoPrograma.setText("");
        txtGrupo.setText("");
        txtAnno.setText("");
        txtSector.setText("");
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
