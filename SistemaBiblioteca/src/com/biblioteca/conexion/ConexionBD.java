package com.biblioteca.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca"; // Cambia 'biblioteca' por el nombre de tu base de datos
    private static final String USER = "root"; // Usuario de tu base de datos
    private static final String PASSWORD = "123456"; // Contraseña de tu base de datos

    // Método para establecer la conexión
    public static Connection conectar() throws SQLException {
        Connection conexion = null;
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            throw e;
        }
        return conexion;
    }
}
