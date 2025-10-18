package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UsuarioLoginExitosoTest {

    @Test()
    public void given_data_when_login_then_ok() {
        DAOUsuario daoUsuario = Mockito.mock(DAOUsuario.class);
        ServicioUsuario servicioUsuario = new ServicioUsuario(daoUsuario);
        Mockito.when(daoUsuario.credencialesCorrectas("pepe.zambrano@gmail.com", "abc123")).thenReturn(true);
        boolean resultado = servicioUsuario.loginUsuario("pepe.zambrano@gmail.com", "abc123");
        assertTrue(resultado);
    }
}
