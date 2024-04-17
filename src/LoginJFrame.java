import javax.swing.*;
import util.CodeUtil;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class LoginJFrame extends JFrame implements ActionListener {
    private final JMenu idiomas = new JMenu();
    private final JMenuItem english = new JMenuItem("English");
    private final JMenuItem espanio = new JMenuItem("Español");
    private final JMenuItem chino = new JMenuItem("中文");
    private JTextField nombreIntro;
    private JPasswordField contraseniaIntro;
    private JTextField verificacionIntro;
    private String ojosIconDirectorio = "close";
    private String codigoDeVerificacion = CodeUtil.getCode();

    public LoginJFrame(){
        iniciarJframe();
        iniciarMenu();
        conficurarMenu();
        iniciarComponentes();

        setVisible(true);
    }
    private void iniciarMenu() {
        JMenuBar menuPrincipal = new JMenuBar();


        english.addActionListener(this);
        espanio.addActionListener(this);
        chino.addActionListener(this);

        menuPrincipal.add(idiomas);
        idiomas.add(english);
        idiomas.add(espanio);
        idiomas.add(chino);

        setJMenuBar(menuPrincipal);
    }

    private void conficurarMenu() {
        idiomas.setText(palabras((byte)3));
    }

    private void iniciarComponentes() {
        getContentPane().removeAll();
        /*
        在Swing应用程序中，通常需要等到组件被添加到屏幕上后才能获得有效的Graphics对象。
        但是，我需要在创建组件之前使用它，这就变成了一个无法达到的条件。
        为了在组件被添加到屏幕上之前就获得Graphics对象，需要创建一个图像来实现。
        即使用BufferedImage来创建一个虚拟的图像，然后从这个图像上获取Graphics对象。
        这个Graphics对象可以用于测量文本的宽度。
        然后使用 FontMetrics 对象来测量文本的宽度和高度
         */
        FontMetrics metrics = getMetrics();

        //为了适应动态文本
        String nombre = palabras((byte)0);
        byte anchura = (byte) metrics.stringWidth(nombre);

        JLabel nombreUsuario = new JLabel(nombre);
        nombreUsuario.setBounds(175 - (anchura+10), 65, anchura+12, 20);
        getContentPane().add(nombreUsuario);
        //+10 是在两者间增加间隙。
        nombreIntro = new JTextField();
        nombreIntro.setBounds(175, 65, 200, 20);
        getContentPane().add(nombreIntro);


        nombre = palabras((byte)1);
        anchura = (byte) metrics.stringWidth(nombre);
        JLabel contrasenia = new JLabel(nombre);
        contrasenia.setBounds(175 - (anchura+10), 125, anchura+12, 20);
        getContentPane().add(contrasenia);

        JButton ojos = new JButton(new ImageIcon("imagenes/"+ojosIconDirectorio +"Eyes.png"));
        ojos.setBounds(375, 125, 28, 20);
        ojos.setActionCommand("EyesChange");
        ojos.addActionListener(this);
        getContentPane().add(ojos);

        contraseniaIntro = new JPasswordField();
        contraseniaIntro.setBounds(175, 125, 200, 20);
        if (ojosIconDirectorio.equals("open")){
            contraseniaIntro.setEchoChar((char) 0);
        }
        getContentPane().add(contraseniaIntro);


        nombre = palabras((byte)2);
        anchura = (byte) metrics.stringWidth(nombre);
        JLabel verificacion = new JLabel(nombre);
        verificacion.setBounds(175 - (anchura+10), 186, anchura+12, 20);
        getContentPane().add(verificacion);

        verificacionIntro = new JTextField();
        verificacionIntro.setBounds(175, 186, 100, 20);
        getContentPane().add(verificacionIntro);

        JLabel jlabelCodVerifi = new JLabel(codigoDeVerificacion);
        jlabelCodVerifi.setBounds(280, 186, 50, 20);
        getContentPane().add(jlabelCodVerifi);

        nombre = palabras((byte)4);
        anchura = (byte) metrics.stringWidth(nombre);
        JButton iniciarSesion = new JButton(nombre);
        iniciarSesion.setBounds(145-anchura, 250, anchura+80, 38);
        iniciarSesion.setFont(new Font("Montserrat",Font.BOLD, 18));
        iniciarSesion.setFocusPainted(false);
        iniciarSesion.setFocusable(false);
        iniciarSesion.setBackground(new Color(255, 208, 109));
        iniciarSesion.addMouseListener(new MouseAdapter() {
            Color original;
            @Override
            public void mousePressed(MouseEvent e) {
                original = e.getComponent().getBackground();
                iniciarSesion.setBackground(original.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                iniciarSesion.setBackground(original);
                examinar();
                iniciarComponentes();
            }

            private void examinar() {
                if (UsuariosManejador.getCantidad() != 0){
                    if (codigoDeVerificacion.equals(verificacionIntro.getText())) {
                        UsuariosManejador.Usuario usuario = UsuariosManejador.getUsuario(UsuariosManejador.getPosiUsuario(nombreIntro.getText(), 0));
                        if (usuario == null) {
                            JOptionPane.showInternalMessageDialog(getContentPane(), palabras((byte) 6), "", JOptionPane.ERROR_MESSAGE);
                        } else if (!usuario.getContrasenia().equals(getContrasenia())) {
                            JOptionPane.showInternalMessageDialog(getContentPane(), palabras((byte) 7), "", JOptionPane.ERROR_MESSAGE);
                        } else {
                            GameJFrame game = new GameJFrame();
                            game.cargarDados(usuario);
                            dispose();
                        }
                    }else {
                        JOptionPane.showInternalMessageDialog(getContentPane(), palabras((byte) 8));
                        codigoDeVerificacion = CodeUtil.getCode();
                    }
                }
            }
        });
        getContentPane().add(iniciarSesion);

        nombre = palabras((byte)5);
        anchura = (byte) metrics.stringWidth(nombre);
        JButton registrarse = new JButton(nombre);
        registrarse.setBounds(315-anchura, 250, anchura+80, 38);
        registrarse.setFont(new Font("Montserrat",Font.BOLD, 18));
        registrarse.setFocusPainted(false);
        registrarse.setBackground(Color.white);
        registrarse.addMouseListener(new MouseAdapter() {
            Color original;
            @Override
            public void mousePressed(MouseEvent e) {
                original = e.getComponent().getBackground();
                iniciarSesion.setBackground(original.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                iniciarSesion.setBackground(original);
                new RegisterJFrame();
                dispose();
            }
        });
        getContentPane().add(registrarse);

        JLabel background = new JLabel(new ImageIcon("imagenes/background.png"));
        background.setBounds(0 ,0, 470, 390);


        getContentPane().add(background);
        getContentPane().repaint();
    }

    private static FontMetrics getMetrics() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // 从BufferedImage中获取Graphics对象
        Graphics2D g2d = img.createGraphics();
        // 使用Graphics对象获取FontMetrics对象
        return g2d.getFontMetrics();
    }

    private void iniciarJframe() {
        setSize(488, 430);
        setTitle("Log In");
        //setAlwaysOnTop(true); //Siempre fijado en la cima
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private String palabras(byte i){
        if (App.idioma.equals("EN")) {
            String[] palabrasEn = {"Username", "Password", "Verification Code", "Language", "Log in", "Sign up", "User does not exist", "Incorrect password", "The verification code is incorrect"};
            return palabrasEn[i];
        }
        if (App.idioma.equals("ES")) {
            String[] palabrasEs = {"Nombre de Usuario", "Contraseña", "Código de Verificación", "Idioma", "Iniciar sesión", "Registrarse", "El usuario no existe", "Contraseña incorrecta", "El código de verificación es incorrecto"};
            return palabrasEs[i];
        }
        String[] palabrasCh = {"用户名", "密码", "验证码", "语言", "登录", "注册", "用户不存在", "密码错误", "验证码错误"};
        return palabrasCh[i];
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comandos = e.getActionCommand();
        String contrasenia = getContrasenia();
        String nombre = nombreIntro.getText();
        String verifica = verificacionIntro.getText();
        switch (comandos) {
            case "English":
                App.cambiarIdiomas((byte) 1);
                break;
            case "Español":
                App.cambiarIdiomas((byte) 2);
                break;
            case "中文":
                App.cambiarIdiomas((byte) 0);
                break;
            case "EyesChange":
                if (ojosIconDirectorio.equals("close")){
                    ojosIconDirectorio = "open";
                    contraseniaIntro.setEchoChar((char) 0);
                }else {
                    ojosIconDirectorio = "close";
                    contraseniaIntro.setEchoChar('*');
                }
                contrasenia = getContrasenia();
                break;
        }
        conficurarMenu();
        iniciarComponentes();
        contraseniaIntro.setText(contrasenia);
        nombreIntro.setText(nombre);
        verificacionIntro.setText(verifica);
    }

    private String getContrasenia() {
        char[] arrayContrasenia = contraseniaIntro.getPassword();
        StringBuilder contrasenia = new StringBuilder();
        for (char c : arrayContrasenia) {
            contrasenia.append(c);
        }
        return contrasenia.toString();
    }
}
