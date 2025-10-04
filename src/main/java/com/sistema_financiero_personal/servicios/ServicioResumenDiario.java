package com.sistema_financiero_personal.servicios;

import com.sistema_financiero_personal.utilidades.ExtractorTexto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ServicioResumenDiario {

    public static Double extraer(String patron, int grupo, HttpServletRequest request, HttpServletResponse response, String textoPDF) throws ServletException, IOException {
        String fragmento = ExtractorTexto.extraerFragmentoDeUnTexto(textoPDF, patron, grupo);
        if(fragmento == null){
            request.setAttribute("error", "No se pudo extraer la información del PDF");
            request.getRequestDispatcher("/resumen_financiero.jsp").forward(request, response);
            return null;
        }
        double dinero = Double.parseDouble(fragmento);
        System.out.println(fragmento);
        return dinero;
    }

    public static Double calcularAhorroNeto(Double ingresos, Double gastos){
        return  ingresos - gastos;
    }
}
