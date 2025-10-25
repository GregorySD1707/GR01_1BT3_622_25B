package com.sistema_financiero_personal.recordatorio.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.List;

import com.sistema_financiero_personal.recordatorio.modelos.Recordatorio;

public class DAORecordatorio extends DAOBase<Recordatorio> {
    public DAORecordatorio(){
        super(Recordatorio.class);
    }

    public List<Recordatorio> listarActivos(Long usuarioId) {
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Recordatorio> cq = cb.createQuery(Recordatorio.class);
            Root<Recordatorio> root = cq.from(Recordatorio.class);

            cq.select(root).where(
                    cb.and(
                            cb.equal(root.get("usuario").get("id"), usuarioId),
                            cb.greaterThanOrEqualTo(root.get("fechaFin"), LocalDate.now())
                    )
            );

            return session.createQuery(cq).getResultList();
        });
    }

    public List<Recordatorio> listarPorCampo(Long usuarioId){
        return buscarPorCampo("usuario.id", usuarioId);
    }

}
