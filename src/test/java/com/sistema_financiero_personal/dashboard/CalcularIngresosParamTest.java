package com.sistema_financiero_personal.dashboard;

import com.sistema_financiero_personal.comun.dashboard.ServicioCompendio;
import com.sistema_financiero_personal.movimiento.modelos.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.junit.Assert.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class CalcularIngresosParamTest {

    private List<Movimiento> movimientos;
    private double expected;

    public CalcularIngresosParamTest(List<Movimiento> movimientos, double expected) {
        this.movimientos = movimientos;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Arrays.asList(new Ingreso(50, LocalDateTime.now(), "Ingreso 1", CategoriaIngreso.REGALOS)), 50},
                {Arrays.asList(
                        new Ingreso(50, LocalDateTime.now(), "Ingreso 1", CategoriaIngreso.REGALOS),
                        new Ingreso(100, LocalDateTime.now(), "Ingreso 2", CategoriaIngreso.SALARIO)
                ), 150},
                {Arrays.asList(
                        new Gasto(30, LocalDateTime.now(), "Gasto 1", CategoriaGasto.ALIMENTACION),
                        new Ingreso(70, LocalDateTime.now(), "Ingreso 1", CategoriaIngreso.SALARIO)
                ), 70},
                {Collections.emptyList(), 0}
        });
    }

    private ServicioCompendio servicioDashboard = new ServicioCompendio();

    @Test
    public void givenMovimientos_whenCalculoIngresos_thenRetornaCorrecto() {
        double ingresos = servicioDashboard.calcularIngresos(movimientos);
        assertEquals(expected, ingresos,0.001);
    }

}
