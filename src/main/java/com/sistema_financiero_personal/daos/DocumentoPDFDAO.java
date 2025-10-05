package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.modelos.DocumentoPDF;

import java.time.LocalDateTime;

public class DocumentoPDFDAO extends BaseDAO<DocumentoPDF> {

    public DocumentoPDFDAO(){
        super(DocumentoPDF.class);
    }

    public Long guardarPDF(String nombre, byte[] archivoPdf) {
        System.out.println("=== DEBUG ANTES DE GUARDAR ===");
        System.out.println("Nombre: " + nombre);
        System.out.println("Tamaño byte[]: " + archivoPdf.length);
        System.out.println("Es null? " + (archivoPdf == null));
        System.out.println("Tipo de clase: " + archivoPdf.getClass().getName());

        Long tamanio = (long) archivoPdf.length;
        DocumentoPDF documento = new DocumentoPDF(nombre, archivoPdf, tamanio);

        System.out.println("ID antes de persist: " + documento.getId());
        System.out.println("archivoPdf en objeto: " + (documento.getArchivoPdf() != null));

        executeInTransaction(session -> {
            session.persist(documento);
            session.flush();
        });

        System.out.println("ID después de persist: " + documento.getId());
        return documento.getId();
    }
}
