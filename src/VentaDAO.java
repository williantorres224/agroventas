import java.sql.Connection;
import java.sql.PreparedStatement;

public class VentaDAO {

    public boolean insertarVenta(Venta v) throws Exception {

        String sql = "INSERT INTO ventas (dni, edad, producto_id, cantidad, total, fecha) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, v.getDni());
            ps.setInt(2, v.getEdad());
            ps.setInt(3, v.getProductoId());
            ps.setInt(4, v.getCantidad());
            ps.setDouble(5, v.getTotal());
            ps.setString(6, v.getFecha());

            return ps.executeUpdate() > 0;
        }
    }
}