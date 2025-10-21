package com.sistema_financiero_personal.auth;

import com.sistema_financiero_personal.usuario.servicios.ServicioRegistroUsuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@WebServlet("/registro")
public class ServletRegistroUsuario extends HttpServlet {
    private ServicioRegistroUsuario servicioRegistroUsuario;
    private static final String PATH = "auth/registro/VistaRegistro.jsp";
    @Override
    public void init() throws ServletException {
        super.init();
        this.servicioRegistroUsuario = new ServicioRegistroUsuario();
    }

    @Override
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response) throws jakarta.servlet.ServletException, java.io.IOException {
        request.getRequestDispatcher(PATH).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String correo = request.getParameter("correo");
            String nombreUsuario = request.getParameter("nombreUsuario");
            String contrasena = request.getParameter("contrasena");
            String confirmarContrasena = request.getParameter("confirmarContrasena");
            String fechaNacimientoStr = request.getParameter("fechaNacimiento");

            if (!contrasena.equals(confirmarContrasena)) {
                request.setAttribute("error", "Las contraseñas no coinciden");
                request.getRequestDispatcher(PATH).forward(request, response);
                return;
            }

            servicioRegistroUsuario.registrarUsuario(
                    nombre,
                    apellido,
                    correo,
                    nombreUsuario,
                    contrasena,
                    LocalDate.parse(fechaNacimientoStr)
            );

            // Registro exitoso
            request.setAttribute("success", "¡Cuenta creada exitosamente! Ya puedes iniciar sesión.");
            HttpSession session = request.getSession();
            session.setAttribute("usuario", nombreUsuario);
            response.sendRedirect(request.getContextPath() + "/inicio");

        } catch (DateTimeParseException e) {
            request.setAttribute("error", "La fecha de nacimiento no es válida");
            request.getRequestDispatcher(PATH).forward(request, response);

        } catch (IllegalArgumentException e) {
            // Capturar las validaciones del servicio
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher(PATH).forward(request, response);

        } catch (Exception e) {
            // Error inesperado
            request.setAttribute("error", "Ocurrió un error al crear la cuenta. Por favor, intenta de nuevo.");
            e.printStackTrace(); // Para debugging en consola
            request.getRequestDispatcher(PATH).forward(request, response);
        }
    }
}
