package com.sistema_financiero_personal.resumen_financiero.daos;

import com.sistema_financiero_personal.daos.DAOBase;
import com.sistema_financiero_personal.resumen_financiero.modelos.DocumentoPDF;

public class DAODocumentoPDF extends DAOBase<DocumentoPDF> {

    public DAODocumentoPDF(){
        super(DocumentoPDF.class);
    }

    public DocumentoPDF guardarPDF(String nombre, byte[] archivoPdf) {
        Long tamanio = (long) archivoPdf.length;
        DocumentoPDF documento = new DocumentoPDF(nombre, archivoPdf, tamanio);

        executeInTransaction(session -> {
            session.persist(documento);
            session.flush();
        });

        return documento;
    }
}
