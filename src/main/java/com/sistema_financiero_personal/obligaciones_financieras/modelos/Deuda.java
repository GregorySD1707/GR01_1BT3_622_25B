package com.sistema_financiero_personal.obligaciones_financieras.modelos;

import com.sistema_financiero_personal.servicios.ServicioMovimientos;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("DEUDA")
public class Deuda extends ObligacionFinanciera{

    public Deuda(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        super(nombrePersona, montoTotal, fechaPago);
    }

    @Override
    public void registrarMovimientoAsociado(double monto) {
        System.out.println("Aquí se registra el gasto asociado a la deuda");
        //servicioMovimientos.registrarGasto(monto, "Abono deuda a " + getNombrePersona(), "Deudas", nombreCartera);
    }

}
