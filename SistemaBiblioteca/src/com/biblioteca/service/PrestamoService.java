package com.biblioteca.service;

import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.dao.EjemplarDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class PrestamoService {
    private PrestamoDAO prestamoDAO = new PrestamoDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private EjemplarDAO ejemplarDAO = new EjemplarDAO();

    public List<String[]> obtenerMaterialesDisponibles(String criterio) {
        return prestamoDAO.buscarMaterialesDisponibles(criterio);
    }

    // Validate if a loan can be made
    public boolean puedePrestar(String username, int materialId) {
        // No SQLException handling required
        if (!prestamoDAO.validarDisponibilidadMaterial(materialId)) {
            System.out.println("El material no está disponible.");
            return false;
        }

        if (prestamoDAO.tienePrestamosDefaulter(username)) {
            System.out.println("El usuario tiene préstamos vencidos.");
            return false;
        }

        String tipoMaterial = ejemplarDAO.obtenerTipoMaterial(materialId);
        if (!prestamoDAO.validarLimitePrestamos(username, tipoMaterial)) {
            System.out.println("El usuario alcanzó el límite de préstamos para el tipo: " + tipoMaterial);
            return false;
        }

        return true;
    }


    // Register a loan
    public void registrarPrestamo(int userId, int materialId) {
        try {
            prestamoDAO.registrarPrestamo(userId, materialId);
            System.out.println("Préstamo registrado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error al registrar el préstamo: " + e.getMessage());
        }
    }


    // Register material return
    public void registrarDevolucion(int prestamoId, double tasaDiaria) {
        double mora = prestamoDAO.calcularMora(prestamoId, tasaDiaria);
        prestamoDAO.registrarDevolucion(prestamoId);

        if (mora > 0) {
            prestamoDAO.registrarMora(prestamoId, mora);
            System.out.println("Mora registrada: $" + mora);
        } else {
            System.out.println("Devolución registrada sin mora.");
        }
    }

    // Get active loans for a user
    public List<Map<String, String>> obtenerPrestamosActivos(String username) {
        return prestamoDAO.obtenerPrestamosActivos(username);
    }

    // Get all loans
    public List<Map<String, String>> obtenerTodosPrestamos() {
        return prestamoDAO.obtenerTodosPrestamos();
    }

    // Get pending loans
    public List<Map<String, String>> obtenerPrestamosPendientes(String username) {
        return prestamoDAO.obtenerPrestamosPendientes(username);
    }

    // Get pending loans with overdue penalties
    public List<Map<String, String>> obtenerPrestamosPendientesConMora(String username) {
        return prestamoDAO.obtenerPrestamosPendientesConMora(username);
    }

    // Add overdue penalties
    public void agregarMora(int prestamoId, double montoDiario) {
        prestamoDAO.agregarMora(prestamoId, montoDiario);
    }
    
    public List<String[]> obtenerUsuarios() {
        return prestamoDAO.obtenerUsuarios();
    }
}
