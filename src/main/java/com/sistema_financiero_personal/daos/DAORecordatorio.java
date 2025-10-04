package com.sistema_financiero_personal.daos;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.List;

import com.sistema_financiero_personal.modelos.Recordatorio;

public class DAORecordatorio extends DAOBase<Recordatorio> {
    public DAORecordatorio(){
        super(Recordatorio.class);
    }

    public List<Recordatorio> listarActivos() {
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Recordatorio> cq = cb.createQuery(Recordatorio.class);
            Root<Recordatorio> root = cq.from(Recordatorio.class);

            cq.select(root).where(cb.greaterThanOrEqualTo(root.get("fechaFin"), LocalDate.now()));

            return session.createQuery(cq).getResultList();
        });
    }

}
