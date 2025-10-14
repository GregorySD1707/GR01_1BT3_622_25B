package com.sistema_financiero_personal.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.modelos.Movimiento;

public class DAOMovimiento extends DAOBase<Movimiento> {
    public DAOMovimiento() { super(Movimiento.class); }

    public double sumIngresos() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(m.monto), 0) from Movimiento m where type(m) = com.sistema_financiero_personal.modelos.Ingreso",
                Double.class
        ).getSingleResult());
    }

    public double sumGastos() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(m.monto), 0) from Movimiento m where type(m) = com.sistema_financiero_personal.modelos.Gasto",
                Double.class
        ).getSingleResult());
    }
}
