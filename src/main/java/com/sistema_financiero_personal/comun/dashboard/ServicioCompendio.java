package com.sistema_financiero_personal.comun.dashboard;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.Gasto;
import com.sistema_financiero_personal.movimiento.modelos.Ingreso;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;

import java.util.*;
import java.util.stream.Collectors;

public class ServicioCompendio {

    public double calcularSaldoTotal(List<Cuenta> cuentas) {
        return cuentas.stream().mapToDouble(Cuenta::getMonto).sum();
    }

    public Map<String, Double> obtenerSaldosIndividuales(List<Cuenta> cuentas) {
        return cuentas.stream()
                .collect(Collectors.toMap(Cuenta::getNombre, Cuenta::getMonto));
    }

    public double calcularIngresos(List<Movimiento> movimientos) {
        return movimientos.stream()
                .filter(m -> m instanceof Ingreso)
                .mapToDouble(Movimiento::getMonto)
                .sum();

    }

    public double calcularGastos(List<Movimiento> movimientos) {
        return movimientos.stream()
                .filter(m -> m instanceof Gasto)
                .mapToDouble(Movimiento::getMonto)
                .sum();
    }

    public List<Movimiento> obtenerUltimosMovimientos(List<Movimiento> movimientos) {
        return movimientos.stream()
                .sorted((m1, m2) -> m2.getFecha().compareTo(m1.getFecha()))
                .limit(5)
                .collect(Collectors.toList());
    }
    public EstatusDashboard determinarEstatus(List<Cuenta> cuentas, List<Movimiento> movimientos) {
        if (cuentas.isEmpty()) return EstatusDashboard.SIN_CUENTAS;
        if (movimientos.isEmpty()) return EstatusDashboard.SIN_MOVIMIENTOS;
        return EstatusDashboard.OK;
    }





}
