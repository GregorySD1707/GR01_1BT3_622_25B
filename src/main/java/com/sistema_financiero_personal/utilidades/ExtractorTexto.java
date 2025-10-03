package com.sistema_financiero_personal.utilidades;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ExtractorTexto {

    public static String extraerTextoDePDF(String rutaPDF) throws IOException {
        try(PDDocument documento = PDDocument.load(new File(rutaPDF))){
            PDFTextStripper stripper = new PDFTextStripper();
            return  stripper.getText(documento);
        }
    }

    public static String extraerFragmentoDeUnTexto(String texto, String patron, int grupo){
        Pattern pattern = Pattern.compile(patron);
        Matcher matcher = pattern.matcher(texto);

        return (matcher.find()) ? matcher.group(grupo) : null;
    }

    public static void main(String[] args) {
        try {
            // 1. Pedir que suba el PDF
            String texto = extraerTextoDePDF("C:/Users/GregorySD/Documents/Universidad/6 Semestre/metodologias_agiles/1_semana/2214456416_22082025.pdf");
            System.out.println(texto);


            // 2. Extraer
            // 2.1. Extraer texto del PDF
            // 2.2. Extraer ingresos, gastos y ahorro neto del PDF
            // 3. Mostrar Salida


            String patron = "DEPÓSITO / CRÉDITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)";
            String fragmento = extraerFragmentoDeUnTexto(texto, patron, 0);
            if(fragmento == null){
                return;
            }
            double ingresos = Double.parseDouble(fragmento);
            System.out.println(fragmento);

            patron = "CHEQUES / DÉBITOS\\s*\\(\\d+\\)\\s+(\\d+\\.\\d+)";
            fragmento = extraerFragmentoDeUnTexto(texto, patron, 0);
            if(fragmento == null){
                return;
            }
            double gastos = Double.parseDouble(fragmento);
            System.out.println(fragmento);

            double ahorroNeto = ingresos - gastos;
            System.out.println(ahorroNeto);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
