package com.sistema_financiero_personal.modelos;

import jakarta.persistence.*;

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

    public ResumenFinanciero() {
        this.ingresosTotales = -1.0;
        this.gastosTotales = -1.0;
        this.ahorroNeto = -1.0;
    }

    public ResumenFinanciero(double ingresosTotales, double gastosTotales, double ahorroNeto) {
        this.ingresosTotales = ingresosTotales;
        this.gastosTotales = gastosTotales;
        this.ahorroNeto = ahorroNeto;
    }

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    public double getGastosTotales() {
        return gastosTotales;
    }

    public double getAhorroNeto() {
        return ahorroNeto;
    }

    public void setIngresosTotales(double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public void setGastosTotales(double gastosTotales) {
        this.gastosTotales = gastosTotales;
    }

    public void setAhorroNeto(double ahorroNeto) {
        this.ahorroNeto = ahorroNeto;
    }
}
