package com.sistema_financiero_personal.servicios;

import com.sistema_financiero_personal.utilidades.ExtractorTexto;
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

    public static Double calcularAhorroNeto(Double ingresos, Double gastos){
        return  ingresos - gastos;
    }
}
