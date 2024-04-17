public class UsuariosManejador {
    private UsuariosManejador(){}
    private static Usuario[] usuarios = new Usuario[1];
    private static int cantidad = 0;
    public static int getCantidad(){return cantidad;}

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
            cantidad++;
        }else {
            crecerArray(new Usuario(nombre, contrasenia));
        }
    }
    public static void aniadeUsuario(String nombre, String contrasenia, String directorio, int pasos, byte[][] posiciones, byte posicionVacioX, byte posicionVacioY){
        if (cantidad < usuarios.length) {
            usuarios[cantidad] = new Usuario(nombre, contrasenia, directorio, pasos, posiciones, posicionVacioX, posicionVacioY);
            cantidad++;
        }else {
            crecerArray(new Usuario(nombre, contrasenia, directorio, pasos, posiciones, posicionVacioX, posicionVacioY));
        }
    }

    private static void crecerArray(Usuario usuario) {
        Usuario[] nuevoArray = new Usuario[usuarios.length+1];
        for (int i = 0; i < nuevoArray.length; i++) {
            if (i < usuarios.length){
                nuevoArray[i] = usuarios[i];
            }else {
                nuevoArray[i] = usuario;
            }
        }
        usuarios = nuevoArray;
        cantidad++;
    }

    public static class Usuario {
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
