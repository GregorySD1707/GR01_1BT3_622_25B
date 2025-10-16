package com.sistema_financiero_personal.usuario.modelos;

public class UsuarioGoogle {
    private String correo, nombre, apellido;
    public UsuarioGoogle(String correo, String nombre, String apellido) {
        this.correo = correo;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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
}
