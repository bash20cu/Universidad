package donkey_kong.controlador;

import donkey_kong.vista.frmPrincipal;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * Clase Weapon es una implementación concreta de la interfaz Element,
 * representa el objeto de arma que se lanza por parte del jugador en el juego.
 * Implementa la interfaz Element, que define los métodos comunes para los
 * elementos gráficos del juego.
 */
public class Weapon implements Element {

    //Variables 
    private int width = 19; // Ancho de la imagen de la arma.
    private int height = 19; // Alto de la imagen de la arma.

    private Point position; // Posición de la arma en la pantalla.

    private boolean visible = true; // Indica si la arma es visible o no.
    private BufferedImage imageWeapon; // Imagen de la arma.

    private int imageNumber = 0; //El numero de imagen de la animacion del arma

    /**
     * Constructor inicializa la posición del arma y carga las imágenes
     * asociadas.
     *
     * @param initialPosition , Objeto Point que representa la posición inicial
     * del arma.
     */
    public Weapon(Point initialPosition) {
        this.position = initialPosition;

        loadImages();
    }

//Metodos
    //Metodos Heredados
    /**
     * Devuelve la imagen actual del arma.
     *
     * @return , Un objeto BufferedImage que representa la imagen actual del
     * arma.
     */
    public BufferedImage getImage() {
        int x = imageNumber * width;
        return imageWeapon.getSubimage(x, 0, this.width, this.height);
    }

    /**
     * Devuelve la coordenada x del arma en el juego.
     *
     * @return la coordenada x del arma.
     */
    @Override
    public int getX() {
        return position.x;
    }

    /**
     * Devuelve la coordenada y del arma en el juego.
     *
     * @return la coordenada y del arma.
     */
    @Override
    public int getY() {
        return position.y;
    }

    /**
     * Devuelve el ancho del arma.
     *
     * @return el ancho del arma.
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * Devuelve la altura del arma.
     *
     * @return la altura del arma.
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * Devuelve un booleano que indica si el objeto arma es visible en el juego.
     *
     * @return true si el objeto es visible, false de lo contrario.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Cambia la visibilidad del objeto arma en el juego.
     *
     * @param isVisible Un booleano que indica si el objeto debe ser visible o
     * no.
     */
    public void setVisible(boolean isVisible) {
        this.visible = isVisible;
    }

    /**
     * Método privado que carga las imágenes del arma desde los archivos. Lanza
     * una excepción en caso de no poder cargar alguna de las imágenes.
     */
    private void loadImages() {
        try {

            imageWeapon = ImageIO.read(new File("src/donkey_kong/recursos/Traps/Saw/On (19x19).png"));

        } catch (Exception e) {
            System.err.println("No se pudieron cargar imagenes de Box");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Actualiza el estado del arma.
     *
     * @param move , Indica si el arma debe moverse o no.
     */
    public void updateWeapon(boolean move) {
        if (imageNumber < (imageWeapon.getWidth() / width) - 1) {
            imageNumber++;
        } else {
            imageNumber = 0;
        }

        position.x = position.x + 3;
        visible = (position.x < frmPrincipal.FRAME_WIDTH) & visible;
    }

}
