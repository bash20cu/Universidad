
package donkey_kong.modelo;

import java.awt.Color;
import java.awt.Font;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Lives {
    //public int width = 8;
    //public int height = 10;
    private int lives = 2;//Cantidad de vidas
    private String title = "Vidas";

    private BufferedImage imageLives;
    private BufferedImage imageAuxLives;
    
    
    public Lives() {        

        drawLives();
    }

    /**
     * 
     * Este Metodo crea una instancia de BufferedImage con un ancho de 100
     * y un alto de 14 píxeles y tipo TYPE_INT_ARGB. Luego se crea un objeto Graphics2D
     * a partir de esta instancia y se configura la fuente y el color de dibujo. 
     * Después se dibuja la cadena "Vidas:" en la imagen en la posición (0,9).
     * Finalmente, se llama al método dispose() del objeto Graphics2D y se retorna 
     * la instancia de BufferedImage resultante.
     * 
     * @return 
     */
    public BufferedImage drawLives() {
        BufferedImage titleImage = new BufferedImage(100, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = titleImage.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        g2d.drawString("Vidas: "+String.valueOf(lives), 0, 9);
        g2d.dispose();
        return titleImage;
    }

    public BufferedImage getLives() {
        return imageAuxLives;
    }


    public int getLivesCount() {
        return lives;
    }
    
    public void setLivesCount(int lives){
        if(lives < 0)
            this.lives = 0;
        else
            this.lives = lives;
    }

    public void loseLife() {
        drawLives();
    }
}
