package com.sistema_financiero_personal.comun.dashboard;

import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet que maneja la lógica del Dashboard
 */
@WebServlet(name = "ServletDashboard", urlPatterns = {"/dashboard"})
public class ServletDashboard extends HttpServlet {

    private ServicioDashboard servicioDashboard;

    @Override
    public void init() throws ServletException {
        this.servicioDashboard = new ServicioDashboard();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Verificar sesión y obtener usuario
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ingreso");
            return;
        }

        Long usuarioId = usuario.getId();

        try {
            // 2. Obtener datos del dashboard
            DatosDashboard datos = servicioDashboard.obtenerResumen(usuarioId);

            // 3. Limpiar mensajes previos
            MensajeUtil.obtenerYLimpiarMensajes(request);

            // 4. Almacenar en request
            request.setAttribute("dashboard", datos);
            request.setAttribute("estatus", datos.getEstatus());

            // 5. Forward al JSP único
            request.getRequestDispatcher("/comun/dashboard/Dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            // Log del error (en producción usar un logger apropiado)
            System.err.println("Error en ServletDashboard: " + e.getMessage());
            e.printStackTrace();

            // Crear un objeto de datos con error para mostrar en el JSP
            DatosDashboard datosError = new DatosDashboard(EstatusDashboard.SIN_CUENTAS);

            request.setAttribute("dashboard", datosError);
            request.setAttribute("estatus", EstatusDashboard.SIN_CUENTAS);
            request.setAttribute("mensajeError", "Error al cargar el dashboard");

            request.getRequestDispatcher("/comun/dashboard/Dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // El dashboard solo responde a GET
        doGet(request, response);
    }

    /**
     * Obtiene el usuario de la sesión actual
     */
    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Usuario) session.getAttribute("usuario");
        }
        return null;
    }

    // Método para testing - permite inyectar un servicio mock
    protected void setServicioDashboard(ServicioDashboard servicioDashboard) {
        this.servicioDashboard = servicioDashboard;
    }
}