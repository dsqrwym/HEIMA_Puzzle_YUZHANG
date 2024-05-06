public class App  {
    public static String idioma = "EN";

    public static void main(String[] args){
        UsuariosManejador.cargarDatos();
        //UsuariosManejador.aniadeUsuario("yuzhang", "yuzhang2004210");
        new LoginJFrame();
    }
    public static void cambiarIdiomas(byte opcion){
        switch (opcion){
            case 1: {
                idioma = "EN";
                break;
            }
            case 2: {
                idioma = "ES";
                break;
            }
            default: idioma = "CH";
        }
    }
}