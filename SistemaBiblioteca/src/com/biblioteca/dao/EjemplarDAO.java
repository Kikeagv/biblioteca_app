package com.biblioteca.dao;

import com.biblioteca.conexion.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EjemplarDAO {

	// Generar el código interno automáticamente según el tipo de material
	private String generarCodigoInterno(String tipo) throws SQLException {
	    String prefijo;
	    switch (tipo) {
	        case "Libro": prefijo = "LIB"; break;
	        case "Revista": prefijo = "REV"; break;
	        case "CD": prefijo = "CD"; break;
	        case "DVD": prefijo = "DVD"; break;
	        case "Tesis": prefijo = "TES"; break;
	        default: throw new IllegalArgumentException("Tipo desconocido: " + tipo);
	    }

	    String sql = "SELECT codigo_interno FROM materiales WHERE tipo = ? ORDER BY id DESC LIMIT 1";
	    try (Connection con = ConexionBD.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, tipo);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                String ultimoCodigo = rs.getString("codigo_interno");
	                int numero = Integer.parseInt(ultimoCodigo.substring(3)) + 1;
	                return String.format("%s%05d", prefijo, numero);
	            }
	        }
	    }
	    return String.format("%s%05d", prefijo, 1);
	}
	
    // Method to register a new material and its specific details
	public void registrarEjemplarConDetalles(String tipo, String titulo, int cantidad, String ubicacion, Map<String, String> detallesEspecificos)
 {
	    String sqlMateriales = "INSERT INTO materiales (tipo, titulo, codigo_interno, unidades_disponibles, ubicacion, fecha_registro) VALUES (?, ?, ?, ?, ?, NOW())";
	    Connection con = null;

	    try {
	        con = ConexionBD.conectar();
	        con.setAutoCommit(false);

	        // Generar el código interno automáticamente
	        String codigoInterno = generarCodigoInterno(tipo);

	        int idMaterial;
	        try (PreparedStatement psMateriales = con.prepareStatement(sqlMateriales, PreparedStatement.RETURN_GENERATED_KEYS)) {
	            psMateriales.setString(1, tipo);
	            psMateriales.setString(2, titulo);
	            psMateriales.setString(3, codigoInterno);
	            psMateriales.setInt(4, cantidad);
	            psMateriales.setString(5, ubicacion);
	            psMateriales.executeUpdate();

	            ResultSet rs = psMateriales.getGeneratedKeys();
	            if (rs.next()) {
	                idMaterial = rs.getInt(1);
	            } else {
	                throw new SQLException("Error al obtener el ID generado para el material.");
	            }
	        }

	        registrarDetallesEspecificos(con, idMaterial, tipo, detallesEspecificos);

	        con.commit();
	    } catch (SQLException e) {
	        if (con != null) {
	            try {
	                con.rollback();
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	        throw new RuntimeException("Error al registrar el material: " + e.getMessage());
	    } finally {
	        if (con != null) {
	            try {
	                con.setAutoCommit(true);
	                con.close();
	            } catch (SQLException closeEx) {
	                closeEx.printStackTrace();
	            }
	        }
	    }
	}


    // Method to handle inserting specific details based on material type
    private void registrarDetallesEspecificos(Connection con, int idMaterial, String tipo, Map<String, String> detallesEspecificos) throws SQLException {
        String sql;

        switch (tipo) {
            case "Libro":
                sql = "INSERT INTO libros (id, autor, editorial, isbn, numero_paginas, genero, anio_publicacion) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idMaterial);
                    ps.setString(2, detallesEspecificos.get("Autor"));
                    ps.setString(3, detallesEspecificos.get("Editorial"));
                    ps.setString(4, detallesEspecificos.get("ISBN"));
                    ps.setInt(5, Integer.parseInt(detallesEspecificos.get("Número de Páginas")));
                    ps.setString(6, detallesEspecificos.get("Género"));
                    ps.setInt(7, Integer.parseInt(detallesEspecificos.get("Año de Publicación")));
                    ps.executeUpdate();
                }
                break;

            case "Revista":
                sql = "INSERT INTO revistas (id, editorial, periodicidad, fecha_publicacion) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idMaterial);
                    ps.setString(2, detallesEspecificos.get("Editorial"));
                    ps.setString(3, detallesEspecificos.get("Periodicidad"));

                    // Convert and set Fecha de Publicación
                    String fecha = detallesEspecificos.get("Fecha de Publicación");
                    ps.setDate(4, java.sql.Date.valueOf(fecha)); // Use java.sql.Date for compatibility

                    ps.executeUpdate();
                }
                break;


            case "Tesis":
                sql = "INSERT INTO tesis (id, autor, titulo_investigacion, carrera, universidad, anio_presentacion) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idMaterial);
                    ps.setString(2, detallesEspecificos.get("Autor"));
                    ps.setString(3, detallesEspecificos.get("Título de Investigación"));
                    ps.setString(4, detallesEspecificos.get("Carrera"));
                    ps.setString(5, detallesEspecificos.get("Universidad"));
                    ps.setInt(6, Integer.parseInt(detallesEspecificos.get("Año de Presentación")));
                    ps.executeUpdate();
                }
                break;

            case "CD":
                sql = "INSERT INTO cds (id, artista, genero, duracion, numero_canciones) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idMaterial);
                    ps.setString(2, detallesEspecificos.get("Artista"));
                    ps.setString(3, detallesEspecificos.get("Género"));
                    ps.setTime(4, java.sql.Time.valueOf(detallesEspecificos.get("Duración")));
                    ps.setInt(5, Integer.parseInt(detallesEspecificos.get("Número de Canciones")));
                    ps.executeUpdate();
                }
                break;

            case "DVD":
                sql = "INSERT INTO dvds (id, director, genero, duracion) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, idMaterial);
                    ps.setString(2, detallesEspecificos.get("Director"));
                    ps.setString(3, detallesEspecificos.get("Género"));
                    ps.setTime(4, java.sql.Time.valueOf(detallesEspecificos.get("Duración")));
                    ps.executeUpdate();
                }
                break;

            default:
                throw new SQLException("Tipo de material desconocido: " + tipo);
        }
    }

    // Method to retrieve the list of all materials
    public List<String[]> consultarEjemplares() {
        String sql = "SELECT id, tipo, titulo, unidades_disponibles, ubicacion FROM materiales";
        List<String[]> ejemplares = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ejemplares.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("tipo"),
                        rs.getString("titulo"),
                        String.valueOf(rs.getInt("unidades_disponibles")),
                        rs.getString("ubicacion")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ejemplares;
    }
    
    public List<String[]> buscarMateriales(String criterio) {
        String sql = "SELECT id, tipo, titulo, unidades_disponibles, ubicacion FROM materiales " +
                     "WHERE titulo LIKE ? OR tipo LIKE ?";
        List<String[]> materiales = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + criterio + "%");
            ps.setString(2, "%" + criterio + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materiales.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("tipo"),
                        rs.getString("titulo"),
                        String.valueOf(rs.getInt("unidades_disponibles")),
                        rs.getString("ubicacion")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiales;
    }
    
    public List<String[]> buscarMaterialesDisponibles(String criterio) {
        String sql = "SELECT id, tipo, titulo, unidades_disponibles, ubicacion " +
                     "FROM materiales WHERE unidades_disponibles > 1 " +
                     "AND (titulo LIKE ? OR tipo LIKE ?)";
        List<String[]> materiales = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "%" + criterio + "%");
            ps.setString(2, "%" + criterio + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    materiales.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("tipo"),
                        rs.getString("titulo"),
                        String.valueOf(rs.getInt("unidades_disponibles")),
                        rs.getString("ubicacion")
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiales;
    }

    public String obtenerTipoMaterial(int materialId) {
        String sql = "SELECT tipo FROM materiales WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, materialId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("tipo");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
