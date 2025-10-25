package com.sistema_financiero_personal.resumen_financiero.controladores;

import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;
import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.DocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.servicios.ServicioResumenFinanciero;
import com.sistema_financiero_personal.comun.utilidades.GestorDeArchivos;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(urlPatterns = {"/resumen_financiero/subirPDF"})
@MultipartConfig(
        fileSizeThreshold =  1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class ServletResumenFinanciero extends HttpServlet {

    private DAOResumenFinanciero DAOResumenFinanciero;
    private DAODocumentoPDF DAODocumentoPDF;
    private ServicioResumenFinanciero servicioResumenFinanciero;
    private static final String PATH = "/resumen_financiero/VistaResumenFinanciero.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        this.DAOResumenFinanciero = new DAOResumenFinanciero();
        this.DAODocumentoPDF = new DAODocumentoPDF();
        this.servicioResumenFinanciero = new ServicioResumenFinanciero();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        try {
            Usuario usuario = obtenerUsuarioSesion(request);
            if (usuario == null) {
                response.sendRedirect(request.getContextPath() + "/ingreso");
                return;
            }

            Part archivo = GestorDeArchivos.obtenerArchivo(request, response);
            if (archivo == null) {
                MensajeUtil.agregarError(session, "No se pudo cargar el archivo PDF");
                response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
                return;
            }

            // Guardar reporte en BD con su nombre y contenido en bytes
            byte[] archivoEnBytes = GestorDeArchivos.transformarArchivoABytes(archivo);
            DocumentoPDF documentoPDF = DAODocumentoPDF.guardarPDF(archivo.getSubmittedFileName(), archivoEnBytes);

            // Obtener ruta temporal donde se va a guardar el reporte
            String rutaArchivo = GestorDeArchivos.obtenerRutaDeArchivoTemporal(this, request, response);
            if (rutaArchivo == null) {
                MensajeUtil.agregarError(session, "Error al procesar el archivo temporal");
                response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
                return;
            }

            // Procesar la información del contenido del PDF para crear el resumen financiero
            ResumenFinanciero resumenFinanciero = servicioResumenFinanciero.procesarInformacion(rutaArchivo, documentoPDF, usuario);
            if (resumenFinanciero == null) {
                MensajeUtil.agregarError(session, "No se pudo procesar la información del PDF");
                response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
                return;
            }

            // Guardar en la BD el resumen financiero
            DAOResumenFinanciero.crear(resumenFinanciero);
            MensajeUtil.agregarExito(session, "Resumen financiero procesado exitosamente");

            prepararVistaResumenFinanciero(request, resumenFinanciero);
            request.getRequestDispatcher(PATH).forward(request, response);



        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al procesar el PDF: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/resumen_financiero/consultarResumenes");
        }
    }

    private void prepararVistaResumenFinanciero(HttpServletRequest request, ResumenFinanciero resumenFinanciero) {
        request.setAttribute("Ingresos", resumenFinanciero.getIngresosTotales());
        request.setAttribute("Gastos", resumenFinanciero.getGastosTotales());
        request.setAttribute("AhorroNeto", resumenFinanciero.getAhorroNeto());
        request.setAttribute("fechaPeriodoAnterior", resumenFinanciero.getFechaPeriodoAnterior());
        request.setAttribute("fechaPeriodoActual", resumenFinanciero.getFechaPeriodoActual());
        request.setAttribute("fechaCreacionFormateada", resumenFinanciero.getFechaCreacionFormateada());
    }

    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Usuario) session.getAttribute("usuario");
        }
        return null;
    }
}