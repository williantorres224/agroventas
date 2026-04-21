public class Sesion {

    private static String usuario;
    private static String rol;

    public static void iniciarSesion(String user, String r) {
        usuario = user;
        rol = r;
    }

    public static void cerrarSesion() {
        usuario = null;
        rol = null;
    }

    public static boolean haySesion() {
        return usuario != null;
    }

    public static String getUsuario() {
        return usuario;
    }

    public static String getRol() {
        return rol;
    }

    public static boolean esAdmin() {
        return "ADMIN".equalsIgnoreCase(rol);
    }
}