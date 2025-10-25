package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    public void given_existing_account_when_create_then_fail() {
        Cartera cartera = new Cartera();
        Cuenta cuentaExistente = new Cuenta("Cuenta Ahorros", TipoCuenta.AHORROS, 200.0, cartera);
        Cuenta cuentaNueva = new Cuenta("Cuenta Ahorros", TipoCuenta.AHORROS, 100.0, cartera);

        List<Cuenta> cuentasMock = Arrays.asList(cuentaExistente);
        when(daoCuenta.listar()).thenReturn(cuentasMock);

        assertThrows(IllegalStateException.class, () -> servicioCuenta.crearCuenta(cuentaNueva));

        verify(daoCuenta, never()).crear(any(Cuenta.class));
    }
}
