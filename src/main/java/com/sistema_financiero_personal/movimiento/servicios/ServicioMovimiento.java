package com.sistema_financiero_personal.movimiento.servicios;

import com.sistema_financiero_personal.movimiento.daos.DAOMovimiento;
import com.sistema_financiero_personal.movimiento.modelos.*;

import java.time.LocalDateTime;
import java.util.List;

public class ServicioMovimiento {
    private final DAOMovimiento movimientoDAO;
    private final ServicioCartera servicioCartera;

    public ServicioMovimiento() {
        this.movimientoDAO = new DAOMovimiento();
        this.servicioCartera = new ServicioCartera();
    }

    public ServicioMovimiento(DAOMovimiento movimientoDAO, ServicioCartera servicioCartera) {
        this.movimientoDAO = movimientoDAO;
        this.servicioCartera = servicioCartera;
    }

    /**
     * Obtiene la suma total de todos los ingresos del sistema
     */
    public double obtenerIngresosTotales() {
        return movimientoDAO.sumIngresos();
    }

    /**
     * Obtiene la suma total de todos los gastos del sistema
     */
    public double obtenerGastosTotales() {
        return movimientoDAO.sumGastos();
    }


    /**
     * Registra un nuevo ingreso
     */
    public void registrarIngreso(Long carteraId, double monto, String descripcion, CategoriaIngreso categoria) {
        // Validaciones
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (!servicioCartera.existe(carteraId)) {
            throw new IllegalArgumentException("La cartera con ID " + carteraId + " no existe");
        }

        // Crear y guardar el ingreso
        Cartera cartera = servicioCartera.buscarPorId(carteraId);
        Movimiento ingreso = new Ingreso(monto, LocalDateTime.now(), descripcion, categoria);
        ingreso.setCartera(cartera);

        movimientoDAO.crear(ingreso);

        // Actualizar saldo (suma porque es ingreso)
        servicioCartera.actualizarSaldo(carteraId, monto);
    }

    /**
     * Registra un nuevo gasto
     */
    public void registrarGasto(Long carteraId, double monto, String descripcion, CategoriaGasto categoria) {
        // Validaciones
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor a 0");
        }
        if (!servicioCartera.existe(carteraId)) {
            throw new IllegalArgumentException("La cartera con ID " + carteraId + " no existe");
        }

        // Validar saldo suficiente (opcional, depende de tu lógica de negocio)
//        double saldoActual = servicioCartera.obtenerSaldo(carteraId);
//        if (saldoActual < monto) {
//            throw new IllegalArgumentException("Saldo insuficiente. Saldo actual: " + saldoActual);
//        }

        // Crear y guardar el gasto
        Cartera cartera = servicioCartera.buscarPorId(carteraId);
        Movimiento gasto = new Gasto(monto, LocalDateTime.now(), descripcion, categoria);
        gasto.setCartera(cartera);

        movimientoDAO.crear(gasto);

        // Actualizar saldo (resta porque es gasto)
        servicioCartera.actualizarSaldo(carteraId, -monto);
    }



    /**
     * Elimina un movimiento y ajusta el saldo
     */
    public void eliminarMovimiento(Long movimientoId) {
        // Buscar el movimiento antes de eliminarlo
        Movimiento movimiento = movimientoDAO.buscarPorId(movimientoId);
        if (movimiento == null) {
            throw new IllegalArgumentException("El movimiento con ID " + movimientoId + " no existe");
        }

        Long carteraId = movimiento.getCartera().getId();
        double monto = movimiento.getMonto();

        // Eliminar el movimiento
        movimientoDAO.borrar(movimientoId);

        // Ajustar el saldo (invertir la operación)
        if (movimiento instanceof Ingreso) {
            servicioCartera.actualizarSaldo(carteraId, -monto); // Restar el ingreso eliminado
        } else if (movimiento instanceof Gasto) {
            servicioCartera.actualizarSaldo(carteraId, monto); // Devolver el gasto eliminado
        }
    }
}