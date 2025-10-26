package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import com.sistema_financiero_personal.usuario.servicios.ServicioRegistroUsuario;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class CuentaTest {
    private static String timestamp = String.valueOf(System.currentTimeMillis());
    private static ServicioRegistroUsuario servicioRegistroUsuario;
    private static Usuario usuarioTest;
    private static ServicioCuenta servicioCuenta;

    @BeforeClass
    public static void setUpClass(){
        servicioRegistroUsuario = new ServicioRegistroUsuario();
        usuarioTest = servicioRegistroUsuario.registrarUsuario("Pepe", "Zambrano", "test" + timestamp + "@gmail.com",
                "Pepe456" + timestamp,  "Abc123$%",    LocalDate.parse("2002-11-27"));
        timestamp = String.valueOf(System.currentTimeMillis());
        servicioCuenta = new ServicioCuenta();
    }

    @Test
    public void given_data_when_create_account_then_ok(){

        String nombre = "test1";
        TipoCuenta tipoCuenta = TipoCuenta.EFECTIVO;
        double monto = 10.0;
        Cuenta cuenta = new Cuenta(nombre, tipoCuenta, monto, usuarioTest.getCartera());

        servicioCuenta.crearCuenta(cuenta);
        assertNotNull(servicioCuenta.buscarCuenta(cuenta.getId()));
    }
}