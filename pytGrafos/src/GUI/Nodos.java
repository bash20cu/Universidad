package GUI;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Nodos extends JLabel {

    private String sID = "";
    private ArrayList<Lineas> lineas;
    private final HashMap<String, Integer> apuntadores;
    boolean selected = false;

    public Nodos(String ID) {
        this.apuntadores = new HashMap<>();
        this.sID = ID;
        this.lineas = new ArrayList<>();
    }

    public Nodos(String ID, int x, int y) {
        this.apuntadores = new HashMap<>();
        this.sID = ID;
        this.lineas = new ArrayList<>();
        repaint();
        setBounds(x, y, 50, 50);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                selected = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                selected = false;
                repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getModifiers() == MouseEvent.BUTTON1_MASK) {
                    MainFrm.inspector.setVisible(true);
                    MainFrm.idtx.setText(sID);
                    MainFrm.model.clear();
                    getNodosApuntadores().forEach((a) -> {
                        MainFrm.model.addElement(a + "->" + getCosto(a));
                    });
                }
            }
        });
    }

    public void addApuntadores(String nodeid, int costo) {
        if (NodosFactory.IDexist(nodeid)) {
            if (!nodeid.equals(sID)) {
                if (ApuntadorExist(nodeid)) {
                    JOptionPane.showMessageDialog(null,
                            "Este Apuntador ya existe");
                } else {
                    apuntadores.put(nodeid, costo);
                    lineas.add(new Lineas(this.getLocation(), NodosFactory.
                            getNodo(nodeid).getLocation(), "" + costo));
                    MainFrm.model.addElement(nodeid + "->" + costo);
                    MainFrm.areaP.repaint();
                    repaint();
                }
            } else {
                JOptionPane.showMessageDialog(null,
                        "\"" + sID + "\" NO puede apuntar a si mismo!!! ");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Ese nodo no existe!!");
        }
    }

    public void removeApuntador(String id) {
        apuntadores.remove(id);
        lineas.clear();
        apuntadores.keySet().forEach((nodeid) -> {
            lineas.add(new Lineas(this.getLocation(), NodosFactory.
                    getNodo(nodeid).getLocation(), "" + getCosto(nodeid)));
        });

        MainFrm.areaP.repaint();
        repaint();
    }

    public boolean ApuntadorExist(String ID) {
        boolean b = false;
        for (String a : getNodosApuntadores()) {
            if (a.equals(ID)) {
                b = true;
                break;
            }
        }
        return b;
    }

    public ArrayList<String> getNodosApuntadores() {
        ArrayList<String> ap = new ArrayList<>();
        apuntadores.keySet().forEach((key) -> {
            ap.add(key);
        });
        return ap;
    }

    public ArrayList<Lineas> getLineas() {
        return lineas;
    }

    public int getCosto(String apuntadorNodoID) {
        return apuntadores.get(apuntadorNodoID);
    }

    public String getID() {
        return sID;
    }

    @Override
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        HoverNodo(g, selected);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
                1.0f));
        g2.setColor(new Color(0, 153, 102));
        g2.fillOval(5, 5, 20, 20);
        drawTextoCentrado(g, 10, 10, sID);
    }

    private void HoverNodo(Graphics g, boolean isSelected) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        if (isSelected) {
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.7f));
        } else {
            g2.setComposite(AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER, 0.0f));
        }
        g2.setColor(new Color(0, 192, 118));
        g2.fillOval(0, 0, 30, 30);
    }

    private void drawTextoCentrado(Graphics g, int x, int y, String texto) {
        g.setFont(new Font("Roboto", Font.BOLD, 12));
        FontMetrics fm = g.getFontMetrics();
        java.awt.geom.Rectangle2D rect = fm.getStringBounds(texto, g);
        g.setColor(Color.black);
        int textHeight = (int) (rect.getHeight());
        int textWidth = (int) (rect.getWidth());
        int cornerX = x - (textWidth / 2);
        int cornerY = y - (textHeight / 2) + fm.getAscent();
        g.drawString(texto, cornerX + 5, cornerY + 5);
    }

}
