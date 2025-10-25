package com.sistema_financiero_personal.auth;

import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioRegistroUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/ingreso")
public class ServletLogInUsuario extends HttpServlet {
    private ServicioRegistroUsuario servicioRegistroUsuario;
    public ServletLogInUsuario() {
        this.servicioRegistroUsuario = new ServicioRegistroUsuario();
    }
    private static final String PATH = "auth/ingreso/VistaIngreso.jsp";
    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("usuario") != null) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String identificadorUsuario = request.getParameter("identificadorUsuario");
        String contrasena = request.getParameter("contrasena");

        if (identificadorUsuario == null || identificadorUsuario.trim().isEmpty() ||
                contrasena == null || contrasena.trim().isEmpty()) {
            request.setAttribute("error", "Por favor, complete todos los campos.");
            request.getRequestDispatcher(PATH).forward(request, response);
            return;
        }
        try {
            Usuario usuario = servicioRegistroUsuario.autenticarUsuario(identificadorUsuario, contrasena);
            if(usuario!=null){
                HttpSession session = request.getSession();
                session.setAttribute("usuario", usuario);
                response.sendRedirect(request.getContextPath() + "/inicio");
            } else {
                request.setAttribute("error", "Identificador de usuario o contraseña incorrectos.");
                request.getRequestDispatcher(PATH).forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Ocurrió un error durante el inicio de sesión. Por favor, inténtelo de nuevo.");
            request.getRequestDispatcher(PATH).forward(request, response);
        }

    }

}
