package com.sistema_financiero_personal.usuario.daos;

import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.util.Optional;

public interface DAOUsuario {
    public Optional<Usuario> buscarPorCorreo(String correo);
}
