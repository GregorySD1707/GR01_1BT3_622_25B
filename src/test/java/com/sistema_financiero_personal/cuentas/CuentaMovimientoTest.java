package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.daos.DAOCartera;
import com.sistema_financiero_personal.movimiento.daos.DAOMovimiento;
import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CuentaMovimientoTest {

    @Mock private DAOCuenta daoCuenta;
    @Mock private DAOMovimiento daoMovimiento;
    @Mock private DAOCartera daoCartera;

    private ServicioMovimiento servicioMovimiento;
    private ServicioCuenta servicioCuenta;
    private ServicioCartera servicioCartera;

    private Cartera cartera;
    private Cuenta cuentaAhorro;

    @Before
    public void setUp() {
        servicioCartera = new ServicioCartera(daoCartera);
        servicioCuenta = new ServicioCuenta(daoCuenta, servicioCartera);
        servicioMovimiento = new ServicioMovimiento(daoMovimiento, servicioCuenta);

        cartera = crearCartera(1L, 10000.0);
        cuentaAhorro = crearCuenta(2L, "Cuenta de Ahorro", TipoCuenta.AHORROS, 1500.0, cartera);
    }

    @Test
    public void givenCuentaConSaldoInicial_whenRegistrarIngreso_thenActualizaSaldoYCreaMovimiento() {
        // Arrange
        double montoIngreso = 500.0;
        String descripcion = "Depósito mensual";
        CategoriaIngreso categoria = CategoriaIngreso.SALARIO;
        configurarMocksCuenta(cuentaAhorro);
        double montoInicial = cuentaAhorro.getMonto();

        servicioMovimiento.registrarIngreso(
                cuentaAhorro.getId(), montoIngreso, descripcion, categoria
        );

        Movimiento movimientoCreado = verificarMovimientoCreado(Ingreso.class, montoIngreso, descripcion, categoria,
                cuentaAhorro
        );

        verificarCuentaActualizada(cuentaAhorro, montoInicial + montoIngreso);
        verificarRecalculoCartera(cartera);
    }

    // ======================================================
    // MÉTODOS AUXILIARES
    // ======================================================

    private Cartera crearCartera(Long id, double saldo) {
        Cartera c = new Cartera();
        c.setId(id);
        c.setSaldo(saldo);
        return c;
    }

    private Cuenta crearCuenta(Long id, String nombre, TipoCuenta tipo, double monto, Cartera cartera) {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(id);
        cuenta.setNombre(nombre);
        cuenta.setTipo(tipo);
        cuenta.setMonto(monto);
        cuenta.setCartera(cartera);
        return cuenta;
    }

    private void configurarMocksCuenta(Cuenta cuenta) {
        when(daoCuenta.existe(cuenta.getId())).thenReturn(true);
        when(daoCuenta.buscarPorId(cuenta.getId())).thenReturn(cuenta);
        when(daoCartera.existe(cuenta.getCartera().getId())).thenReturn(true);
    }

    private Movimiento verificarMovimientoCreado(
            Class<? extends Movimiento> tipoMovimiento,
            double monto, String descripcion, Enum<?> categoria, Cuenta cuenta) {

        ArgumentCaptor<Movimiento> movimientoCaptor = ArgumentCaptor.forClass(Movimiento.class);
        verify(daoMovimiento).crear(movimientoCaptor.capture());
        Movimiento movimiento = movimientoCaptor.getValue();

        assertTrue("Tipo de movimiento incorrecto", tipoMovimiento.isInstance(movimiento));
        assertEquals("Monto incorrecto", monto, movimiento.getMonto(), 0.01);
        assertEquals("Descripción incorrecta", descripcion, movimiento.getDescripcion());
        assertEquals("Cuenta incorrecta", cuenta, movimiento.getCuenta());

        if (movimiento instanceof Ingreso)
            assertEquals("Categoría incorrecta", categoria, ((Ingreso) movimiento).getCategoria());

        return movimiento;
    }

    private void verificarCuentaActualizada(Cuenta cuentaOriginal, double saldoEsperado) {
        ArgumentCaptor<Cuenta> cuentaCaptor = ArgumentCaptor.forClass(Cuenta.class);
        verify(daoCuenta).actualizar(cuentaCaptor.capture());
        Cuenta cuentaActualizada = cuentaCaptor.getValue();

        assertEquals("Saldo de la cuenta incorrecto", saldoEsperado, cuentaActualizada.getMonto(), 0.01);
    }

    private void verificarRecalculoCartera(Cartera cartera) {
        verify(daoCartera).recalcularSaldoDesdeDB(cartera.getId());
    }
}
