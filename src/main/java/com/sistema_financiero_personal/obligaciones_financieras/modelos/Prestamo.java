package com.sistema_financiero_personal.obligaciones_financieras.modelos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("PRESTAMO")
public class Prestamo extends ObligacionFinanciera {

    public Prestamo(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        super(nombrePersona, montoTotal, fechaPago);
    }

    @Override
    public void registrarMovimientoAsociado(double monto) {
        System.out.println("Aquí se registra el ingreso asociado al préstamo");
        // servicioMovimientos.registrarIngreso(monto, "Abono préstamo de " + getNombrePersona(), "Préstamos", nombreCartera);
    }
}
