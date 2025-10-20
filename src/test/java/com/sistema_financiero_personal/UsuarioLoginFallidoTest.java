package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.daos.DAOUsuarioTest;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuarioTest;
import org.junit.Test;
import org.mockito.Mockito;

public class UsuarioLoginFallidoTest {

    @Test(expected = RuntimeException.class)
    public void given_wrong_credentials_when_login_then_throw_exception() {
        DAOUsuarioTest daoUsuarioTest = Mockito.mock(DAOUsuarioTest.class);
        ServicioUsuarioTest servicioUsuarioTest = new ServicioUsuarioTest(daoUsuarioTest);
        Mockito.when(daoUsuarioTest.credencialesCorrectas("pepe.zambrano@gmail.com", "wrongpass"))
                .thenReturn(false);
        servicioUsuarioTest.loginUsuario("pepe.zambrano@gmail.com", "wrongpass");
    }
}
