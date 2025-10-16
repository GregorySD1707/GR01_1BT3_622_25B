package com.sistema_financiero_personal.obligaciones_financieras.servicios;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import com.sistema_financiero_personal.obligaciones_financieras.daos.DAOObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.ObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.EstadoObligacionFinanciera;

public class ServicioObligacionFinanciera {

    private final DAOObligacionFinanciera daoObligacionFinanciera;
    private final ServicioMovimiento servicioMovimiento;

    public ServicioObligacionFinanciera() {
        this.daoObligacionFinanciera = new DAOObligacionFinanciera();
        this.servicioMovimiento = new ServicioMovimiento();
    }

    public void abonarADeuda(Long idCartera, Long idObligacionFinanciera, double monto) {

        ObligacionFinanciera deuda = daoObligacionFinanciera.buscarPorId(idObligacionFinanciera);

        if (deuda == null || deuda.getEstado() != EstadoObligacionFinanciera.PENDIENTE) {
            return;
        }
        deuda.registrarAbono(monto);
        daoObligacionFinanciera.actualizar(deuda);
        deuda.registrarMovimientoAsociado(idCartera, monto);
    }

}
