package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.daos.DAODocumentoPDF;
import com.sistema_financiero_personal.modelos.DocumentoPDF;
import com.sistema_financiero_personal.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.servicios.ServicioResumenFinanciero;
import com.sistema_financiero_personal.utilidades.ExtractorTexto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(urlPatterns = {"/subirPDF", "/consultarResumenes"})
@MultipartConfig(
        fileSizeThreshold =  1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class ServletResumenFinanciero extends HttpServlet {

    private DAOResumenFinanciero DAOResumenFinanciero;
    private DAODocumentoPDF DAODocumentoPDF;

    public ServletResumenFinanciero(){
        DAOResumenFinanciero = new DAOResumenFinanciero();
        DAODocumentoPDF = new DAODocumentoPDF();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        try {
            // 1. Obtener todos los resumenes de la base de datos
             List<ResumenFinanciero> resumenes = DAOResumenFinanciero.listar();

            // 2. Para cada resumen, obtener información del PDF
            List<DocumentoPDF> documentos = new ArrayList<>();
            for (ResumenFinanciero resumen : resumenes) {
                DocumentoPDF doc = DAODocumentoPDF.buscarPorId((long) resumen.getDocumentoPDFId());
                documentos.add(doc);
                System.out.println(resumen);
            }

            // 2. Enviar al JSP
            request.setAttribute("ResumenesFinancieros", resumenes);
            request.setAttribute("DocumentosPDF", documentos); // Nueva lista
            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al consultar resúmenes: " + e.getMessage());
            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            // 1. Obtener archivo del formulario
            Part filePart = request.getPart("archivoPDF");

            if(filePart == null || filePart.getSize() == 0){
                request.setAttribute("error", "Por favor selecciona un archivo PDF");
                request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
                return;
            }

            // Leer archivo PDF como bytes[]
            byte[] archivoBytes;
            try (InputStream inputStream = filePart.getInputStream()){
                archivoBytes = inputStream.readAllBytes();
            }

            // Guardar PDF en la base de datos
            String nombre = filePart.getSubmittedFileName();
            Long documentoPDFId = DAODocumentoPDF.guardarPDF(nombre, archivoBytes);

            // 2. Crear directorio temporal si no existe
            String uploadPath = getServletContext().getRealPath("") + File.separator + "temp";
            File uploadDir = new File(uploadPath);
            if(!uploadDir.exists()){
                uploadDir.mkdir();
            }

            // 3. Guardar archivo temporalmente
            String fileName = "temp_" + System.currentTimeMillis() + ".pdf";
            String filePath = uploadPath + File.separator + fileName;

            try (InputStream inputStream = filePart.getInputStream()){
                Files.copy(inputStream, new File(filePath).toPath(),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            // 4. Extraer texto del PDF
            String textoPDF = ExtractorTexto.extraerTextoDePDF(filePath);
            System.out.println(textoPDF);

            // 5. Extraer ingresos y gastos
            Double ingresos = ServicioResumenFinanciero.extraerMonto("DEPÓSITO / CRÉDITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)", 1, request, response, textoPDF);
            if (ingresos == null) return;

            Double gastos = ServicioResumenFinanciero.extraerMonto("CHEQUES / DÉBITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)",1,request,response,textoPDF);
            if(gastos == null) return;

            LocalDate fechaPeriodoAnterior = ServicioResumenFinanciero.extraerFecha("FECHA ÚLTIMO CORTE\\s*\\(FACTURA\\)\\s*(\\d{2}-\\d{2}-\\d{4})", 1, request, response, textoPDF);
            if(fechaPeriodoAnterior == null) {
                return;
            }

            LocalDate fechaPeriodoActual = ServicioResumenFinanciero.extraerFecha("FECHA ESTE CORTE\\s*\\(FACTURA\\)\\s*(\\d{2}\\-\\d{2}\\-\\d{4})", 1, request, response, textoPDF);
            if(fechaPeriodoActual == null) return;

            // 6. Eliminar archivo temporal
            new File(filePath).delete();

            // 7. Crear Resumen Financiero y guardar en Base de Datos sus ingresos, gastos y ahorro neto asociados
            ResumenFinanciero resumenFinanciero = getResumenFinanciero(ingresos, gastos, filePath, fechaPeriodoAnterior, fechaPeriodoActual, documentoPDFId.intValue());

            // 8. Enviar resultado al JSP
            request.setAttribute("Ingresos", resumenFinanciero.getIngresosTotales());
            request.setAttribute("Gastos", resumenFinanciero.getGastosTotales());
            request.setAttribute("AhorroNeto", resumenFinanciero.getAhorroNeto());
            request.setAttribute("fechaPeriodoAnterior", resumenFinanciero.getFechaPeriodoAnterior());
            request.setAttribute("fechaPeriodoActual", resumenFinanciero.getFechaPeriodoActual());
            request.setAttribute("fechaCreacionFormateada", resumenFinanciero.getFechaCreacionFormateada());

            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request,response);

        } catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el PDF: "+e.getMessage());
            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
        }
    }

    private ResumenFinanciero getResumenFinanciero(Double ingresos, Double gastos, String filePath, LocalDate fechaPeriodoAnterior, LocalDate fechaPeriodoActual, int documentoPDFId) {
        double ahorroNeto = ServicioResumenFinanciero.calcularAhorroNeto(ingresos, gastos);
        //System.out.println(ahorroNeto);

        ResumenFinanciero resumenFinanciero = new ResumenFinanciero(ingresos, gastos, ahorroNeto, fechaPeriodoAnterior, fechaPeriodoActual, documentoPDFId);
        DAOResumenFinanciero.crear(resumenFinanciero);
        return resumenFinanciero;
    }
}
