package com.sistema_financiero_personal.obligaciones_financieras.modelos;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "obligacion_financiera")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_obligacion", discriminatorType = DiscriminatorType.STRING)
public abstract class ObligacionFinanciera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "obligacion_financiera_id")
    private Long id;

    @Column(name = "nombre_persona", nullable = false)
    private String nombrePersona;

    @Column(name = "monto_total", nullable = false)
    private double montoTotal;

    @Column(name = "monto_pagado", nullable = false)
    private double montoPagado;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoObligacionFinanciera estado;

    public ObligacionFinanciera() {} // Constructor sin parámetros requerido por JPA

    public ObligacionFinanciera(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        this.nombrePersona = nombrePersona;
        this.montoTotal = montoTotal;
        this.montoPagado = 0.0;
        this.fechaPago = fechaPago;
        this.estado = EstadoObligacionFinanciera.PENDIENTE;
    }

    public Long getId() { return id; }
    public String getNombrePersona() { return nombrePersona; }
    public double getMontoTotal() { return montoTotal; }
    public double getMontoPagado() { return montoPagado; }
    public LocalDate getFechaPago() { return fechaPago; }
    public EstadoObligacionFinanciera getEstado() { return estado; }

    public void setNombrePersona(String nombrePersona) { this.nombrePersona = nombrePersona; }
    public void setMontoTotal(double montoTotal) { this.montoTotal = montoTotal; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }
    public void setEstado(EstadoObligacionFinanciera estado) { this.estado = estado; }

    public double calcularSaldoPendiente() {
        return montoTotal - montoPagado;
    }

    public void registrarAbono(double monto) {
        if (monto <= 0) return;

        // montoPago + monto > montoTotal -> se pasó del total, escogemos montoTotal
        // montoPago + monto < montoTotal -> todavía no se pasa del total (todavía queda por pagar) escogemos montoPago + monto
        montoPagado = Math.min(montoPagado + monto, montoTotal); // Menor valor entre la suma y el total, evitando que se exceda

        estado = (montoPagado >= montoTotal) ? EstadoObligacionFinanciera.PAGADA : estado;
    }

    public abstract void registrarMovimientoAsociado(Long idCartera, double monto);
}
