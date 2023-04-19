package donkey_kong.modelo;

import donkey_kong.controlador.Element;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/*
Jugador
(Tipo Abstracto de Datos)
Se definen las variables y métodos necesarios para representar al personaje del jugador en el juego,
como su posición, tipo de jugador, estado actual (corriendo, saltando, cayendo, inactivo, etc.),
tamaño, y las imágenes correspondientes a cada estado del jugador.

La clase también proporciona métodos para actualizar y obtener la imagen del jugador,
así como para cambiar su estado (correr, saltar, caer, etc.) y devolver su estado actual.
*/
public class Jugador implements Element{

    //Variables
    
    //JUGADORES: constante que indica el tipo de personaje
    public static int MASK_DUDE = 0;
    public static int NINJA_FROG = 0;
    public static int PINK_MAN = 0;
    public static int VIRTUAL_GUY = 0;

     //estados del Personaje: constante que indica el estado del personaje
    public static int STATE_IDLE = 0;
    public static int STATE_RUN = 1;
    public static int STATE_JUMP = 2;
    public static int STATE_FALL = 3;
    
    // Estado anterior del personaje
    private int previousState = 0;
    // Estado en la posición Y en el momento.
    private int baseline; 
    
    //posición del jugador
    private Point position; 
    private int type; // tipo de jugador
    private int state = STATE_IDLE; // estado inicial del personaje;

    //tamano del jugador
    private int width = 32;
    private int height = 32;

    private BufferedImage imageIdle; // imagen cuando el personaje está en reposo
    private BufferedImage imageRun; // imagen cuando el personaje está corriendo
    private BufferedImage imageJump; // imagen cuando el personaje está saltando
    private BufferedImage imageFall; // imagen cuando el personaje está cayendo
    private BufferedImage imageHit; // imagen cuando el personaje es golpeado
    
    private int imageReference = 0; // referencia a la imagen actual
    private int jumpDistance = height*2; // distancia del salto
    private boolean tieneArma = false;

    //Constructor
    /**
     * Constructor de la clase Jugador.
     *
     * @param type el tipo de jugador
     * @param initialPoint la posición inicial del jugador
     */
    public Jugador(int type, Point initialPoint) {
        this.type = type;
        this.position = initialPoint;
        cargarImagen();

    }

      //Metodos
    
    //Metodo para cargar las imagenes
    /**
     * Carga las imágenes correspondientes a cada estado del personaje.
     */
    public void cargarImagen() {
        String[] pathImages = {
            "src/donkey_kong/recursos/characters/Mask Dude/",
            "src/donkey_kong/recursos/characters/Ninja Frog/",
            "src/donkey_kong/recursos/characters/Pink Man/",
            "src/donkey_kong/recursos/characters/Virtual Guy/"
        };

        String pathIdle = pathImages[type] + "Idle.png";
        String pathRun = pathImages[type] + "Run.png";
        String pathJump = pathImages[type] + "Jump (32x32).png";
        String pathFall = pathImages[type] + "Fall (32x32).png";
        String pathHit = pathImages[type] + "Hit (32x32).png";

        try {
            //Se cargan las imagenes correspondientes al jugador
            imageIdle = ImageIO.read(new File(pathIdle));
            imageRun = ImageIO.read(new File(pathRun));
            imageJump = ImageIO.read(new File(pathJump));
            imageFall = ImageIO.read(new File(pathFall));
            imageHit = ImageIO.read(new File(pathHit));

        } catch (Exception e) {
            //Si no se pudieron cargar las imagenes se muestra el error y se sale del programa
            System.err.println("No se pudieron cargar imagenes");
            System.err.println(e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Actualiza el estado del jugador en función del estado actual.
     */    
    public void updatePlayer() {
        if (state == STATE_IDLE) {            
            if (imageReference < (imageIdle.getWidth() / width) - 1) {
                imageReference++;
            } else {
                imageReference = 0;
            }
        } else if (state == STATE_JUMP){
            //Se calcula la posición del jugador mientras está saltando
            if(position.y > (baseline-jumpDistance)){
                position.y = position.y - 2;
            }else{
                fall();
            }
        } else if (state == STATE_FALL){
            //Se calcula la posición del jugador mientras está cayendo
            if(position.y < baseline){
                position.y = position.y + 2;
            }else{
                state = previousState;
            }
        
        } else if (state == STATE_RUN) {
            if (imageReference < (imageRun.getWidth() / width) - 1) {
                imageReference++;
            } else {
                imageReference = 0;
            }
        }
    }

    
    
    
            
    //Metodo para establecer el estado "corriendo" del jugador
    public void run() {
        if (state == Jugador.STATE_IDLE) {
            state = STATE_RUN;
        }
    }

    //Metodo para establecer el estado "Inicial" del jugador 
    public void idle() {
        if (state == Jugador.STATE_RUN) {
            state = STATE_IDLE;
        }
    }
    
    //Metodo para establecer el estado "saltar" del jugador
    public void jump() {
        if ((state == Jugador.STATE_RUN)||(state == Jugador.STATE_IDLE) ) {
            previousState = state;
            state = STATE_JUMP;
            baseline = position.y;
        }
    }
    
    //Metodo para establecer el estado "callendo" del jugador
    public void fall(){
        state = Jugador.STATE_FALL;
    }
        
    //Metodo para devolver el estado del personaje
    public int getState(){
        return state;
    }
    //Metodo para saber si tiene arma el jugador
    public boolean tieneArma(){        
        return tieneArma;
    }
    public void seTtieneArma(boolean tieneArma){
        this.tieneArma = tieneArma;
    }

    //Metodo para obtener la posición en X del elemento
    @Override
    public int getX() {
        return position.x;
    }
    //Metodo para obtener la posición en Y del elemento
    @Override
    public int getY() {
        return position.y;
    }
    //Metodo para obtener la posición el ancho del elemento
    @Override
    public int getWidth() {
        return this.width;
    }
    //Metodo para obtener el alto del elemento
    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    //Metodo para obtener la imagen del elemento
    public BufferedImage getImage() {
        int x = imageReference * width;

        if(state == STATE_RUN) {
            return imageRun.getSubimage(x,0,width,height);
        } else if(state == STATE_JUMP) {   
            return imageJump;
        } else if(state == STATE_FALL) {   
            return imageFall;            
        } else {
            return imageIdle.getSubimage(x,0,width,height);
        }

    }

}
