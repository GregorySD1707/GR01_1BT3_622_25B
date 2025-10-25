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
        boolean cuentaNoCreada = cuenta == null;
        boolean nombreVacio = cuenta.getNombre() == null || cuenta.getNombre().isBlank();
        boolean tipoCuentaVacio = cuenta.getTipo() == null;
        boolean carteraNoAsignada = cuenta.getCartera() == null;

        if (cuentaNoCreada || nombreVacio || tipoCuentaVacio || carteraNoAsignada) {
            throw new IllegalArgumentException("Todos los campos deben ser llenados");
        }
    }

}