package com.sistema_financiero_personal.resumen_financiero.servicios;

import com.sistema_financiero_personal.resumen_financiero.modelos.DocumentoPDF;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.comun.utilidades.ExtractorTexto;
import com.sistema_financiero_personal.comun.utilidades.GestorDeArchivos;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ServicioResumenFinanciero {

    private static final int POSICION_GRUPO_PARENTESIS = 1;
    private static final DateTimeFormatter FORMATTER_FECHA = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static Double extraerMonto(String patron, String textoPDF) {
        try {
            String fragmento = ExtractorTexto.extraerFragmentoDeUnTexto(textoPDF, patron, POSICION_GRUPO_PARENTESIS);
            if (fragmento == null) {
                return null;
            }
            return Double.parseDouble(fragmento);
        } catch (NumberFormatException e) {
            System.err.println("Error al parsear monto: " + e.getMessage());
            return null;
        }
    }

    public static LocalDate extraerFecha(String patron, String textoPDF) {
        try {
            String fragmento = ExtractorTexto.extraerFragmentoDeUnTexto(textoPDF, patron, POSICION_GRUPO_PARENTESIS);
            if (fragmento == null) {
                return null;
            }
            return LocalDate.parse(fragmento, FORMATTER_FECHA);
        } catch (DateTimeParseException e) {
            System.err.println("Error al parsear fecha con patrón " + patron + ": " + e.getMessage());
            return null;
        }
    }

    public static ResumenFinanciero procesarInformacion(String rutaArchivo, DocumentoPDF documentoPDF, Usuario usuario) {
        try {
            // Extraer texto del PDF
            String textoPDF = ExtractorTexto.extraerTextoDePDF(rutaArchivo);
            if (textoPDF == null || textoPDF.trim().isEmpty()) {
                System.err.println("No se pudo extraer texto del PDF");
                return null;
            }

            // Patrones de extracción
            String patronIngresos = "DEPÓSITO / CRÉDITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)";
            String patronGastos = "CHEQUES / DÉBITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)";
            String patronFechaPeriodoAnterior = "FECHA ÚLTIMO CORTE\\s*\\(FACTURA\\)\\s*(\\d{2}-\\d{2}-\\d{4})";
            String patronFechaPeriodoActual = "FECHA ESTE CORTE\\s*\\(FACTURA\\)\\s*(\\d{2}\\-\\d{2}\\-\\d{4})";

            // Extraer información
            Double ingresos = extraerMonto(patronIngresos, textoPDF);
            if (ingresos == null) {
                System.err.println("No se pudieron extraer los ingresos");
                return null;
            }

            Double gastos = extraerMonto(patronGastos, textoPDF);
            if (gastos == null) {
                System.err.println("No se pudieron extraer los gastos");
                return null;
            }

            LocalDate fechaPeriodoAnterior = extraerFecha(patronFechaPeriodoAnterior, textoPDF);
            if (fechaPeriodoAnterior == null) {
                System.err.println("No se pudo extraer la fecha del período anterior");
                return null;
            }

            LocalDate fechaPeriodoActual = extraerFecha(patronFechaPeriodoActual, textoPDF);
            if (fechaPeriodoActual == null) {
                System.err.println("No se pudo extraer la fecha del período actual");
                return null;
            }

            // Eliminar archivo temporal
            GestorDeArchivos.eliminarArchivo(rutaArchivo);

            // Crear y retornar el resumen financiero
            ResumenFinanciero resumen = new ResumenFinanciero(ingresos, gastos, fechaPeriodoAnterior, fechaPeriodoActual, documentoPDF);
            resumen.setUsuario(usuario);
            return resumen;

        } catch (Exception e) {
            System.err.println("Error al procesar información del PDF: " + e.getMessage());
            e.printStackTrace();
            // Intentar eliminar el archivo temporal en caso de error
            try {
                GestorDeArchivos.eliminarArchivo(rutaArchivo);
            } catch (Exception ex) {
                System.err.println("Error al eliminar archivo temporal: " + ex.getMessage());
            }
            return null;
        }
    }
}