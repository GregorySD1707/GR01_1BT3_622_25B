package com.sistema_financiero_personal.cuentas.servicios;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;

public class ServicioCuenta {
    private DAOCuenta daoCuenta;

    public ServicioCuenta(DAOCuenta daoCuenta) {
        this.daoCuenta = daoCuenta;
    }

    public ServicioCuenta() {
        daoCuenta = new DAOCuenta();
    }
    public void crearCuenta(Cuenta cuenta) {
        daoCuenta.crear(cuenta);
    }

    public Cuenta buscarCuenta(Long id) {
        return daoCuenta.buscarPorId(id);
    }
}
