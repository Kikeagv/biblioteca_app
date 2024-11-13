package com.biblioteca.ui;

import com.biblioteca.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

public class AgregarUsuarioUI {
    private UsuarioService usuarioService = new UsuarioService();

    public void mostrarVentana() {
        JFrame frame = new JFrame("Agregar Usuario Nuevo");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);

        JLabel titulo = new JLabel("Agregar Usuario Nuevo", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titulo, BorderLayout.NORTH);

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JTextField txtNombre = new JTextField();
        JTextField txtUsername = new JTextField();
        JTextField txtCorreo = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        JTextField txtTelefono = new JTextField();
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{"Administrador", "Profesor", "Alumno"});

        panelFormulario.add(new JLabel("Nombre:", SwingConstants.RIGHT));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Usuario:", SwingConstants.RIGHT));
        panelFormulario.add(txtUsername);
        panelFormulario.add(new JLabel("Correo:", SwingConstants.RIGHT));
        panelFormulario.add(txtCorreo);
        panelFormulario.add(new JLabel("Contraseña:", SwingConstants.RIGHT));
        panelFormulario.add(txtPassword);
        panelFormulario.add(new JLabel("Teléfono:", SwingConstants.RIGHT));
        panelFormulario.add(txtTelefono);
        panelFormulario.add(new JLabel("Tipo:", SwingConstants.RIGHT));
        panelFormulario.add(cmbTipo);

        frame.add(panelFormulario, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        estilizarBoton(btnGuardar, new Color(40, 167, 69), Color.WHITE);
        estilizarBoton(btnCancelar, new Color(220, 53, 69), Color.WHITE);

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        frame.add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText();
            String username = txtUsername.getText();
            String correo = txtCorreo.getText();
            String password = new String(txtPassword.getPassword());
            String telefono = txtTelefono.getText();
            String tipo = (String) cmbTipo.getSelectedItem();

            usuarioService.registrarUsuario(nombre, username, password, correo, telefono, tipo);
            JOptionPane.showMessageDialog(frame, "Usuario agregado correctamente.");
            frame.dispose();
            new GestionUsuariosUI().mostrarVentana();
        });

        btnCancelar.addActionListener(e -> {
            frame.dispose();
            new GestionUsuariosUI().mostrarVentana();
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
