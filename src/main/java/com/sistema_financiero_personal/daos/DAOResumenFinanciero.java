package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.modelos.ResumenFinanciero;

public class DAOResumenFinanciero extends BaseDAO<ResumenFinanciero> {

    public DAOResumenFinanciero(){
        super(ResumenFinanciero.class);
    }

    // Método de conveniencia requerido por el código existente (Servlet y Tests)
    public void guardar(ResumenFinanciero resumenFinanciero) {
        // delega al método genérico de creación del BaseDAO
        crear(resumenFinanciero);
    }
}
