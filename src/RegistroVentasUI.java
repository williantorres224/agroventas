import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegistroVentasUI extends JFrame {

    private JTextField txtDni, txtEdad, txtCantidad, txtPrecio, txtTotal, txtFecha;
    private JComboBox<Producto> comboProductos;
    private JButton btnCalcular, btnGuardar, btnCerrarSesion, btnProductos;

    private JTable tablaVentas;
    private DefaultTableModel modeloTabla;

    public RegistroVentasUI() {

        // CONTROL DE SESIÓN
        if (!Sesion.haySesion()) {
            JOptionPane.showMessageDialog(this, "Acceso denegado");
            new LoginUI().setVisible(true);
            dispose();
            return;
        }

        setTitle("Registro de Ventas - Agroventas");
        setSize(900, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        //  10x2 = 20 espacios (evita desorden)
        JPanel panelFormulario = new JPanel(new GridLayout(10, 2, 8, 8));

        txtDni = new JTextField();
        txtEdad = new JTextField();
        txtCantidad = new JTextField();
        txtPrecio = new JTextField();
        txtTotal = new JTextField();
        txtFecha = new JTextField();

        txtPrecio.setEditable(false);
        txtTotal.setEditable(false);
        txtFecha.setEditable(false);

        txtFecha.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        comboProductos = new JComboBox<>();
        cargarProductos();

        comboProductos.addActionListener(e -> {
            Producto p = (Producto) comboProductos.getSelectedItem();
            if (p != null) {
                txtPrecio.setText(String.valueOf(p.getPrecio()));
                txtTotal.setText("");
            }
        });

        // Botones
        btnCalcular = new JButton("Calcular Total");
        btnGuardar = new JButton("Guardar Venta");
        btnCerrarSesion = new JButton("Cerrar sesión");
        btnProductos = new JButton("Gestionar Productos");

        btnCalcular.addActionListener(e -> calcularTotal());
        btnGuardar.addActionListener(e -> guardarVenta());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());
        btnProductos.addActionListener(e -> new GestionProductosUI().setVisible(true));

        //  Usuario
        JLabel lblUsuario = new JLabel("Usuario: " + Sesion.getUsuario());

        //  FORMULARIO
        panelFormulario.add(lblUsuario);
        panelFormulario.add(new JLabel(""));

        panelFormulario.add(new JLabel("DNI:"));
        panelFormulario.add(txtDni);

        panelFormulario.add(new JLabel("Edad:"));
        panelFormulario.add(txtEdad);

        panelFormulario.add(new JLabel("Producto:"));
        panelFormulario.add(comboProductos);

        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Total:"));
        panelFormulario.add(txtTotal);

        panelFormulario.add(new JLabel("Fecha:"));
        panelFormulario.add(txtFecha);

        panelFormulario.add(btnCalcular);
        panelFormulario.add(btnGuardar);

        panelFormulario.add(btnProductos);
        panelFormulario.add(btnCerrarSesion);

        add(panelFormulario, BorderLayout.NORTH);

        //  TABLA
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("DNI");
        modeloTabla.addColumn("Edad");
        modeloTabla.addColumn("Producto");
        modeloTabla.addColumn("Cantidad");
        modeloTabla.addColumn("Total");
        modeloTabla.addColumn("Fecha");

        tablaVentas = new JTable(modeloTabla);
        add(new JScrollPane(tablaVentas), BorderLayout.CENTER);

        listarVentas();
    }

    //  CERRAR SESIÓN
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            Sesion.cerrarSesion();
            new LoginUI().setVisible(true);
            dispose();
        }
    }

    //  CARGAR PRODUCTOS
    private void cargarProductos() {
        try {
            ProductoDAO dao = new ProductoDAO();
            ArrayList<Producto> lista = dao.listarProductos();

            comboProductos.removeAllItems();

            if (lista == null || lista.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay productos en la BD");
                return;
            }

            for (Producto p : lista) {
                comboProductos.addItem(p);
            }

            Producto p = (Producto) comboProductos.getSelectedItem();
            if (p != null) {
                txtPrecio.setText(String.valueOf(p.getPrecio()));
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error cargando productos: " + e.getMessage());
        }
    }

    //  CALCULAR TOTAL
    private void calcularTotal() {
        try {
            Producto p = (Producto) comboProductos.getSelectedItem();
            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            double total = p.getPrecio() * cantidad;
            txtTotal.setText(String.valueOf(total));

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ingresa una cantidad válida");
        }
    }

    //  GUARDAR VENTA
    private void guardarVenta() {
    try {
        String dni = txtDni.getText().trim();
        int edad = Integer.parseInt(txtEdad.getText().trim());
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        //  VALIDACIONES
        if (dni.length() != 8 || !dni.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "DNI inválido (debe tener 8 números)");
            return;
        }

        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida");
            return;
        }

        if (edad <= 0) {
            JOptionPane.showMessageDialog(this, "Edad inválida");
            return;
        }

        Producto p = (Producto) comboProductos.getSelectedItem();

        double total = p.getPrecio() * cantidad;
        String fecha = txtFecha.getText();

        Venta venta = new Venta(dni, edad, p.getId(), cantidad, total, fecha);
        VentaDAO dao = new VentaDAO();

        if (dao.insertarVenta(venta)) {
            JOptionPane.showMessageDialog(this, "Venta guardada");
            listarVentas();

            txtDni.setText("");
            txtEdad.setText("");
            txtCantidad.setText("");
            txtTotal.setText("");
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Edad y cantidad deben ser números");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error al guardar");
        ex.printStackTrace();
    }
}

    //  LISTAR VENTAS
    private void listarVentas() {
        modeloTabla.setRowCount(0);

        String sql = "SELECT v.id, v.dni, v.edad, p.nombre AS producto, " +
                "v.cantidad, v.total, v.fecha " +
                "FROM ventas v INNER JOIN productos p ON v.producto_id = p.id";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] fila = {
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getInt("edad"),
                        rs.getString("producto"),
                        rs.getInt("cantidad"),
                        rs.getDouble("total"),
                        rs.getString("fecha")
                };
                modeloTabla.addRow(fila);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar ventas: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistroVentasUI().setVisible(true));
    }
}
