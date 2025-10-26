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
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }

        if (cuenta.getNombre() == null || cuenta.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la cuenta es obligatorio");
        }

        if (cuenta.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de cuenta es obligatorio");
        }

        if (cuenta.getCartera() == null) {
            throw new IllegalArgumentException("Debe asignarse una cartera a la cuenta");
        }
    }


    public List<Cuenta> listarCuentasPorCartera(Long id) {
        return daoCuenta.listarPorCartera(id);
    }

    public void ajustarMonto(Cuenta cuenta, double gastoSuperior) {
        double montoResultante = cuenta.getMonto() - gastoSuperior;
        if(montoResultante < 0){
            return;
        }
        cuenta.setMonto(montoResultante);
    }
}







