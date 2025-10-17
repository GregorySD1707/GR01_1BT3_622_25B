package com.sistema_financiero_personal.usuario.daos;

import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.util.Optional;

import java.util.List;

public interface DAOUsuario {
    public Optional<Usuario> buscarPorCorreo(String correo);

    void crear(Usuario usuario);

    Usuario borrar(Usuario usuario);

    List<Usuario> listar();

    boolean existe(String correo);

    boolean credencialesCorrectas(String correo, String password);
}
