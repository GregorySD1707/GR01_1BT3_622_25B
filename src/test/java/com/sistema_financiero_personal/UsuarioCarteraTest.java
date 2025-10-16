package com.sistema_financiero_personal;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import static org.junit.Assert.*;

import org.junit.Test;

import java.time.LocalDate;

public class UsuarioCarteraTest {

    @Test
    public void given_user_when_register_then_create_wallet(){
        Usuario usuario = registrarUsuario("Mateo", "Rodriguez", "mateo@gmail.com", "Matein", "123456", LocalDate.of(2008, 5, 15));
        assertNotNull(usuario.getCartera());
    }

    public Usuario registrarUsuario(String nombre, String apellido, String correo, String nombreUsuario, String contrasena, LocalDate fechaNacimiento){
        Usuario usuario = new Usuario(nombre, apellido, correo, nombreUsuario, contrasena, fechaNacimiento);
        usuario.setCartera(new Cartera());
        DAOUsuario daoUsuario = new DAOUsuario();
        daoUsuario.crear(usuario);
        return usuario;
    }
}
