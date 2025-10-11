package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.modelos.DocumentoPDF;
import com.sistema_financiero_personal.modelos.ResumenFinanciero;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/consultarResumenes"})

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
            List<ResumenFinanciero> resumenes = DAOResumenFinanciero.listar();

            // Para cada resumen, obtener información del PDF
            List<DocumentoPDF> documentos = new ArrayList<>();
            for (ResumenFinanciero resumen : resumenes) {
                DocumentoPDF doc = DAODocumentoPDF.buscarPorId((long) resumen.getDocumentoPDFId());
                documentos.add(doc);
                System.out.println(resumen);
            }

            mostrarInformacionDeLosResumenesFinancieros(request, response, resumenes, documentos);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al consultar resúmenes: " + e.getMessage());
            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
        }
    }

    private static void mostrarInformacionDeLosResumenesFinancieros(HttpServletRequest request, HttpServletResponse response, List<ResumenFinanciero> resumenes, List<DocumentoPDF> documentos) throws ServletException, IOException {
        // Enviar al JSP
        request.setAttribute("ResumenesFinancieros", resumenes);
        request.setAttribute("DocumentosPDF", documentos); // Nueva lista
        request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
    }

}
