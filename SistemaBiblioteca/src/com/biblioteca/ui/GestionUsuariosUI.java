package com.biblioteca.ui;

import com.biblioteca.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

public class GestionUsuariosUI {
    private UsuarioService usuarioService = new UsuarioService();

    public void mostrarVentana() {
        JFrame frame = new JFrame("Gestión de Usuarios");
        frame.setSize(400, 300); // Increased height to fit the "Volver" button
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 20, 20)); // Updated to fit 3 buttons
        JButton btnAgregarUsuario = new JButton("Agregar Usuario Nuevo");
        JButton btnListarUsuarios = new JButton("Listar Usuarios");
        JButton btnVolver = new JButton("Volver"); // Added "Volver" button

        estilizarBoton(btnAgregarUsuario, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnListarUsuarios, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnVolver, new Color(192, 57, 43), Color.WHITE); // Red background for "Volver"

        panelBotones.add(btnAgregarUsuario);
        panelBotones.add(btnListarUsuarios);
        panelBotones.add(btnVolver); // Add "Volver" button to panel
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 50, 20, 50));
        frame.add(panelBotones, BorderLayout.CENTER);

        // Action listeners for the buttons
        btnAgregarUsuario.addActionListener(e -> {
            frame.dispose();
            new AgregarUsuarioUI().mostrarVentana();
        });

        btnListarUsuarios.addActionListener(e -> {
            frame.dispose();
            new ListarUsuariosUI().mostrarVentana();
        });

        btnVolver.addActionListener(e -> {
            frame.dispose();
            MainUI.mostrarVentana(); // Correctly call the static method to show the main menu
        });


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void estilizarBoton(JButton boton, Color bgColor, Color fgColor) {
        boton.setBackground(bgColor);
        boton.setForeground(fgColor);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 16));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }
}
