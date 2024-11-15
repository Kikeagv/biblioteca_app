package com.biblioteca.ui;

import com.biblioteca.dao.EjemplarDAO;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AgregarMaterialUI {
    private EjemplarDAO ejemplarDAO = new EjemplarDAO();
    private Map<String, JTextField> camposEspecificos = new HashMap<>();

    private static final Map<String, String[]> CAMPOS_ESPECIFICOS = Map.of(
        "Libro", new String[] {"Autor", "Editorial", "ISBN", "Número de Páginas", "Género", "Año de Publicación"},
        "Revista", new String[] {"Editorial", "Periodicidad", "Fecha de Publicación"},
        "CD", new String[] {"Artista", "Género", "Duración", "Número de Canciones"},
        "DVD", new String[] {"Director", "Género", "Duración"},
        "Tesis", new String[] {"Autor", "Título de Investigación", "Carrera", "Universidad", "Año de Presentación"}
    );

    public void mostrarVentana() {
        JFrame frame = new JFrame("Agregar Nuevo Material");
        frame.setSize(600, 750);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel lblTipo = new JLabel("Tipo de Material:");
        lblTipo.setBounds(20, 20, 150, 25);
        frame.add(lblTipo);

        JComboBox<String> comboTipo = new JComboBox<>(CAMPOS_ESPECIFICOS.keySet().toArray(new String[0]));
        comboTipo.setBounds(180, 20, 200, 25);
        frame.add(comboTipo);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(20, 60, 150, 25);
        frame.add(lblTitulo);

        JTextField txtTitulo = new JTextField();
        txtTitulo.setBounds(180, 60, 200, 25);
        frame.add(txtTitulo);

        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setBounds(20, 100, 150, 25);
        frame.add(lblCantidad);

        JTextField txtCantidad = new JTextField();
        txtCantidad.setBounds(180, 100, 200, 25);
        frame.add(txtCantidad);

        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setBounds(20, 140, 150, 25);
        frame.add(lblUbicacion);

        JTextField txtUbicacion = new JTextField();
        txtUbicacion.setBounds(180, 140, 200, 25);
        frame.add(txtUbicacion);

        JPanel panelDetalles = new JPanel(new GridBagLayout());
        JScrollPane scrollDetalles = new JScrollPane(panelDetalles);
        scrollDetalles.setBounds(20, 200, 550, 250);
        frame.add(scrollDetalles);

        actualizarCampos(panelDetalles, camposEspecificos, (String) comboTipo.getSelectedItem());

        comboTipo.addActionListener(e -> {
            panelDetalles.removeAll();
            actualizarCampos(panelDetalles, camposEspecificos, (String) comboTipo.getSelectedItem());
            panelDetalles.revalidate();
            panelDetalles.repaint();
        });

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(150, 500, 120, 30);
        estilizarBoton(btnGuardar, new Color(40, 167, 69), Color.WHITE);
        frame.add(btnGuardar);

        btnGuardar.addActionListener(e -> {
            try {
                String tipo = (String) comboTipo.getSelectedItem();
                String titulo = txtTitulo.getText();
                int cantidad = Integer.parseInt(txtCantidad.getText());
                String ubicacion = txtUbicacion.getText();

                if (titulo.isEmpty() || ubicacion.isEmpty()) {
                    throw new IllegalArgumentException("Todos los campos son obligatorios.");
                }

                Map<String, String> detallesEspecificos = new HashMap<>();
                for (String campo : CAMPOS_ESPECIFICOS.get(tipo)) {
                    String valor = camposEspecificos.get(campo).getText();
                    if (valor.isEmpty()) {
                        throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
                    }
                    detallesEspecificos.put(campo, valor);
                }

                // Llamar al DAO para registrar el ejemplar
                ejemplarDAO.registrarEjemplarConDetalles(tipo, titulo, cantidad, ubicacion, detallesEspecificos);

                JOptionPane.showMessageDialog(frame, "Material registrado correctamente.");
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error al registrar el material: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botón Regresar
        JButton btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(300, 500, 120, 30);
        estilizarBoton(btnRegresar, new Color(220, 53, 69), Color.WHITE);
        frame.add(btnRegresar);


        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void actualizarCampos(JPanel panel, Map<String, JTextField> camposEspecificos, String tipo) {
        panel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (String campo : CAMPOS_ESPECIFICOS.get(tipo)) {
            JLabel label = new JLabel(campo + ":");
            JTextField textField = new JTextField(15);
            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(label, gbc);
            gbc.gridx = 1;
            panel.add(textField, gbc);
            camposEspecificos.put(campo, textField);
        }
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
