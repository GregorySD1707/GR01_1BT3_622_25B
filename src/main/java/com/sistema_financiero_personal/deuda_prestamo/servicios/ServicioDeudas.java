package com.sistema_financiero_personal.deuda_prestamo.servicios;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.sistema_financiero_personal.deuda_prestamo.daos.DAODeudaPrestamo;
import com.sistema_financiero_personal.deuda_prestamo.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.deuda_prestamo.modelos.EstadoDeudaPrestamo;
import com.sistema_financiero_personal.deuda_prestamo.modelos.TipoDeudaPrestamo;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;

public class ServicioDeudas {
    private final DAODeudaPrestamo daoDeudaPrestamo;
    private final ServicioMovimiento servicioMovimiento;

    public ServicioDeudas() {
        this.daoDeudaPrestamo = new DAODeudaPrestamo();
        this.servicioMovimiento = new ServicioMovimiento();
    }

    public DeudaPrestamo registrarDeuda(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        DeudaPrestamo deuda = new DeudaPrestamo(nombrePersona, montoTotal, fechaPago, TipoDeudaPrestamo.DEUDA);
        daoDeudaPrestamo.crear(deuda);
        return deuda;
    }

    public DeudaPrestamo registrarPrestamo(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        DeudaPrestamo prestamo = new DeudaPrestamo(nombrePersona, montoTotal, fechaPago, TipoDeudaPrestamo.PRESTAMO);
        daoDeudaPrestamo.crear(prestamo);
        return prestamo;
    }

    public void abonarADeuda(Long idDeuda, Long idCartera, double monto) {
        DeudaPrestamo deuda = daoDeudaPrestamo.buscarPorId(idDeuda);
        if (deuda != null && deuda.getEstado() == EstadoDeudaPrestamo.PENDIENTE) {
            deuda.registrarAbono(monto);
            daoDeudaPrestamo.actualizar(deuda);
            registrarMovimientoPorAbono(idCartera, deuda, monto);
        }
    }

    private void registrarMovimientoPorAbono(Long idCartera, DeudaPrestamo deuda, double monto) {
         if (deuda.getTipo() == com.sistema_financiero_personal.deuda_prestamo.modelos.TipoDeudaPrestamo.DEUDA) {
            servicioMovimiento.registrarGasto(idCartera, monto,"Abono deuda a " + deuda.getNombrePersona(), CategoriaGasto.ABONO_DEUDA);
             } else if (deuda.getTipo() == com.sistema_financiero_personal.deuda_prestamo.modelos.TipoDeudaPrestamo.PRESTAMO) {
            servicioMovimiento.registrarIngreso(idCartera, monto, "Abono préstamo de " + deuda.getNombrePersona(), CategoriaIngreso.ABONO_PRESTAMO);
        }
    }

    public List<DeudaPrestamo> obtenerDeudasPendientes() {
        return daoDeudaPrestamo.listarPendientes();
    }

    public List<DeudaPrestamo> obtenerPorPersona(String nombrePersona) {
        return daoDeudaPrestamo.listarPorPersona(nombrePersona);
    }
}
