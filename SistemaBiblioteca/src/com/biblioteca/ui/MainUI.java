package com.biblioteca.ui;

import javax.swing.*;
import java.awt.*;

public class MainUI {

    private static JFrame frame; // Static reference to ensure only one instance

    public static void mostrarVentana() {
        if (frame != null) { // If the frame already exists, bring it to the front
            frame.setVisible(true);
            frame.toFront();
            return;
        }

        frame = new JFrame("Sistema Biblioteca - Menú Principal");
        frame.setSize(400, 350); // Increased height to fit the "Cerrar sesión" button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Main panel with BoxLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel);

        // Title
        JLabel lblTitulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        mainPanel.add(lblTitulo);

        // Buttons
        JButton btnGestionUsuarios = new JButton("Gestión de Usuarios");
        JButton btnGestionMateriales = new JButton("Gestión de Materiales");
        JButton btnPrestamos = new JButton("Gestión de Préstamos");
        JButton btnCerrarSesion = new JButton("Cerrar sesión"); // Added "Cerrar sesión" button

        estilizarBoton(btnGestionUsuarios, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnGestionMateriales, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnPrestamos, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnCerrarSesion, new Color(192, 57, 43), Color.WHITE); // Red background for "Cerrar sesión"

        btnGestionUsuarios.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnGestionMateriales.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPrestamos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(btnGestionUsuarios);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing between buttons
        mainPanel.add(btnGestionMateriales);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnPrestamos);
        mainPanel.add(Box.createVerticalStrut(30)); // Add extra spacing before "Cerrar sesión"
        mainPanel.add(btnCerrarSesion);

        // Button actions
        btnGestionUsuarios.addActionListener(e -> {
            frame.setVisible(false); // Hide the main menu
            new GestionUsuariosUI().mostrarVentana();
        });
        btnGestionMateriales.addActionListener(e -> {
            frame.setVisible(false); // Hide the main menu
            new GestionMaterialesUI().mostrarVentana();
        });
        btnPrestamos.addActionListener(e -> {
            frame.setVisible(false); // Hide the main menu
            new PrestamosUI().mostrarVentana();
        });
        btnCerrarSesion.addActionListener(e -> {
            frame.dispose(); // Close the main menu
            frame = null; // Clear the reference for new login
            new LoginUI().mostrarLogin(); // Return to the login screen
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void estilizarBoton(JButton boton, Color bgColor, Color fgColor) {
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
