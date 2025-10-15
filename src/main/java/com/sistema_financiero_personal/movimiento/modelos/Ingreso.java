package com.sistema_financiero_personal.movimiento.modelos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("INGRESO")
public class Ingreso extends Movimiento {
    public Ingreso() { }
    public Ingreso(double monto, LocalDateTime fecha, String descripcion, String categoria) {
        super(monto, fecha, descripcion, categoria);
    }
}

