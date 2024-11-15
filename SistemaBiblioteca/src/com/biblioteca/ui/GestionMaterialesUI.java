package com.biblioteca.ui;

import javax.swing.*;
import java.awt.*;

public class GestionMaterialesUI {

    public void mostrarVentana() {
        JFrame frame = new JFrame("Gestión de Materiales");
        frame.setSize(400, 300); // Increased height to fit the "Atrás" button
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JLabel titulo = new JLabel("Gestión de Materiales", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titulo, BorderLayout.NORTH);

        // Panel for buttons
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 30, 30)); // Updated to fit 3 buttons
        JButton btnAgregarMaterial = new JButton("Agregar Material Nuevo");
        JButton btnBuscarMaterial = new JButton("Buscar Materiales");
        JButton btnAtras = new JButton("Atrás"); // Added "Atrás" button

        estilizarBoton(btnAgregarMaterial, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnBuscarMaterial, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnAtras, new Color(192, 57, 43), Color.WHITE); // Red background for "Atrás"

        panelBotones.add(btnAgregarMaterial);
        panelBotones.add(btnBuscarMaterial);
        panelBotones.add(btnAtras); // Add "Atrás" button to panel
        panelBotones.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // Adjusted padding
        frame.add(panelBotones, BorderLayout.CENTER);

        // Add actions to buttons
        btnAgregarMaterial.addActionListener(e -> {
            frame.dispose();
            new AgregarMaterialUI().mostrarVentana();
        });

        btnBuscarMaterial.addActionListener(e -> {
            frame.dispose();
            new BuscarMaterialesUI().mostrarVentana();
        });

        btnAtras.addActionListener(e -> {
            frame.dispose();
            MainUI.mostrarVentana(); // Reuse the existing Main Menu window
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