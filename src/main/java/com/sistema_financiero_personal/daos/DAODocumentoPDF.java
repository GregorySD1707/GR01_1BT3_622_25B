package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.modelos.DocumentoPDF;

public class DAODocumentoPDF extends DAOBase<DocumentoPDF> {

    public DAODocumentoPDF(){
        super(DocumentoPDF.class);
    }

    public Long guardarPDF(String nombre, byte[] archivoPdf) {
        Long tamanio = (long) archivoPdf.length;
        DocumentoPDF documento = new DocumentoPDF(nombre, archivoPdf, tamanio);

        executeInTransaction(session -> {
            session.persist(documento);
            session.flush();
        });

        return documento.getId();
    }
}
