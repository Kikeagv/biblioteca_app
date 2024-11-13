package com.biblioteca.ui;

import com.biblioteca.service.PrestamoService;

import javax.swing.*;

public class RegresarMaterialUI {
    private PrestamoService prestamoService = new PrestamoService();

    public void mostrarVentana() {
        JFrame frame = new JFrame("Regresar Material");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        frame.add(panel);

        colocarComponentes(panel);

        frame.setVisible(true);
    }

    private void colocarComponentes(JPanel panel) {
        JLabel lblPrestamoId = new JLabel("ID del préstamo:");
        lblPrestamoId.setBounds(20, 20, 150, 25);
        panel.add(lblPrestamoId);

        JTextField txtPrestamoId = new JTextField(20);
        txtPrestamoId.setBounds(150, 20, 200, 25);
        panel.add(txtPrestamoId);

        JButton btnRegresar = new JButton("Regresar Material");
        btnRegresar.setBounds(120, 70, 150, 30);
        panel.add(btnRegresar);

        btnRegresar.addActionListener(e -> {
            try {
                int prestamoId = Integer.parseInt(txtPrestamoId.getText());
                prestamoService.registrarDevolucion(prestamoId, 1.0); // Example: daily rate = 1.0
                JOptionPane.showMessageDialog(panel, "Material regresado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(panel, "El ID del préstamo debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(panel, "Error al regresar material: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
