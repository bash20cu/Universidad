package donkey_kong.controlador;

import java.awt.image.BufferedImage;

/**
 * La interfaz Element define una serie de métodos que deben ser implementados
 * por cualquier clase que represente un elemento en una pantalla de un juego o
 * aplicación gráfica. Los métodos getX() y getY() devuelven las coordenadas x e
 * y del elemento respectivamente, getWidth() y getHeight() devuelven el ancho y
 * alto del elemento. Además, la interfaz define el método getImage() que
 * devuelve la imagen asociada al elemento en forma de BufferedImage.
 */
public interface Element {
    public int getX();
    public int getY();
    public int getWidth();
    public int getHeight();

    public BufferedImage getImage();
}
