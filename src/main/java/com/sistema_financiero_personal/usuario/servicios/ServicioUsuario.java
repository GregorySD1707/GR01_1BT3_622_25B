package com.sistema_financiero_personal.usuario.servicios;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.time.LocalDate;

public class ServicioUsuario {
    private DAOUsuario daoUsuario;
    public ServicioUsuario() {
        this.daoUsuario = new DAOUsuario();
    }

    public Usuario registrarUsuario(String nombre, String apellido, String correo, String nombreUsuario,
                                    String contrasena, LocalDate fechaNacimiento) {
        Usuario usuario = new Usuario(nombre, apellido, correo, nombreUsuario, contrasena, fechaNacimiento);
        asignarCartera(usuario);
        daoUsuario.crear(usuario);
        return usuario;
    }

    private void asignarCartera(Usuario usuario) {
        Cartera cartera = new Cartera();
        usuario.setCartera(cartera);
    }
}
