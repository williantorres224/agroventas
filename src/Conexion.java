import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static final String URL =
        "jdbc:mysql://localhost:3306/agroventas?useSSL=false&allowPublicKeyRetrieval=true";

    private static final String USER = System.getenv("DB_USER");
    private static final String PASS = System.getenv("DB_PASS");

    public static Connection getConexion() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}