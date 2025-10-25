package com.sistema_financiero_personal.resumen_financiero.controladores;

import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;
import com.sistema_financiero_personal.resumen_financiero.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/resumen_financiero/consultarResumenes"})

public class ServletConsultaResumenesFinancieros extends HttpServlet {

    private DAOResumenFinanciero DAOResumenFinanciero;
    private static final String PATH = "/resumen_financiero/VistaResumenFinanciero.jsp";

    public ServletConsultaResumenesFinancieros(){
        DAOResumenFinanciero = new DAOResumenFinanciero();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        try {
            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "ingreso");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");
            MensajeUtil.obtenerYLimpiarMensajes(request);
            // Obtener todos los resumenes de la base de datos
            List<ResumenFinanciero> resumenes = DAOResumenFinanciero.listarConDocumentosPDF(usuario.getId());

            mostrarInformacionDeLosResumenesFinancieros(request, response, resumenes);
        } catch (Exception e) {
            e.printStackTrace();
            MensajeUtil.agregarError(session, "Error al consultar resúmenes: " + e.getMessage());
            request.getRequestDispatcher(PATH).forward(request, response);
        }
    }

    private static void mostrarInformacionDeLosResumenesFinancieros(HttpServletRequest request, HttpServletResponse response, List<ResumenFinanciero> resumenes) throws ServletException, IOException {
        // Enviar al JSP
        request.setAttribute("ResumenesFinancieros", resumenes);
        request.getRequestDispatcher(PATH).forward(request, response);
    }

}
