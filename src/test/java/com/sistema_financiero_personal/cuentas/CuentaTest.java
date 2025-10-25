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
        // assertEquals("El nombre no coincide", nombre, cuentaEncontrada.getNombre());
        // assertEquals("El tipo no coincide", tipoCuenta, cuentaEncontrada.getTipo());
        // assertEquals("El monto no coincide", monto, cuentaEncontrada.getMonto(), 0.001);
    }
}