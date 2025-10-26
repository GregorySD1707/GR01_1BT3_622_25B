package com.sistema_financiero_personal.cuentas.servicios;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;

import java.util.List;

public class ServicioCuenta {
    private DAOCuenta daoCuenta;

    public ServicioCuenta(DAOCuenta daoCuenta) {
        this.daoCuenta = daoCuenta;
    }

    public ServicioCuenta() {
        daoCuenta = new DAOCuenta();
    }

    public void crearCuenta(Cuenta cuenta) {
        validarObligatorios(cuenta);

        if (!validarSaldoInicial(cuenta.getMonto())) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor que cero");
        }

        if (existeCuentaDuplicada(cuenta.getNombre(), cuenta.getTipo(), cuenta.getCartera().getId())) {
            throw new IllegalStateException("Ya existe una cuenta con el mismo nombre y tipo en esta cartera");
        }

        daoCuenta.crear(cuenta);
    }

    public boolean existeCuentaDuplicada(String nombre, TipoCuenta tipo, Long carteraId) {
        return daoCuenta.existeCuentaPorNombreYTipo(nombre, tipo, carteraId);
    }


    public Cuenta buscarCuenta(Long id) {
        return daoCuenta.buscarPorId(id);
    }

    public boolean validarSaldoInicial(double saldo) {

        if (saldo <= 0) {
            return false;
        }

        double centavos = Math.round(saldo * 100.0) / 100.0;
        return Double.compare(saldo, centavos) == 0;
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

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}