package com.sistema_financiero_personal.usuario.servicios;

import com.sistema_financiero_personal.usuario.daos.DAOUsuarioTest;
import com.sistema_financiero_personal.usuario.modelos.GoogleAuth;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.modelos.UsuarioGoogle;


public class ServicioAutorizacion {
    private final GoogleAuth googleAuth;
    private final DAOUsuarioTest daoUsuarioTest;

    public ServicioAutorizacion(GoogleAuth googleAuth, DAOUsuarioTest daoUsuarioTest) {
        this.googleAuth = googleAuth;
        this.daoUsuarioTest = daoUsuarioTest;
    }

    public Usuario loginConGoogle(String token) {
        UsuarioGoogle usuarioGoogle = validarYObtenerUsuarioGoogle(token);
        return obtenerOCrearUsuario(usuarioGoogle);
    }

    private UsuarioGoogle validarYObtenerUsuarioGoogle(String token) {
        UsuarioGoogle usuarioGoogle = googleAuth.validarToken(token);

        if (usuarioGoogle == null || usuarioGoogle.getCorreo() == null) {
            throw new IllegalArgumentException("Token inválido o usuario de Google sin correo");
        }

        return usuarioGoogle;
    }

    private Usuario obtenerOCrearUsuario(UsuarioGoogle usuarioGoogle) {
        return daoUsuarioTest.buscarPorCorreo(usuarioGoogle.getCorreo())
                .orElseGet(() -> crearNuevoUsuario(usuarioGoogle));
    }

    private Usuario crearNuevoUsuario(UsuarioGoogle usuarioGoogle) {
        Usuario nuevoUsuario = construirUsuarioDesdeGoogle(usuarioGoogle);
        //asignarCarteraAlUsuario(nuevoUsuario);
        // daoUsuario.crear(nuevoUsuario);
        return nuevoUsuario;
    }

    private Usuario construirUsuarioDesdeGoogle(UsuarioGoogle usuarioGoogle) {
        return new Usuario(
                usuarioGoogle.getNombre(),
                usuarioGoogle.getApellido(),
                usuarioGoogle.getCorreo(),
                usuarioGoogle.getCorreo(), // username basado en correo
                null,  // sin contraseña para usuarios de Google
                null   // fecha de nacimiento opcional
        );
    }

//    private void asignarCarteraAlUsuario(Usuario usuario) {
//        Cartera cartera = new Cartera();
//        usuario.setCartera(cartera);
//    }
}