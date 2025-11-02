package com.sistema_financiero_personal.plantillas.daos;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.TipoMovimiento;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;


import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

public class DAOPlantilla extends DAOBase<Usuario> {

    public DAOPlantilla() {
        super(Usuario.class);
    }

    public boolean crearPlantilla(Long usuarioId, String nombre, TipoMovimiento tipo, double monto, Long cuentaId, CategoriaGasto categoriaGasto) {
        return true;
    }
    public boolean crearPlantilla(Plantilla plantilla, Long usuarioId) {
        return true;
    }

    public boolean eliminarPlantilla(Long plantilla_Id) {
        return true;
    }
}
