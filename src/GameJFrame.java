import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class GameJFrame extends JFrame implements ActionListener {
    private String nombre;
    private String contrasenia;
    private byte[][] posicionImagen;
    private final Random aleatorio = new Random();
    private final byte[][] posicionCorrecto = {
            {1, 5, 9, 13},
            {2, 6, 10, 14},
            {3, 7, 11, 15},
            {4, 8, 12, 0}
    };
    private byte posicionVacioX;
    private byte posicionVacioY;
    private int pasos;
    private String directorioTipo = "animal";
    private byte imagenId = (byte) (aleatorio.nextInt(getCantidaDeDirectorioEnDirectorio("imagenes/"+directorioTipo))+1);
    private String directorio = directorioTipo+"/"+directorioTipo+imagenId+"/";
    private final JMenu cambiarImagenes = new JMenu();
    private final JMenu funciones = new JMenu();
    private final JMenuItem personajes = new JMenuItem();
    private final JMenuItem animales = new JMenuItem();
    private final JMenuItem deportivos = new JMenuItem();
    private final JMenuItem reiniciar = new JMenuItem();
    private final JMenuItem reiniciarSesion = new JMenuItem();
    private final JMenuItem cerrarJuego = new JMenuItem();
    private final JMenu idiomas = new JMenu();
    private final JMenuItem english = new JMenuItem("English");
    private final JMenuItem espaniol = new JMenuItem("Español");
    private final JMenuItem chino = new JMenuItem("中文");

    public GameJFrame(){
        iniciarJFrame();
        iniciarMenuCompleto();
        iniciarPosiciones();
        iniciarImagenes();
        aniadeKeyLestenerKey();
        setVisible(true);
    }

    private void aniadeKeyLestenerKey() {
        addKeyListener(new KeyAdapter() {
            long tiempoI = 0;
            long tiempoA = 0;
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                byte nuevoPosicion;
                byte limite = (byte)posicionImagen.length;
                if (KeyEvent.VK_SPACE != key) {
                    switch (key) {
                        case KeyEvent.VK_UP:
                        case KeyEvent.VK_W:
                            nuevoPosicion = (byte) (posicionVacioY - 1);
                            if (nuevoPosicion >= 0) {
                                posicionImagen[posicionVacioX][posicionVacioY] = posicionImagen[posicionVacioX][nuevoPosicion];
                                posicionImagen[posicionVacioX][nuevoPosicion] = 0;
                                posicionVacioY--;
                                pasos++;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                        case KeyEvent.VK_D:
                            nuevoPosicion = (byte) (posicionVacioX + 1);
                            if (nuevoPosicion < limite) {
                                posicionImagen[posicionVacioX][posicionVacioY] = posicionImagen[nuevoPosicion][posicionVacioY];
                                posicionImagen[nuevoPosicion][posicionVacioY] = 0;
                                posicionVacioX++;
                                pasos++;
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                        case KeyEvent.VK_S:
                            nuevoPosicion = (byte) (posicionVacioY + 1);
                            if (nuevoPosicion < limite) {
                                posicionImagen[posicionVacioX][posicionVacioY] = posicionImagen[posicionVacioX][nuevoPosicion];
                                posicionImagen[posicionVacioX][nuevoPosicion] = 0;
                                posicionVacioY++;
                                pasos++;
                            }
                            break;
                        case KeyEvent.VK_A:
                        case KeyEvent.VK_LEFT:
                            nuevoPosicion = (byte) (posicionVacioX - 1);
                            if (nuevoPosicion >= 0) {
                                posicionImagen[posicionVacioX][posicionVacioY] = posicionImagen[nuevoPosicion][posicionVacioY];
                                posicionImagen[nuevoPosicion][posicionVacioY] = 0;
                                posicionVacioX--;
                                pasos++;
                            }
                            break;
                    }
                    iniciarImagenes();
                }else {
                    mostarImagenCompleta();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_SPACE){
                    iniciarImagenes();
                } else if (key == KeyEvent.VK_A) {
                    tiempoA = System.currentTimeMillis();
                } else if (key == KeyEvent.VK_I) {
                    tiempoI = System.currentTimeMillis();
                }
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_I) {
                    if (tiempoA != 0 && tiempoI != 0) {
                        if (Math.abs(tiempoI - tiempoA) < 8) {
                            cobiarArrayDeepCopy(posicionCorrecto, posicionImagen);
                            /*
                             La copia profunda se utiliza para evitar que dos o más
                             variables hagan referencia al mismo objeto en la memoria
                            */
                            posicionVacioX = (byte) (posicionCorrecto.length - 1);
                            posicionVacioY = (byte) (posicionCorrecto.length - 1);
                            iniciarImagenes();
                        }
                    }
                }
            }
            private void cobiarArrayDeepCopy(byte[][] arrayToCopy, byte[][] targetArray) {
                for (byte i = 0; i < arrayToCopy.length; i++){
                    for (byte j = 0; j<arrayToCopy[i].length; j++) {
                        targetArray[i][j] = arrayToCopy[i][j];
                    }
                }
            }
        });
    }
    private boolean victoria() {
        for (byte i = 0; i<posicionImagen.length; i++){
            for (byte j = 0; j<posicionImagen[i].length; j++){
                if (posicionImagen[i][j] != posicionCorrecto[i][j]){
                    return false;
                }
            }
        }
        return true;
    }
    private void mostarImagenCompleta() {
        getContentPane().removeAll();

        JLabel imagen = new JLabel(new ImageIcon("imagenes/"+directorio+"all.jpg"));
        imagen.setBounds(91, 126, 420, 420);
        getContentPane().add(imagen);

        JLabel background = new JLabel(new ImageIcon("imagenes/background.png"));
        background.setBounds(40, 40, 508, 560);
        getContentPane().add(background);

        getContentPane().repaint();
    }
    private byte[][] matrizBidimensional(byte[] array){
        byte tamanio = (byte) Math.ceil(Math.sqrt(array.length));
        byte[][] matriz = new byte[tamanio][tamanio];
        for (byte i = 0; i<array.length; i++){
            if (array[i] == 0){
                posicionVacioX = (byte) (i/matriz.length);
                posicionVacioY = (byte)(i%matriz.length);
            }else {
                matriz[i / matriz.length][i % matriz.length] = array[i];
            }
        }
        return matriz;
    }

    private void buscaCeroRestablecerXY() {
        byte x = 0;
        boolean encontrado = false;
        while (!encontrado && x < posicionImagen.length){
            byte y = 0;
            while (!encontrado && y < posicionImagen[x].length){
                if (posicionImagen[x][y] == 0){
                    posicionVacioX = x;
                    posicionVacioY = y;
                    encontrado = true;
                }
                y++;
            }
            x++;
        }
    }
    private void desOrdenarBidimensional(byte[][] array){
        aleatorio.setSeed(System.nanoTime());
        for (int i = array.length-1; i > 0; i--) {
            for (int j = array[i].length-1; j > 0; j--) {
                byte posicionI = (byte) aleatorio.nextInt(i+1);
                byte posicionJ = (byte) aleatorio.nextInt(j+1);
                byte aux = array[i][j];
                array[i][j] = array[posicionI][posicionJ];
                array[posicionI][posicionJ] = aux;
            }
        }
    }
    private void desordenar(byte[] image) {
        //Algoritmo de Barajar de Fisher-Yates --> YYDS
        for (int i = image.length - 1; i > 0; i--) {
            int index = aleatorio.nextInt(i + 1);
            byte aux = image[i];
            image[i] = image[index];
            image[index] = aux;
        }
    }
    private void iniciarPosiciones() {
        byte[] image = new byte[16];
        for (byte i = 0; i<image.length; i++){
            image[i] = i;
        }
        desordenar(image);
        posicionImagen = matrizBidimensional(image);
    }
    private void iniciarImagenes() {
        getContentPane().removeAll();
        if (victoria()){
            JLabel victoria = new JLabel(new ImageIcon("imagenes/" +palabras((byte)5) +".png"));
            victoria.setBounds(150, 180, 300, 300);
            getContentPane().add(victoria);
        }

        JLabel pasos = new JLabel(palabras((byte) 6) + ": " +this.pasos);
        pasos.setBounds(60, 30, 300, 20);
        pasos.setFont(new Font("宋体",Font.BOLD, 20));
        getContentPane().add(pasos);

        for (byte x = 0; x<posicionImagen.length; x++) {
            for (byte y = 0; y<posicionImagen[x].length; y++) {
                byte posi = posicionImagen[x][y];
                if (posi == 0){
                    continue;
                }
                JLabel imagenes = new JLabel(new ImageIcon("imagenes/"+directorio +posi  +".jpg"));
                imagenes.setBounds(x * 105 +91, y * 105 +126, 105, 105);
                imagenes.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
                getContentPane().add(imagenes);
            }
        }

        JLabel background = new JLabel(new ImageIcon("imagenes/background.png"));
        background.setBounds(40, 40, 508, 560);
        getContentPane().add(background);
        getContentPane().repaint();
    }
    private void iniciarMenuCompleto() {
        iniciarMenu();
        aniadeAlMenu();
        aniadeActionListenerClik();
    }
    private void iniciarMenu() {
        funciones.setText(palabras((byte) 0));

        cambiarImagenes.setText(palabras((byte) 7));
        cambiarImagenes.setActionCommand("Cambiar Imagenes");
        personajes.setText(palabras((byte) 8));
        personajes.setActionCommand("Personajes");
        animales.setText(palabras((byte) 9));
        animales.setActionCommand("Animales");
        deportivos.setText(palabras((byte) 10));
        deportivos.setActionCommand("Deportivos");

        reiniciar.setText(palabras((byte) 2));
        reiniciar.setActionCommand("Reiniciar Juego");
        reiniciarSesion.setText(palabras((byte) 3));
        reiniciarSesion.setActionCommand("Reiniciar Sesión");
        cerrarJuego.setText(palabras((byte) 4));
        cerrarJuego.setActionCommand("Cerrar Juego");

        idiomas.setText(palabras((byte) 1));
    }
    private void aniadeAlMenu() {
        funciones.add(cambiarImagenes);
        cambiarImagenes.add(personajes);
        cambiarImagenes.add(animales);
        cambiarImagenes.add(deportivos);

        funciones.add(reiniciar);
        funciones.add(reiniciarSesion);
        funciones.add(cerrarJuego);

        idiomas.add(english);
        idiomas.add(espaniol);
        idiomas.add(chino);

        JMenuBar menuPrincipar = new JMenuBar();
        menuPrincipar.add(funciones);
        menuPrincipar.add(idiomas);

        setJMenuBar(menuPrincipar);
    }

    private void aniadeActionListenerClik() {
        personajes.addActionListener(this);
        animales.addActionListener(this);
        deportivos.addActionListener(this);
        reiniciar.addActionListener(this);
        reiniciarSesion.addActionListener(this);
        cerrarJuego.addActionListener(this);
        english.addActionListener(this);
        espaniol.addActionListener(this);
        chino.addActionListener(this);
    }

    private void iniciarJFrame() {
        setSize(603, 680);
        setTitle("Puzzle -- V1.0");
        setAlwaysOnTop(true); //Siempre fijado en la cima
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarYguardar();
            }
        });
    }
    private String palabras(byte i){
        if (App.idioma.equals("EN")) {
            String[] palabrasEn = {"Function", "Language", "Restart", "Restart Session", "Close Game", "victory", "Steps", "Change Images", "Characters", "Animals", "Sports"};
            return palabrasEn[i];
        }
        if (App.idioma.equals("ES")) {
            String[] palabrasEs = {"Funcíon", "idioma", "Reiniciar Juego", "Reiniciar Sesión", "Cerrar Juego", "victoria", "Pasos", "Cambiar Imagenes", "Personajes", "Animales", "Deportivos"};
            return palabrasEs[i];
        }
        String[] palabrasCh = {"功能", "语言", "重新游戏", "重新登陆", "关闭游戏", "胜利", "步数", "更换图片", "人物", "动物", "运动"};
        return palabrasCh[i];
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        String comandos = e.getActionCommand();
        switch (comandos){
            case "English":
                App.cambiarIdiomas((byte) 1);
                break;
            case "Español":
                App.cambiarIdiomas((byte) 2);
                break;
            case "中文":
                App.cambiarIdiomas((byte) 0);
                break;
            case "Reiniciar Juego":
                desOrdenarBidimensional(posicionImagen);
                buscaCeroRestablecerXY();
                pasos = 0;
                break;
            case "Reiniciar Sesión":
                almacenarDados();
                dispose();
                new LoginJFrame();
                break;
            case "Cerrar Juego":
                cerrarYguardar();
                break;
            case "Personajes":
                cambiarAlImagenes("girl");
                break;
            case "Animales":
                cambiarAlImagenes("animal");
                break;
            case "Deportivos":
                cambiarAlImagenes("sport");
                break;
        }
        iniciarMenu();
        iniciarImagenes();
    }

    private void cerrarYguardar() {
        almacenarDados();
        System.exit(0);
    }

    private void cambiarAlImagenes (String tipo) {
        directorioTipo = tipo;
        directorio = directorioTipo+"/"+directorioTipo+imagenId+"/";
        byte cantidad = getCantidaDeDirectorioEnDirectorio("imagenes/"+directorioTipo);
        imagenId = (byte) (aleatorio.nextInt(cantidad)+1);
        iniciarPosiciones();
        pasos = 0;
    }

    private byte getCantidaDeDirectorioEnDirectorio(String directorio) {
        return (byte) Stream.of(
                        Objects.requireNonNull(
                                new File(directorio).listFiles()
                        )
                ).filter(File::isDirectory)
                .count();
    }

    private void almacenarDados(){
        int posicion = UsuariosManejador.getPosiUsuario(nombre, 0);
        if (posicion != -1) {
            UsuariosManejador.Usuario usuario = new UsuariosManejador.Usuario(nombre, contrasenia, directorio, pasos, posicionImagen, posicionVacioX, posicionVacioY);
            UsuariosManejador.setUsuarios(posicion, usuario);
        }
    }
    public void cargarDados(UsuariosManejador.Usuario usuario){
        nombre = usuario.getNombre();
        contrasenia = usuario.getContrasenia();
        if (!usuario.isNuevoUsuario()) {
            App.idioma = usuario.getIdioma();
            posicionImagen = usuario.getPosiciones();
            posicionVacioX = usuario.getPosicionVacioX();
            posicionVacioY = usuario.getPosicionVacioY();
            pasos = usuario.getPasos();
            directorio = usuario.getDirectorio();
            iniciarMenu();
            iniciarImagenes();
        }
    }
}