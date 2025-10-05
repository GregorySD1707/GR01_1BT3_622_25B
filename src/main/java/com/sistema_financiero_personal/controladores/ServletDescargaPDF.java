package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.daos.DocumentoPDFDAO;
import com.sistema_financiero_personal.modelos.DocumentoPDF;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/descargarPDF")
public class ServletDescargaPDF extends HttpServlet {
    private DocumentoPDFDAO documentoPDFDAO = new DocumentoPDFDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado");
                return;
            }

            Long id = Long.parseLong(idParam);
            DocumentoPDF documento = documentoPDFDAO.buscarPorId((long) id.intValue());

            if (documento == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "PDF no encontrado");
                return;
            }

            // Configurar headers para descarga
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"" + documento.getNombre() + "\"");
            response.setContentLength(documento.getArchivoPdf().length);

            // Escribir el PDF en la respuesta
            ServletOutputStream out = response.getOutputStream();
            out.write(documento.getArchivoPdf());
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al descargar el PDF");
        }
    }
}
