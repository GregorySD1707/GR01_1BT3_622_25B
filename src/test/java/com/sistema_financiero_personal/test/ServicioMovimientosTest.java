package com.sistema_financiero_personal.test;

import com.sistema_financiero_personal.modelos.Ingreso;
import com.sistema_financiero_personal.servicios.ServicioMovimientos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServicioMovimientosTest {
    @Test
    public void testRegistrarIngreso() {
        ServicioMovimientos servicio = new ServicioMovimientos();
        Ingreso ingreso = (Ingreso) servicio.registrarIngreso(166.0, "Test ingreso", "Prueba", "CarteraTest");
        Assertions.assertNotNull(ingreso);
        Assertions.assertEquals(166.0, ingreso.getMonto());
        Assertions.assertEquals("Test ingreso", ingreso.getDescripcion());
        // Verifica que el total de ingresos sea mayor o igual a 166
        double totalIngresos = servicio.obtenerIngresosTotales();
        Assertions.assertTrue(totalIngresos >= 166.0);
    }
}
