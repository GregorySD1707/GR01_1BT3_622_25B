package com.sistema_financiero_personal;

import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.modelos.TipoDeudaPrestamo;
import com.sistema_financiero_personal.usuario.Usuario;

import java.time.LocalDate;

public class UsuarioDeudaTest {
    public void given_user_when_debt_then_ok(){
        Usuario usuarioDeudor = new Usuario();
        Usuario prestamista = new Usuario(); // Es "persona" ¿Es un usuario?
        double monto = 10.0;
        LocalDate fechaDePago = null;
        TipoDeudaPrestamo tipoDeudaPrestamo = TipoDeudaPrestamo.DEUDA; // Movimiento padre de Ingreso, Gasto, Deuda, Prestamo ???
        //DeudaPrestamo deudaPrestamo = new DeudaPrestamo(usuarioDeudor, prestamista, monto, fechaDePago, tipoDeudaPrestamo); // ¿Cómo sería las relaciones en la BD?
        //usuarioDeudor.agregarDeuda(deudaPrestamo);

        //assertEquals(deudaPrestamo, usuarioDeudor.obtenerUltimaDeuda());
    }
}
