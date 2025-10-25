package com.sistema_financiero_personal.cuentas.servicios;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;

public class ServicioCuenta {
    DAOCuenta daoCuenta = new DAOCuenta();

    public void crearCuenta(Cuenta cuenta) {
        daoCuenta.crear(cuenta);
    }

    public Object buscarCuenta(Long id) {
        return daoCuenta.buscarPorId(id);
    }
}
