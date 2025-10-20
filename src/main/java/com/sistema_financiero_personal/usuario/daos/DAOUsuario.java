package com.sistema_financiero_personal.usuario.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

public class DAOUsuario extends DAOBase<Usuario> {
    public DAOUsuario() {
        super(Usuario.class);
    }
}
