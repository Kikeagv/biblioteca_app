package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biblioteca.conexion.ConexionBD;

public class PrestamoDAO {
	
	public List<Map<String, String>> obtenerPrestamosActivos(String username) {
	    String sql = "SELECT p.id, m.titulo AS material, p.fecha_prestamo, p.estado " +
	                 "FROM prestamos p " +
	                 "JOIN materiales m ON p.id_material = m.id " +
	                 "WHERE p.id_usuario = (SELECT id FROM usuarios WHERE username = ?) " +
	                 "AND p.estado = 'Pendiente'";

	    List<Map<String, String>> prestamos = new ArrayList<>();
	    try (Connection con = ConexionBD.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, username);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, String> prestamo = new HashMap<>();
	                prestamo.put("id", String.valueOf(rs.getInt("id")));
	                prestamo.put("material", rs.getString("material"));
	                prestamo.put("fecha_prestamo", rs.getString("fecha_prestamo"));
	                prestamo.put("estado", rs.getString("estado"));
	                prestamos.add(prestamo);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return prestamos;
	}


	public boolean tienePrestamosDefaulter(String username) {
		String sql = "SELECT COUNT(*) AS prestamos_vencidos " +
	             "FROM prestamos p " +
	             "WHERE p.id_usuario = (SELECT id FROM usuarios WHERE username = ?) " +
	             "AND p.estado = 'Pendiente' " +
	             "AND DATE_ADD(p.fecha_prestamo, INTERVAL 15 DAY) < NOW()";


	    try (Connection con = ConexionBD.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, username);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                int prestamosVencidos = rs.getInt("prestamos_vencidos");
	                return prestamosVencidos > 0; // True if there are overdue loans
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
	


	
    // Validate if a user has outstanding penalties or is inactive
	public boolean validarUsuario(String username) {
	    String sql = "SELECT COUNT(*) AS prestamos_activos " +
	                 "FROM prestamos " +
	                 "WHERE id_usuario = (SELECT id FROM usuarios WHERE username = ?) " +
	                 "AND estado = 'Pendiente'";

	    try (Connection con = ConexionBD.conectar();
	         PreparedStatement ps = con.prepareStatement(sql)) {
	        ps.setString(1, username);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("prestamos_activos") == 0; // User has no active loans
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

    // Validate if there are enough copies available for a loan
    public boolean validarDisponibilidadMaterial(int ejemplarId) {
        String sql = "SELECT unidades_disponibles FROM materiales WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ejemplarId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int disponibles = rs.getInt("unidades_disponibles");
                    return disponibles > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Register a new loan
    public void registrarPrestamo(String username, int ejemplarId) {
    	String sql = "INSERT INTO prestamos (id_usuario, id_material, fecha_prestamo, estado) " +
                "VALUES ((SELECT id FROM usuarios WHERE username = ?), ?, NOW(), 'Pendiente')";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setInt(2, ejemplarId);
            ps.executeUpdate();
            actualizarUnidades(ejemplarId, -1); // Deduct one copy
            System.out.println("Préstamo registrado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Register a material return
    public void registrarDevolucion(int prestamoId) {
    	String sql = "UPDATE prestamos SET estado = 'Devuelto', fecha_devolucion = NOW() WHERE id = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prestamoId);
            ps.executeUpdate();

            // Restore the copy count
            String sqlMaterial = "SELECT id_material FROM prestamos WHERE id = ?";
            try (PreparedStatement psMaterial = con.prepareStatement(sqlMaterial)) {
                psMaterial.setInt(1, prestamoId);
                try (ResultSet rs = psMaterial.executeQuery()) {
                    if (rs.next()) {
                        int ejemplarId = rs.getInt("id_material");
                        actualizarUnidades(ejemplarId, 1);
                    }
                }
            }
            System.out.println("Devolución registrada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Calculate overdue penalties
    public double calcularMora(int prestamoId, double tasaDiaria) {
        String sql = "SELECT DATEDIFF(NOW(), fecha_prestamo) AS dias FROM prestamos WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prestamoId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int dias = rs.getInt("dias");
                    return dias > 0 ? dias * tasaDiaria : 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Register overdue penalties
    public void registrarMora(int prestamoId, double mora) {
        String sql = "INSERT INTO multas (id_prestamo, monto, pagada, fecha_generacion) VALUES (?, ?, false, NOW())";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prestamoId);
            ps.setDouble(2, mora);
            ps.executeUpdate();

            // Update user's accumulated mora
            String sqlUsuario = "UPDATE usuarios SET mora_acumulada = mora_acumulada + ? " +
                                "WHERE id = (SELECT id_usuario FROM prestamos WHERE id = ?)";
            try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario)) {
                psUsuario.setDouble(1, mora);
                psUsuario.setInt(2, prestamoId);
                psUsuario.executeUpdate();
            }
            System.out.println("Mora registrada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update the number of available copies for a material
    public void actualizarUnidades(int ejemplarId, int cambio) {
        String sql = "UPDATE materiales SET unidades_disponibles = unidades_disponibles + ? WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cambio);
            ps.setInt(2, ejemplarId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean validarLimitePrestamos(String username, String tipoMaterial) {
        String sqlLimite = "SELECT max_prestamos FROM configuracion_prestamos WHERE tipo_material = ?";
        String sqlConteo = "SELECT COUNT(*) AS prestamos_activos " +
                "FROM prestamos p " +
                "JOIN materiales m ON p.id_material = m.id " +
                "WHERE p.estado = 'Pendiente' AND m.tipo = ? AND p.id_usuario = " +
                "(SELECT id FROM usuarios WHERE username = ?)";


        try (Connection con = ConexionBD.conectar();
             PreparedStatement psLimite = con.prepareStatement(sqlLimite);
             PreparedStatement psConteo = con.prepareStatement(sqlConteo)) {

            // Fetch loan limit for material type
            psLimite.setString(1, tipoMaterial);
            ResultSet rsLimite = psLimite.executeQuery();
            int maxPrestamos = 0;
            if (rsLimite.next()) {
                maxPrestamos = rsLimite.getInt("max_prestamos");
            }

            // Count active loans for the user and material type
            psConteo.setString(1, tipoMaterial);
            psConteo.setString(2, username);
            ResultSet rsConteo = psConteo.executeQuery();
            int prestamosActivos = 0;
            if (rsConteo.next()) {
                prestamosActivos = rsConteo.getInt("prestamos_activos");
            }

            // Validate limit
            return prestamosActivos < maxPrestamos;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<Map<String, String>> obtenerTodosPrestamos() {
        String sql = "SELECT p.id, u.username AS usuario, m.titulo AS material, " +
                     "p.fecha_prestamo, p.estado " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id " +
                     "JOIN materiales m ON p.id_material = m.id";

        List<Map<String, String>> prestamos = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, String> prestamo = new HashMap<>();
                prestamo.put("id", String.valueOf(rs.getInt("id")));
                prestamo.put("usuario", rs.getString("usuario"));
                prestamo.put("material", rs.getString("material"));
                prestamo.put("fecha_prestamo", rs.getString("fecha_prestamo"));
                prestamo.put("estado", rs.getString("estado"));
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }
    
    public List<Map<String, String>> obtenerPrestamosPendientes(String username) {
        String sql = "SELECT p.id, u.username AS usuario, m.titulo AS material, " +
                     "p.fecha_prestamo " +
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id " +
                     "JOIN materiales m ON p.id_material = m.id " +
                     "WHERE p.estado = 'Pendiente'";

        if (username != null) {
            sql += " AND u.username = ?";
        }

        List<Map<String, String>> prestamos = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (username != null) {
                ps.setString(1, username);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> prestamo = new HashMap<>();
                    prestamo.put("id", String.valueOf(rs.getInt("id")));
                    prestamo.put("usuario", rs.getString("usuario"));
                    prestamo.put("material", rs.getString("material"));
                    prestamo.put("fecha_prestamo", rs.getString("fecha_prestamo"));
                    prestamos.add(prestamo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    public List<Map<String, String>> obtenerPrestamosPendientesConMora(String username) {
        String sql = "SELECT p.id, u.username AS usuario, m.titulo AS material, " +
                     "p.fecha_prestamo, p.mora_acumulada " + // Fetch mora_acumulada directly
                     "FROM prestamos p " +
                     "JOIN usuarios u ON p.id_usuario = u.id " +
                     "JOIN materiales m ON p.id_material = m.id " +
                     "WHERE p.estado = 'Pendiente'";

        if (username != null) {
            sql += " AND u.username = ?";
        }

        List<Map<String, String>> prestamos = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (username != null) {
                ps.setString(1, username);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, String> prestamo = new HashMap<>();
                    prestamo.put("id", String.valueOf(rs.getInt("id")));
                    prestamo.put("usuario", rs.getString("usuario"));
                    prestamo.put("material", rs.getString("material"));
                    prestamo.put("fecha_prestamo", rs.getString("fecha_prestamo"));
                    prestamo.put("mora_acumulada", String.format("%.2f", rs.getDouble("mora_acumulada"))); // Mora
                    prestamos.add(prestamo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    
    public void agregarMora(int prestamoId, double montoDiario) {
        String sql = "UPDATE prestamos SET mora_acumulada = mora_acumulada + ? WHERE id = ?";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, montoDiario);
            ps.setInt(2, prestamoId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void registrarPrestamo(int userId, int materialId) {
        String sql = "INSERT INTO prestamos (id_usuario, id_material, fecha_prestamo, estado) " +
                     "VALUES (?, ?, NOW(), 'Pendiente')";
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, materialId);
            ps.executeUpdate();
            actualizarUnidades(materialId, -1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<String[]> buscarMaterialesDisponibles(String criterio) {
        String sql = "SELECT id, tipo, titulo, unidades_disponibles FROM materiales WHERE unidades_disponibles > 1 AND (titulo LIKE ? OR tipo LIKE ?)";
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
                        String.valueOf(rs.getInt("unidades_disponibles"))
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return materiales;
    }

    public List<String[]> obtenerUsuarios() {
        String sql = "SELECT id, nombre, (CASE WHEN EXISTS " +
                     "(SELECT 1 FROM prestamos WHERE id_usuario = usuarios.id AND estado = 'Pendiente' AND DATE_ADD(fecha_prestamo, INTERVAL 15 DAY) < NOW()) " +
                     "THEN 'Bloqueado' ELSE 'Activo' END) AS estado " +
                     "FROM usuarios";
        List<String[]> usuarios = new ArrayList<>();
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("nombre"),
                    rs.getString("estado")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }





}
