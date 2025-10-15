package com.sistema_financiero_personal.movimiento.modelos;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 10)
public abstract class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long id;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 120)
    private String descripcion;

    @Column(length = 60)
    private String categoria;

    protected Movimiento() { }

    protected Movimiento(double monto, LocalDateTime fecha, String descripcion, String categoria) {
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }

    public void setMonto(double monto) { this.monto = monto; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}

