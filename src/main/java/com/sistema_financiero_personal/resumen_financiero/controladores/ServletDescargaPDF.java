package com.sistema_financiero_personal.resumen_financiero.controladores;

import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;
import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.modelos.DocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(urlPatterns = {"/resumen_financiero/descargarPDF"})
public class ServletDescargaPDF extends HttpServlet {

    private DAOResumenFinanciero daoResumenFinanciero;

    @Override
    public void init() throws ServletException {
        super.init();
        this.daoResumenFinanciero = new DAOResumenFinanciero();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Validar que el usuario esté autenticado
        HttpSession session = request.getSession(false);
        try {

            if (session == null || session.getAttribute("usuario") == null) {
                response.sendRedirect(request.getContextPath() + "/ingreso");
                return;
            }

            Usuario usuario = (Usuario) session.getAttribute("usuario");

            // Obtener ID del resumen financiero
            Long resumenId = obtenerIdDelResumen(request, response);
            if (resumenId == null){
                MensajeUtil.agregarError(session, "ID inválido");
                return;
            }

            // CRÍTICO: Verificar que el resumen pertenezca al usuario
            ResumenFinanciero resumen = daoResumenFinanciero.buscarPorIdYUsuario(resumenId, usuario.getId());
            if (resumen == null) {
                MensajeUtil.agregarError(session, "No tiene permisos para acceder a este documento");
                response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
                return;
            }

            DocumentoPDF documento = resumen.getDocumentoPDF();
            if (documento == null || documento.getArchivoPdf() == null) {
                MensajeUtil.agregarError(session, "PDF no encontrado");
                response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
                return;
            }

            // Configurar respuesta HTTP
            configurarRespuestaDeDescarga(response, documento);

            // Escribir el PDF en la respuesta
            escribirContenidoDelArchivoEnLaRespuesta(response, documento);

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "ID inválido");
            response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
        } catch (Exception e) {
            System.err.println("Error al descargar el PDF: " + e.getMessage());
            e.printStackTrace();
            MensajeUtil.agregarError(session, "Error al procesar la descarga del PDF");
            response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
        }
    }

    /**
     * Configura los headers HTTP para la descarga del PDF
     */
    private void configurarRespuestaDeDescarga(HttpServletResponse response, DocumentoPDF documento) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + sanitizarNombreArchivo(documento.getNombre()) + "\"");
        response.setContentLength(documento.getArchivoPdf().length);

        // Headers de seguridad
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

    /**
     * Escribe el contenido del PDF en la respuesta HTTP
     */
    private void escribirContenidoDelArchivoEnLaRespuesta(HttpServletResponse response, DocumentoPDF documento)
            throws IOException {
        try (ServletOutputStream out = response.getOutputStream()) {
            out.write(documento.getArchivoPdf());
            out.flush();
        }
    }

    /**
     * Sanitiza el nombre del archivo removiendo caracteres peligrosos
     */
    private String sanitizarNombreArchivo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "documento.pdf";
        }

        // Remover caracteres peligrosos y espacios
        String nombreSanitizado = nombre.replaceAll("[^a-zA-Z0-9._-]", "_");

        // Asegurar que termine en .pdf
        if (!nombreSanitizado.toLowerCase().endsWith(".pdf")) {
            nombreSanitizado += ".pdf";
        }

        return nombreSanitizado;
    }

    /**
     * Obtiene y valida el ID del resumen financiero desde los parámetros
     */
    private Long obtenerIdDelResumen(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("resumenId");

        if (idParam == null || idParam.trim().isEmpty()) {
            return null;
        }

        try {
            return Long.parseLong(idParam);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}