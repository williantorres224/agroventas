import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;

public class App {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/agroventas";
        String user = "root";
        String password = "Mysql1234*TORRES";

        try {
     
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println(" Conexión exitosa a MySQL");

            Statement stmt = conn.createStatement();


            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS prueba (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(50)
                )
            """);
            System.out.println("Tabla creada/verificada");

        
            stmt.executeUpdate("INSERT INTO prueba(nombre) VALUES ('Willian')");
            System.out.println(" Registro insertado");  

      
            ResultSet rs = stmt.executeQuery("SELECT * FROM prueba");
            System.out.println(" Datos en la tabla:");

            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " - " + rs.getString("nombre")
                );
            }

          
            conn.close();
            System.out.println("Conexión cerrada");

        } catch (Exception e) {
            System.out.println("Error:");
            e.printStackTrace();
        }
    }
}