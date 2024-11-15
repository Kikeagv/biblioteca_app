package com.biblioteca.ui;

import com.biblioteca.service.PrestamoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Map;

public class VerPrestamosUI {
    private PrestamoService prestamoService = new PrestamoService();
    private JTable table;

    public void mostrarVentana() {
        JFrame frame = new JFrame("Préstamos Pendientes");
        frame.setSize(700, 500); // Increased size to fit buttons
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        colocarComponentes(panel);

        frame.setVisible(true);
    }

    private void colocarComponentes(JPanel panel) {
        JLabel lblTitulo = new JLabel("Préstamos Pendientes");
        lblTitulo.setBounds(20, 10, 200, 25);
        panel.add(lblTitulo);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(20, 40, 100, 25);
        panel.add(lblUsuario);

        JTextField txtUsuario = new JTextField(20);
        txtUsuario.setBounds(80, 40, 200, 25);
        panel.add(txtUsuario);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setBounds(300, 40, 100, 25);
        panel.add(btnBuscar);

        table = new JTable();
        table.setBounds(20, 80, 640, 300);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(20, 80, 640, 300);
        panel.add(scrollPane);

        JButton btnRegresarMaterial = new JButton("Regresar material");
        btnRegresarMaterial.setBounds(20, 400, 150, 30);
        panel.add(btnRegresarMaterial);

        JButton btnAgregarMora = new JButton("Agregar mora");
        btnAgregarMora.setBounds(200, 400, 150, 30);
        panel.add(btnAgregarMora);

        // Load all outstanding loans at startup
        cargarPrestamos(null);

        // Search loans by user when the search button is clicked
        btnBuscar.addActionListener(e -> {
            String username = txtUsuario.getText().trim();
            cargarPrestamos(username.isEmpty() ? null : username);
        });

        // Return the selected item
        btnRegresarMaterial.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int prestamoId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                try {
                    prestamoService.registrarDevolucion(prestamoId, 1.0); // Example: $1.0 daily rate
                    JOptionPane.showMessageDialog(panel, "Material regresado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPrestamos(null); // Refresh table with latest data
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al regresar material: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un préstamo de la lista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });


        // Add a day of arrears for the selected item
        btnAgregarMora.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int prestamoId = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
                try {
                    prestamoService.agregarMora(prestamoId, 1.0); // Add 1 day of mora equivalent to $1.0
                    JOptionPane.showMessageDialog(panel, "Mora agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPrestamos(null); // Refresh table with latest data
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(panel, "Error al agregar mora: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(panel, "Seleccione un préstamo de la lista.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

    }

    private void cargarPrestamos(String username) {
        List<Map<String, String>> prestamos = prestamoService.obtenerPrestamosPendientesConMora(username);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Usuario");
        model.addColumn("Material");
        model.addColumn("Fecha Préstamo");
        model.addColumn("Mora Acumulada");

        for (Map<String, String> prestamo : prestamos) {
            model.addRow(new Object[]{
                    prestamo.get("id"),
                    prestamo.get("usuario"),
                    prestamo.get("material"),
                    prestamo.get("fecha_prestamo"),
                    prestamo.get("mora_acumulada") // Ensure this field updates
            });
        }

        table.setModel(model);
    }

}
