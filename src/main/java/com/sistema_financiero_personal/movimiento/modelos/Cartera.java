package com.sistema_financiero_personal.movimiento.modelos;

import com.sistema_financiero_personal.movimiento.modelos.Gasto;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.persistence.*;

import java.math.BigDecimal; // 1. Importar BigDecimal
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cartera")
public class Cartera {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cartera_id")
    private Long id;

    @Column(name = "saldo", nullable = false)
    private double saldo;

    @OneToMany(
            mappedBy = "cartera",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Movimiento> movimientos = new ArrayList<>();

    @OneToOne(mappedBy = "cartera", cascade = CascadeType.ALL, orphanRemoval = true)
    private Usuario usuario;

    public Cartera() {
        this.saldo = 0.0;
    }

    public void addMovimiento(Movimiento movimiento) {
        this.movimientos.add(movimiento);
        movimiento.setCartera(this);
    }

    public void ajustarSaldo(double cambio) {
        this.saldo += cambio;
    }

    public Long getId() { return id; }
    public double getSaldo() { return saldo; }
    public List<Movimiento> getMovimientos() { return movimientos; }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}