package com.sistema_financiero_personal.usuarios;

import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;

import java.util.List;

public class Usuario {
    private String nombre;
    private String apellido;

    public Usuario(String nombre, String apellido, String mail, String nombreUsuario, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getName() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
