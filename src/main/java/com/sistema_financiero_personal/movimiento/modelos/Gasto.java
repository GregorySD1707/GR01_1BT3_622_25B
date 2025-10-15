package com.sistema_financiero_personal.movimiento.modelos;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("GASTO")
public class Gasto extends Movimiento {
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    CategoriaGasto categoria;
    public Gasto() { }
    public Gasto(double monto, LocalDateTime fecha, String descripcion, CategoriaGasto categoria) {
        super(monto, fecha, descripcion);
        this.categoria = categoria;
    }

    public CategoriaGasto getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaGasto categoria) {
        this.categoria = categoria;
    }
}

