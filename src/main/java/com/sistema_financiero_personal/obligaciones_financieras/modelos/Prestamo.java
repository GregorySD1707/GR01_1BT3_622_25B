package com.sistema_financiero_personal.obligaciones_financieras.modelos;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PRESTAMO")
public class Prestamo extends ObligacionFinanciera {
    @Transient
    ServicioMovimiento servicioMovimiento;

    public Prestamo(){
        this.servicioMovimiento = new ServicioMovimiento();
    } // Constructor sin parámetros requerido por JPA

    public Prestamo(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        super(nombrePersona, montoTotal, fechaPago);
    }

    @Override
    public void registrarMovimientoAsociado(Long idCartera, double monto) {
        servicioMovimiento.registrarIngreso(idCartera, monto, "Abono préstamo de " + getNombrePersona(), CategoriaIngreso.ABONO_PRESTAMO);
    }
}
