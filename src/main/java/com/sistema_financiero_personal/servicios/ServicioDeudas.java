package com.sistema_financiero_personal.servicios;

import com.sistema_financiero_personal.daos.DAODeudaPrestamo;
import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.modelos.EstadoDeudaPrestamo;
import com.sistema_financiero_personal.modelos.TipoDeudaPrestamo;
import java.time.LocalDate;
import java.util.List;

public class ServicioDeudas {
    private final DAODeudaPrestamo daoDeudaPrestamo;
    private final ServicioMovimientos servicioMovimientos;

    public ServicioDeudas() {
        this.daoDeudaPrestamo = new DAODeudaPrestamo();
        this.servicioMovimientos = new ServicioMovimientos();
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

    public void abonarADeuda(Long idDeuda, double monto) {
        DeudaPrestamo deuda = daoDeudaPrestamo.buscarPorId(idDeuda);
        if (deuda != null && deuda.getEstado() == EstadoDeudaPrestamo.PENDIENTE) {
            deuda.registrarAbono(monto);
            daoDeudaPrestamo.actualizar(deuda);
            // Actualizar saldo de cartera según el tipo de deuda/prestamo
            String nombreCartera = "Cartera principal"; // Puedes parametrizar esto si lo necesitas
            if (deuda.getTipo() == com.sistema_financiero_personal.modelos.TipoDeudaPrestamo.DEUDA) {
                servicioMovimientos.registrarGasto(monto, "Abono deuda a " + deuda.getNombrePersona(), "Deudas", nombreCartera);
            } else if (deuda.getTipo() == com.sistema_financiero_personal.modelos.TipoDeudaPrestamo.PRESTAMO) {
                servicioMovimientos.registrarIngreso(monto, "Abono préstamo de " + deuda.getNombrePersona(), "Préstamos", nombreCartera);
            }
        }
    }

    public List<DeudaPrestamo> obtenerDeudasPendientes() {
        return daoDeudaPrestamo.listarPendientes();
    }

    public List<DeudaPrestamo> obtenerPorPersona(String nombrePersona) {
        return daoDeudaPrestamo.listarPorPersona(nombrePersona);
    }
}
