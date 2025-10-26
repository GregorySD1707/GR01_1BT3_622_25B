package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CuentaDuplicadaTest {

    @Mock
    private DAOCuenta daoCuenta;

    private ServicioCuenta servicioCuenta;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servicioCuenta = new ServicioCuenta(daoCuenta);
    }

    @Test
    public void given_no_existing_account_when_check_duplicate_then_returns_false() {

        when(daoCuenta.existeCuentaPorNombreYTipo("Ahorros", TipoCuenta.AHORROS, 1L))
                .thenReturn(false);

        boolean resultado = servicioCuenta.existeCuentaDuplicada("Ahorros", TipoCuenta.AHORROS, 1L);

        assertFalse(resultado);
        verify(daoCuenta, times(1))
                .existeCuentaPorNombreYTipo("Ahorros", TipoCuenta.AHORROS, 1L);
    }

    @Test
    public void given_existing_account_when_check_duplicate_then_returns_true() {

        when(daoCuenta.existeCuentaPorNombreYTipo("Corriente", TipoCuenta.CORRIENTE, 1L))
                .thenReturn(true);

        boolean resultado = servicioCuenta.existeCuentaDuplicada("Corriente", TipoCuenta.CORRIENTE, 1L);

        assertTrue(resultado);
        verify(daoCuenta, times(1))
                .existeCuentaPorNombreYTipo("Corriente", TipoCuenta.CORRIENTE, 1L);
    }

    @Test
    public void given_different_wallet_when_check_duplicate_then_returns_false() {

        when(daoCuenta.existeCuentaPorNombreYTipo("Ahorros", TipoCuenta.AHORROS, 2L))
                .thenReturn(false);

        boolean resultado = servicioCuenta.existeCuentaDuplicada("Ahorros", TipoCuenta.AHORROS, 2L);

        assertFalse(resultado);
        verify(daoCuenta, times(1))
                .existeCuentaPorNombreYTipo("Ahorros", TipoCuenta.AHORROS, 2L);
    }
}
