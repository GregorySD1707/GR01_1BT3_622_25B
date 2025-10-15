package com.sistema_financiero_personal.movimiento.modelos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("GASTO")
public class Gasto extends Movimiento {
    public Gasto() { }
    public Gasto(double monto, LocalDateTime fecha, String descripcion, String categoria) {
        super(monto, fecha, descripcion, categoria);
    }
}

