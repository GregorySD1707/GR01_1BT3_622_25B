package com.sistema_financiero_personal.usuario;

public class Usuario {
    private String nombre;
    private String apellido;

    public Usuario(String nombre, String apellido, String mail, String nombreUsuario, String clave) {
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public Usuario() {

    }

    public String getName() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }
}
