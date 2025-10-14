package com.sistema_financiero_personal.servicios;

import com.sistema_financiero_personal.daos.DAOCartera;
import com.sistema_financiero_personal.daos.DAOMovimiento;
import com.sistema_financiero_personal.modelos.Cartera;
import com.sistema_financiero_personal.modelos.Movimiento;
import com.sistema_financiero_personal.modelos.TipoMovimiento;
import com.sistema_financiero_personal.modelos.Ingreso;
import com.sistema_financiero_personal.modelos.Gasto;


import java.time.LocalDateTime;
import java.util.Objects;

public class ServicioMovimientos {

    private final DAOMovimiento movimientoDAO;
    private final DAOCartera carteraDAO;

    public ServicioMovimientos() {
        this.movimientoDAO = new DAOMovimiento();
        this.carteraDAO = new DAOCartera();
    }

    public ServicioMovimientos(DAOMovimiento movimientoDAO, DAOCartera carteraDAO) {
        this.movimientoDAO = movimientoDAO;
        this.carteraDAO = carteraDAO;
    }
    public Movimiento registrarMovimiento(TipoMovimiento tipo, double monto, String descripcion, String categoria, String  nombreCartera){

        validarMonto(monto);
        Objects.requireNonNull(nombreCartera, "nombreCartera no puede ser null");
        Movimiento mov = construirMovimiento(tipo, monto, descripcion, categoria);
        movimientoDAO.crear(mov);

        double delta = deltaFrom(tipo, monto);
        actualizarCartera(delta, nombreCartera, null);
        return mov;
    }
    private Movimiento construirMovimiento(TipoMovimiento tipo, double monto, String descripcion, String categoria) {
        return (tipo == TipoMovimiento.INGRESO)
                 ?new Ingreso (monto, LocalDateTime.now(), descripcion, categoria)
                : new Gasto(monto, LocalDateTime.now(), descripcion, categoria);
    }


    public Movimiento registrarIngreso(double monto, String descripcion, String categoria, String nombreCartera) {
        return registrarMovimiento(TipoMovimiento.INGRESO, monto, descripcion, categoria, nombreCartera);
    }
    public Movimiento registrarGasto(double monto, String descripcion, String categoria, String nombreCartera) {
        return registrarMovimiento(TipoMovimiento.GASTO, monto, descripcion, categoria, nombreCartera);
    }


    private double deltaFrom(TipoMovimiento tipo, double monto) {
        return (tipo == TipoMovimiento.INGRESO) ? monto : -monto;
    }

    public Cartera actualizarCartera(double delta, String nombreCartera, String tipoSiNoExiste) {
        String tipo = tipoSiNoExiste == null ? "EFECTIVO" : tipoSiNoExiste;
        Cartera cartera = carteraDAO.obtenerOCrear(nombreCartera, tipo);
        cartera.actualizarCartera(delta);
        carteraDAO.actualizar(cartera);
        return cartera;
    }

    // Nuevos métodos de totales
    public double obtenerIngresosTotales() {
        return movimientoDAO.sumIngresos();
    }

    public double obtenerGastosTotales() {
        return movimientoDAO.sumGastos();
    }

    public double obtenerSaldoActualTotal() {
        return carteraDAO.sumSaldoActual();
    }

    private void validarMonto(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("El monto debe ser positivo");
    }
}
