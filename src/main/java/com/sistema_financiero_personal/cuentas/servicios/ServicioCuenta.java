package com.sistema_financiero_personal.cuentas.servicios;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;

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

        if (existeCuentaDuplicada(cuenta)) {
            throw new IllegalStateException("Ya existe una cuenta con ese nombre y tipo en la cartera");
        }

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

    public boolean existeCuentaDuplicada(Cuenta nuevaCuenta) {
        List<Cuenta> cuentas = daoCuenta.listar();

        for (Cuenta existente : cuentas) {
            boolean mismoNombre = existente.getNombre().equalsIgnoreCase(nuevaCuenta.getNombre());
            boolean mismoTipo = existente.getTipo() == nuevaCuenta.getTipo();
            boolean mismaCartera = existente.getCartera().equals(nuevaCuenta.getCartera());
            if (mismoNombre && mismoTipo && mismaCartera) {
                return true;
            }
        }
        return false;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}