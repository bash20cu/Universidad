package donkey_kong.modelo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import javax.imageio.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 *
 * La clase Credits se encarga de manejar y mostrar los créditos del jugador en
 * el juego Donkey Kong.
 *
 * Carga las imágenes necesarias y las utiliza para construir una imagen del
 * puntaje actual del jugador.
 *
 * La clase también tiene la capacidad de actualizar el puntaje y mostrar una
 * imagen que representa la suma de uno a la puntuación actual.
 *
 * La imagen del puntaje es un conjunto de caracteres de 8x10 píxeles que se
 * utiliza para construir la imagen del puntaje actual.
 */
public class Credits {
    //Variables 
    // Ancho y alto de los caracteres utilizados para mostrar los créditos.

    public int width = 8;
    public int height = 10;
    // La imagen que contiene los caracteres para mostrar los créditos.
    private BufferedImage imageCredits;
    // La imagen construida que representa el puntaje actual del jugador.
    private BufferedImage imageScore;

    /**
     * Constructor Carga las imágenes necesarias y actualiza el puntaje a cero.
     *
     */
    public Credits() {
        loadImages();
        updateScore(0);
    }

    //Metodos
    /**
     * Carga las imágenes necesarias para mostrar los créditos.
     */
    private void loadImages() {
        try {
// Se carga la imagen que contiene los caracteres utilizados para mostrar los créditos.
            imageCredits = ImageIO.read(new File("src/donkey_kong/recursos/Menu/Text/Text (White) (8x10).png"));

        } catch (Exception e) {
            // En caso de error, se muestra un mensaje en la consola.
            System.err.println("No se pudieron cargar imagenes de Box");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Retorna la imagen que representa el número pasado como parámetro.
     *
     * @param nro, El número a representar.
     * @return La imagen que representa el número.
     */
    public BufferedImage getCreditNumber(int nro) {
        return imageCredits.getSubimage(nro * width, 30, width, height);
    }

    /**
     *
     * Retorna la imagen que representa la suma de uno a la puntuación actual.
     *
     * @return La imagen que representa la suma de uno a la puntuación actual.
     */
    public BufferedImage getPlusOne() {
        return imageCredits.getSubimage(7 * width, 40, width, height);
    }

    /**
     *
     * Actualiza el puntaje y construye la imagen que representa el puntaje
     * actual del jugador.
     *
     * @param score El puntaje actual del jugador.
     */
    public void updateScore(int score) {
        String strCredits = "" + score;// convertimos el numero en una cadenas de caracteres
        char[] listN = strCredits.toCharArray();//Array de caracteres de imagenes.

        //Creamos una nueva imagen, largo * ancho de cada caracter, + tipo de imagen en memoria.
        imageScore = new BufferedImage(listN.length * width, height, imageCredits.getType());
        //Optenemos el elemento g, grafico
        Graphics g = imageScore.createGraphics();

        //Iteramos sobre el arreglo, vector de imagenes
        for (int i = 0; i < listN.length; i++) {
            int n = ((int) listN[i]) - 48; //se le restan 48, para lograr llevar de char a entero.Pasando de valor de caracter a numero entero
            g.drawImage(getCreditNumber(n), i * width, 0, null);
        }
    }
    
    /**
     * 
     * Metodo para dibujar el titulo de los puntos.
     * 
     * @return BufferedImage
     */
    public BufferedImage drawTitle() {
        BufferedImage titleImage = new BufferedImage(50, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = titleImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Puntos:", 0, 9);
        g2d.dispose();
        return titleImage;
    }
    /**
     * Sobrecarga del metodo dibujar titulo, 
     * en caso se le termine los puntos.
     * 
     * @param noCredit El puntaje actual del jugador.
     * @return BufferedImage
     */
    public BufferedImage drawTitle(boolean noCredit) {
        BufferedImage titleImage = new BufferedImage(80, 10, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = titleImage.createGraphics();
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("¡Sin crédito!", 0,9);
        // Agregar efecto de salto
       
        g2d.dispose();
        return titleImage;
    }

    /**
     *
     * Retorna la imagen que representa el puntaje actual del jugador.
     *
     * @return La imagen que representa el puntaje actual del jugador.
     */
    public BufferedImage getScore() {
        return imageScore;
    }

    /**
     *
     * Retorna el ancho de los caracteres utilizados para mostrar los créditos.
     *
     * @return El ancho de los caracteres utilizados para mostrar los créditos.
     */
    public int getWidth() {
        return width;
    }

}
