package donkey_kong.controlador;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/*
La clase Fruit es una implementación concreta de la interfaz Element.
La interfaz Element define un conjunto de métodos que deben ser implementados
por cualquier clase que represente un elemento del juego, 
y la clase Fruit implementa estos métodos para representar una fruta en el juego.
 */
public class Fruit implements Element {

    //Variables
    // Variables que indican los diferentes tipos de frutas que pueden aparecer
    public static int APPLE = 0;
    public static int BANANAS = 1;
    public static int CHERRYS = 2;
    public static int KIWI = 3;
    public static int MELON = 4;
    public static int ORANGE = 5;
    public static int PINEAPPLE = 6;
    public static int STRAWBERRY = 7;

    private Point position; // Posición de la fruta en el juego
    private int width = 32; // Ancho de la imagen de la fruta
    private int height = 32; // Alto de la imagen de la fruta

    private int fruitNumber; // Tipo de fruta

    private boolean visible = true; // Indica si la fruta es visible en el juego
    private boolean collected = false; // Indica si la fruta ha sido recolectada por el personaje

    private int creditValue = 1; // Valor de crédito de la fruta

    private BufferedImage imageFruit; // Imagen de la fruta
    private BufferedImage imageCollected; // Imagen de la fruta recolectada

    private int imageNumber = 0; // Número de imagen a mostrar

    /**
     * Constructor de la clase Fruit. Recibe el tipo de fruta y la posición
     * inicial de la misma.
     *
     * @param type Tipo de fruta
     *
     * @param initialPoint Posición inicial de la fruta
     */
    public Fruit(int type, Point initialPoint) {

        fruitNumber = type;
        position = initialPoint;

        this.creditValue = type + 1;

        loadImage();
    }

    /**
     *
     * Método que retorna el valor de crédito de la fruta.
     *
     * @return El valor de crédito de la fruta
     */
    public int getCreditValue() {
        return creditValue;
    }

    /**
     *
     * Método que indica si la fruta es visible en el juego.
     *
     * @return true si la fruta es visible, false si no lo es
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * Método que carga las imágenes de la fruta y de la fruta recolectada desde
     * los archivos de imagen.
     */
    public void loadImage() {
        String[] imagesPath = {
            "src/donkey_kong/recursos/Items/Fruits/Apple.png",
            "src/donkey_kong/recursos/Items/Fruits/Bananas.png",
            "src/donkey_kong/recursos/Items/Fruits/Cherries.png",
            "src/donkey_kong/recursos/Items/Fruits/Kiwi.png",
            "src/donkey_kong/recursos/Items/Fruits/Melon.png",
            "src/donkey_kong/recursos/Items/Fruits/Orange.png",
            "src/donkey_kong/recursos/Items/Fruits/Pineapple.png",
            "src/donkey_kong/recursos/Items/Fruits/Strawberry.png"

        };

        try {

            imageFruit = ImageIO.read(new File(imagesPath[this.fruitNumber]));
            imageCollected = ImageIO.read(new File("src/donkey_kong/recursos/Items/Fruits/Collected.png"));

        } catch (Exception e) {
            System.err.println("No se pudieron cargar imagenes de Fruit");
            System.err.println(e.getMessage());
            System.exit(0);
        }

    }

    /**
     *
     * Actualiza el estado de la fruta en el juego. Si la fruta ya fue recogida,
     * se actualiza su imagen a una imagen de fruta recolectada. Si la fruta no
     * ha sido recolectada, se actualiza su imagen a una imagen de fruta normal.
     * La fruta se mueve en el eje x hacia la izquierda, a menos que el
     * parámetro "move" sea falso. En ese caso, la fruta permanece en su
     * posición actual.
     *
     * @param move indica si la fruta se debe mover o no.
     */
    public void updateFruit(boolean move) {

        if (collected) {
            if (imageNumber < (imageCollected.getWidth() / width) - 1) {
                imageNumber++;
            } else {
                visible = false;
            }
        } else {
            if (imageNumber < (imageFruit.getWidth() / width) - 1) {
                imageNumber++;
            } else {
                imageNumber = 0;
            }
        }

        //Si se mueve el personaje, no se mueve la fruta
        if (move) {
            position.x--;
            //Cuando al actualizar 
            visible = (position.x > 0) && visible;
        }

    }

    /**
     *
     * Obtiene la imagen actual de la fruta, dependiendo si ha sido recolectada
     * o no
     *
     * @return La imagen de la fruta actual
     */
    public BufferedImage getImage() {
        int x = imageNumber * width;
        if (collected) {
            return imageCollected.getSubimage(x, 0, width, height);
        } else {
            return imageFruit.getSubimage(x, 0, width, height);
        }
    }

    /**
     * Devuelve la coordenada x de la fruta en el juego.
     *
     * @return la coordenada x de la fruta.
     */
    @Override
    public int getX() {
        return position.x;
    }

    /**
     * Devuelve la coordenada y de la fruta en el juego.
     *
     * @return la coordenada y de la fruta.
     */
    @Override
    public int getY() {
        return position.y;
    }

    /**
     * Devuelve el ancho de la fruta.
     *
     * @return el ancho de la fruta.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Devuelve la altura de la fruta.
     *
     * @return la altura de la fruta.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Establece si la fruta ha sido recolectada o no.
     *
     * @param collected true si la fruta ha sido recolectada, false si no.
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
        this.imageNumber = 0;
    }

    /**
     * Devuelve si la fruta ha sido recolectada o no.
     *
     * @return true si la fruta ha sido recolectada, false si no.
     */
    public boolean isCollected() {
        return collected;
    }

    public void setPosition(Point point) {
       this.position.x = point.x;
       this.position.y = point.y;
    }

}
