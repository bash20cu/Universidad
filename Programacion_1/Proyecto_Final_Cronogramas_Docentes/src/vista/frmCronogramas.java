/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista;

import java.awt.Color;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import modelo.clsCentroFormacion;
import modelo.clsCronograma;
import modelo.clsDocentes;
import modelo.clsModulos;
import modelo.clsPrograma;

/**
 *
 * @author migue
 */
public class frmCronogramas extends javax.swing.JInternalFrame {

    /**
     * Creates new form frmCronogramas
     */
    public frmCronogramas() {
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
        cmbDocente = new javax.swing.JComboBox<>();
        lblDocente = new javax.swing.JLabel();
        lblPrograma = new javax.swing.JLabel();
        cmbPrograma = new javax.swing.JComboBox<>();
        lblModulo = new javax.swing.JLabel();
        cmbModulos = new javax.swing.JComboBox<>();
        lblVacaciones = new javax.swing.JLabel();
        txtVacaciones = new javax.swing.JTextField();
        panOperaciones = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnElminar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        panTabla = new javax.swing.JPanel();
        scrScroll = new javax.swing.JScrollPane();
        tblCronogramas = new javax.swing.JTable();

        setBorder(null);
        setClosable(true);
        setTitle("Cronogramas");
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        panFormulario.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario Cronogramas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        lblDocente.setText("Docente");

        lblPrograma.setText("Programa");

        lblModulo.setText("Modulo");

        lblVacaciones.setText("Vacaciones");

        javax.swing.GroupLayout panFormularioLayout = new javax.swing.GroupLayout(panFormulario);
        panFormulario.setLayout(panFormularioLayout);
        panFormularioLayout.setHorizontalGroup(
            panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbDocente, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDocente)
                    .addComponent(cmbModulos, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModulo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbPrograma, 0, 151, Short.MAX_VALUE)
                    .addComponent(lblPrograma)
                    .addComponent(lblVacaciones)
                    .addComponent(txtVacaciones))
                .addGap(52, 52, 52))
        );
        panFormularioLayout.setVerticalGroup(
            panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panFormularioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addComponent(lblDocente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbDocente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addComponent(lblPrograma)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbPrograma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panFormularioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addComponent(lblModulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbModulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panFormularioLayout.createSequentialGroup()
                        .addComponent(lblVacaciones)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtVacaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(43, Short.MAX_VALUE))
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

        panTabla.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tabla Cronogramas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 18))); // NOI18N

        tblCronogramas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Docente", "Programa", "Modulo", "Vacaciones"
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
        tblCronogramas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCronogramasMouseClicked(evt);
            }
        });
        scrScroll.setViewportView(tblCronogramas);
        if (tblCronogramas.getColumnModel().getColumnCount() > 0) {
            tblCronogramas.getColumnModel().getColumn(0).setResizable(false);
            tblCronogramas.getColumnModel().getColumn(1).setResizable(false);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(panOperaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panFormulario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
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

            clsCronograma crono = new clsCronograma(Integer.parseInt(txtVacaciones.getText()));

            clsDocentes Doc = new clsDocentes();
            ArrayList<Object> docentes = Doc.getRegistros();
            String SelectedDocente = (String) cmbDocente.getSelectedItem();

            for (Object docente : docentes)
            {

                clsDocentes docenteTemp = (clsDocentes) docente;
                if (SelectedDocente.equals(docenteTemp.getNombre()))
                {
                    crono.setDocente(docenteTemp);
                }
            }

            clsPrograma Prog = new clsPrograma();
            ArrayList<Object> programas = Prog.getRegistros();
            String SelectedProg = (String) cmbPrograma.getSelectedItem();

            for (Object programa : programas)
            {
                clsPrograma programaTemp = (clsPrograma) programa;
                if (SelectedProg.equals(programaTemp.getCodigo()))
                {
                    crono.setPrograma(programaTemp);
                }
            }

            clsModulos Mod1 = new clsModulos();
            ArrayList<Object> modulos = Mod1.getRegistros();
            String selectedModulo = (String) cmbModulos.getSelectedItem();

            for (Object modulo : modulos)
            {
                clsModulos moduloTemp = (clsModulos) modulo;
                if (selectedModulo.equals(moduloTemp.getNombre()))
                {
                    crono.setModulo(moduloTemp);
                }
            }

            if (crono.guardar() == 1)
            {
                JOptionPane.showMessageDialog(null, "Cronograma Guardado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
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

                clsCronograma crono = new clsCronograma(Integer.parseInt(txtVacaciones.getText()));
                clsDocentes Doc = new clsDocentes();
                ArrayList<Object> docentes = Doc.getRegistros();
                String SelectedDocente = (String) cmbDocente.getSelectedItem();

                for (Object docente : docentes)
                {

                    clsDocentes docenteTemp = (clsDocentes) docente;
                    if (SelectedDocente.equals(docenteTemp.getNombre()))
                    {
                        crono.setDocente(docenteTemp);
                    }
                }

                clsPrograma Prog = new clsPrograma();
                ArrayList<Object> programas = Prog.getRegistros();
                String SelectedProg = (String) cmbPrograma.getSelectedItem();

                for (Object programa : programas)
                {
                    clsPrograma programaTemp = (clsPrograma) programa;
                    if (SelectedProg.equals(programaTemp.getCodigo()))
                    {
                        crono.setPrograma(programaTemp);
                    }
                }

                clsModulos Mod1 = new clsModulos();
                ArrayList<Object> modulos = Mod1.getRegistros();
                String selectedModulo = (String) cmbModulos.getSelectedItem();

                for (Object modulo : modulos)
                {
                    clsModulos moduloTemp = (clsModulos) modulo;
                    if (selectedModulo.equals(moduloTemp.getNombre()))
                    {
                        crono.setModulo(moduloTemp);
                    }
                }
                if (crono.modificar() == 1)
                {
                    JOptionPane.showMessageDialog(null, "Cronograma modificado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
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
            clsCronograma crono = new clsCronograma(Integer.parseInt(txtVacaciones.getText()));
            clsDocentes Doc = new clsDocentes();
            ArrayList<Object> docentes = Doc.getRegistros();
            String SelectedDocente = (String) cmbDocente.getSelectedItem();

            for (Object docente : docentes)
            {

                clsDocentes docenteTemp = (clsDocentes) docente;
                if (SelectedDocente.equals(docenteTemp.getNombre()))
                {
                    crono.setDocente(docenteTemp);
                }
            }

            clsPrograma Prog = new clsPrograma();
            ArrayList<Object> programas = Prog.getRegistros();
            String SelectedProg = (String) cmbPrograma.getSelectedItem();

            for (Object programa : programas)
            {
                clsPrograma programaTemp = (clsPrograma) programa;
                if (SelectedProg.equals(programaTemp.getCodigo()))
                {
                    crono.setPrograma(programaTemp);
                }
            }

            clsModulos Mod1 = new clsModulos();
            ArrayList<Object> modulos = Mod1.getRegistros();
            String selectedModulo = (String) cmbModulos.getSelectedItem();

            for (Object modulo : modulos)
            {
                clsModulos moduloTemp = (clsModulos) modulo;
                if (selectedModulo.equals(moduloTemp.getNombre()))
                {
                    crono.setModulo(moduloTemp);
                }
            }
            if (crono.eliminar() == 1)
            {
                JOptionPane.showMessageDialog(null, "El docente eliminado correctamente", "Mensaje", JOptionPane.INFORMATION_MESSAGE);
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
        cargarTabla();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tblCronogramasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCronogramasMouseClicked
        btnGuardar.setEnabled(false);
        btnModificar.setEnabled(true);
        btnElminar.setEnabled(true);
        cmbDocente.setEnabled(false);

        clsCronograma m = new clsCronograma();
        m = (clsCronograma) m.getRegistro(tblCronogramas.getSelectedRow());
        txtVacaciones.setText(String.valueOf(m.getVacaciones()));

        clsDocentes Doc = new clsDocentes();
        ArrayList<Object> docentes = Doc.getRegistros();
        //rellenar Docente:     
        String SelectedDocente = m.getDocente().getNombre();

        for (Object docente : docentes)
        {

            clsDocentes docenteTemp = (clsDocentes) docente;
            if (SelectedDocente.equals(docenteTemp.getNombre()))
            {
                cmbDocente.removeAllItems();
                cmbDocente.addItem(m.getDocente().getNombre());
                m.setDocente(docenteTemp);
            }
        }

        clsPrograma Prog = new clsPrograma();
        ArrayList<Object> programas = Prog.getRegistros();

        //rellenar los programas:
        cmbPrograma.removeAllItems();
        for (Object programa : programas)
        {
            clsPrograma programaTemp = (clsPrograma) programa;
            cmbPrograma.addItem(programaTemp.getCodigo());
        }

        clsModulos Mod1 = new clsModulos();
        ArrayList<Object> modulos = Mod1.getRegistros();
        cmbModulos.removeAllItems();
        //rellenar la lista de modulos disponibles    
        for (Object modulo : modulos)
        {
            clsModulos m1 = (clsModulos) modulo;
            //generar la fila
            cmbModulos.addItem(m1.getNombre());
        }


    }//GEN-LAST:event_tblCronogramasMouseClicked

    private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
        cargarTabla();
    }//GEN-LAST:event_formFocusGained


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnElminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cmbDocente;
    private javax.swing.JComboBox<String> cmbModulos;
    private javax.swing.JComboBox<String> cmbPrograma;
    private javax.swing.JLabel lblDocente;
    private javax.swing.JLabel lblModulo;
    private javax.swing.JLabel lblPrograma;
    private javax.swing.JLabel lblVacaciones;
    private javax.swing.JPanel panFormulario;
    private javax.swing.JPanel panOperaciones;
    private javax.swing.JPanel panTabla;
    private javax.swing.JScrollPane scrScroll;
    private javax.swing.JTable tblCronogramas;
    private javax.swing.JTextField txtVacaciones;
    // End of variables declaration//GEN-END:variables

    private void cargarTabla() {
        limpiarCampos();

        clsCronograma Crono = new clsCronograma();
        ArrayList<Object> cronogramas = Crono.getRegistros();
        DefaultTableModel model = (DefaultTableModel) tblCronogramas.getModel();
        //borrar filas
        model.setRowCount(0);

        for (Object cronograma : cronogramas)
        {

            clsCronograma c = (clsCronograma) cronograma;

            //generar la fila
            model.addRow(new Object[]
            {
                c.getDocente().getNombre(),
                c.getPrograma().getCodigo(),
                c.getModulo().getNombre(),
                c.getVacaciones()
            });
        }

        clsDocentes Doc = new clsDocentes();
        ArrayList<Object> docentes = Doc.getRegistros();

        //rellenar Docentes:     
        cmbDocente.removeAllItems();
        for (Object docente : docentes)
        {
            clsDocentes docenteTemp = (clsDocentes) docente;

            cmbDocente.addItem(docenteTemp.getNombre());

        }

        clsPrograma Prog = new clsPrograma();
        ArrayList<Object> programas = Prog.getRegistros();

        //rellenar los programas:
        cmbPrograma.removeAllItems();
        for (Object programa : programas)
        {
            clsPrograma programaTemp = (clsPrograma) programa;
            cmbPrograma.addItem(programaTemp.getCodigo());
        }

        clsModulos Mod1 = new clsModulos();
        ArrayList<Object> modulos = Mod1.getRegistros();
        cmbModulos.removeAllItems();
        //rellenar la lista de modulos disponibles    
        for (Object modulo : modulos)
        {
            clsModulos m1 = (clsModulos) modulo;
            //generar la fila
            cmbModulos.addItem(m1.getNombre());
        }
    }

    private void limpiarCampos() {
        txtVacaciones.setText("");
        cmbModulos.setEnabled(true);
        cmbDocente.setEnabled(true);
        cmbPrograma.setEnabled(true);
    }

    private boolean validar() {
        if (txtVacaciones.getText().length() == 0 || txtVacaciones.getText().compareTo("") == 0)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar las vacaciones del docente", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtVacaciones);
            return false;
        }
        try
        {
            int x = Integer.parseInt(txtVacaciones.getText());
        } catch (NumberFormatException e)
        {
            JOptionPane.showMessageDialog(null, "Debe ingresar las vacaciones con solo digitos numéricos, ejemplo 2022", "Mensaje", JOptionPane.ERROR_MESSAGE);
            this.formatoError(txtVacaciones);
            return false;
        }
        return true;
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

    private void CancelarBtn() {
        limpiarCampos();
        btnGuardar.setEnabled(true);
        btnModificar.setEnabled(false);
        btnElminar.setEnabled(false);
        cmbDocente.setEnabled(true);
    }
}
