package com.sistema_financiero_personal;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import static org.junit.Assert.*;
import org.junit.Test;

import java.time.LocalDate;

public class UsuarioCarteraTest {

    @Test
    public void given_user_when_register_then_create_cartera(){

        Usuario usuario = new Usuario("pepito", "guaman", "xxXPepito", "ppitosalvaje", "123", LocalDate.now());
        Cartera cartera = new Cartera();
        DAOUsuario daoUsuario = new DAOUsuario();

        usuario.setCartera(cartera);
        daoUsuario.crear(usuario);

        assertNotNull(usuario.getCartera());
    }
}
