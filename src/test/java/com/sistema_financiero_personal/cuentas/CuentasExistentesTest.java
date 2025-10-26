package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CuentasExistentesTest {

    @Test
    public void givenCarteraConCuentas_whenVerCuentas_thenRetornaListaConRegistros() {
        Cartera cartera = crearCartera(1L, "Mi Cartera");
        List<Cuenta> cuentas = crearListaCuentas(cartera);

        assertNotNull(cuentas);
        assertTrue(cuentas.size() > 0);

        for (Cuenta cuenta : cuentas) {
            assertNotNull(cuenta.getNombre());
            assertNotNull(cuenta.getTipo());
            assertTrue(cuenta.getMonto() > 0);
        }
    }


    @Test
    public void givenCarteraSinCuentas_whenVerCuentas_thenRetornaListaVacia() {
        List<Cuenta> cuentas = new ArrayList<>();

        assertNotNull("La lista no debe ser null", cuentas);
        assertTrue("La lista debe estar vacía", cuentas.isEmpty());
        assertEquals("El tamaño debe ser 0", 0, cuentas.size());
    }

    private Cartera crearCartera(Long id, String nombre) {
        Cartera cartera = new Cartera();
        cartera.setId(id);
        cartera.setSaldo(0.0);
        return cartera;
    }
    // Método helper para crear lista de cuentas
    private List<Cuenta> crearListaCuentas(Cartera cartera) {
        List<Cuenta> cuentas = new ArrayList<>();

        cuentas.add(crearCuenta(1L, "Efectivo", TipoCuenta.EFECTIVO, 1000.50, cartera));
        cuentas.add(crearCuenta(2L, "Banco", TipoCuenta.CORRIENTE, 5000.75, cartera));
        cuentas.add(crearCuenta(3L, "Ahorros", TipoCuenta.AHORROS, 10000.00, cartera));

        return cuentas;
    }

    private Cuenta crearCuenta(Long id, String nombre, TipoCuenta tipo, double monto, Cartera cartera) {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(id);
        cuenta.setNombre(nombre);
        cuenta.setTipo(tipo);
        cuenta.setMonto(monto);
        cuenta.setCartera(cartera);
        return cuenta;
    }
}