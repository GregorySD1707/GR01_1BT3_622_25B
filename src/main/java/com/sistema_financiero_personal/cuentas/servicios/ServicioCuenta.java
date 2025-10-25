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
        validarSaldoInicial(cuenta.getMonto());
        daoCuenta.crear(cuenta);
    }

    public Cuenta buscarCuenta(Long id) {
        return daoCuenta.buscarPorId(id);
    }

    public boolean validarSaldoInicial(double saldo) {
        return saldo > 0;
    }

    public void validarObligatorios(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("Todos los campos deben ser llenados");
        }
        if (isBlank(cuenta.getNombre())) {
            throw new IllegalArgumentException("El nombre de la cuenta es obligatorio");
        }
        if (cuenta.getTipo() == null) {
            throw new IllegalArgumentException("Debe seleccionar el tipo de cuenta");
        }
        if (cuenta.getCartera() == null) {
            throw new IllegalArgumentException("Debe seleccionar la cartera");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
