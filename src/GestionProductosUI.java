import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class GestionProductosUI extends JFrame {

    private JTextField txtNombre, txtPrecio;
    private JTable tabla;
    private DefaultTableModel modelo;

    private ProductoDAO dao = new ProductoDAO();
    private int idSeleccionado = -1;

    public GestionProductosUI() {
        setTitle("Gestión de Productos");
        setSize(650, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10,10));

        //  FORMULARIO
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        txtNombre = new JTextField();
        txtPrecio = new JTextField();

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);

        add(panel, BorderLayout.NORTH);

        //  TABLA
        modelo = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio"}, 0);
        tabla = new JTable(modelo);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        //  BOTONES
        JPanel botones = new JPanel();

        JButton btnAgregar = new JButton("Agregar");
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        botones.add(btnAgregar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        add(botones, BorderLayout.SOUTH);

        //  EVENTOS
        btnAgregar.addActionListener(e -> agregar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        tabla.getSelectionModel().addListSelectionListener(e -> cargarSeleccion());

        listar();
    }

    //  LISTAR
    private void listar() {
        try {
            modelo.setRowCount(0);
            ArrayList<Producto> lista = dao.listarProductos();

            for (Producto p : lista) {
                modelo.addRow(new Object[]{
                        p.getId(),
                        p.getNombre(),
                        p.getPrecio()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al listar productos");
        }
    }

    //  VALIDAR
    private boolean validar() {
        String nombre = txtNombre.getText().trim();
        String precioStr = txtPrecio.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio");
            return false;
        }

        try {
            double precio = Double.parseDouble(precioStr);

            if (precio <= 0) {
                JOptionPane.showMessageDialog(this, "El precio debe ser mayor a 0");
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "El precio debe ser numérico");
            return false;
        }

        return true;
    }

    //  AGREGAR
    private void agregar() {
        try {
            if (!validar()) return;

            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            dao.insertarProducto(new Producto(0, nombre, precio));

            JOptionPane.showMessageDialog(this, "Producto agregado");
            listar();
            limpiar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar producto");
        }
    }

    //  ACTUALIZAR
    private void actualizar() {
        try {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto");
                return;
            }

            if (!validar()) return;

            String nombre = txtNombre.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            dao.actualizarProducto(new Producto(idSeleccionado, nombre, precio));

            JOptionPane.showMessageDialog(this, "Producto actualizado");
            listar();
            limpiar();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar producto");
        }
    }

    //  ELIMINAR
    private void eliminar() {
        try {
            if (idSeleccionado == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "¿Seguro que deseas eliminar este producto?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dao.eliminarProducto(idSeleccionado); // ← aquí valida ventas
                JOptionPane.showMessageDialog(this, "Producto eliminado");
                listar();
                limpiar();
            }

        } catch (Exception e) {
            //  Mensaje real (ej: producto tiene ventas)
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    //  CARGAR SELECCIÓN
    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();

        if (fila != -1) {
            idSeleccionado = (int) modelo.getValueAt(fila, 0);
            txtNombre.setText(modelo.getValueAt(fila, 1).toString());
            txtPrecio.setText(modelo.getValueAt(fila, 2).toString());
        }
    }

    //  LIMPIAR
    private void limpiar() {
        txtNombre.setText("");
        txtPrecio.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}