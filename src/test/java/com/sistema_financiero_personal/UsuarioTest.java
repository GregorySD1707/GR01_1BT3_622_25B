package com.sistema_financiero_personal;

import com.sistema_financiero_personal.resumen_financiero.daos.DAOResumenFinanciero;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class UsuarioTest {

    private static DAOUsuario daoUsuario;
    private static ServicioUsuario servicioUsuario;
    private static final String CORREO = "pepe.zambrano1234@gmail.com";

    @BeforeClass
    public static void setUpClass(){
        daoUsuario = Mockito.mock(DAOUsuario.class);
        servicioUsuario = new ServicioUsuario(daoUsuario);
    }

    @Test()
    public void given_data_when_sign_up_then_ok() {
        // usuario no existe en BD
        Mockito.when(daoUsuario.existe(CORREO)).thenReturn(false);

        servicioUsuario.registrarUsuario("Pepe", "Zambrano", CORREO,
                "pepe123", "abc123", LocalDate.parse("2002-11-27"));

        // usuario existe en BD
        Mockito.when(daoUsuario.existe(CORREO)).thenReturn(true);
        assertTrue(servicioUsuario.existeUsuario(CORREO));
    }

    @Test(timeout = 700)
    public void given_dao_when_list_users_then_tiemout(){

        Mockito.when(daoUsuario.listar()).thenAnswer(invocationOnMock -> {
            Thread.sleep(500);
            return Arrays.asList(new Usuario());
        });
        assertNotNull(servicioUsuario.listar());
    }
}
