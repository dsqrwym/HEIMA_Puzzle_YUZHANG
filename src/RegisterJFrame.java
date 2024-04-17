import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class RegisterJFrame extends JFrame implements ActionListener {
    private final JMenu idiomas = new JMenu();
    private final JMenuItem english = new JMenuItem("English");
    private final JMenuItem espanio = new JMenuItem("Español");
    private final JMenuItem chino = new JMenuItem("中文");
    private JPasswordField contraseniaIntro;
    private JTextField nombreIntro;
    private String ojosIconDirectorio = "close";
    private JPasswordField contraseniaIntroRepite;
    public RegisterJFrame(){
        iniciarJFrame();
        iniciarMenu();
        conficurarMenu();
        iniciarComponentes();

        setVisible(true);
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
        ojos.repaint();

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

        contraseniaIntroRepite = new JPasswordField();
        contraseniaIntroRepite.setBounds(175, 186, 200, 20);
        getContentPane().add(contraseniaIntroRepite);

        nombre = palabras((byte)4);
        anchura = (byte) metrics.stringWidth(nombre);
        JButton registrarse = new JButton(nombre);
        registrarse.setBounds(230-anchura, 250, anchura+80, 38);
        registrarse.setFont(new Font("Montserrat",Font.BOLD, 18));
        registrarse.setFocusPainted(false);
        registrarse.setFocusable(false);
        registrarse.setBackground(new Color(255, 208, 109));
        registrarse.addMouseListener(new MouseAdapter() {
            Color original;
            @Override
            public void mousePressed(MouseEvent e) {
                original = e.getComponent().getBackground();
                registrarse.setBackground(original.darker());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                registrarse.setBackground(original);
                examinar();
            }
            private void examinar() {
                String nombre = nombreIntro.getText();
                String contrasenia = getContrasenia(contraseniaIntro);
                String contraseniaRepetida = getContrasenia(contraseniaIntroRepite);

                if (nombre.isEmpty()) {
                    mostrarError(palabras((byte) 5));
                    return;
                }

                if (contrasenia.isEmpty()) {
                    mostrarError(palabras((byte) 7));
                    nombreIntro.setText(nombre);
                    return;
                }

                if (!contrasenia.equals(contraseniaRepetida)) {
                    mostrarError(palabras((byte) 8));
                    return;
                }

                if (UsuariosManejador.getPosiUsuario(nombre, 0) != -1) {
                    mostrarError(palabras((byte)6));
                    return;
                }

                UsuariosManejador.aniadeUsuario(nombre, contrasenia);
                JOptionPane.showInternalMessageDialog(getContentPane(), palabras((byte) 9));
                new LoginJFrame();
                dispose();
            }
            private void mostrarError(String mensaje) {
                JOptionPane.showInternalMessageDialog(getContentPane(), mensaje, "", JOptionPane.WARNING_MESSAGE);
            }
        });
        getContentPane().add(registrarse);

        JLabel background = new JLabel(new ImageIcon("imagenes/background.png"));
        background.setBounds(0 ,0, 470, 390);


        getContentPane().add(background);
    }
    private static FontMetrics getMetrics() {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // 从BufferedImage中获取Graphics对象
        Graphics2D g2d = img.createGraphics();
        // 使用Graphics对象获取FontMetrics对象
        return g2d.getFontMetrics();
    }

    private void conficurarMenu() {
        idiomas.setText(palabras((byte)3));
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

    private void iniciarJFrame() {
        setSize(488, 430);
        setTitle("Register");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    private String palabras(byte i){
        if (App.idioma.equals("EN")) {
            String[] nombresEn = {"Username", "Password", "Verify Password", "Language", "Sign up", "Username is empty", "User already exist", "Password is empty", "The passwords do not match", "Registration Success"};
            return nombresEn[i];
        }
        if (App.idioma.equals("ES")) {
            String[] nombresEs = {"Nombre de Usuario", "Contraseña", "Verificar contraseña", "Idioma", "Registrarse", "El nombre de usuario está vació", "El usuario ya existe", "La contraseña está vacía", "Las contraseñas son inconsistentes", "Registración exitosa"};
            return nombresEs[i];
        }
        String[] nombresCh = {"用户名", "密码", "验证密码", "语言", "注册", "用户名为空", "用户已存在", "密码为空", "密码不一致", "注册成功"};
        return nombresCh[i];
    }
    public void actionPerformed(ActionEvent e) {
        String comandos = e.getActionCommand();
        String contrasenia = getContrasenia(contraseniaIntro);
        String contraseniaRepite = getContrasenia(contraseniaIntroRepite);
        String nombre = nombreIntro.getText();
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
                contrasenia = getContrasenia(contraseniaIntro);
                break;
        }
        conficurarMenu();
        iniciarComponentes();
        contraseniaIntro.setText(contrasenia);
        contraseniaIntroRepite.setText(contraseniaRepite);
        nombreIntro.setText(nombre);
    }

    private String getContrasenia(JPasswordField contraseniaIntro) {
        char[] arrayContrasenia = contraseniaIntro.getPassword();
        StringBuilder contrasenia = new StringBuilder();
        for (char c : arrayContrasenia) {
            contrasenia.append(c);
        }
        return contrasenia.toString();
    }
}