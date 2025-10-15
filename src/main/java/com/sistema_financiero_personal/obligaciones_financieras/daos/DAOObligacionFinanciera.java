
package com.sistema_financiero_personal.obligaciones_financieras.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.ObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.EstadoObligacionFinanciera;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAOObligacionFinanciera extends DAOBase<ObligacionFinanciera> {
    public DAOObligacionFinanciera() {
        super(ObligacionFinanciera.class);
    }

    public List<ObligacionFinanciera> listarPorPersona(String nombrePersona) {
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ObligacionFinanciera> cq = cb.createQuery(ObligacionFinanciera.class);
            Root<ObligacionFinanciera> root = cq.from(ObligacionFinanciera.class);

            cq.select(root).where(cb.equal(root.get("nombrePersona"), nombrePersona));

            return session.createQuery(cq).getResultList();
        });
    }

    public List<ObligacionFinanciera> listarPendientes() {
        return executeQuery(session ->{
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ObligacionFinanciera> cq = cb.createQuery(ObligacionFinanciera.class);
            Root<ObligacionFinanciera> root = cq.from(ObligacionFinanciera.class);

            cq.select(root).where(cb.equal(root.get("estado"), EstadoObligacionFinanciera.PENDIENTE));

            return session.createQuery(cq).getResultList();
        });
    }

    public List<ObligacionFinanciera> buscarConFiltros(String nombrePersona, LocalDate fechaInicio, LocalDate fechaFin) {

        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ObligacionFinanciera> cq = cb.createQuery(ObligacionFinanciera.class);
            Root<ObligacionFinanciera> root = cq.from(ObligacionFinanciera.class);

            List<Predicate> predicates = new ArrayList<>();

            // Filtro: solo pendientes
            predicates.add(cb.equal(root.get("estado"), EstadoObligacionFinanciera.PENDIENTE));

            // Filtro: nombre persona (si existe)
            if(nombrePersona != null && !nombrePersona.trim().isEmpty()){
                predicates.add(cb.like(
                        cb.lower(root.get("nombrePersona")),
                        "%" + nombrePersona.toLowerCase() + "%"
                ));
            }

            // Filtro: fecha Inicio (si existe)
            if ( fechaInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("fechaPago"), fechaInicio));
            }

            // Filtro: fecha fin (si existe)
            if (fechaFin != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("fechaPago"), fechaFin));
            }

            // aplicar todos los filtros con AND
            cq.where(cb.and(predicates.toArray(new Predicate[0])));

            return session.createQuery(cq).getResultList();
        });
    }
}
