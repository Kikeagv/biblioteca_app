package com.biblioteca.ui;

import com.biblioteca.service.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListarUsuariosUI {
    private UsuarioService usuarioService = new UsuarioService();

    public void mostrarVentana() {
        JFrame frame = new JFrame("Usuarios Registrados");
        frame.setSize(850, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false); // No redimensionable
        frame.getContentPane().setBackground(Color.WHITE);

        // Título
        JLabel titulo = new JLabel("Usuarios Registrados", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titulo, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"Nombre", "Usuario", "Tipo", "Correo", "Teléfono", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tablaUsuarios.setRowHeight(25);
        tablaUsuarios.setGridColor(Color.LIGHT_GRAY);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        frame.add(scrollPane, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(Color.WHITE);
        JButton btnCambiarContraseña = new JButton("Cambiar Contraseña");
        JButton btnEliminarUsuario = new JButton("Eliminar Usuario");
        JButton btnRegresar = new JButton("Regresar");

        estilizarBoton(btnCambiarContraseña, new Color(59, 89, 182), Color.WHITE);
        estilizarBoton(btnEliminarUsuario, new Color(220, 53, 69), Color.WHITE);
        estilizarBoton(btnRegresar, new Color(108, 117, 125), Color.WHITE);

        panelBotones.add(btnCambiarContraseña);
        panelBotones.add(btnEliminarUsuario);
        panelBotones.add(btnRegresar);
        frame.add(panelBotones, BorderLayout.SOUTH);

        // Llenar la tabla con datos
        actualizarTablaUsuarios(modeloTabla);

        // Eventos
        btnCambiarContraseña.addActionListener(e -> {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();
            if (filaSeleccionada != -1) {
                String username = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
                mostrarFormularioCambiarContraseña(frame, username);
            } else {
                JOptionPane.showMessageDialog(frame, "Seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminarUsuario.addActionListener(e -> {
            int filaSeleccionada = tablaUsuarios.getSelectedRow();
            if (filaSeleccionada != -1) {
                String username = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
                int confirmacion = JOptionPane.showConfirmDialog(
                        frame,
                        "¿Está seguro de que desea eliminar al usuario " + username + "?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    usuarioService.eliminarUsuario(username);
                    actualizarTablaUsuarios(modeloTabla);
                    JOptionPane.showMessageDialog(frame, "Usuario eliminado correctamente.");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRegresar.addActionListener(e -> {
            frame.dispose();
            new GestionUsuariosUI().mostrarVentana();
        });

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void actualizarTablaUsuarios(DefaultTableModel modeloTabla) {
        modeloTabla.setRowCount(0);
        List<String[]> usuarios = usuarioService.obtenerUsuarios();
        for (String[] usuario : usuarios) {
            modeloTabla.addRow(usuario);
        }
    }

    private void mostrarFormularioCambiarContraseña(JFrame parentFrame, String username) {
        JDialog dialog = new JDialog(parentFrame, "Cambiar Contraseña", true);
        dialog.setSize(400, 200);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false); // No redimensionable
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Nueva Contraseña", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        dialog.add(lblTitulo, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 10, 10));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        panelCentro.setBackground(Color.WHITE);
        JPasswordField txtNuevaContrasena = new JPasswordField();
        txtNuevaContrasena.setFont(new Font("SansSerif", Font.PLAIN, 16));

        panelCentro.add(new JLabel("Contraseña:"));
        panelCentro.add(txtNuevaContrasena);
        dialog.add(panelCentro, BorderLayout.CENTER);

        JButton btnGuardar = new JButton("Guardar");
        estilizarBoton(btnGuardar, new Color(40, 167, 69), Color.WHITE);
        dialog.add(btnGuardar, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> {
            String nuevaContrasena = new String(txtNuevaContrasena.getPassword());
            usuarioService.actualizarContrasena(username, nuevaContrasena);
            JOptionPane.showMessageDialog(dialog, "Contraseña actualizada correctamente.");
            dialog.dispose();
        });

        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private void estilizarBoton(JButton boton, Color bgColor, Color fgColor) {
        boton.setBackground(bgColor);
        boton.setForeground(fgColor);
        boton.setFocusPainted(false);
        boton.setFont(new Font("SansSerif", Font.BOLD, 14));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
    }
}
