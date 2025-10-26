package com.sistema_financiero_personal.dashboard;

import com.sistema_financiero_personal.comun.dashboard.EstatusDashboard;
import com.sistema_financiero_personal.comun.dashboard.ServicioDashboard;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.movimiento.modelos.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class ServicioDashboardTest {

    private ServicioDashboard servicioDashboard = new ServicioDashboard();

    @Test
    public void givenVariasCuentas_whenCalculoSaldoTotal_then_Ok() {
        List<Cuenta> cuentas = Arrays.asList(
                new Cuenta("Ahorros", TipoCuenta.AHORROS, 100, null),
                new Cuenta("Corriente", TipoCuenta.CORRIENTE, 200, null)
        );
        double total = servicioDashboard.calcularSaldoTotal(cuentas);
        assertEquals(300, total, 0.001);
    }

    @Test
    public void givenVariasCuentas_whenObtengoSaldosIndividuales_then_Ok(){
        List<Cuenta> cuentas = Arrays.asList(
                new Cuenta("Ahorros", TipoCuenta.AHORROS, 100, null),
                new Cuenta("Corriente", TipoCuenta.CORRIENTE, 200, null)
        );
        Map<String, Double> saldos = servicioDashboard.obtenerSaldosIndividuales(cuentas);
        assertEquals(2, saldos.size());
        assertEquals(100, saldos.get("Ahorros"), 0.001);
        assertEquals(200, saldos.get("Corriente"), 0.001);
    }
}
