package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UsuarioTest {

    @Test
    public void given_data_when_login_then_ok() {

        Usuario usuario = new Usuario("Pepe", "Zambrano", "pepe.zambrano@gmail.com",
                "xXXpepeXxx", "abc123", LocalDate.parse("2002-11-27"));
        DAOUsuario daoUsuario = new DAOUsuario();

        daoUsuario.crear(usuario);

        assertEquals("Pepe", usuario.getNombreUsuario());
    }

}
