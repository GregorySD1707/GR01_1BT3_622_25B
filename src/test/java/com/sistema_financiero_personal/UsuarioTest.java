package com.sistema_financiero_personal;

import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class UsuarioTest {

    @Test()
    public void given_data_when_login_then_ok() {
        Usuario usuario = new Usuario("Pepe", "Zambrano", "pepe.zambrano@gmail.com",
                "xXXpepeXxx", "abc123", LocalDate.parse("2002-11-27"));
        DAOUsuario daoUsuario = Mockito.mock(DAOUsuario.class);
        Usuario usuarioRegistrado = (Usuario) Mockito.when(daoUsuario.crear(usuario)).thenReturn(usuario);

        assertNotNull(usuarioRegistrado);
    }

    @Test(timeout = 500)
    public void given_dao_when_list_users_then_tiemout(){
        ServicioUsuario servicioUsuario = new ServicioUsuario();
        assertNotNull(servicioUsuario.listar());
    }
}
