package com.sistema_financiero_personal.deuda_prestamo.modelos;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DeudaPrestamo")
public class DeudaPrestamo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombrePersona;

    @Column(nullable = false)
    private double montoTotal;

    @Column(nullable = false)
    private double montoPagado;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDeudaPrestamo tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoDeudaPrestamo estado;

    public DeudaPrestamo() {}

    public DeudaPrestamo(String nombrePersona, double montoTotal, LocalDate fechaPago, TipoDeudaPrestamo tipo) {
        this.nombrePersona = nombrePersona;
        this.montoTotal = montoTotal;
        this.montoPagado = 0.0;
        this.fechaPago = fechaPago;
        this.tipo = tipo;
        this.estado = EstadoDeudaPrestamo.PENDIENTE;
    }

    public Long getId() { return id; }
    public String getNombrePersona() { return nombrePersona; }
    public double getMontoTotal() { return montoTotal; }
    public double getMontoPagado() { return montoPagado; }
    public LocalDate getFechaPago() { return fechaPago; }
    public TipoDeudaPrestamo getTipo() { return tipo; }
    public EstadoDeudaPrestamo getEstado() { return estado; }

    public void setNombrePersona(String nombrePersona) { this.nombrePersona = nombrePersona; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public void setTipo(TipoDeudaPrestamo tipo) { this.tipo = tipo; }
    public void setEstado(EstadoDeudaPrestamo estado) { this.estado = estado; }

    public double calcularSaldoPendiente() {
        return montoTotal - montoPagado;
    }

    public void registrarAbono(double monto) {
        if (monto <= 0) return;
        if (montoPagado + monto > montoTotal) {
            montoPagado = montoTotal;
        } else {
            montoPagado += monto;
        }
        if (montoPagado >= montoTotal) {
            estado = EstadoDeudaPrestamo.PAGADA;
        }
    }
}
