import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/agroventas?useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASS");

    public static Connection getConexion() throws Exception {

        if (USER == null || PASS == null) {
            throw new RuntimeException(" Configura las variables de entorno DB_USER y DB_PASS");
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}