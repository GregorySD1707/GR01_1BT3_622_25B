package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class CuentaParamTest {

    @Mock
    private DAOCuenta daoCuenta;

    private ServicioCuenta servicioCuenta;
    private Usuario usuarioTest;

    @Parameterized.Parameter(0)
    public String nombreCuenta;

    @Parameterized.Parameter(1)
    public TipoCuenta tipoCuenta;

    @Parameterized.Parameter(2)
    public double montoInicial;

    @Before
    public void setUp() {
        // Inicializar mocks
        MockitoAnnotations.openMocks(this);

        // Crear servicio con DAO mockeado
        servicioCuenta = new ServicioCuenta(daoCuenta);

        // Crear usuario de prueba sin persistir
        usuarioTest = new Usuario();
        usuarioTest.setNombre("Pepe");
        usuarioTest.setApellido("Zambrano");
        usuarioTest.setCartera(new Cartera());
    }

    @Parameterized.Parameters(name = "{index}: nombre=\"{0}\", tipo={1}, monto={2}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // nombre, tipoCuenta, monto
                {"Cuenta Efectivo", TipoCuenta.EFECTIVO, 100.0},
                {"Cuenta Vacaciones", TipoCuenta.AHORROS, 500.50},
                {"Cuenta Corriente", TipoCuenta.CORRIENTE, 1000.0},
                {"Ahorro Personal", TipoCuenta.EFECTIVO, 0.0},
                {"Cuenta Principal", TipoCuenta.CORRIENTE, 2500.75}
        });
    }

    @Test
    public void given_data_when_create_account_then_ok() {
        // Arrange
        Cuenta cuenta = new Cuenta(nombreCuenta, tipoCuenta, montoInicial, usuarioTest.getCartera());
        cuenta.setId(1L); // Simular ID asignado

        // Configurar comportamiento del mock
        doNothing().when(daoCuenta).crear(any(Cuenta.class));
        when(daoCuenta.buscarPorId(1L)).thenReturn(cuenta);

        // Act
        servicioCuenta.crearCuenta(cuenta);
        Cuenta cuentaEncontrada = servicioCuenta.buscarCuenta(cuenta.getId());

        // Assert
        assertNotNull("La cuenta debería existir después de crearla", cuentaEncontrada);
        assertEquals("El nombre no coincide", nombreCuenta, cuentaEncontrada.getNombre());
        assertEquals("El tipo de cuenta no coincide", tipoCuenta, cuentaEncontrada.getTipo());
        assertEquals("El monto no coincide", montoInicial, cuentaEncontrada.getMonto(), 0.001);

        // Verificar interacciones
        verify(daoCuenta, times(1)).crear(cuenta);
        verify(daoCuenta, times(1)).buscarPorId(1L);
    }
}