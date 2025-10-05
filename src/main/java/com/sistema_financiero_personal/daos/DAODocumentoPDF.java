package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.modelos.DocumentoPDF;

public class DAODocumentoPDF extends DAOBase<DocumentoPDF> {

    public DAODocumentoPDF(){
        super(DocumentoPDF.class);
    }

    public void guardarPDF(DocumentoPDF documentoPDF) {
        // Forzar a Hibernate a ejecutar el SQL inmediatamente dentro de la transacción, permitiendo que el ID esté disponible
        executeInTransaction(session -> {
            session.persist(documentoPDF);
            session.flush();
        });
    }
}

