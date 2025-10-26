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
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CuentaTest {

    @Mock
    private DAOCuenta daoCuenta;

    private ServicioCuenta servicioCuenta;
    private Usuario usuarioTest;

    @Before
    public void setUp() {
        // Inyectar el DAO mockeado al servicio
        servicioCuenta = new ServicioCuenta(daoCuenta);

        // Crear usuario de prueba (sin registrar en BD)
        usuarioTest = new Usuario();
        usuarioTest.setNombre("Pepe");
        usuarioTest.setApellido("Zambrano");
        usuarioTest.setCartera(new Cartera());
    }

    @Test
    public void given_data_when_create_account_then_ok() {
        // Arrange
        String nombre = "test1";
        TipoCuenta tipoCuenta = TipoCuenta.EFECTIVO;
        double monto = 10.0;
        Cuenta cuenta = new Cuenta(nombre, tipoCuenta, monto, usuarioTest.getCartera());
        cuenta.setId(1L); // Simular ID asignado

        // Configurar el comportamiento del mock
        doNothing().when(daoCuenta).crear(any(Cuenta.class));
        when(daoCuenta.buscarPorId(1L)).thenReturn(cuenta);

        // Act
        servicioCuenta.crearCuenta(cuenta);
        Cuenta cuentaEncontrada = servicioCuenta.buscarCuenta(cuenta.getId());

        // Assert
        assertNotNull("La cuenta debería existir", cuentaEncontrada);
    }

    @Test
    public void given_null_tipo_when_validate_then_throw_and_no_repo_calls() {
        String nombre = "Cuenta sin tipo";
        double monto = 10.0;
        Cartera cartera = new Cartera();
        Cuenta cuenta = new Cuenta(nombre, /* tipo */ null, monto, cartera);
        // verificar que no se hacen llamadas al DAO
        verify(daoCuenta, never()).crear(any(Cuenta.class));
        verify(daoCuenta, never()).buscarPorId(anyLong());

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicioCuenta.validarObligatorios(cuenta)
        );
        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";
        //significa que el mensaje de error debe referirse al tipo de cuenta
        assertTrue("El mensaje debe referir al tipo de cuenta", msg.contains("tipo"));

    }
}