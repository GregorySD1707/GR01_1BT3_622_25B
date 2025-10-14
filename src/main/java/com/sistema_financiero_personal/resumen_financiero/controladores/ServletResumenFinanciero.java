package com.sistema_financiero_personal.resumen_financiero.controladores;

import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.DocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.resumen_financiero.servicios.ServicioResumenFinanciero;
import com.sistema_financiero_personal.comun.utilidades.GestorDeArchivos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

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

    @Override
    public void init() throws ServletException {
        super.init();
        this.DAOResumenFinanciero = new DAOResumenFinanciero();
        this.DAODocumentoPDF = new DAODocumentoPDF();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            Part archivo = GestorDeArchivos.obtenerArchivo(request, response);
            if (archivo == null) return;

            // Guardar reporte en BD con su nombre y contenido en bytes
            byte[] archivoEnBytes = GestorDeArchivos.transformarArchivoABytes(archivo);
            DocumentoPDF documentoPDF = DAODocumentoPDF.guardarPDF(archivo.getSubmittedFileName(),archivoEnBytes);

            // Obtener ruta temporal donde se va a guardar el reporte
            String rutaArchivo = GestorDeArchivos.obtenerRutaDeArchivoTemporal(this, request, response);
            if (rutaArchivo == null) return;

            // Procesar la información del contenido del PDF para crear el resumen financiero
            ResumenFinanciero resumenFinanciero = ServicioResumenFinanciero.procesarInformacion(request,
                    response, rutaArchivo, documentoPDF);
            if (resumenFinanciero == null) return;

            // Guardar en la BD el resumen financiero
            DAOResumenFinanciero.crear(resumenFinanciero);

            prepararVistaResumenFinanciero(request, response, resumenFinanciero);

            // Enviar resultado al JSP
            request.getRequestDispatcher("/resumen_financiero/VistaResumenFinanciero.jsp").forward(request, response);

        } catch (Exception e){
            request.setAttribute("error", "Error al procesar el PDF: "+e.getMessage());
            request.getRequestDispatcher("/resumen_financiero/VistaResumenFinanciero.jsp").forward(request, response);
        }
    }

    private static void prepararVistaResumenFinanciero(HttpServletRequest request, HttpServletResponse response, ResumenFinanciero resumenFinanciero) throws ServletException, IOException {
        request.setAttribute("Ingresos", resumenFinanciero.getIngresosTotales());
        request.setAttribute("Gastos", resumenFinanciero.getGastosTotales());
        request.setAttribute("AhorroNeto", resumenFinanciero.getAhorroNeto());
        request.setAttribute("fechaPeriodoAnterior", resumenFinanciero.getFechaPeriodoAnterior());
        request.setAttribute("fechaPeriodoActual", resumenFinanciero.getFechaPeriodoActual());
        request.setAttribute("fechaCreacionFormateada", resumenFinanciero.getFechaCreacionFormateada());
    }
}
