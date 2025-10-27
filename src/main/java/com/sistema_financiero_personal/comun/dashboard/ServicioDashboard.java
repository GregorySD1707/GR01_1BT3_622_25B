package com.sistema_financiero_personal.comun.dashboard;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.daos.DAOCartera;
import com.sistema_financiero_personal.movimiento.daos.DAOMovimiento;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;

import java.util.List;
import java.util.Map;

/**
 * Servicio que orquesta la lógica del Dashboard
 * Delega cálculos al ServicioCompendio (siguiendo TDD)
 */
public class ServicioDashboard {
    private final DAOCuenta daoCuenta;
    private final DAOMovimiento daoMovimiento;
    private final DAOCartera daoCartera;
    private final ServicioCompendio servicioCompendio;

    // Constructor para producción
    public ServicioDashboard() {
        this.daoCuenta = new DAOCuenta();
        this.daoMovimiento = new DAOMovimiento();
        this.daoCartera = new DAOCartera();
        this.servicioCompendio = new ServicioCompendio();
    }

    // Constructor para testing (inyección de dependencias)
    public ServicioDashboard(DAOCuenta daoCuenta,
                             DAOMovimiento daoMovimiento,
                             DAOCartera daoCartera,
                             ServicioCompendio servicioCompendio) {
        this.daoCuenta = daoCuenta;
        this.daoMovimiento = daoMovimiento;
        this.daoCartera = daoCartera;
        this.servicioCompendio = servicioCompendio;
    }

    /**
     * Obtiene el resumen completo del dashboard para un usuario
     * @param usuarioId ID del usuario
     * @return DatosDashboard con toda la información
     */
    public DatosDashboard obtenerResumen(Long usuarioId) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("El ID de usuario no puede ser nulo");
        }

        try {
            // 1. Obtener la cartera del usuario (relación 1:1)
            Cartera cartera = daoCartera.buscarPorUsuario(usuarioId);

            if (cartera == null) {
                // Usuario sin cartera = sin cuentas
                return new DatosDashboard(EstatusDashboard.SIN_CUENTAS);
            }

            Long carteraId = cartera.getId();

            // 2. Obtener datos desde DAOs (usando carteraId)
            List<Cuenta> cuentas = daoCuenta.listarPorCartera(carteraId);
            List<Movimiento> movimientos = daoMovimiento.buscarPorCartera(carteraId);

            // 3. Determinar estatus (delegar a ServicioCompendio)
            EstatusDashboard estatus = servicioCompendio.determinarEstatus(cuentas, movimientos);

            // 4. Calcular métricas (delegar a ServicioCompendio)
            double saldoTotal = servicioCompendio.calcularSaldoTotal(cuentas);
            Map<String, Double> saldosIndividuales = servicioCompendio.obtenerSaldosIndividuales(cuentas);
            double ingresosTotal = servicioCompendio.calcularIngresos(movimientos);
            double gastosTotal = servicioCompendio.calcularGastos(movimientos);
            List<Movimiento> ultimosMovimientos = servicioCompendio.obtenerUltimosMovimientos(movimientos);

            // 5. Construir objeto de respuesta
            return new DatosDashboard(
                    estatus,
                    saldoTotal,
                    saldosIndividuales,
                    ingresosTotal,
                    gastosTotal,
                    ultimosMovimientos,
                    cuentas
            );

        } catch (Exception e) {
            // Si hay error en consultas, propagar la excepción
            // para que sea manejada por el servlet
            System.err.println("Error al obtener resumen del dashboard: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al obtener datos del dashboard", e);
        }
    }

    /**
     * Verifica si un usuario tiene cuentas registradas
     */
    public boolean usuarioTieneCuentas(Long usuarioId) {
        Cartera cartera = daoCartera.buscarPorUsuario(usuarioId);
        if (cartera == null) return false;

        List<Cuenta> cuentas = daoCuenta.listarPorCartera(cartera.getId());
        return !cuentas.isEmpty();
    }

    /**
     * Verifica si un usuario tiene movimientos registrados
     */
    public boolean usuarioTieneMovimientos(Long usuarioId) {
        Cartera cartera = daoCartera.buscarPorUsuario(usuarioId);
        if (cartera == null) return false;

        List<Movimiento> movimientos = daoMovimiento.buscarPorCartera(cartera.getId());
        return !movimientos.isEmpty();
    }
}