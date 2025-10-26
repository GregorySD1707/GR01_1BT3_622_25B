package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class MovimientoCuentaTest {

    private static Cuenta cuenta;
    private static Cartera cartera;
    private static Usuario usuario;
    private static ServicioCuenta servicioCuenta;

    @BeforeClass
    public static void setUpClass(){
        usuario = new Usuario();
        cartera = new Cartera();
        cuenta = new Cuenta("cuentaTest", TipoCuenta.EFECTIVO, 10.0, usuario.getCartera());
        servicioCuenta = new ServicioCuenta();

        usuario.setCartera(cartera);
        cuenta.setCartera(cartera);
    }

    @Test
    public void given_superior_spending_when_make_update_account_amount_then_not_allow(){
        double gastoSuperior = 100.0;
        servicioCuenta.ajustarMonto(cuenta, gastoSuperior);
        assertTrue(cuenta.getMonto() >= 0);
    }
}
