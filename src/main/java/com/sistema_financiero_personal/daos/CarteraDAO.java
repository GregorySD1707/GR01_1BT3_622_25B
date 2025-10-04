package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.modelos.Cartera;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class CarteraDAO extends BaseDAO<Cartera> {
    public CarteraDAO() { super(Cartera.class); }

    public Cartera buscarPorNombre(String nombre) {
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Cartera> cq = cb.createQuery(Cartera.class);
            Root<Cartera> root = cq.from(Cartera.class);
            cq.select(root).where(cb.equal(root.get("nombre"), nombre));
            return session.createQuery(cq).uniqueResult();
        });
    }

    public Cartera obtenerOCrear(String nombre, String tipo) {
        Cartera existente = buscarPorNombre(nombre);
        if (existente != null) return existente;
        Cartera nueva = new Cartera(nombre, 0.0, tipo);
        crear(nueva);
        return buscarPorNombre(nombre);
    }
}

