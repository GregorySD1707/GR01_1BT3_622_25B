package com.sistema_financiero_personal.auth;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet("/SignUpUsuario")
public class ServletSignUpUsuario extends HttpServlet {
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
        request.getRequestDispatcher("auth/signUp/VistaSignUp.jsp").forward(request, response);
    }
}
