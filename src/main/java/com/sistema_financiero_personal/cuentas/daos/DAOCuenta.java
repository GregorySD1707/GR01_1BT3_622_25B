package com.sistema_financiero_personal.cuentas.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;

public class DAOCuenta extends DAOBase<Cuenta> {

    public DAOCuenta() {super(Cuenta.class);}

    public boolean existeCuentaPorNombreYTipo(String nombre, TipoCuenta tipo, Long carteraId){
        return true;
    }
}
