package com.biblioteca.dao;

import com.biblioteca.conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void registrarUsuario(String nombre, String username, String password, String correo, String telefono, String tipo) {
        String sql = "INSERT INTO usuarios (nombre, username, password, correo, telefono, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nombre);
            ps.setString(2, username);
            ps.setString(3, password);
            ps.setString(4, correo);
            ps.setString(5, telefono);
            ps.setString(6, tipo);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> obtenerUsuarios() {
        String sql = "SELECT nombre, username, tipo, correo, telefono, estado FROM usuarios";
        List<String[]> usuarios = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new String[]{
                    rs.getString("nombre"),
                    rs.getString("username"),
                    rs.getString("tipo"),
                    rs.getString("correo"),
                    rs.getString("telefono"),
                    rs.getString("estado")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    public boolean validarCredenciales(String username, String password) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE username = ? AND password = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void actualizarContrasena(String username, String nuevaContrasena) {
        String sql = "UPDATE usuarios SET password = ? WHERE username = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevaContrasena);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarUsuario(String username) {
        String sql = "DELETE FROM usuarios WHERE username = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas == 0) {
                System.out.println("Advertencia: No se encontró un usuario con el username proporcionado.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<String[]> obtenerUsuariosSinAtrasos() {
        String sql = "SELECT u.id, u.nombre, u.username " +
                     "FROM usuarios u " +
                     "WHERE NOT EXISTS ( " +
                     "SELECT 1 FROM prestamos p " +
                     "WHERE p.id_usuario = u.id " +
                     "AND p.estado = 'Pendiente' " +
                     "AND DATE_ADD(p.fecha_prestamo, INTERVAL 15 DAY) < NOW()" +
                     ")";
        List<String[]> usuarios = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("nombre"),
                    rs.getString("username")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }


}
