package com.sistema_financiero_personal.usuarios;

import com.sistema_financiero_personal.usuario.daos.DAOUsuarioTest;
import com.sistema_financiero_personal.usuario.modelos.GoogleAuth;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.modelos.UsuarioGoogle;
import com.sistema_financiero_personal.usuario.servicios.ServicioAutorizacion;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;

public class GoogleLoginTest {
    private static final String TOKEN_VALIDO = "tokenValido";
    private static final String CORREO = "mateo@gmail.com";
    private static final String NOMBRE = "Mateo";
    private static final String APELLIDO = "Perez";
    private GoogleAuth googleAuth;
    private ServicioAutorizacion servicioAutorizacion;
    private DAOUsuarioTest daoUsuarioTest;

    @Before
    public void setUp(){
        googleAuth = Mockito.mock(GoogleAuth.class);
        daoUsuarioTest = Mockito.mock(DAOUsuarioTest.class);
        servicioAutorizacion = new ServicioAutorizacion(googleAuth, daoUsuarioTest);
    }
    @Test
    public void given_googleUser_when_login_then_registresOrReturnExistingUser() {
        UsuarioGoogle usuarioGoogle = crearUsuarioGoogle(CORREO, NOMBRE, APELLIDO);
        configurarMocks(usuarioGoogle);
        Usuario usuario = servicioAutorizacion.loginConGoogle(TOKEN_VALIDO);
        assertNotNull("usuario no deberia ser null", usuario);
        assertEquals(CORREO, usuario.getCorreo());
        assertEquals(NOMBRE, usuario.getNombre());
        assertEquals(APELLIDO, usuario.getApellido());
    }
    private void configurarMocks(UsuarioGoogle usuarioGoogle) {
        Mockito.when(googleAuth.validarToken(TOKEN_VALIDO)).thenReturn(usuarioGoogle);
        Mockito.when(daoUsuarioTest.buscarPorCorreo(usuarioGoogle.getCorreo()))
                .thenReturn(Optional.empty());
    }
    private UsuarioGoogle crearUsuarioGoogle(String email, String nombre, String apellido) {
          return  new UsuarioGoogle(email, nombre, apellido);
    }
}

