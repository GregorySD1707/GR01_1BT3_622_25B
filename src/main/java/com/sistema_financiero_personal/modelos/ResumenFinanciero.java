package com.sistema_financiero_personal.modelos;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ResumenFinanciero")
public class ResumenFinanciero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resumen_financiero_id")
    private Long id;

    @Column(name="ingresos_totales", nullable = false)
    private double ingresosTotales;

    @Column(name="gastos_totales",nullable = false)
    private double gastosTotales;

    @Column(name="ahorro_neto",nullable = false)
    private double ahorroNeto;

    @Column(name="fecha_periodo_anterior", nullable = false)
    private LocalDate fechaPeriodoAnterior;

    @Column(name="fecha_periodo_actual", nullable = false)
    private LocalDate fechaPeriodoActual;

    @Column(name="documento_pdf_id", nullable = false)
    private int documentoPDFId;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    public ResumenFinanciero() {
        this.ingresosTotales = -1.0;
        this.gastosTotales = -1.0;
        this.ahorroNeto = -1.0;
        fechaPeriodoAnterior = null;
        fechaPeriodoActual = null;
        documentoPDFId = -1;
        this.fechaCreacion = LocalDateTime.now();
    }

    public ResumenFinanciero(Double ingresosTotales, double gastosTotales, double ahorroNeto, LocalDate fechaPeriodoAnterior, LocalDate fechaPeriodoActual, int documentoPDFId) {
        this.ingresosTotales = ingresosTotales;
        this.gastosTotales = gastosTotales;
        this.ahorroNeto = ahorroNeto;
        this.fechaPeriodoAnterior = fechaPeriodoAnterior;
        this.fechaPeriodoActual = fechaPeriodoActual;
        this.documentoPDFId = documentoPDFId;
        this.fechaCreacion = LocalDateTime.now();
    }
    public Long getId(){return id;}

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    public double getGastosTotales() {
        return gastosTotales;
    }

    public double getAhorroNeto() {
        return ahorroNeto;
    }

    public LocalDate getFechaPeriodoAnterior() {return  fechaPeriodoAnterior;}

    public LocalDate getFechaPeriodoActual() {return  fechaPeriodoActual;}

    public int getDocumentoPDFId() {return documentoPDFId;}

    public void setIngresosTotales(double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public void setGastosTotales(double gastosTotales) {
        this.gastosTotales = gastosTotales;
    }

    public void setAhorroNeto(double ahorroNeto) {
        this.ahorroNeto = ahorroNeto;
    }

    public void setFechaPeriodoAnterior(LocalDate fechaPeriodoAnterior){ this.fechaPeriodoAnterior = fechaPeriodoAnterior;}

    public void setFechaPeriodoActual(LocalDate fechaPeriodoActual){ this.fechaPeriodoActual = fechaPeriodoActual;}

    public void setDocumentoPDFId(int documentoPDFId){this.documentoPDFId = documentoPDFId;}

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaCreacionFormateada() {
        if (fechaCreacion == null) return "";
        return fechaCreacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

}
