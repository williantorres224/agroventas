import javax.swing.*;
import java.awt.*;

public class LoginUI extends JFrame {

    private JTextField txtUser;
    private JPasswordField txtPass;
    private JButton btnLogin;

    public LoginUI() {
        setTitle("Login - Agroventas");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2, 10, 10));

        txtUser = new JTextField();
        txtPass = new JPasswordField();
        btnLogin = new JButton("Ingresar");

        add(new JLabel("Usuario:"));
        add(txtUser);

        add(new JLabel("Contraseña:"));
        add(txtPass);

        add(new JLabel());
        add(btnLogin);

        btnLogin.addActionListener(e -> autenticar());
    }

    private void autenticar() {
        try {
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword());

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa todos los campos");
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.login(user, pass);

            if (u != null) {

                //  guardar sesión con rol
                Sesion.iniciarSesion(u.getUsername(), u.getRol());

                JOptionPane.showMessageDialog(this,
                        "Bienvenido " + u.getUsername() + " (" + u.getRol() + ")");

                new RegistroVentasUI().setVisible(true);
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error en login");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}