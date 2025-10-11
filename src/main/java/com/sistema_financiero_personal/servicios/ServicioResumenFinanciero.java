package com.sistema_financiero_personal.servicios;

import com.sistema_financiero_personal.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.utilidades.ExtractorTexto;
import com.sistema_financiero_personal.utilidades.GestorDeArchivos;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServicioResumenFinanciero {



    public static Double extraerMonto(String patron, int grupo, HttpServletRequest request, HttpServletResponse response, String textoPDF) throws ServletException, IOException {
        String fragmento = ExtractorTexto.extraerFragmentoDeUnTexto(textoPDF, patron, grupo);
        if(fragmento == null){
            request.setAttribute("error", "No se pudo extraer la información del PDF");
            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
            return null;
        }
        double dinero = Double.parseDouble(fragmento);
        System.out.println(fragmento);
        return dinero;
    }

    public static LocalDate extraerFecha(String patron, int grupo, HttpServletRequest request, HttpServletResponse response, String textoPDF) throws ServletException, IOException {
        String fragmento = ExtractorTexto.extraerFragmentoDeUnTexto(textoPDF, patron, grupo);
        if(fragmento == null){
            request.setAttribute("error", "No se pudo extraer la fecha del PDF: " + patron);
            request.getRequestDispatcher("/VistaResumenFinanciero.jsp").forward(request, response);
            return null;
        }

        // Convertir de "dd-MM-yyyy" a LocalDateTime
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate fecha = LocalDate.parse(fragmento, formatter);
        System.out.println("Fecha extraída: " + fragmento + " -> " + fecha);
        return fecha;
    }

    public static double calcularAhorroNeto(Double ingresos, Double gastos) {
        return ingresos - gastos;
    }

    public static ResumenFinanciero procesarInformacion(HttpServletRequest request, HttpServletResponse response, String rutaArchivo, Integer idArchivo) throws IOException, ServletException {
        // Extraer texto del PDF
        String textoPDF = ExtractorTexto.extraerTextoDePDF(rutaArchivo);
        System.out.println(textoPDF);

        int posicionGrupoDeParentesis = 1;

        // Extraer ingresos y gastos
        String patronParaExtraerIngresos = "DEPÓSITO / CRÉDITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)";
        Double ingresos = ServicioResumenFinanciero.extraerMonto(patronParaExtraerIngresos, posicionGrupoDeParentesis, request, response, textoPDF);
        if (ingresos == null) return null;

        String patroneParaExtraerGastos = "CHEQUES / DÉBITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)";
        Double gastos = ServicioResumenFinanciero.extraerMonto( patroneParaExtraerGastos,posicionGrupoDeParentesis, request, response,textoPDF);
        if(gastos == null) return null;

        String patronParaExtraerFechaPeriodoAnterior = "FECHA ÚLTIMO CORTE\\s*\\(FACTURA\\)\\s*(\\d{2}-\\d{2}-\\d{4})";
        LocalDate fechaPeriodoAnterior = ServicioResumenFinanciero.extraerFecha(patronParaExtraerFechaPeriodoAnterior, posicionGrupoDeParentesis, request, response, textoPDF);
        if(fechaPeriodoAnterior == null) {return null;}

        String patronParaExtraerFechaPeriodoActual = "FECHA ESTE CORTE\\s*\\(FACTURA\\)\\s*(\\d{2}\\-\\d{2}\\-\\d{4})";
        LocalDate fechaPeriodoActual = ServicioResumenFinanciero.extraerFecha(patronParaExtraerFechaPeriodoActual, posicionGrupoDeParentesis, request, response, textoPDF);
        if(fechaPeriodoActual == null) return null;

        GestorDeArchivos.eliminarArchivo(rutaArchivo);

        double ahorroNeto = ServicioResumenFinanciero.calcularAhorroNeto(ingresos, gastos);

        return new ResumenFinanciero(ingresos, gastos, ahorroNeto, fechaPeriodoAnterior, fechaPeriodoActual, idArchivo);
    }
}
