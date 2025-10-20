package com.sistema_financiero_personal.usuario.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

public class DAOUsuario extends DAOBase<Usuario> {
    public DAOUsuario() {
        super(Usuario.class);
    }

    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        return executeQuery(session -> {
            String hql = "FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario";
            return session.createQuery(hql, Usuario.class)
                    .setParameter("nombreUsuario", nombreUsuario)
                    .uniqueResult();
        });
    }

    public Usuario buscarPorCorreo(String correo){
        return executeQuery(session -> {
            String hql = "FROM Usuario u WHERE u.correo = :correo";
            return session.createQuery(hql, Usuario.class)
                    .setParameter("correo", correo)
                    .uniqueResult();
        });
    }
}
