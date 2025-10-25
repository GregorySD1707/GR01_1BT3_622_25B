package com.sistema_financiero_personal.movimiento.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;
import com.sistema_financiero_personal.movimiento.modelos.*;

import java.time.LocalDateTime;
import java.util.List;

public class DAOMovimiento extends DAOBase<Movimiento> {

    public DAOMovimiento() {
        super(Movimiento.class);
    }

    /**
     * Suma todos los ingresos del sistema
     */
    public double sumIngresos() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(m.monto), 0) from Movimiento m where type(m) = Ingreso",
                Double.class
        ).getSingleResult());
    }

    /**
     * Suma todos los gastos del sistema
     */
    public double sumGastos() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(m.monto), 0) from Movimiento m where type(m) = Gasto",
                Double.class
        ).getSingleResult());
    }

    /**
     * Suma los ingresos de una cartera específica
     */
    public double sumIngresosPorCartera(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select coalesce(sum(m.monto), 0) from Movimiento m " +
                                "where m.cartera.id = :carteraId and type(m) = Ingreso",
                        Double.class
                ).setParameter("carteraId", carteraId)
                .getSingleResult());
    }

    /**
     * Suma los gastos de una cartera específica
     */
    public double sumGastosPorCartera(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select coalesce(sum(m.monto), 0) from Movimiento m " +
                                "where m.cartera.id = :carteraId and type(m) = Gasto",
                        Double.class
                ).setParameter("carteraId", carteraId)
                .getSingleResult());
    }

    /**
     * Obtiene todos los movimientos de una cartera ordenados por fecha
     */
    public List<Movimiento> buscarPorCartera(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select m from Movimiento m " +
                                "where m.cartera.id = :carteraId " +
                                "order by m.fecha desc",
                        Movimiento.class
                ).setParameter("carteraId", carteraId)
                .getResultList());
    }

    /**
     * Obtiene movimientos de una cartera en un rango de fechas
     */
    public List<Movimiento> buscarPorFechas(Long carteraId, LocalDateTime desde, LocalDateTime hasta) {
        return executeQuery(session -> session.createQuery(
                        "select m from Movimiento m " +
                                "where m.cartera.id = :carteraId " +
                                "and m.fecha between :desde and :hasta " +
                                "order by m.fecha desc",
                        Movimiento.class
                ).setParameter("carteraId", carteraId)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList());
    }

    /**
     * Obtiene solo los ingresos de una cartera
     */
    public List<Ingreso> buscarIngresosPorCartera(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select i from Ingreso i " +
                                "where i.cartera.id = :carteraId " +
                                "order by i.fecha desc",
                        Ingreso.class
                ).setParameter("carteraId", carteraId)
                .getResultList());
    }

    /**
     * Obtiene solo los gastos de una cartera
     */
    public List<Gasto> buscarGastosPorCartera(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select g from Gasto g " +
                                "where g.cartera.id = :carteraId " +
                                "order by g.fecha desc",
                        Gasto.class
                ).setParameter("carteraId", carteraId)
                .getResultList());
    }

    /**
     * Obtiene los últimos N movimientos de una cartera
     */
    public List<Movimiento> buscarUltimosMovimientos(Long carteraId, int limite) {
        return executeQuery(session -> session.createQuery(
                        "select m from Movimiento m " +
                                "where m.cartera.id = :carteraId " +
                                "order by m.fecha desc",
                        Movimiento.class
                ).setParameter("carteraId", carteraId)
                .setMaxResults(limite)
                .getResultList());
    }

    /**
     * Obtiene movimientos por categoría de ingreso
     */
    public List<Ingreso> buscarPorCategoriaIngreso(Long carteraId, CategoriaIngreso categoria) {
        return executeQuery(session -> session.createQuery(
                        "select i from Ingreso i " +
                                "where i.cartera.id = :carteraId " +
                                "and i.categoria = :categoria " +
                                "order by i.fecha desc",
                        Ingreso.class
                ).setParameter("carteraId", carteraId)
                .setParameter("categoria", categoria)
                .getResultList());
    }

    /**
     * Obtiene movimientos por categoría de gasto
     */
    public List<Gasto> buscarPorCategoriaGasto(Long carteraId, CategoriaGasto categoria) {
        return executeQuery(session -> session.createQuery(
                        "select g from Gasto g " +
                                "where g.cartera.id = :carteraId " +
                                "and g.categoria = :categoria " +
                                "order by g.fecha desc",
                        Gasto.class
                ).setParameter("carteraId", carteraId)
                .setParameter("categoria", categoria)
                .getResultList());
    }

    /**
     * Suma gastos por categoría en un rango de fechas
     */
    public double sumGastosPorCategoria(Long carteraId, CategoriaGasto categoria,
                                        LocalDateTime desde, LocalDateTime hasta) {
        return executeQuery(session -> session.createQuery(
                        "select coalesce(sum(g.monto), 0) from Gasto g " +
                                "where g.cartera.id = :carteraId " +
                                "and g.categoria = :categoria " +
                                "and g.fecha between :desde and :hasta",
                        Double.class
                ).setParameter("carteraId", carteraId)
                .setParameter("categoria", categoria)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getSingleResult());
    }

    /**
     * Cuenta el total de movimientos de una cartera
     */
    public long contarMovimientos(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select count(m) from Movimiento m where m.cartera.id = :carteraId",
                        Long.class
                ).setParameter("carteraId", carteraId)
                .getSingleResult());
    }

}