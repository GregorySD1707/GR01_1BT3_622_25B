package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.Test;
import org.mockito.Mockito;

public class UsuarioLoginFallidoTest {

    @Test(expected = RuntimeException.class)
    public void given_wrong_credentials_when_login_then_throw_exception() {
        DAOUsuario daoUsuario = Mockito.mock(DAOUsuario.class);
        ServicioUsuario servicioUsuario = new ServicioUsuario(daoUsuario);
        Mockito.when(daoUsuario.credencialesCorrectas("pepe.zambrano@gmail.com", "wrongpass"))
                .thenReturn(false);
        servicioUsuario.loginUsuario("pepe.zambrano@gmail.com", "wrongpass");
    }
}
