package com.sistema_financiero_personal.dashboard;

import com.sistema_financiero_personal.comun.dashboard.ServicioCompendio;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.movimiento.modelos.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class ServicioCompendioTest {

    private ServicioCompendio servicioDashboard = new ServicioCompendio();

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

    @Test
    public void given_ingresos_when_calcular_total_ingresos_de_varios_movimientos_then_retornar_total() {
        List<Movimiento> movimientos = Arrays.asList(
                new Ingreso(50, LocalDateTime.now(), "Ingreso 1", CategoriaIngreso.REGALOS),
                new Ingreso(100, LocalDateTime.now(), "Ingreso 2", CategoriaIngreso.SALARIO),
                new Gasto(30, LocalDateTime.now(), "Gasto 1", CategoriaGasto.ENTRETENIMIENTO)
        );
        double ingresos = servicioDashboard.calcularIngresos(movimientos);
        assertEquals(150, ingresos, 0.001);
    }

    @Test
    public void given_gastos_when_calcular_total_gastos_de_varios_movimientos_then_retornar_total() {
        List<Movimiento> movimientos = Arrays.asList(
                new Gasto(30, LocalDateTime.now(), "Gasto 1", CategoriaGasto.ALIMENTACION),
                new Gasto(20, LocalDateTime.now(), "Gasto 2", CategoriaGasto.TRANSPORTE),
                new Ingreso(50, LocalDateTime.now(), "Ingreso 1", CategoriaIngreso.OTROS)
        );
        double gastos = servicioDashboard.calcularGastos(movimientos);
        assertEquals(50, gastos, 0.001);
    }

    @Test
    public void given_more_than_five_movements_when_get_last_movements_then_returns_only_five_sorted() {
        List<Movimiento> movimientos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            movimientos.add(new Ingreso(i, LocalDateTime.now().minusDays(10 - i), "Ingreso " + i, CategoriaIngreso.OTROS));
        }
        List<Movimiento> ultimos = servicioDashboard.obtenerUltimosMovimientos(movimientos);
        assertEquals(5, ultimos.size());
        assertEquals(10, ultimos.get(0).getMonto(), 0.001);
        assertEquals(6, ultimos.get(4).getMonto(), 0.001);
    }

}
