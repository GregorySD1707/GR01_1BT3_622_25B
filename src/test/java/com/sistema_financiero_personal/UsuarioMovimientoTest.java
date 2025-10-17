package com.sistema_financiero_personal;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is; // Import para un assert más legible

import java.time.LocalDate;

public class UsuarioMovimientoTest {
    private static final String NOMBRE = "Mateo";
    private static final String APELLIDO = "Perez";
    private static final String NOMBRE_USUARIO = "Mateiaqiioin";
    private static final String CORREO = "mateoooiioaqo@gmail.com";
    private static final String CONTRASENA = "contrasenaSegura123";
    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(1990, 1, 1);
    private static final double SALARIO_INICIAL = 1000.0;
    private ServicioUsuario servicioUsuario;
    private ServicioMovimiento servicioMovimiento;
    private Usuario usuario;

    @Before
    public void setUp() {
        servicioUsuario = new ServicioUsuario();
        servicioMovimiento = new ServicioMovimiento();
        usuario = servicioUsuario.registrarUsuario(NOMBRE, APELLIDO, NOMBRE_USUARIO, CORREO, CONTRASENA, FECHA_NACIMIENTO);
    }
    @Test
    public void registrarIngreso_cuandoSeAgregaUnSalario_deberiaAumentarElSaldoDeLaCartera() {
        Cartera cartera = usuario.getCartera();
        servicioMovimiento.registrarIngreso(cartera.getId(), SALARIO_INICIAL, "Salario mensual", CategoriaIngreso.OTROS);

        Cartera carteraActualizada = servicioUsuario.obtenerCartera(cartera.getId());

        assertThat("El saldo (usando hamcrest) debería ser el esperado.", carteraActualizada.getSaldo(), is(SALARIO_INICIAL));
    }
}