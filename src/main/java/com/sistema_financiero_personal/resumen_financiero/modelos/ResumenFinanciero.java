package com.sistema_financiero_personal.resumen_financiero.modelos;

import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "Resumen_Financiero")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "documento_pdf_id")
    private DocumentoPDF documentoPDF;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public ResumenFinanciero() {
        this.ingresosTotales = -1.0;
        this.gastosTotales = -1.0;
        this.ahorroNeto = -1.0;
        fechaPeriodoAnterior = null;
        fechaPeriodoActual = null;
        documentoPDF = null;
        this.fechaCreacion = LocalDateTime.now();
        System.out.println("prueba docker");
    }

    public ResumenFinanciero(Double ingresosTotales, double gastosTotales, LocalDate fechaPeriodoAnterior, LocalDate fechaPeriodoActual, DocumentoPDF documentoPDF) {
        this.ingresosTotales = ingresosTotales;
        this.gastosTotales = gastosTotales;
        this.fechaPeriodoAnterior = fechaPeriodoAnterior;
        this.fechaPeriodoActual = fechaPeriodoActual;
        this.documentoPDF = documentoPDF;
        this.fechaCreacion = LocalDateTime.now();
    }

    public double getAhorroNeto() {return calcularAhorroNeto();}

    public double calcularAhorroNeto(){return ingresosTotales - gastosTotales;}

    public Long getId(){return id;}

    public double getIngresosTotales() {return ingresosTotales;}

    public double getGastosTotales() {return gastosTotales;}

    public LocalDate getFechaPeriodoAnterior() {return  fechaPeriodoAnterior;}

    public LocalDate getFechaPeriodoActual() {return  fechaPeriodoActual;}

    public DocumentoPDF getDocumentoPDF() {return documentoPDF;}

    public void setIngresosTotales(double ingresosTotales) {this.ingresosTotales = ingresosTotales;}

    public void setGastosTotales(double gastosTotales) {this.gastosTotales = gastosTotales;}

    public void setAhorroNeto(double ahorroNeto) {this.ahorroNeto = ahorroNeto;}

    public void setFechaPeriodoAnterior(LocalDate fechaPeriodoAnterior){ this.fechaPeriodoAnterior = fechaPeriodoAnterior;}

    public void setFechaPeriodoActual(LocalDate fechaPeriodoActual){ this.fechaPeriodoActual = fechaPeriodoActual;}

    public void setDocumentoPDF(DocumentoPDF documentoPDF){this.documentoPDF = documentoPDF;}

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }

    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public String getFechaCreacionFormateada() {
        if (fechaCreacion == null) return "";
        return fechaCreacion.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public Usuario getUsuario() {
        return usuario;
    }
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
