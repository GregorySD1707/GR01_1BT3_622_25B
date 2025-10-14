package com.sistema_financiero_personal.resumen_financiero.controladores;

import com.sistema_financiero_personal.resumen_financiero.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.DocumentoPDF;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Anotación para indicar al servidor que este servlet responderá a las peticiones a la URL /descargarPDF
@WebServlet(urlPatterns = {"/resumen_financiero/descargarPDF"})
public class ServletDescargaPDF extends HttpServlet { // Hereda de la clase base que maneja peticiones HTTP
    private DAODocumentoPDF DAODocumentoPDF;

    @Override
    public void init() throws ServletException { // Se ejecuta UNA SOLA VEZ cuando el servlet se carga por primera vez
        super.init();
        this.DAODocumentoPDF = new DAODocumentoPDF();
    }

    // HttpServletRequest request -> Contiene toda la información de la petición (parámetros, headers, etc.)
    // HttpServletResponse response -> Se usa para enviar la respuesta al navegador
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long id = obtenerID(request, response);
            if (id == null) return;

            DocumentoPDF documento = DAODocumentoPDF.buscarPorId(id);
            if (documento == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "PDF no encontrado");
                return;
            }

            // Configurar respuesta HTTP
            configurarRespuestaDeDescarga(response, documento);

            // Escribir el PDF en la respuesta
            escribirContenidoDelArchivoEnLaRespuesta(response, documento);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error al descargar el PDF");
        }
    }

    private void configurarRespuestaDeDescarga(HttpServletResponse response, DocumentoPDF documento) throws IOException {
        // Configurar headers para descarga
        response.setContentType("application/pdf"); // Le dice al navegador que es un PDF

        response.setHeader(
                "Content-Disposition",
                // "attachment" fuerza la descarga en lugar de mostrar en el navegador
                "attachment; filename=\"" + sanitizarNombreArchivo(documento.getNombre())  + "\"");

        response.setContentLength(documento.getArchivoPdf().length); // tamaño del archivo

        // Headers de seguridad
        response.setHeader("X-Content-Type-Options", "nosniff"); // OBLIGATORIAMENTE el navegador usa el Content-Type que le envíe para que no decida por él mismo el tipo
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // No guardar en caché
    }

    private static void escribirContenidoDelArchivoEnLaRespuesta(HttpServletResponse response, DocumentoPDF documento) throws IOException {
        try (ServletOutputStream out = response.getOutputStream()){
            out.write(documento.getArchivoPdf()); // Escribe los bytes del PDF
            out.flush(); // Asegura que se envía completamente
        }
    }

    private String sanitizarNombreArchivo(String nombre) {
        if (nombre == null) return "documento.pdf";

        // Remover caracteres peligrosos
        // Reemplazar lo que no sea letras o números por "_"
        return nombre.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private Long obtenerID(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String idParam = request.getParameter("id");  // Si la URL es /descargarPDF?id=123, idParam será 123

        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID no proporcionado");
            return null;
        }

        Long id = Long.parseLong(idParam);
        return id;
    }
}
