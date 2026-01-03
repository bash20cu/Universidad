package objetos;

import java.awt.*;
import java.io.Serializable;

public class Circulo implements Serializable{

    // Instanciando Variables
    private static final long serialVersionUID = 6529685098267777690L;
    private Coordenadas coordenadas;
    private Color color;
    private int diametro;
    private String etiqueta;
    private Font fuente;
    private int izquierda;
    private int grosorBorde;

    /**
     * Constructor para objetos de la clase circulo
     */
    public Circulo(Coordenadas coordenadas) {
        // inicializando variables de instancia
        this.coordenadas = coordenadas;
        color = Color.yellow;
        diametro = 10;
        etiqueta = "";
        fuente = null;
        izquierda = 0;
        grosorBorde = 2;
    }

    public void dibujarCirculo(Graphics g) {
        if (coordenadas.size() > 0) {
            ((Graphics2D) g).setColor(color);

            ((Graphics2D) g).setStroke(new BasicStroke(getGrosorBorde()));

            ((Graphics2D) g).fillOval(coordenadas.get(0)[0], coordenadas.get(0)[1], diametro, diametro);
            ((Graphics2D) g).setColor(Color.black);
            ((Graphics2D) g).drawOval(coordenadas.get(0)[0], coordenadas.get(0)[1], diametro, diametro);
            if (!"".equals(etiqueta)) {
                if (fuente != null) {
                    g.setFont(fuente);
                }
                ((Graphics2D) g).drawString(etiqueta, coordenadas.get(0)[0] - (izquierda), coordenadas.get(0)[1]);

                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            }
        } else {
            System.out.println("No hay coordenadas para el circulo");
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setDiametro(int diametro) {
        this.diametro = diametro;
    }

    public int getDiametro() {
        return diametro;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public void setEtiqueta(String etiqueta, boolean izquierda) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public int getX() {
        if (coordenadas.size() > 0) {
            return coordenadas.get(0)[0];
        } else {
            return -1;
        }
    }

    public int getY() {
        if (coordenadas.size() > 0) {
            return coordenadas.get(0)[1];
        } else {
            return -1;
        }
    }

    public Font getFuente() {
        return fuente;
    }

    public void setFuente(Font fuente) {
        this.fuente = fuente;
    }

    public int isIzquierda() {
        return izquierda;
    }

    public void setIzquierda(int izquierda) {
        this.izquierda = izquierda;
    }

    public int getGrosorBorde() {
        return grosorBorde;
    }

    public void setGrosorBorde(int grosorBorde) {
        this.grosorBorde = grosorBorde;
    }

}
