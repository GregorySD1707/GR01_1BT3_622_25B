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

    public double sumIngresosPorCuenta(Long cuentaId) {
        return executeQuery(session -> session.createQuery(
                        "select coalesce(sum(m.monto), 0) from Movimiento m " +
                                "where m.cuenta.id = :cuentaId and type(m) = Ingreso",
                        Double.class
                ).setParameter("cuentaId", cuentaId)
                .getSingleResult());
    }


    public double sumGastosPorCuenta(Long cuentaId) {
        return executeQuery(session -> session.createQuery(
                        "select coalesce(sum(m.monto), 0) from Movimiento m " +
                                "where m.cuenta.id = :cuentaId and type(m) = Gasto",
                        Double.class
                ).setParameter("cuentaId", cuentaId)
                .getSingleResult());
    }

    public List<Ingreso> buscarIngresosPorCuenta(Long cuentaId) {
        return executeQuery(session -> session.createQuery(
                        "select i from Ingreso i " +
                                "where i.cuenta.id = :cuentaId " +
                                "order by i.fecha desc",
                        Ingreso.class
                ).setParameter("cuentaId", cuentaId)
                .getResultList());
    }


    public List<Gasto> buscarGastosPorCuenta(Long cuentaId) {
        return executeQuery(session -> session.createQuery(
                        "select g from Gasto g " +
                                "where g.cuenta.id = :cuentaId " +
                                "order by g.fecha desc",
                        Gasto.class
                ).setParameter("cuentaId", cuentaId)
                .getResultList());
    }

    public long contarMovimientos(Long cuentaId) {
        return executeQuery(session -> session.createQuery(
                        "select count(m) from Movimiento m where m.cuenta.id = :cuentaId",
                        Long.class
                ).setParameter("cuentaId", cuentaId)
                .getSingleResult());
    }
    public List<Movimiento> buscarPorCuenta(Long cuentaId) {
        return executeQuery(session -> session.createQuery(
                        "select m from Movimiento m " +
                                "where m.cuenta.id = :cuentaId " +
                                "order by m.fecha desc",
                        Movimiento.class
                ).setParameter("cuentaId", cuentaId)
                .getResultList());
    }
    public List<Movimiento> buscarPorCartera(Long carteraId) {
        return executeQuery(session -> session.createQuery(
                        "select m from Movimiento m " +
                                "join fetch m.cuenta c " +
                                "where c.cartera.id = :carteraId " +
                                "order by m.fecha desc",
                        Movimiento.class
                ).setParameter("carteraId", carteraId)
                .getResultList());
    }
}