package donkey_kong.controlador;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * La clase Box es una implementación concreta de la interfaz Element,
 * implementa la interfaz Element y representa una caja en el juego. Hay tres
 * tipos de cajas con diferentes imágenes y propiedades.
 */
public class Box implements Element {

    //Variables
    //Variables para los tipos de caja
    public static int BOX1 = 0;
    public static int BOX2 = 1;
    public static int BOX3 = 2;

    private int height = 24;//Altura de la caja
    private int width = 28;//Ancho de la caja

    private int type;//Tipo de caja
    private Point position;//Posicion de la caja

    private boolean isBreak = false;//Si la caja esta rota
    private boolean visible = true;//Si la caja es visible

    private BufferedImage imageBoxIdle;//Imagen de la caja esta en reposo
    private BufferedImage imageBoxBreak;//Imagen de la caja se rompe

    private int numberImageBreak = 0; //El numero de imagen de la animacion de la caja rota

    /**
     * Constructor Crea una nueva instancia de la clase Box con el tipo y la
     * posición especificados.
     *
     * @param type el tipo de la caja.
     *
     * @param initialPosition la posición inicial de la caja.
     */
    public Box(int type, Point initialPosition) {
        this.type = type;
        this.position = initialPosition;

        loadImages();
    }

    //Metodos
    /**
     * Carga las imágenes para la caja
     */
    private void loadImages() {
        String[] imagesPath = {
            "src/donkey_kong/recursos/Items/Boxes/Box1/",
            "src/donkey_kong/recursos/Items/Boxes/Box2/",
            "src/donkey_kong/recursos/Items/Boxes/Box3/"
        };

        String pathIdle = imagesPath[this.type] + "Idle.png";
        String pathBreak = imagesPath[this.type] + "Break.png";

        try {

            imageBoxIdle = ImageIO.read(new File(pathIdle));
            imageBoxBreak = ImageIO.read(new File(pathBreak));

        } catch (Exception e) {
            System.err.println("No se pudieron cargar imagenes de Box");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Actualiza la caja
     *
     * @param move , indica si se está moviendo la caja o no
     */
    public void updateBox(boolean move) {
        if (isBreak) {
            if (numberImageBreak < (imageBoxBreak.getWidth() / width) - 1) {
                numberImageBreak++;
            } else {
                visible = false;
            }
        }

        if (move) {
            position.x--;
            visible = (position.x > 0) & visible;
        }
    }

    /**
     * Crear una imagen de la caja
     *
     * @return , La imagen correspondiente a la caja
     */
    public BufferedImage getImage() {
        if (isBreak) {
            int x = numberImageBreak * width;
            return imageBoxBreak.getSubimage(x, 0, width, height);
        } else {
            return imageBoxIdle;
        }
    }

    /**
     * Devuelve la coordenada x de la caja en el juego.
     *
     * @return la coordenada x de la caja.
     */
    @Override
    public int getX() {
        return position.x;
    }

    /**
     * Devuelve la coordenada y de la caja en el juego.
     *
     * @return la coordenada y de la caja.
     */
    @Override
    public int getY() {
        return position.y;
    }
    
    public void setY(int y) {
       this.position.y = y;
    }

    /**
     * Devuelve el ancho de la caja.
     *
     * @return el ancho de la caja.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Devuelve la altura de la caja.
     *
     * @return la altura de la caja.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Devuelve si la caja esta rota
     *
     * @return , true - false
     */
    public boolean isBreak() {
        return isBreak;
    }

    /**
     * Marca la caja como rota
     */
    public void setBreak() {
        isBreak = true;
    }

    /**
     * Retorna si la caja es visible
     *
     * @return , true - false
     */
    public boolean isVisible() {
        return visible;
    }

}
