package GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;

public class Lineas {

    Point N1, N2;
    String texto = "";
    int grosor = 1;
    Color defaultcolor = new Color(0);

    Lineas(Point n1, Point n2, String text) {
        N1 = n1;
        N2 = n2;
        texto = text;
    }

    Lineas(Point n1, Point n2, String text, int grosor) {
        N1 = n1;
        N2 = n2;
        texto = text;
        this.grosor = grosor;
    }

    Lineas(Point n1, Point n2) {
        N1 = n1;
        N2 = n2;
    }

    Lineas(Point n1, Point n2, int grosor) {
        N1 = n1;
        N2 = n2;
        this.grosor = grosor;
    }

    public void setColor(Color c) {
        defaultcolor = c;
    }

    public void setColor(int r, int g, int b) {
        defaultcolor = new Color(r, g, b);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(defaultcolor);
        g2.setStroke(new BasicStroke(grosor));
        g2.draw(new Line2D.Double((N1.x + 12), (N1.y + 12), (N2.x + 12), (N2.y + 12)));
        g2.setFont(new Font("Serif", Font.BOLD, 13));
        g2.setColor(new Color(0));
        if (texto != null) {
            g2.drawString(texto, ((N1.x + 12) + (N2.x + 12)) / 2, ((N1.y + 12) + (N2.y + 12)) / 2);
        }

    }
}
