package com.biblioteca.main;

import com.biblioteca.ui.LoginUI;

public class MainApp {

    public static void main(String[] args) {
        // Mostrar la interfaz de inicio de sesión
        LoginUI loginUI = new LoginUI();
        loginUI.mostrarLogin(); // Esto redirigirá al menú principal si el inicio de sesión es exitoso.
    }
}
