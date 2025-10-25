package com.sistema_financiero_personal.cuentas.modelos;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "cuenta")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuenta_id")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false)
    private TipoCuenta tipo;

    @Column(name = "monto", nullable = false)
    private double monto;

    // Muchas cuentas pertenecen a UNA cartera
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartera_id", nullable = false)
    private Cartera cartera;

    public Cuenta(){}

    public Cuenta(String nombre, TipoCuenta tipoCuenta, double montoInicial, Cartera cartera) {
        this.nombre = nombre;
        this.tipo = tipoCuenta;
        this.monto = montoInicial;
        this.cartera = cartera;
    }

    // Getters y Setters
    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoCuenta getTipo() { return tipo; }
    public void setTipo(TipoCuenta tipo) { this.tipo = tipo; }

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }

    public Cartera getCartera() { return cartera; }
    public void setCartera(Cartera cartera) { this.cartera = cartera; }
}
