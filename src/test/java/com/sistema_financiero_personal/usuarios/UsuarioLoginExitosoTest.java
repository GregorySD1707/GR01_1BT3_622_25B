package com.sistema_financiero_personal.usuarios;

import com.sistema_financiero_personal.usuario.daos.DAOUsuarioTest;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuarioTest;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class UsuarioLoginExitosoTest {

    @Test()
    public void given_data_when_login_then_ok() {
        DAOUsuarioTest daoUsuarioTest = Mockito.mock(DAOUsuarioTest.class);
        ServicioUsuarioTest servicioUsuarioTest = new ServicioUsuarioTest(daoUsuarioTest);
        Mockito.when(daoUsuarioTest.credencialesCorrectas("pepe.zambrano@gmail.com", "abc123")).thenReturn(true);
        boolean resultado = servicioUsuarioTest.loginUsuario("pepe.zambrano@gmail.com", "abc123");
        assertTrue(resultado);
    }
}
