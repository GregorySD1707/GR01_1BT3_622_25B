package com.sistema_financiero_personal.usuario.daos;

import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.util.Optional;

import java.util.List;

public interface DAOUsuario {
    public Optional<Usuario> buscarPorCorreo(String correo);
    Usuario crear(Usuario usuario);

    Usuario borrar(Usuario usuario);

    List<Usuario> listar();
}
