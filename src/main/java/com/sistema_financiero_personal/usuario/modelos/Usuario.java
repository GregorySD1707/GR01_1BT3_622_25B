package com.sistema_financiero_personal.usuario.modelos;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.recordatorio.modelos.Recordatorio;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un usuario del sistema.
 * Contiene la información personal esencial y las credenciales para la autenticación.
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String apellido;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(name="nombre_usuario",nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(nullable = false, length = 60) // Longitud ideal para un hash de BCrypt
    private String contrasena;

    @Column(name="fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name="fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Recordatorio> recordatorios = new ArrayList<>();

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cartera cartera;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumenFinanciero> resumenesFinancieros = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String correo, String nombreUsuario, String contrasena, LocalDate fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.fechaCreacion = LocalDateTime.now();
        this.fechaNacimiento = fechaNacimiento;
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setCartera(Cartera cartera) {
        this.cartera = cartera;
        if (cartera != null) {
            cartera.setUsuario(this);
        }
    }
    public Cartera getCartera() {
        return cartera;
    }
    public List<Recordatorio> getRecordatorios() {
        return recordatorios;
    }

    public void setRecordatorios(List<Recordatorio> recordatorios) {
        this.recordatorios = recordatorios;
    }
    // Getter
    public List<ResumenFinanciero> getResumenesFinancieros() {
        return resumenesFinancieros;
    }

    public void setResumenesFinancieros(List<ResumenFinanciero> resumenesFinancieros) {
        this.resumenesFinancieros = resumenesFinancieros;
    }

    public void addResumenFinanciero(ResumenFinanciero resumen) {
        resumenesFinancieros.add(resumen);
        resumen.setUsuario(this);
    }

    public void removeResumenFinanciero(ResumenFinanciero resumen) {
        resumenesFinancieros.remove(resumen);
        resumen.setUsuario(null);
    }
}