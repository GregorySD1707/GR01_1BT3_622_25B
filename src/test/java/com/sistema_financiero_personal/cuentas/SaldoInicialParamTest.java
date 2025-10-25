package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class SaldoInicialParamTest {

    @Mock
    private DAOCuenta daoCuenta;

    private ServicioCuenta servicioCuenta;
    private Usuario usuarioTest;

    @Parameterized.Parameter(0)
    public double saldoInicial;

    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        servicioCuenta = new ServicioCuenta(daoCuenta);

        usuarioTest = new Usuario();
        usuarioTest.setNombre("Mateo");
        usuarioTest.setApellido("Yunga");
        usuarioTest.setCartera(new Cartera());
    }

    @Parameterized.Parameters(name = "{index}: saldo={0} => esperado={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // saldo, resultado esperado
                {-100.0, false},    // saldo negativo
                {-1.0, false},      // saldo negativo
                {0.0, false},       // saldo cero
                {0.01, true},       // saldo positivo mínimo
                {100.0, true}       // saldo positivo normal
        });
    }

    @Test
    public void given_saldo_inicial_when_validate_then_expected_result() {
        // Act
        boolean result = servicioCuenta.validarSaldoInicial(saldoInicial);

        // Assert
        assertEquals(
                "Fallo al validar el saldo inicial: " + saldoInicial,
                expectedResult,
                result
        );
    }
}