package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.servicios.ServicioResumenDiario;
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

@WebServlet("/subirPDF")
@MultipartConfig(
        fileSizeThreshold =  1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class ServletResumenDiario extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try{
            // 1. Obtener archivo del formulario
            Part filePart = request.getPart("archivoPDF");

            if(filePart == null || filePart.getSize() == 0){
                request.setAttribute("error", "Por favor selecciona un archivo PDF");
                request.getRequestDispatcher("/resumen_financiero.jsp").forward(request, response);
                return;
            }

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

            // 5. Extraer ingresos y gastos
            Double ingresos = ServicioResumenDiario.extraer("DEPÓSITO / CRÉDITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)", 1, request, response, textoPDF);
            if (ingresos == null) return;

            Double gastos = ServicioResumenDiario.extraer("CHEQUES / DÉBITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)",1,request,response,textoPDF);
            if(gastos == null) return;

            // 6. Eliminar archivo temporal
            new File(filePath).delete();

            // 7. Crear Resumen Financiero y guardar en Base de Datos sus ingresos, gastos y ahorro neto asociados
            ResumenFinanciero resumenFinanciero = getResumenFinanciero(ingresos, gastos, filePath);

            // 8. Enviar resultado al JSP
            request.setAttribute("Ingresos", resumenFinanciero.getIngresosTotales());
            request.setAttribute("Gastos", resumenFinanciero.getGastosTotales());
            request.setAttribute("Ahorro Neto", resumenFinanciero.getAhorroNeto());
            request.getRequestDispatcher("/resumen_financiero.jsp").forward(request,response);

        } catch (Exception e){
            e.printStackTrace();
            request.setAttribute("error", "Error al procesar el PDF: "+e.getMessage());
            request.getRequestDispatcher("/resumen_financiero.jsp").forward(request, response);
        }
    }

    private static ResumenFinanciero getResumenFinanciero(Double ingresos, Double gastos, String filePath) {
        double ahorroNeto = ServicioResumenDiario.calcularAhorroNeto(ingresos, gastos);
        //System.out.println(ahorroNeto);

        ResumenFinanciero resumenFinanciero = new ResumenFinanciero(ingresos, gastos, ahorroNeto);
        DAOResumenFinanciero daoResumenFinanciero = new DAOResumenFinanciero();
        daoResumenFinanciero.guardar(resumenFinanciero);
        return resumenFinanciero;
    }
}
