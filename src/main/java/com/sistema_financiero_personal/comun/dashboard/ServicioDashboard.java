package com.sistema_financiero_personal.comun.dashboard;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.Gasto;
import com.sistema_financiero_personal.movimiento.modelos.Ingreso;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;

import java.util.*;
import java.util.stream.Collectors;

public class ServicioDashboard {

    public double calcularSaldoTotal(List<Cuenta> cuentas) {
        return cuentas.stream().mapToDouble(Cuenta::getMonto).sum();
    }

    public Map<String, Double> obtenerSaldosIndividuales(List<Cuenta> cuentas) {
        return cuentas.stream()
                .collect(Collectors.toMap(Cuenta::getNombre, Cuenta::getMonto));
    }
}
