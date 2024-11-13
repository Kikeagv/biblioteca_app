package com.biblioteca.ui;

import javax.swing.*;

public class OpcionesUsuarioUI {

    private String username;

    public OpcionesUsuarioUI(String username) {
        this.username = username;
    }

    public void mostrarVentana() {
        JFrame frame = new JFrame("Opciones de Usuario");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        JLabel lblUsuario = new JLabel("Usuario: " + username);
        lblUsuario.setBounds(20, 20, 200, 25);
        panel.add(lblUsuario);

        JButton btnCambiarContrasena = new JButton("Cambiar Contraseña");
        btnCambiarContrasena.setBounds(20, 60, 150, 25);
        panel.add(btnCambiarContrasena);
       
        frame.setVisible(true);
    }
}
