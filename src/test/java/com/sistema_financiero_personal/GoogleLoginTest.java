package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.GoogleAuth;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.modelos.UsuarioGoogle;
import com.sistema_financiero_personal.usuario.servicios.ServicioAutorizacion;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
public class GoogleLoginTest {
    @Test
    public void given_googleUser_when_login_then_registresOrReturnExistingUser() {
        String token = "tokenValido";
        UsuarioGoogle usuarioGoogle = new UsuarioGoogle(
                "mateo@gmail.com",
                "Mateo",
                "Perez"
        );
        GoogleAuth googleAuth = Mockito.mock(GoogleAuth.class);
        DAOUsuario daoUsuario = Mockito.mock(DAOUsuario.class);
        ServicioAutorizacion servicioAutorizacion = new ServicioAutorizacion(googleAuth, daoUsuario);

        Mockito.when(googleAuth.validarToken(token)).thenReturn(usuarioGoogle);
        Mockito.when(daoUsuario.buscarPorCorreo(usuarioGoogle.getCorreo())).thenReturn(Optional.empty());

        Usuario usuario = servicioAutorizacion.loginConGoogle(token);

        assertNotNull("El usuario no debería ser null", usuario);
        assertEquals("Mateo", usuario.getNombre());
        assertEquals("Perez", usuario.getApellido());
        assertEquals("mateo@gmail.com", usuario.getCorreo());
    }
}
