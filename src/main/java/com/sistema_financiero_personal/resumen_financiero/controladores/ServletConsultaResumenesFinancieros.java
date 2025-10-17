package com.sistema_financiero_personal.resumen_financiero.controladores;

import com.sistema_financiero_personal.resumen_financiero.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/resumen_financiero/consultarResumenes"})

public class ServletConsultaResumenesFinancieros extends HttpServlet {

    private DAOResumenFinanciero DAOResumenFinanciero;
    private DAODocumentoPDF DAODocumentoPDF;

    public ServletConsultaResumenesFinancieros(){
        DAOResumenFinanciero = new DAOResumenFinanciero();
        DAODocumentoPDF = new DAODocumentoPDF();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Obtener todos los resumenes de la base de datos
            List<ResumenFinanciero> resumenes = DAOResumenFinanciero.listarConDocumentosPDF();

            mostrarInformacionDeLosResumenesFinancieros(request, response, resumenes);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al consultar resúmenes: " + e.getMessage());
            request.getRequestDispatcher("/resumen_financiero/VistaResumenFinanciero.jsp").forward(request, response);
        }
    }

    private static void mostrarInformacionDeLosResumenesFinancieros(HttpServletRequest request, HttpServletResponse response, List<ResumenFinanciero> resumenes) throws ServletException, IOException {
        // Enviar al JSP
        request.setAttribute("ResumenesFinancieros", resumenes);
        request.getRequestDispatcher("/resumen_financiero/VistaResumenFinanciero.jsp").forward(request, response);
    }

}
