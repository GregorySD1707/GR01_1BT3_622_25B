package com.sistema_financiero_personal;

import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.usuario.Usuario;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class UsuarioPrestamoTest {

    @Test
    public void given_user_when_loan_then_ok() {
        Usuario usuarioPrestamista = new Usuario();
        Usuario deudor = new Usuario();
        double monto = 10.0;
        LocalDate fechaDePago = null;

        DeudaPrestamo deudaPrestamo = new DeudaPrestamo(usuarioPrestamista, deudor, monto, fechaDePago, tipoDeudaPrestamo.Prestamo);
        usuarioPrestamista.guardarPrestamo(deudaPrestamo);
        assertEquals(deudaPrestamo, usuarioPrestamista.obtenerUltimoPrestamo());
    }
}
