package com.sistema_financiero_personal.usuario.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.hibernate.query.Query;
import jakarta.persistence.TypedQuery;

import java.util.Optional;

public class DAOUsuario extends DAOBase<Usuario> {
    public DAOUsuario() {
        super(Usuario.class);
    }
    public Optional<Usuario> buscarPorCorreo(String correo) {
        return executeQuery(session -> {
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario u WHERE u.correo = :correo", Usuario.class);
            query.setParameter("correo", correo);
            return query.uniqueResultOptional();
        });
    }


}
