
package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.modelos.EstadoDeudaPrestamo;
import java.util.List;

public class DAODeudaPrestamo extends DAOBase<DeudaPrestamo> {
    public DAODeudaPrestamo() {
        super(DeudaPrestamo.class);
    }

    public List<DeudaPrestamo> listarPorPersona(String nombrePersona) {
        return executeQuery(session -> session.createQuery(
                "FROM DeudaPrestamo d WHERE d.nombrePersona = :nombrePersona",
                DeudaPrestamo.class)
            .setParameter("nombrePersona", nombrePersona)
            .getResultList()
        );
    }

    public List<DeudaPrestamo> listarPendientes() {
        return executeQuery(session -> session.createQuery(
                "FROM DeudaPrestamo d WHERE d.estado = :estado",
                DeudaPrestamo.class)
            .setParameter("estado", EstadoDeudaPrestamo.PENDIENTE)
            .getResultList()
        );
    }
}
