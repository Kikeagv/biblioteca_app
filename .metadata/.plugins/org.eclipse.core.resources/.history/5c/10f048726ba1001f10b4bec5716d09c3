package com.biblioteca.ui;

import com.biblioteca.dao.EjemplarDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BuscarMaterialesUI {
    private EjemplarDAO ejemplarDAO = new EjemplarDAO();

    public void mostrarVentana() {
        JFrame frame = new JFrame("Buscar Materiales Disponibles");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Materiales Disponibles para Préstamo", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        frame.add(titulo, BorderLayout.NORTH);

        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        JLabel lblBuscar = new JLabel("Buscar por título:");
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar");

        estilizarBoton(btnBuscar, new Color(40, 167, 69), Color.WHITE);

        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);
        frame.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = {"ID", "Tipo", "Título", "Unidades", "Ubicación"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaResultados = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaResultados);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSeleccionar = new JButton("Prestar Material");
        estilizarBoton(btnSeleccionar, new Color(59, 89, 182), Color.WHITE);

        panelInferior.add(btnSeleccionar);
        frame.add(panelInferior, BorderLayout.SOUTH);

        btnBuscar.addActionListener(e -> {
            String criterio = txtBuscar.getText();
            modeloTabla.setRowCount(0);

            List<String[]> resultados = ejemplarDAO.buscarMaterialesDisponibles(criterio);
            for (String[] fila : resultados) {
                modeloTabla.addRow(fila);
            }
        });

        btnSeleccionar.addActionListener(e -> {
            int filaSeleccionada = tablaResultados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(frame, "Seleccione un material.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int materialId = Integer.parseInt((String) modeloTabla.getValueAt(filaSeleccionada, 0));
            frame.dispose();
            new HacerPrestamoUI(materialId).mostrarVentana();
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
