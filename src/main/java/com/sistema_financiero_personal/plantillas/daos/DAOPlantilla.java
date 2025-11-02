package com.sistema_financiero_personal.plantillas.daos;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.TipoMovimiento;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;


public class DAOPlantilla {


    public boolean crearPlantilla(Long usuarioId, String nombre, TipoMovimiento tipo, double monto, Long cuentaId, CategoriaGasto categoriaGasto) {
        return true;
    }
    public boolean crearPlantilla(Plantilla plantilla, Long usuarioId) {
        return true;
    }

}
