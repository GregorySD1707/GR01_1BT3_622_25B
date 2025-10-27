package com.sistema_financiero_personal.movimiento.servicios;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.daos.DAOMovimiento;
import com.sistema_financiero_personal.movimiento.modelos.*;

import java.time.LocalDateTime;
import java.util.List;

public class ServicioMovimiento {
    private final DAOMovimiento movimientoDAO;
    private ServicioCuenta servicioCuenta;

    public ServicioMovimiento() {
        this.movimientoDAO = new DAOMovimiento();
        this.servicioCuenta = new ServicioCuenta();
    }

    public ServicioMovimiento(DAOMovimiento daoMovimiento, ServicioCuenta servicioCuenta) {
        this.movimientoDAO = daoMovimiento;
        this.servicioCuenta = servicioCuenta;
    }

    public void registrarIngreso(Long cuentaId, double monto, String descripcion, CategoriaIngreso categoria) {
        // Validaciones
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (!servicioCuenta.existe(cuentaId)) {
            throw new IllegalArgumentException("La cuenta con ID " + cuentaId + " no existe");
        }

        // Crear y guardar el ingreso
        Cuenta cuenta = servicioCuenta.buscarCuenta(cuentaId);
        Movimiento ingreso = new Ingreso(monto, LocalDateTime.now(), descripcion, categoria);
        ingreso.setCuenta(cuenta);

        movimientoDAO.crear(ingreso);

        // Actualizar monto de la cuenta (suma porque es ingreso)
        servicioCuenta.ajustarMonto(cuentaId, monto);
    }

    /**
     * Registra un nuevo gasto
     */
    public void registrarGasto(Long cuentaId, double monto, String descripcion, CategoriaGasto categoria) {
        // Validaciones
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (!servicioCuenta.existe(cuentaId)) {
            throw new IllegalArgumentException("La cuenta con ID " + cuentaId + " no existe");
        }

        // Validar saldo suficiente
        double saldoActual = servicioCuenta.obtenerMonto(cuentaId);
        if (saldoActual < monto) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente en la cuenta. Saldo actual: " + saldoActual
            );
        }

        // Crear y guardar el gasto
        Cuenta cuenta = servicioCuenta.buscarCuenta(cuentaId);
        Movimiento gasto = new Gasto(monto, LocalDateTime.now(), descripcion, categoria);
        gasto.setCuenta(cuenta);

        movimientoDAO.crear(gasto);

        // Actualizar monto de la cuenta (resta porque es gasto)
        servicioCuenta.ajustarMonto(cuentaId, -monto);
    }
    public long contarMovimientos(Long cuentaId) {
        return movimientoDAO.contarMovimientos(cuentaId);
    }
    public double sumarGastosPorCuenta(Long cuentaId) {
        return movimientoDAO.sumGastosPorCuenta(cuentaId);
    }
    public double sumarIngresosPorCuenta(Long cuentaId) {
        return movimientoDAO.sumIngresosPorCuenta(cuentaId);
    }
    public List<Ingreso> obtenerIngresosPorCuenta(Long cuentaId) {
        return movimientoDAO.buscarIngresosPorCuenta(cuentaId);
    }
    public List<Gasto> obtenerGastosPorCuenta(Long cuentaId) {
        return movimientoDAO.buscarGastosPorCuenta(cuentaId);
    }
    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId) {
        return movimientoDAO.buscarPorCuenta(cuentaId);
    }


}