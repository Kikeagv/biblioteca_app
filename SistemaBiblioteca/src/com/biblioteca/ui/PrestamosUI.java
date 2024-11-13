package com.biblioteca.ui;

import com.biblioteca.service.PrestamoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PrestamosUI {
    private PrestamoService prestamoService = new PrestamoService();
    private JTable materialTable;
    private JTable userTable;
    private int materialId = -1; // Material ID selected
    private int userId = -1; // User ID selected


    public void mostrarVentana() {
        JFrame frame = new JFrame("Gestión de Préstamos");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        frame.add(mainPanel);

        colocarComponentes(mainPanel, frame);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void colocarComponentes(JPanel mainPanel, JFrame frame) {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel materialPanel = new JPanel(new BorderLayout());
        colocarListaMateriales(materialPanel);
        tabbedPane.addTab("Materiales Disponibles", materialPanel);

        JPanel userPanel = new JPanel(new BorderLayout());
        colocarListaUsuarios(userPanel);
        tabbedPane.addTab("Usuarios", userPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPrestar = new JButton("Registrar Préstamo");
        JButton btnCancelar = new JButton("Cancelar");

        btnPrestar.addActionListener(e -> registrarPrestamo(frame));
        btnCancelar.addActionListener(e -> frame.dispose());

        footer.add(btnPrestar);
        footer.add(btnCancelar);
        mainPanel.add(footer, BorderLayout.SOUTH);
    }

    private void colocarListaMateriales(JPanel materialPanel) {
        JPanel searchPanel = new JPanel(new BorderLayout());
        JTextField txtSearch = new JTextField();
        JButton btnSearch = new JButton("Buscar");

        searchPanel.add(txtSearch, BorderLayout.CENTER);
        searchPanel.add(btnSearch, BorderLayout.EAST);

        DefaultTableModel materialsModel = new DefaultTableModel(new Object[]{"ID", "Tipo", "Título", "Unidades Disponibles"}, 0);
        materialTable = new JTable(materialsModel);
        JScrollPane scrollPane = new JScrollPane(materialTable);

        materialPanel.add(searchPanel, BorderLayout.NORTH);
        materialPanel.add(scrollPane, BorderLayout.CENTER);

        List<String[]> materiales = prestamoService.obtenerMaterialesDisponibles("");
        actualizarTablaMateriales(materialsModel, materiales);

        btnSearch.addActionListener(e -> {
            String criterio = txtSearch.getText().trim();
            List<String[]> filtrados = prestamoService.obtenerMaterialesDisponibles(criterio);
            actualizarTablaMateriales(materialsModel, filtrados);
        });

        materialTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = materialTable.getSelectedRow();
            if (selectedRow != -1) {
                materialId = Integer.parseInt(materialsModel.getValueAt(selectedRow, 0).toString());
            }
        });
    }

    private void colocarListaUsuarios(JPanel userPanel) {
        DefaultTableModel usersModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Estado"}, 0);
        userTable = new JTable(usersModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        userPanel.add(scrollPane, BorderLayout.CENTER);

        List<String[]> usuarios = prestamoService.obtenerUsuarios();
        actualizarTablaUsuarios(usersModel, usuarios);

        userTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow != -1) {
                userId = Integer.parseInt(usersModel.getValueAt(selectedRow, 0).toString());
            }
        });
    }

    private void actualizarTablaMateriales(DefaultTableModel model, List<String[]> data) {
        model.setRowCount(0);
        for (String[] row : data) {
            model.addRow(row);
        }
    }

    private void actualizarTablaUsuarios(DefaultTableModel model, List<String[]> data) {
        model.setRowCount(0);
        for (String[] row : data) {
            model.addRow(new Object[]{row[0], row[1], row[2].equals("Bloqueado") ? "Bloqueado" : "Activo"});
        }
    }

    private void registrarPrestamo(JFrame frame) {
        if (materialId == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un material.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (userId == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            prestamoService.registrarPrestamo(userId, materialId);
            JOptionPane.showMessageDialog(frame, "Préstamo registrado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            frame.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Error al registrar el préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
