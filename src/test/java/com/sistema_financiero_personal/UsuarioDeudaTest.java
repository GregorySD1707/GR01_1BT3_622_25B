package com.sistema_financiero_personal;

import com.sistema_financiero_personal.movimiento.daos.DAOCartera;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.modelos.Ingreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import com.sistema_financiero_personal.obligaciones_financieras.daos.DAOObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.ObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.Prestamo;
import com.sistema_financiero_personal.obligaciones_financieras.servicios.ServicioObligacionFinanciera;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UsuarioDeudaTest {
    public void given_user_when_debt_then_ok(){
        Usuario usuarioDeudor = new Usuario();
        Usuario prestamista = new Usuario(); // Es "persona" ¿Es un usuario?
        double monto = 10.0;
        LocalDate fechaDePago = null;
        //TipoDeudaPrestamo tipoDeudaPrestamo = TipoDeudaPrestamo.DEUDA; // Movimiento padre de Ingreso, Gasto, Deuda, Prestamo ???
        //ObligacionFinanciera deudaPrestamo = new ObligacionFinanciera(usuarioDeudor, prestamista, monto, fechaDePago, tipoDeudaPrestamo); // ¿Cómo sería las relaciones en la BD?
        //usuarioDeudor.agregarDeuda(deudaPrestamo);

        //assertEquals(deudaPrestamo, usuarioDeudor.obtenerUltimaDeuda());
    }
}
