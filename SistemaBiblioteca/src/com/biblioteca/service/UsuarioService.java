package com.biblioteca.service;

import com.biblioteca.dao.UsuarioDAO;

import java.util.List;

public class UsuarioService {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void registrarUsuario(String nombre, String username, String password, String correo, String telefono, String tipo) {
        usuarioDAO.registrarUsuario(nombre, username, password, correo, telefono, tipo);
    }

    public List<String[]> obtenerUsuarios() {
        return usuarioDAO.obtenerUsuarios();
    }

    public boolean login(String username, String password) {
        return usuarioDAO.validarCredenciales(username, password);
    }

    public void actualizarContrasena(String username, String nuevaContrasena) {
        usuarioDAO.actualizarContrasena(username, nuevaContrasena);
    }
    
    public void eliminarUsuario(String username) {
        usuarioDAO.eliminarUsuario(username); // Llamada al DAO
    }
}
