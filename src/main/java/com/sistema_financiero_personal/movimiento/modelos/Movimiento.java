package com.sistema_financiero_personal.movimiento.modelos;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", length = 10)

public abstract class Movimiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Long movimiento_id;

    @Column(nullable = false)
    private double monto;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 120)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartera_id", nullable = false)
    private Cartera cartera;

    public Movimiento() { }

    protected Movimiento(double monto, LocalDateTime fecha, String descripcion) {
        this.monto = monto;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public Long getId() { return movimiento_id; }
    public double getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public Cartera getCartera() { return cartera; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setCartera(Cartera cartera) { this.cartera = cartera; }
}

