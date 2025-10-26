package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CuentaSaldoDecimalTest {

    @Mock
    private DAOCuenta daoCuenta;

    private ServicioCuenta servicioCuenta;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servicioCuenta = new ServicioCuenta(daoCuenta);
    }

    @Test
    public void given_decimal_values_when_validate_saldo_inicial_then_correct_validation() {

        assertTrue(servicioCuenta.validarSaldoInicial(0.01));
        assertTrue(servicioCuenta.validarSaldoInicial(99.99));

        assertFalse(servicioCuenta.validarSaldoInicial(-0.001));

        verifyNoInteractions(daoCuenta);
    }
}
