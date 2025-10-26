package com.sistema_financiero_personal.cuentas.servicios;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;

import java.util.List;

public class ServicioCuenta {
    private  DAOCuenta daoCuenta;
    private ServicioCartera servicioCartera;

    public ServicioCuenta() {
        this.daoCuenta = new DAOCuenta();
        this.servicioCartera = new ServicioCartera();
    }
    public ServicioCuenta(DAOCuenta daoCuenta, ServicioCartera servicioCartera) {
        this.daoCuenta = daoCuenta;
        this.servicioCartera = servicioCartera;
    }

    public ServicioCuenta(DAOCuenta daoCuenta){
        this.daoCuenta = daoCuenta;
    }

    /**
     * Crea una nueva cuenta
     */
    public void crearCuenta(Cuenta cuenta) {
        validarObligatorios(cuenta);

        if (!validarSaldoInicial(cuenta.getMonto())) {
            throw new IllegalArgumentException("El saldo inicial debe ser mayor que cero");
        }

        if (existeCuentaDuplicada(cuenta.getNombre(), cuenta.getTipo(), cuenta.getCartera().getId())) {
            throw new IllegalStateException("Ya existe una cuenta con el mismo nombre y tipo en esta cartera");
        }

        daoCuenta.crear(cuenta);

        // NUEVO: Actualizar el saldo de la cartera después de crear la cuenta
        servicioCartera.recalcularSaldo(cuenta.getCartera().getId());
    }

    /**
     * Busca una cuenta por ID
     */
    public Cuenta buscarCuenta(Long id) {
        return daoCuenta.buscarPorId(id);
    }

    /**
     * NUEVO: Busca una cuenta con todos sus movimientos cargados
     */
    public Cuenta buscarCuentaConMovimientos(Long id) {
        return daoCuenta.buscarConMovimientos(id);
    }

    /**
     * Lista todas las cuentas de una cartera
     */
    public List<Cuenta> listarCuentasPorCartera(Long carteraId) {
        return daoCuenta.listarPorCartera(carteraId);
    }

    /**
     * NUEVO: Lista cuentas por tipo
     */
    public List<Cuenta> listarCuentasPorTipo(Long carteraId, TipoCuenta tipo) {
        return daoCuenta.listarPorTipo(carteraId, tipo);
    }

    /**
     * NUEVO: Ajusta el monto de una cuenta (cuando hay movimientos)
     * Este método es clave: actualiza la cuenta Y recalcula el saldo de la cartera
     */
    public void ajustarMonto(Long cuentaId, double cambio) {
        Cuenta cuenta = buscarCuenta(cuentaId);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta con ID " + cuentaId + " no existe");
        }

        double nuevoMonto = cuenta.getMonto() + cambio;
        if (nuevoMonto < 0) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente. Saldo actual: " + cuenta.getMonto() +
                            ", cambio solicitado: " + cambio
            );
        }

        cuenta.setMonto(nuevoMonto);
        daoCuenta.actualizar(cuenta);

        // Actualizar el saldo de la cartera padre
        servicioCartera.recalcularSaldo(cuenta.getCartera().getId());
    }

    /**
     * NUEVO: Actualiza una cuenta completa
     */
    public void actualizarCuenta(Cuenta cuenta) {
        validarObligatorios(cuenta);

        Cuenta cuentaExistente = buscarCuenta(cuenta.getId());
        if (cuentaExistente == null) {
            throw new IllegalArgumentException("La cuenta no existe");
        }

        // Verificar si cambió el nombre/tipo y si genera duplicado
        if (!cuentaExistente.getNombre().equals(cuenta.getNombre()) ||
                !cuentaExistente.getTipo().equals(cuenta.getTipo())) {
            if (existeCuentaDuplicada(cuenta.getNombre(), cuenta.getTipo(), cuenta.getCartera().getId())) {
                throw new IllegalStateException("Ya existe una cuenta con el mismo nombre y tipo");
            }
        }

        daoCuenta.actualizar(cuenta);

        // Si cambió el monto, recalcular saldo de cartera
        if (Double.compare(cuentaExistente.getMonto(), cuenta.getMonto()) != 0) {
            servicioCartera.recalcularSaldo(cuenta.getCartera().getId());
        }
    }

    /**
     * NUEVO: Elimina una cuenta (y sus movimientos por cascade)
     */
    public void eliminarCuenta(Long cuentaId) {
        Cuenta cuenta = buscarCuenta(cuentaId);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta con ID " + cuentaId + " no existe");
        }

        Long carteraId = cuenta.getCartera().getId();
        daoCuenta.borrar(cuentaId);

        // Recalcular saldo de la cartera después de eliminar
        servicioCartera.recalcularSaldo(carteraId);
    }

    /**
     * NUEVO: Obtiene el monto actual de una cuenta
     */
    public double obtenerMonto(Long cuentaId) {
        return daoCuenta.obtenerMonto(cuentaId);
    }

    /**
     * NUEVO: Verifica si existe una cuenta
     */
    public boolean existe(Long cuentaId) {
        return daoCuenta.existe(cuentaId);
    }

    /**
     * NUEVO: Cuenta el número de cuentas de una cartera
     */
    public long contarCuentas(Long carteraId) {
        return daoCuenta.contarCuentas(carteraId);
    }

    /**
     * Verifica si existe una cuenta duplicada
     */
    public boolean existeCuentaDuplicada(String nombre, TipoCuenta tipo, Long carteraId) {
        return daoCuenta.existeCuentaPorNombreYTipo(nombre, tipo, carteraId);
    }

    /**
     * Valida que el saldo inicial sea positivo y tenga máximo 2 decimales
     */
    public boolean validarSaldoInicial(double saldo) {
        if (saldo <= 0) {
            return false;
        }

        double centavos = Math.round(saldo * 100.0) / 100.0;
        return Double.compare(saldo, centavos) == 0;
    }

    /**
     * Valida campos obligatorios de una cuenta
     */
    public void validarObligatorios(Cuenta cuenta) {
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no puede ser nula");
        }

        if (cuenta.getNombre() == null || cuenta.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la cuenta es obligatorio");
        }

        if (cuenta.getTipo() == null) {
            throw new IllegalArgumentException("El tipo de cuenta es obligatorio");
        }

        if (cuenta.getCartera() == null) {
            throw new IllegalArgumentException("Debe asignarse una cartera a la cuenta");
        }
    }
}