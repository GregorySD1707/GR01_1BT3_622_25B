package com.sistema_financiero_personal.movimiento.modelos;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.persistence.*;

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
    private List<Cuenta> cuentas = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    public Cartera() {
        this.saldo = 0.0;
    }

    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        cuenta.setCartera(this);
        recalcularSaldo();
    }

    public void recalcularSaldo() {
        this.saldo = cuentas.stream()
                .mapToDouble(Cuenta::getMonto)
                .sum();
    }

    // Getters
    public Long getId() { return id; }
    public double getSaldo() { return saldo; }
    public List<Cuenta> getCuentas() { return cuentas; }
    public Usuario getUsuario() { return usuario; }

    // Setters
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public void setId(Long id) { this.id = id; }
}