package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.daos.DAOUsuarioTest;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuarioTest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

public class UsuarioTest {

    private static DAOUsuarioTest daoUsuarioTest;
    private static ServicioUsuarioTest servicioUsuarioTest;
    private static final String CORREO = "pepe.zambrano1234@gmail.com";

    @BeforeClass
    public static void setUpClass(){
        daoUsuarioTest = Mockito.mock(DAOUsuarioTest.class);
        servicioUsuarioTest = new ServicioUsuarioTest(daoUsuarioTest);
    }

    @Test()
    public void given_data_when_sign_up_then_ok() {
        // usuario no existe en BD
        Mockito.when(daoUsuarioTest.existe(CORREO)).thenReturn(false);

        servicioUsuarioTest.registrarUsuario("Pepe", "Zambrano", CORREO,
                "pepe123", "abc123", LocalDate.parse("2002-11-27"));

        // usuario existe en BD
        Mockito.when(daoUsuarioTest.existe(CORREO)).thenReturn(true);
        assertTrue(servicioUsuarioTest.existeUsuario(CORREO));
    }

    @Test(timeout = 700)
    public void given_dao_when_list_users_then_tiemout(){

        Mockito.when(daoUsuarioTest.listar()).thenAnswer(invocationOnMock -> {
            Thread.sleep(500);
            return Arrays.asList(new Usuario());
        });
        assertNotNull(servicioUsuarioTest.listar());
    }
}
