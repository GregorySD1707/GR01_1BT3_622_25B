package com.sistema_financiero_personal.comun.utilidades;

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
}
