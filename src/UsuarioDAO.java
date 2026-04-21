import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario login(String user, String pass) throws Exception {

        String sql = "SELECT * FROM usuarios WHERE username=? AND password=?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, user);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("rol")
                );
            }
            
        }
        

        return null; // login fallido
    }
}