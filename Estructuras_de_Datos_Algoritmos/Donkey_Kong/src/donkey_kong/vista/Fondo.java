package donkey_kong.vista;

import donkey_kong.vista.frmPrincipal;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


/*
Fondo es una clase auxiliar utilizada en la implementación de la clase principal.

Es utlizzada para representar un fondo en el juego.
Esta clase es responsable de cargar una imagen de fondo desde un archivo,
actualizar la posición del fondo en la pantalla y proporcionar una sección
de la imagen del fondo que se ajusta a las dimensiones del marco en el que se está mostrando.

Cuenta con varias variables, incluyendo un punto que representa 
la posición actual del fondo en la pantalla, varias variables BufferedImage que 
almacenan las imágenes de fondo, y una variable booleana que indica si el fondo 
está compuesto por una o dos imágenes. 

Además, tiene varios métodos para cargar imágenes, 
actualizar la posición del fondo y devolver una sección de la imagen del fondo 
que se ajusta a las dimensiones del marco.

Fondo es una implementación de un TAD (Tipo Abstracto de Datos) 
que representa un fondo gráfico en una aplicación de Java y proporciona una interfaz para
manipular y acceder a las propiedades del fondo.
 */
public class Fondo {

    private Point posicion; //Variable para saber el punto exacto de la imagen.
    private BufferedImage fondo; //Variable para cargar la imagen al buffer.
    private BufferedImage fondo1; //Variable para cargar la imagen al buffer.
    private BufferedImage fondo2; //Variable para cargar la imagen al buffer.
    private BufferedImage fondoTotal; //Variable para cargar la imagen al buffer.
    private boolean DosImagenes = false; // Variable para determinar 2 imagenes.

    //Constructor
    /**
     * Carga una imagen de fondo en el buffer.
     *
     * @param imageFile la ruta del archivo de imagen.
     */
    public Fondo(String imageFile) {
        loadImage(imageFile);
        posicion = new Point(0, fondo.getHeight() - frmPrincipal.FRAME_HEIGTH);
    }

    //Metodos
    /**
     * Carga una imagen de fondo en el buffer.
     *
     * @param imageFile la ruta del archivo de imagen.
     */
    private void loadImage(String imageFile) {
        //Al trabajar con archivos fisico, se debe crear una captura de errores.
        try {
            fondo = ImageIO.read(new File(imageFile));
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    /**
     * Método público que actualiza la posición del fondo dentro del frame. Si
     * la imagen es menor que el fondo, mueve la posición en 1 unidad hacia la
     * derecha. Si la posición x supera el ancho de la imagen de fondo, vuelve a
     * comenzar desde 0.
     */
    public void actualizarFondo() {
        //Si la imagen es menor que el fondo.
        if ((posicion.x + frmPrincipal.FRAME_WIDTH) + 1 < fondo.getWidth()) {
            posicion.x++;
            DosImagenes = false;
        } else {
            if (posicion.x < fondo.getWidth()) {
                posicion.x++;
                DosImagenes = true;
            } else if (posicion.x >= fondo.getWidth()) {
                posicion.x = 0;
                DosImagenes = false;
            }
        }
    }

    /**
     * Método público que devuelve la porción de la imagen de fondo que se
     * muestra en el frame actual. Si la imagen es menor que el frame, devuelve
     * la imagen completa. Si la imagen es mayor que el frame, devuelve la unión
     * de dos imágenes para evitar saltos.
     *
     * @return una subimagen de la imagen de fondo que se muestra actualmente en
     * el frame.
     */
    public BufferedImage getImageFondo() {
        if (!DosImagenes) {
            return fondo.getSubimage(posicion.x, posicion.y, frmPrincipal.FRAME_WIDTH, frmPrincipal.FRAME_HEIGTH);
        } else {
            int xMax1 = fondo.getWidth() - posicion.x;
            int xMax2 = (posicion.x + frmPrincipal.FRAME_WIDTH) - fondo.getWidth();
            fondo2 = fondo.getSubimage(0, posicion.y, xMax2, frmPrincipal.FRAME_HEIGTH);

            if (xMax1 > 0) {
                fondo1 = fondo.getSubimage(posicion.x, posicion.y, xMax1, frmPrincipal.FRAME_HEIGTH);
                return joinImage(fondo1, fondo2, xMax1);
            } else {
                return fondo2;
            }
        }
    }

    /**
     * Método privado que une dos imágenes para evitar saltos en el frame.
     *
     * @param image1 la primera imagen a unir.
     * @param image2 la segunda imagen a unir.
     * @param xMin la posición x de la segunda imagen en la imagen total.
     * @return la unión de las dos imágenes.
     */
    private BufferedImage joinImage(BufferedImage image1, BufferedImage image2, int xMin) {
        fondoTotal = new BufferedImage(frmPrincipal.FRAME_WIDTH, frmPrincipal.FRAME_HEIGTH, fondo.getType());

        Graphics g = fondoTotal.getGraphics();

        g.drawImage(image1, 0, 0, null);
        g.drawImage(image2, xMin, 0, null);

        return fondoTotal;
    }

}
