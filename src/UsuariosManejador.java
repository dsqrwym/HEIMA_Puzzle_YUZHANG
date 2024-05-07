import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UsuariosManejador {
    private UsuariosManejador(){}
    private static final String patch = "src/puzzle.ser";
    private static Usuario[] usuarios = new Usuario[2];
    private static int cantidad = 0;
    public static int getCantidad(){return cantidad;}
    public static void cargarDatos() {
        File datos = new File(patch);
        if (datos.exists()){
            ObjectInputStream input = null;
            try {
                input = new ObjectInputStream(Files.newInputStream(Paths.get(patch)));
                usuarios = (Usuario[]) input.readObject();
                cantidad = contarCantidad();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } finally {
                if (input!=null){
                    try {
                        input.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }else {
            almacenarDatos();
        }
    }

    private static int contarCantidad() {
        int i;
        for (i = 0; i < usuarios.length; i++) {
            if (usuarios[i] == null){
                break;
            }
        }
        return i;
    }

    public static void almacenarDatos() {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(Files.newOutputStream(Paths.get(patch)));
            out.writeObject(usuarios);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public static int getPosiUsuario(String nombre, int index){
        if (index< usuarios.length) {
            Usuario usuarioEnIndex = usuarios[index];
            if (usuarioEnIndex.nombre.equals(nombre)){
                return index;
            }else {
                return getPosiUsuario(nombre, index+1);
            }
        }
        return -1;
    }
    public static void setUsuarios(int index, Usuario nuevoUsuario){
        if (index< usuarios.length && index>=0) {
            usuarios[index] = nuevoUsuario;
            almacenarDatos();
        }
    }
    public static Usuario getUsuario(int index){
        if (index< usuarios.length && index>=0) {
            return usuarios[index];
        }
        return null;
    }
    public static void aniadeUsuario(String nombre, String contrasenia){
        if (cantidad < usuarios.length){
            usuarios[cantidad] = new Usuario(nombre, contrasenia);
        }else {
            crecerArray(new Usuario(nombre, contrasenia));
        }
        cantidad++;
    }

    private static void crecerArray(Usuario usuario) {
        Usuario[] nuevoArray = new Usuario[usuarios.length+3];
        System.arraycopy(usuarios, 0, nuevoArray, 0, usuarios.length);
        nuevoArray[usuarios.length] = usuario;
        usuarios = nuevoArray;
    }

    public static class Usuario implements Serializable {
        private static final long serialVersionUID = -5563065689438193822L;
        private final String idioma;
        private final String nombre;
        private final String contrasenia;
        private String directorio;
        private final int pasos;
        private byte[][] posiciones;
        private byte posicionVacioX;
        private byte posicionVacioY;
        private final boolean nuevoUsuario;
        Usuario(String nombre, String contrasenia){
            this.nombre = nombre;
            this.contrasenia = contrasenia;
            this.idioma = App.idioma;
            pasos = 0;
            nuevoUsuario = true;
        }
        Usuario(String nombre, String contrasenia, String directorio, int pasos, byte[][] posiciones, byte posicionVacioX, byte posicionVacioY){
            this.nombre = nombre;
            this.contrasenia = contrasenia;
            this.directorio = directorio;
            this.pasos = pasos;
            this.posiciones = posiciones;
            this.posicionVacioX = posicionVacioX;
            this.posicionVacioY = posicionVacioY;
            this.idioma = App.idioma;
            nuevoUsuario = false;
        }

        public String getIdioma() {
            return idioma;
        }

        public String getNombre() {
            return nombre;
        }

        public String getContrasenia() {
            return contrasenia;
        }

        public String getDirectorio() {
            return directorio;
        }

        public int getPasos() {
            return pasos;
        }


        public byte[][] getPosiciones() {
            return posiciones;
        }

        public byte getPosicionVacioX() {
            return posicionVacioX;
        }

        public byte getPosicionVacioY() {
            return posicionVacioY;
        }

        public boolean isNuevoUsuario() {
            return nuevoUsuario;
        }
    }
}
