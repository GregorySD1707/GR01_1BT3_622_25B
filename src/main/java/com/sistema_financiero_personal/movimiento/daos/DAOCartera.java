package com.sistema_financiero_personal.movimiento.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class DAOCartera extends DAOBase<Cartera> {

    public DAOCartera() {
        super(Cartera.class);
    }

    /**
     * Obtiene el saldo de una cartera específica
     */
    public double obtenerSaldo(Long carteraId) {
        return executeQuery(session -> {
            Double saldo = session.createQuery(
                            "select c.saldo from Cartera c where c.id = :id",
                            Double.class
                    ).setParameter("id", carteraId)
                    .uniqueResult();
            return saldo != null ? saldo : 0.0;
        });
    }

    /**
     * Suma el saldo de TODAS las carteras del sistema
     * (Útil para dashboards administrativos)
     */
    public double sumSaldoTotalSistema() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(c.saldo), 0) from Cartera c",
                Double.class
        ).getSingleResult());
    }

    /**
     * Verifica si existe una cartera con el ID dado
     */
    public boolean existe(Long id) {
        return executeQuery(session -> session.createQuery(
                        "select count(c) > 0 from Cartera c where c.id = :id",
                        Boolean.class
                ).setParameter("id", id)
                .getSingleResult());
    }

    /**
     * Actualiza el saldo de una cartera
     */
    public void actualizarSaldo(Long carteraId, double nuevoSaldo) {
        executeInTransaction(session -> {
            session.createQuery(
                            "update Cartera c set c.saldo = :saldo where c.id = :id"
                    ).setParameter("saldo", nuevoSaldo)
                    .setParameter("id", carteraId)
                    .executeUpdate();
        });
    }

    public void recalcularSaldoDesdeDB(Long carteraId) {
        executeInTransaction(session -> {
            Double saldoCalculado = session.createQuery(
                            "select coalesce(sum(cu.monto), 0) from Cuenta cu " +
                                    "where cu.cartera.id = :carteraId",
                            Double.class
                    ).setParameter("carteraId", carteraId)
                    .getSingleResult();

            session.createQuery(
                            "update Cartera c set c.saldo = :saldo where c.id = :id"
                    ).setParameter("saldo", saldoCalculado)
                    .setParameter("id", carteraId)
                    .executeUpdate();
        });
    }
    public Cartera buscarPorUsuario(Long usuarioId) {
        return executeQuery(session -> session.createQuery(
                        "select c from Cartera c where c.usuario.id = :usuarioId",
                        Cartera.class
                ).setParameter("usuarioId", usuarioId)
                .uniqueResult());
    }
}