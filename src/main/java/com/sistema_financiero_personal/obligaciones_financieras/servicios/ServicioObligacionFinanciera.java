package com.sistema_financiero_personal.obligaciones_financieras.servicios;

import com.sistema_financiero_personal.obligaciones_financieras.daos.DAOObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.ObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.EstadoObligacionFinanciera;
import com.sistema_financiero_personal.servicios.ServicioMovimientos;

public class ServicioObligacionFinanciera {

    private final DAOObligacionFinanciera daoObligacionFinanciera;
    private final ServicioMovimientos servicioMovimientos;

    public ServicioObligacionFinanciera() {
        this.daoObligacionFinanciera = new DAOObligacionFinanciera();
        this.servicioMovimientos = new ServicioMovimientos();
    }

    public void abonarADeuda(Long idObligacionFinanciera, double monto) {

        ObligacionFinanciera deuda = daoObligacionFinanciera.buscarPorId(idObligacionFinanciera);

        if (deuda == null || deuda.getEstado() != EstadoObligacionFinanciera.PENDIENTE) {
            return;
        }
        deuda.registrarAbono(monto);
        daoObligacionFinanciera.actualizar(deuda);
        deuda.registrarMovimientoAsociado(monto);
    }

    private void registrarMovimientoPorAbono(ObligacionFinanciera deuda, double monto) {
        String nombreCartera = "Cartera principal";
        deuda.registrarMovimientoAsociado(monto);
        /*if (deuda.getTipo() == TipoDeudaPrestamo.DEUDA) {
            servicioMovimientos.registrarGasto(monto, "Abono deuda a " + deuda.getNombrePersona(), "Deudas", nombreCartera);
        } else if (deuda.getTipo() == com.sistema_financiero_personal.obligaciones_financieras.modelos) {
            servicioMovimientos.registrarIngreso(monto, "Abono préstamo de " + deuda.getNombrePersona(), "Préstamos", nombreCartera);
        }*/
    }
}
