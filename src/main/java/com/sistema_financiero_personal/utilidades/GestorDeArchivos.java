package com.sistema_financiero_personal.utilidades;

import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class GestorDeArchivos {

    public static void eliminarArchivo(String rutaDeArchivo) {
        // Eliminar archivo
        new File(rutaDeArchivo).delete();
    }

    public static byte[] transformarArchivoABytes(Part filePart) throws IOException {
        // Leer archivo PDF como bytes[]
        byte[] archivoBytes;
        try (InputStream inputStream = filePart.getInputStream()){
            archivoBytes = inputStream.readAllBytes();
        }
        return archivoBytes;
    }

    public static String generarNombreDeArchivoTemporal(String uploadPath) {
        // Generar nombre para el archivo temporal
        String fileName = "temp_" + System.currentTimeMillis() + ".pdf";
        String filePath = uploadPath + File.separator + fileName;
        return filePath;
    }

    public static void guardarArchivoTemporal(Part filePart, String filePath) {
        // guardar el archivo en una ubicación
        try (InputStream inputStream = filePart.getInputStream()){
            Files.copy(inputStream, new File(filePath).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
