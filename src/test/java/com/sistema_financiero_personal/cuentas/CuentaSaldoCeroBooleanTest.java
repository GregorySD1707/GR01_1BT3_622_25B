// java
package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CuentaSaldoCeroBooleanTest {

    @Test
    public void given_amount_when_the_amount_is_equal_to_the_expense_then_the_boolean_balance_zero_is_true() {
        ServicioCuenta servicio = new ServicioCuenta();
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNombre("Ahorros");
        cuenta.setMonto(100.00);

        boolean saldoCero = servicio.verificarSaldoCero(cuenta, 100.00);

        assertTrue("Se esperaba que el saldo quedara exactamente en cero", saldoCero);
        assertEquals(0.0, cuenta.getMonto(), 0.0001);
    }
}
