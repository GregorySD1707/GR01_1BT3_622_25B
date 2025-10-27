package com.sistema_financiero_personal.auth;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filtro que protege las rutas que requieren autenticación
 * Si el usuario no está logueado, lo redirige al login
 */
@WebFilter(urlPatterns = {
        "/dashboard/*",
        "/recordatorios/*",
        "/resumen_financiero/*",
        "/movimientos/*",
        "/obligacion_financiera/*"
})
public class FiltroAutenticacion implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización si es necesaria
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Obtener la sesión (sin crear una nueva)
        HttpSession session = httpRequest.getSession(false);

        // Verificar si el usuario está logueado
        boolean usuarioLogueado = (session != null && session.getAttribute("usuario") != null);

        if (usuarioLogueado) {
            // Usuario autenticado, continuar con la petición
            chain.doFilter(request, response);
        } else {
            // Usuario NO autenticado, redirigir al login
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/ingreso");
        }
    }

    @Override
    public void destroy() {
        // Limpieza si es necesaria
    }
}