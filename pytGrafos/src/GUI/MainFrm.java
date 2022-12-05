package GUI;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrm extends javax.swing.JFrame {

    static DefaultListModel<String> model = new DefaultListModel<>();
    static boolean ospfaction = false;
    static areaP areaP = new areaP();
    static OSPF ospf;
    String pathtitle = "", DEFAULT_TITLE = "Algoritmo de Dijkstra";

    public MainFrm() {
        /* try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
          //  Logger.getLogger(MainFrm.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        chooser.setFileFilter(new FileNameExtensionFilter("Archivo Anubis (.anb)", "anb"));
        initComponents();
        getContentPane().add(areaP, java.awt.BorderLayout.CENTER);
        inspector.setVisible(false);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        inspector = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        idtx = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaA = new javax.swing.JList<>(model);
        addApuntadorBt = new javax.swing.JButton();
        removeApuntadorBt = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        inicio = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        fin = new javax.swing.JTextField();
        RESULT = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        nuevoMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        abrirMenu = new javax.swing.JMenuItem();
        saveMenu = new javax.swing.JMenuItem();
        saveasMenu = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Algoritmo de Dijkstra");

        inspector.setBackground(new java.awt.Color(204, 204, 255));
        inspector.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Inspector", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        jLabel2.setText("ID:");

        idtx.setText("  ");

        listaA.setBackground(new java.awt.Color(204, 204, 255));
        listaA.setBorder(javax.swing.BorderFactory.createTitledBorder("Apuntadores"));
        listaA.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        jScrollPane1.setViewportView(listaA);

        addApuntadorBt.setText("+");
        addApuntadorBt.setToolTipText("Agregar Apuntador");
        addApuntadorBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addApuntadorBtActionPerformed(evt);
            }
        });

        removeApuntadorBt.setText("-");
        removeApuntadorBt.setToolTipText("Eliminar apuntador seleccionado");
        removeApuntadorBt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeApuntadorBtActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout inspectorLayout = new javax.swing.GroupLayout(inspector);
        inspector.setLayout(inspectorLayout);
        inspectorLayout.setHorizontalGroup(
                inspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                        .addGroup(inspectorLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(inspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(inspectorLayout.createSequentialGroup()
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(inspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(addApuntadorBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(removeApuntadorBt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(inspectorLayout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(idtx, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        inspectorLayout.setVerticalGroup(
                inspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(inspectorLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(inspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(idtx))
                                .addGap(30, 30, 30)
                                .addGroup(inspectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(inspectorLayout.createSequentialGroup()
                                                .addComponent(addApuntadorBt)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(removeApuntadorBt))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 100, Short.MAX_VALUE))
        );

        getContentPane().add(inspector, java.awt.BorderLayout.LINE_END);

        jPanel5.setBackground(new java.awt.Color(153, 153, 255));

        jButton2.setText("Buscar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Nodo Inicial");

        jLabel4.setText("Nodo Final");

        RESULT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel5.setText("Ruta más Corta");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(29, 29, 29)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(inicio, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(fin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(55, 55, 55)
                                                .addComponent(jButton2)))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(RESULT, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(570, 570, 570))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(fin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jLabel4)
                                                        .addComponent(jLabel5))
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(inicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(RESULT, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(21, 21, 21)
                                .addComponent(jButton2)
                                .addContainerGap(17, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel5, java.awt.BorderLayout.PAGE_END);

        nuevoMenu.setText("Archivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Nuevo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        nuevoMenu.add(jMenuItem1);

        abrirMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        abrirMenu.setText("Abrir");
        abrirMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirMenuActionPerformed(evt);
            }
        });
        nuevoMenu.add(abrirMenu);

        saveMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenu.setText("Guardar");
        saveMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuActionPerformed(evt);
            }
        });
        nuevoMenu.add(saveMenu);

        saveasMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveasMenu.setText("Guardar como");
        saveasMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveasMenuActionPerformed(evt);
            }
        });
        nuevoMenu.add(saveasMenu);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Salir");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        nuevoMenu.add(jMenuItem3);

        jMenuBar1.add(nuevoMenu);

        setJMenuBar(jMenuBar1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>                        

    private void addApuntadorBtActionPerformed(java.awt.event.ActionEvent evt) {
        String[] s = JOptionPane.showInputDialog("Ingresa el ID del nodo a apuntar y su Costo\nEjemplo: X,5").split(",");
        NodosFactory.getNodo(idtx.getText()).addApuntadores(s[0], Integer.parseInt(s[1]));
        if (!getTitle().contains("*")) {
            setTitle(getTitle() + "*");
        }
    }

    private void removeApuntadorBtActionPerformed(java.awt.event.ActionEvent evt) {
        int s = listaA.getSelectedIndex();
        if (s != -1) {
            NodosFactory.getNodo(idtx.getText()).removeApuntador(model.remove(s).split("->")[0]);
            if (!getTitle().contains("*")) {
                setTitle(getTitle() + "*");
            }
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (NodosFactory.IDexist(inicio.getText()) && NodosFactory.IDexist(fin.getText())) {
            ospf = new OSPF(inicio.getText(), fin.getText());
            ospf.ospf(0, OSPF.Nodo_Inicial);
            ospf.LineasCorto();
            //ospf.LineasLargo();
            ospfaction = true;
            RESULT.setText("" + OSPF.costomenor);
            areaP.repaint();
            //JOptionPane.showMessageDialog(null, OSPF.costoscorto+"\n"+OSPF.costoslargo);
        } else {
            JOptionPane.showMessageDialog(null, "Uno de los nodos NO existe!!");
        }
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        clearPanel();
        pathtitle = "";
        setTitle(DEFAULT_TITLE);
        inspector.setVisible(false);
    }

    private void saveMenuActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //clearPanel();
            if (!pathtitle.equals("")) {
                FileUtils.create_ANB_File(pathtitle);
                setTitle(DEFAULT_TITLE + "   " + pathtitle);
            } else {
                pathtitle = getPathtoSave(true);
                FileUtils.create_ANB_File(pathtitle);
                setTitle(DEFAULT_TITLE + "   " + pathtitle);
            }
            inspector.setVisible(false);
        } catch (IOException ex) {
        }
    }

    private void saveasMenuActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            //clearPanel();
            pathtitle = getPathtoSave(true);
            if (!pathtitle.equals("")) {
                FileUtils.create_ANB_File(pathtitle);
                setTitle(DEFAULT_TITLE + "   " + pathtitle);
            }
        } catch (IOException ex) {
        }
        inspector.setVisible(false);
    }

    private void abrirMenuActionPerformed(java.awt.event.ActionEvent evt) {
        try {

            pathtitle = getPathtoSave(false);
            clearPanel();
            FileUtils.read_ANB_File(pathtitle);
            setTitle(DEFAULT_TITLE + "   " + pathtitle);
        } catch (IOException ex) {
        }
        inspector.setVisible(false);
    }

    private String getPathtoSave(boolean save) {
        String title = "";
        int r = -1;
        if (save) {
            title = "Guardar como";
        } else {
            title = "Abrir Archivo";
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        chooser.setCurrentDirectory(new java.io.File(""));
        chooser.setDialogTitle(title);
        chooser.setAcceptAllFileFilterUsed(false);
        if (save) {
            r = chooser.showSaveDialog(this);
        } else {
            r = chooser.showOpenDialog(this);
        }

        if (r == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().toString();
        } else {
            return "";
        }
    }

    private void clearPanel() {
        NodosFactory.getListaNodos().clear();
        areaP.revalidate();
        areaP.removeAll();
        inicio.setText("");
        fin.setText("");
        RESULT.setText("");
        areaP.updateUI();
    }

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
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainFrm().setVisible(true);
        });
    }
    public JFileChooser chooser = new JFileChooser();
    // Variables declaration - do not modify                     
    public javax.swing.JLabel RESULT;
    public javax.swing.JMenuItem abrirMenu;
    public javax.swing.JButton addApuntadorBt;
    public static javax.swing.JTextField fin;
    public static javax.swing.JLabel idtx;
    public static javax.swing.JTextField inicio;
    public static javax.swing.JPanel inspector;
    public javax.swing.JButton jButton2;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JMenuBar jMenuBar1;
    public javax.swing.JMenuItem jMenuItem1;
    public javax.swing.JMenuItem jMenuItem3;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JPanel jPanel5;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JList<String> listaA;
    public javax.swing.JMenu nuevoMenu;
    public javax.swing.JButton removeApuntadorBt;
    public javax.swing.JMenuItem saveMenu;
    public javax.swing.JMenuItem saveasMenu;
    // End of variables declaration                   
}
