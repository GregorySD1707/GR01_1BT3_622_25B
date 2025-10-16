package com.sistema_financiero_personal;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDateTime;

public class UsuarioCarteraTest {
    Usuario usuario = null;
    ServicioUsuario servicio = null;

    @Test
    public void given_user_when_register_then_create_cartera(){
        Usuario usuario = registrarUsuario("pepito", "guaman", "xxXPepito", "ppitosalvaje", "123",LocalDateTime.now());
        assertNotNull(usuario.getCartera());
    }

    public Usuario registrarUsuario(String nombre, String apellido, String correo, String nombreUsuario, String contrasena, LocalDateTime fechaNacimiento) {
        Usuario usuario = new Usuario(nombre, apellido, correo, nombreUsuario, contrasena, fechaNacimiento);
        Cartera cartera = new Cartera();
        DAOUsuario daoUsuario = new DAOUsuario();
        usuario.setCartera(cartera);
        daoUsuario.crear(usuario);
        return usuario;
    }
}
