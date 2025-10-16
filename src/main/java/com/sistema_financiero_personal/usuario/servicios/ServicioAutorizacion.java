package com.sistema_financiero_personal.usuario.servicios;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.GoogleAuth;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.modelos.UsuarioGoogle;

import javax.security.sasl.AuthenticationException;
import java.time.LocalDate;

public class ServicioAutorizacion {
    private final GoogleAuth googleAuth;
    private final DAOUsuario daoUsuario;
    public ServicioAutorizacion(GoogleAuth googleAuth, DAOUsuario daoUsuario) {
        this.googleAuth = googleAuth;
        this.daoUsuario = daoUsuario;
    }
    public Usuario loginConGoogle(String token) {
        UsuarioGoogle usuarioGoogle = googleAuth.validarToken(token);

        if (usuarioGoogle == null) {
            throw new IllegalArgumentException("Token inválido o usuario de Google sin correo");
        }
        Usuario usuario = daoUsuario.buscarPorCorreo(usuarioGoogle.getCorreo())
                .orElseGet(() -> {
                    Usuario nuevo = new Usuario(
                            usuarioGoogle.getNombre(),
                            usuarioGoogle.getApellido(),
                            usuarioGoogle.getCorreo(),
                            usuarioGoogle.getCorreo(), // también como username
                            null,
                            null
                    );
                    nuevo.setCartera(new Cartera());
                    daoUsuario.crear(nuevo);
                    return nuevo;
                });
        return usuario;
    }
}
