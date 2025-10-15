package com.sistema_financiero_personal.movimiento.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;
import com.sistema_financiero_personal.movimiento.modelos.*;
public class DAOMovimiento extends DAOBase<Movimiento> {
    public DAOMovimiento() { super(Movimiento.class); }

    public double sumIngresos() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(m.monto), 0) from Movimiento m where type(m) = Ingreso",
                Double.class
        ).getSingleResult());
    }

    public double sumGastos() {
        return executeQuery(session -> session.createQuery(
                "select coalesce(sum(m.monto), 0) from Movimiento m where type(m) = Gasto ",
                Double.class
        ).getSingleResult());
    }
}
