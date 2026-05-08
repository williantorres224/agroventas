import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ProductoDAO {

    //  LISTAR
    public ArrayList<Producto> listarProductos() throws Exception {
        ArrayList<Producto> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, precio FROM productos";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                double precio = rs.getDouble("precio");

                lista.add(new Producto(id, nombre, precio));
            }
        }

        return lista;
    }

    //  INSERT
    public boolean insertarProducto(Producto p) throws Exception {
        String sql = "INSERT INTO productos (nombre, precio) VALUES (?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());

            return ps.executeUpdate() > 0;
        }
    }

    //  UPDATE
    public boolean actualizarProducto(Producto p) throws Exception {
        String sql = "UPDATE productos SET nombre = ?, precio = ? WHERE id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getPrecio());
            ps.setInt(3, p.getId());

            return ps.executeUpdate() > 0;
        }
    }

    //  DELETE
    public boolean eliminarProducto(int id) throws Exception {
        String sql = "DELETE FROM productos WHERE id = ?";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
