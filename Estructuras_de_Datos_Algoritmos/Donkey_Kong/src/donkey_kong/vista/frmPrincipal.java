package donkey_kong.vista;

import donkey_kong.controlador.Box;
import donkey_kong.controlador.Element;
import donkey_kong.controlador.Fruit;
import donkey_kong.controlador.ListaArreglo;
import donkey_kong.modelo.Lives;
import donkey_kong.controlador.Weapon;
import donkey_kong.modelo.Credits;
import donkey_kong.modelo.Jugador;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import javax.swing.JFrame;
import javax.swing.Timer;

public class frmPrincipal extends JFrame implements ActionListener {

    //Variables
    public static int FRAME_WIDTH = 640;//Alto de la Ventana ( Frame )
    public static int FRAME_HEIGTH = 480;//Ancho de la ventana ( Frame )
    public final int DELAY = 25; //40 milisegundos por para llegar a los 25fps

    private Image dbImage; //Almacena la imagen en el 2do buffer.
    private Graphics dbg; //Almacena el contexto grafico del 2do buffer.
    BufferedImage titleImage;

    private Fondo fondo; //Instancia de la clase Fondo.
    private Jugador jugador;//Instancia el Jugador.
    private Timer timer; //Instancia de la clase swing.Timer, Utilizada para lograr la transicion
    private boolean pause = false; // Tecla para Pausa.
   
    private ListaArreglo<Fruit> listFruit; //Lista de frutas

   
    private ListaArreglo<Box> listBox; //Lista de cajas

   
    private ListaArreglo<Weapon> listWeapon; //Lista de armas

    private Credits showCredits; //credito del jugador
    private boolean winCredits = false; // 
    private int numberWinCredits = 0; //
    private int credits = 0;
    private int showCreditNumber = 0;
    private Lives showlives;
    private int lives;
    boolean gameOver = false;//Termina el Juego

    /**
     * Constructor
     */
    public frmPrincipal() {
        super();
        setTitle("Donkey Kong");//Ponemos el titulo de la ventana.
        this.setSize(FRAME_WIDTH, FRAME_HEIGTH);//Obtenemos el tamanno de la centana.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Indicamos que salir al cerrar la ventana.
        this.setResizable(false);

        //Sabemos el tamanno de la ventana.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        //Mantenemos una resolucion manejable , con resultado del sistema.
        this.setLocation(dim.width / 2 - this.getSize().width, dim.height / 2 - this.getSize().height / 2);

        //Evento para cerrar el Frame con la tecla Escape
        addKeyListener(new GameKeys());

        //Iniciamos el fondo del juego
        fondo = new Fondo("src/donkey_kong/recursos/bgd1.png");

        //Iniciamos al Jugador
        jugador = new Jugador(Jugador.MASK_DUDE, new Point(50, 380));

        //Iniciamos las cajas
        //Inicializamos la lista
        listBox = new ListaArreglo<Box>();
        generarCajas(3);

        //Iniciamos las frutas
        listFruit = new ListaArreglo<Fruit>();
        generarFrutas();

        /**
         * Armas
         */
        listWeapon = new ListaArreglo<Weapon>();

        /**
         * Credito del jugador
         */
        showCredits = new Credits();

        /**
         * Vidas del jugador
         */
        showlives = new Lives();

        //Timer para la transicion del fondo 
        timer = new Timer(DELAY, this);
        timer.start();

        setVisible(true);
    }

    //Metodos de la clase
    /**
     * Dibuja una lista de elementos en un objeto Graphics.
     *@param <T> tipo de objeto que se almacenará en la lista
     * @param itemList la lista de elementos a dibujar
     * @param g el objeto Graphics utilizado para dibujar los elementos
     *
     */
    private <T> void drawList(ListaArreglo<T> itemList, Graphics g) {

        if (!itemList.estaVacia()) {
            Iterator it = itemList.iterator();
            while (it.hasNext()) {
                Element elem = (Element) it.next();
                g.drawImage(elem.getImage(), elem.getX(), elem.getY(), null);
            }
        }
    }


    /**
     * Metodo para pintar el fondo en el frame
     *
     * @param g , un parametro grafico
     */
    public void paint(Graphics g) {

        //Implementamos el double buffer
        if (dbImage == null) {
            dbImage = createImage(this.getSize().width, this.getSize().height);
            dbg = dbImage.getGraphics();
        }

        dbg.setColor(getBackground());
        dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

        dbg.setColor(getForeground());
        paintDoubleBuffered(dbg);

        g.drawImage(dbImage, 0, 0, this);

    }

    /**
     * Metodo que genera en memoria la imagen, antes de pintarla evitando el
     * efecto flickering o parpadeo.
     *
     * @param g , Imagen a dibujar
     */
    private void paintDoubleBuffered(Graphics g) {
        // Aquí es donde debes dibujar todo lo que quieres que se vea en el frame

        //Dibujamos al fondo
        g.drawImage(fondo.getImageFondo(), 0, 0, null);

        //Dibujamos las vidas
        g.drawImage(showlives.drawLives(), 3, 50, null);
        //g.drawImage(showlives.getLives(), 40, 50, null);

        //Dibujamos los creditos:
        if (credits == 0) {
            g.drawImage(showCredits.drawTitle(true), 520, 50, null);
        } else {
            g.drawImage(showCredits.drawTitle(), 540, 50, null);
        }
        g.drawImage(showCredits.getScore(), 600, 50, null);

        //Dibujamos las frutas
        drawList(listFruit, g);

        //Dibujamos las cajas
        drawList(listBox, g);

        //Dibujamos el arma
        drawList(listWeapon, g);

        //Dibujamos al Jugador
        g.drawImage(jugador.getImage(), jugador.getX(), jugador.getY(), null);

        //Ganador de puntos
        if (winCredits) {

            if (showCreditNumber < 3) {

                g.drawImage(showCredits.getPlusOne(), jugador.getX() + jugador.getWidth(), jugador.getY() - 3, null);
                g.drawImage(showCredits.getCreditNumber(numberWinCredits), jugador.getX() + jugador.getWidth() + showCredits.getWidth(), jugador.getY() - 3, null);

                showCreditNumber++;
            } else {
                winCredits = false;
                showCreditNumber = 0;
            }

        }
        if (gameOver) {
            g.drawImage(endGame(), 10, 100, null);
        }

        if ((jugador.getState() == 0) && (!gameOver)) {
            g.drawImage(drawKeysExplanation(), 10, 100, null);
        }
    }

    /**
     * Metodo que comprueba si hay colisión entre el jugador y una caja. Para
     * ello, compara las posiciones y dimensiones del jugador y de la caja. Si
     * hay una intersección en ambos ejes, entonces se considera que hay
     * colisión
     *
     * @param elemA, Objeto elemento
     * @param elemB, Objeto elemento, caja, Fruta, etc
     * @return , boolean
     */
    private boolean isBoxCollision(Element elemA, Element elemB) {
        // Comprobamos si hay colisión vertical entre el elemA y la caja
        if (elemA.getY() + elemA.getHeight() <= elemB.getY()) {
            return false;
        }
        if (elemA.getY() >= elemB.getY() + elemB.getHeight()) {
            return false;
        }
        // Comprobamos si hay colisión horizontal entre el elemA y la caja
        if (elemA.getX() + elemA.getWidth() <= elemB.getX()) {
            return false;
        }
        if (elemA.getX() >= elemB.getX() + elemB.getWidth()) {
            return false;
        }
        // Si no hay colisión vertical ni horizontal, entonces hay una colisión
        return true;
    }

    /**
     * Metodo para actualizar el Fondo , pausar el juego
     */
    private void actualizarFondo() {
        if (pause) {
            return;
        }

        if (jugador.getState() != Jugador.STATE_IDLE) {
            fondo.actualizarFondo();
        }
    }

    /**
     * Metodo para actualizar las frutas, dentro del juego
     */
    private void actualizarFrutas() {
        if (pause) {
            return;
        }

        if (jugador.getState() != Jugador.STATE_IDLE) {
            for (Fruit fruit : listFruit) {
                fruit.updateFruit(true);
            }
        }

        //Iterar sobre la lista de frutas con un for-each loop
        for (Fruit fruit : listFruit) {
            if (isBoxCollision(jugador, fruit)) {
                if (!fruit.isCollected()) {
                    winCredits = true;
                    numberWinCredits = fruit.getCreditValue();
                    credits += fruit.getCreditValue();
                    showCredits.updateScore(credits);
                    fruit.setCollected(true);
                }
            }

            fruit.updateFruit(false);

            if (!fruit.isVisible()) {
                listFruit.eliminar(fruit);
            }
        }

        if (listFruit.estaVacia()) {
            generarFrutas();
        }
    }

    /**
     * Este metodo genera 5 frutas aleatorias, cuando se acaben las del juego
     * Verifica, que las frutas no esten dentro de las cajas, ni despues del
     * pesonaje, ni muy juntas
     */
    private void generarFrutas() {
        for (int i = 0; i < 5; i++) {
            // Generar posición aleatoria en el eje X
            int posX = (int) (Math.random() * (FRAME_WIDTH - 50) + jugador.getX());

            int posY = 390;

            // Generar un tipo de fruta aleatorio
            int tipoFruta = (int) (Math.random() * 3);

            // Crear nueva fruta y agregarla a la lista
            Fruit nuevaFruta;
            switch (tipoFruta) {
                case 0:
                    nuevaFruta = new Fruit(Fruit.APPLE, new Point(posX, posY));
                    break;
                case 1:
                    nuevaFruta = new Fruit(Fruit.BANANAS, new Point(posX, posY));
                    break;
                case 2:
                    nuevaFruta = new Fruit(Fruit.STRAWBERRY, new Point(posX, posY));
                    break;
                default:
                    nuevaFruta = new Fruit(Fruit.APPLE, new Point(posX, posY));
                    break;
            }

            // Comprobar si la nueva fruta colisiona con alguna de las frutas existentes y 
            //cambiar su posición si es necesario
            for (Fruit frutaExistente : listFruit) {
                if (isBoxCollision(nuevaFruta, frutaExistente)) {
                    posX += 50;
                    nuevaFruta.setPosition(new Point(nuevaFruta.getX() + 50, nuevaFruta.getY()));
                }

            }
            
              // Comprobar si la nueva fruta colisiona con alguna de las cajas existentes y 
            //cambiar su posición si es necesario
            for (Box cajasExistente : listBox) {
                if (isBoxCollision(nuevaFruta, cajasExistente)) {
                    posX += 50;
                    nuevaFruta.setPosition(new Point(nuevaFruta.getX() + 50, nuevaFruta.getY()));
                }
            }
            
            //Asegurarse que la fruta este delante del jugador
            //cambiar su posición si es necesario.            
            if (isBoxCollision(jugador, nuevaFruta)) {
                posX += 50;
                nuevaFruta.setPosition(new Point(nuevaFruta.getX() + 50, nuevaFruta.getY()));
            }

            listFruit.agregarAlFinal(nuevaFruta);
        }
    }
    
    /**
     * Metodo recursivo para generar x cantidad de cajas en posiciones aleatorias Y verifica que no
     * colisionen entre ellas
     * @param cantidad , cantidad de cajas a crear 
     */
    private void generarCajas(int cantidad){
         if (cantidad == 0) {
        return;
    }

    // Generar posición aleatoria en el eje X
    int randomX = (int) (Math.random() * 350) - 100;
    int elementoX = jugador.getX() + 100 + (randomX + 80);

    int posY = 390;

    // Crear nueva caja y agregarla a la lista
    Box nuevaBox = new Box(Box.BOX1, new Point(elementoX, posY));

    // Verificar si la nueva caja colisiona con alguna de las cajas existentes
    boolean colision = false;
    for (Box cajaExistente : listBox) {
        if (isBoxCollision(nuevaBox, cajaExistente)) {
            colision = true;
            break;
        }
    }

    // Si no hay colisión, agregar la nueva caja a la lista
    if (!colision) {
        listBox.agregarAlFinal(nuevaBox);
    }

    generarCajas(cantidad - 1);
    }

    /**
     * Este método actualiza la lista de cajas en el juego. Primero, comprueba
     * si el juego está en pausa, en cuyo caso, no hace nada. Luego, itera a
     * través de la lista de cajas y comprueba si hay una colisión entre el
     * jugador y la caja. Si hay una colisión y la caja no ha sido recolectada,
     * se suman créditos y se marca la caja como recolectada. Si la caja no es
     * visible, se elimina de la lista. Si la lista está vacía, se generan
     * nuevas cajas aleatorias en la pantalla cerca del jugador.
     */
    private void actualizarCajas() {
        if (pause) {
            return;
        }
        // Si la lista de cajas no está vacía, la recorremos con un ciclo for each
        if (!listBox.estaVacia()) {
            for (Box box : listBox) {
                // Si el jugador no está saltando o cayendo, comprobamos si hay colisión con la caja
                if (jugador.getState() < 2) {
                    if (isBoxCollision(jugador, box)) {
                        // Si el jugador ya no tiene vidas, se termina el juego
                        if (showlives.getLivesCount() == 0) {
                            gameOver = true;
                        }
                        // Se reduce en uno la cantidad de vidas y se actualiza el contador en pantalla
                        lives = showlives.getLivesCount();
                        lives = lives - 1;
                        showlives.setLivesCount(lives);
                        showlives.drawLives();
                        // Se rompe la caja y se elimina de la lista
                        box.setBreak();
                        listBox.eliminar(box);
                        break; // Salimos del ciclo for each ya que ya no es necesario continuar buscando colisiones
                    }
                }

                // Actualizamos la posición de la caja y comprobamos si debe ser eliminada
                box.updateBox(jugador.getState() != Jugador.STATE_IDLE);
                if (!box.isVisible()) {
                    listBox.eliminar(box);
                }
            }
        }
        

// Si la lista de cajas está vacía, se generan nuevas cajas
        if (listBox.estaVacia()) {
            generarCajas(3);
        }

    }

    private void actualizarArmas() {
        if (pause) {
            return;
        }
        for (Weapon weapon : listWeapon) {
            if (!listBox.estaVacia()) {
                for (Box box : listBox) {
                    if (isBoxCollision(weapon, box)) {
                        box.setBreak();
                        weapon.setVisible(false);
                        break;
                    }
                }
            }

            weapon.updateWeapon(jugador.getState() != Jugador.STATE_IDLE);

            if (!weapon.isVisible()) {
                listWeapon.eliminar(weapon);
            }
        }
    }

    /**
     * Método que actualiza el estado del juego. Si el juego está en pausa, no
     * hace nada. Actualiza el fondo si el jugador no está en estado idle.
     * Actualiza las frutas, detecta si hay colisión con el jugador y actualiza
     * su estado. Actualiza las cajas y las armas. Actualiza el estado del
     * jugador.
     *
     */
    private void actualizarJuego() {
        actualizarFondo();

        // Verifica si el jugador se está moviendo.
        //boolean move = (jugador.getState() != Jugador.STATE_IDLE);
        // Actualiza el estado de las frutas.
        actualizarFrutas();

        //Actualizamos las cajas
        actualizarCajas();

        //Actualizar el arma
        actualizarArmas();
        //Jugador
        jugador.updatePlayer();
    }

    /**
     * Implementacion del metodo heredado de la interfaz Action Listener, para
     * escuchar el evento y repintar el fondo
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
        if (!gameOver) {
            actualizarJuego();
        }
    }

    /**
     * Metodo fin del juego
     */
    private BufferedImage endGame() {
        //Dibujar , juego terminando
        BufferedImage endGame = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = endGame.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 25));
        g2d.drawString("Game Over, press enter to continue", 20, 100);
        g2d.dispose();
        return endGame;
    }

    /**
     * Metodo para advertir al jugador de las teclas que debe usar para jugar.
     *
     * @param g2d
     */
    private BufferedImage drawKeysExplanation() {

        //Dibujar , la explicacion de las teclas a usar
        BufferedImage KeysExplanation = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = KeysExplanation.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));
        // Dibujar explicaciones de las teclas
        g2d.drawString("Teclas de Juego:", 20, 20);
        g2d.drawString("Mover a la Derecha: Tecla Derecha o Tecla D", 20, 40);
        g2d.drawString("Saltar: Tecla Arriba o Tecla W", 20, 60);
        g2d.drawString("Disparar: Barra Espaciadora", 20, 80);
        g2d.drawString("Pausar el juego: Tecla P", 20, 100);
        g2d.drawString("Cerrar el juego: Tecla Esc", 20, 120);
        g2d.drawString("Se comienza con 3 vidas y 0 creditos", 20, 140);
        g2d.drawString("Para disparar se necesita creditos", 20, 160);
        g2d.dispose();
        return KeysExplanation;
    }

    //Eventos
    //Clase para Heredar los Eventos del teclado
    /**
     * La clase GameKeys es una subclase que extiende la clase KeyAdapter y
     * define los métodos para manejar eventos de teclado. En el método
     * keyPressed, se detectan las teclas presionadas por el usuario y se toman
     * las acciones correspondientes para cada tecla, como cerrar el juego,
     * pausarlo, mover al jugador a la derecha, saltar y disparar un arma si hay
     * créditos disponibles. En el método keyReleased, se detecta cuándo se
     * suelta una tecla y se detiene el movimiento del jugador hacia la derecha.
     * También hay un método shootWeapon que crea un objeto Weapon y lo agrega a
     * la lista de armas.
     *
     * la subclase GameKeys es utilizada para manejar la entrada de teclado para
     * el juego, específicamente para controlar el personaje principal y para
     * permitir que el jugador dispare armas. Al tener la subclase dentro de la
     * clase principal DonkeyKong, se asegura que l a lógica del manejo de
     * entrada de teclado está encapsulada dentro de la clase principal y no se
     * utiliza en ningún otro lugar del programa. Además, puede acceder
     * fácilmente a las variables y métodos de la clase principal, lo que
     * simplifica la implementación de la lógica del juego.
     *
     */
    public class GameKeys extends KeyAdapter {

        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                //Cerrar el juego precionando la tecla Escape
                System.exit(0);
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                //Pausar el Juego presionando la tecla P.
                pause = !pause;
            } //Camniar a la Derecha
            else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                jugador.run();
            } //Saltar
            else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
                jugador.jump();
            } //Disparo, controlamos la cantidad de disparos, siempre que tenga credito.
            else if (e.getKeyCode() == KeyEvent.VK_SPACE && credits > 0) {
                credits--;
                showCredits.updateScore(credits);
                shootWeapon();
            } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                gameOver = false;
                showlives.setLivesCount(2);
                credits = 0;
                showCredits.updateScore(credits);
            }

        }

        //Soltando la tecla
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
                jugador.idle();
            }
        }

        private void shootWeapon() {
            Weapon weapon = new Weapon(new Point(jugador.getX(), jugador.getY() + 6));
            listWeapon.agregarAlFinal(weapon);
        }
    }

}
