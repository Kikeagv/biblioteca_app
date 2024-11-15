package com.biblioteca.ui;

import com.biblioteca.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

public class LoginUI {
    private UsuarioService usuarioService = new UsuarioService();

    public void mostrarLogin() {
        // Configuración de la ventana principal
        JFrame frame = new JFrame("Sistema Biblioteca - Login");
        frame.setSize(450, 350); // Tamaño aumentado
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false); // No permitir redimensionar

        // Título
        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 26)); // Fuente más grande
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(lblTitulo, BorderLayout.NORTH);

        // Panel Central: Formulario de Login
        JPanel panelCentral = new JPanel(new GridLayout(4, 1, 15, 15)); // Espaciado aumentado
        panelCentral.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JTextField txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("SansSerif", Font.PLAIN, 18)); // Fuente más grande
        txtUsuario.setPreferredSize(new Dimension(250, 40)); // Tamaño ajustado

        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("SansSerif", Font.PLAIN, 18));

        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("SansSerif", Font.PLAIN, 18)); // Fuente más grande
        txtPassword.setPreferredSize(new Dimension(250, 40)); // Tamaño ajustado

        panelCentral.add(lblUsuario);
        panelCentral.add(txtUsuario);
        panelCentral.add(lblPassword);
        panelCentral.add(txtPassword);

        frame.add(panelCentral, BorderLayout.CENTER);

        // Panel Inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnLogin = new JButton("Login");
        JButton btnSalir = new JButton("Salir");

        estilizarBoton(btnLogin, new Color(40, 167, 69), Color.WHITE);
        estilizarBoton(btnSalir, new Color(220, 53, 69), Color.WHITE);

        panelBotones.add(btnLogin);
        panelBotones.add(btnSalir);
        frame.add(panelBotones, BorderLayout.SOUTH);

        // Eventos de los Botones
        btnLogin.addActionListener(e -> {
            String username = txtUsuario.getText();
            String password = new String(txtPassword.getPassword());

            if (usuarioService.login(username, password)) {
                JOptionPane.showMessageDialog(frame, "Inicio de sesión exitoso.");
                frame.dispose();
                MainUI.mostrarVentana(); // Correct method call to show the main menu
            } else {
                JOptionPane.showMessageDialog(frame, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnSalir.addActionListener(e -> System.exit(0));

        // Centrar la ventana y hacerla visible
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void estilizarBoton(JButton boton, Color bgColor, Color fgColor) {
        boton.setBackground(bgColor);
        boton.setForeground(fgColor);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 16)); // Fuente más grande
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }
}
