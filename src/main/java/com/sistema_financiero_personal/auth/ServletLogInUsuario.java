package com.sistema_financiero_personal.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ingreso")
public class ServletLogInUsuario extends HttpServlet {
    private static final String PATH = "auth/ingreso/VistaIngreso.jsp";
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nombreUsuario = request.getParameter("identificadorUsuario");
        String contrasena = request.getParameter("contrasena");

        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {

            request.setAttribute("error", "Por favor complete todos los campos");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;

            // logica con DAO
            // boolean exitoso = validarCredenciales(nombreUsuario, contrasena);
        }
    }

}
