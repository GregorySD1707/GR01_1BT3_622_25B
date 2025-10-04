package com.sistema_financiero_personal.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "cartera")
public class Cartera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartera_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String nombre;

    @Column(name = "saldo_actual", nullable = false)
    private double saldoActual;

    @Column(length = 30)
    private String tipo; // e.g., EFECTIVO, BANCO

    public Cartera() { }

    public Cartera(String nombre, double saldoActual, String tipo) {
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.tipo = tipo;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public double getSaldoActual() { return saldoActual; }
    public String getTipo() { return tipo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setSaldoActual(double saldoActual) { this.saldoActual = saldoActual; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    // Actualiza el saldo de la cartera aplicando el delta (positivo ingreso, negativo gasto)
    public void actualizarCartera(double monto) {
        this.saldoActual += monto;
    }
}
