package com.sistema_financiero_personal.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/LogInUsuario")
public class ServletLogInUsuario extends HttpServlet {
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
        request.getRequestDispatcher("auth/logIn/VistaLogIn.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombreUsuario = request.getParameter("identificadorUsuario");
        String contrasena = request.getParameter("contrasena");

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {

            request.setAttribute("error", "Por favor complete todos los campos");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;

            // logica con DAO
            // boolean exitoso = validarCredenciales(nombreUsuario, contrasena);
        }
    }

}
