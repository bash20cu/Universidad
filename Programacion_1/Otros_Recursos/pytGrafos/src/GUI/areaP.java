package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class areaP extends javax.swing.JPanel {

    private int px = 0, py = 0;
    private Nodos nod;

    public areaP() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(156, 214, 181));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
String id = "";
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        px = evt.getX();
        py = evt.getY();

        if (evt.getModifiers() == MouseEvent.BUTTON1_MASK) {

            if (MainFrm.inspector.isVisible()) {
                MainFrm.inspector.setVisible(false);
            } else {

                id = "" + JOptionPane.showInputDialog("ID del Nodo");
                id = id.replace("null", "").replaceAll(" ", "");
                if (!id.isEmpty()) {
                    if (!NodosFactory.IDexist(id)) {
                        nod = new Nodos(id, px - 25, py - 25);
                        add(NodosFactory.addNodo(nod));
                        updateUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "El nodo ya existe");
                    }
                }

            }
        }
    }//GEN-LAST:event_formMouseClicked
    Color c = new Color(1850);

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(c);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintAll(g2);
    }

    private void paintAll(Graphics2D g2) {
        if (MainFrm.ospfaction) {
            MainFrm.ospf.getLineasCorto().forEach((Lineas b) -> {
                b.setColor(0, 153, 0);
                b.draw(g2);
            });
            MainFrm.ospf.getLineasLargo().forEach((Lineas b) -> {
                b.setColor(255, 51, 51);
                b.draw(g2);
            });
        }
        MainFrm.ospfaction = false;
        NodosFactory.getListaNodos().forEach((Nodos a) -> {
            a.getLineas().forEach((Lineas b) -> {
                b.setColor(102, 102, 102);
                b.draw(g2);
            });
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
