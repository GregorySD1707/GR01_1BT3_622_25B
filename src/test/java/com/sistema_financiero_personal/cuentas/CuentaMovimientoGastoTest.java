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
public class CuentaMovimientoGastoTest {

    @Mock private DAOCuenta daoCuenta;
    @Mock private DAOMovimiento daoMovimiento;
    @Mock private DAOCartera daoCartera;

    private ServicioMovimiento servicioMovimiento;
    private ServicioCuenta servicioCuenta;
    private ServicioCartera servicioCartera;

    private Cartera cartera;
    private Cuenta cuentaCorriente;
    private Cuenta cuentaAhorro;

    @Before
    public void setUp() {
        servicioCartera = new ServicioCartera(daoCartera);
        servicioCuenta = new ServicioCuenta(daoCuenta, servicioCartera);
        servicioMovimiento = new ServicioMovimiento(daoMovimiento, servicioCuenta);

        cartera = crearCartera(1L, 5000.0);
        cuentaCorriente = crearCuenta(2L, "Cuenta Corriente", TipoCuenta.CORRIENTE, 500.0, cartera);
        cuentaAhorro = crearCuenta(1L, "Ahorro Principal", TipoCuenta.AHORROS, 1000.0, cartera);
    }

    // ======================================================
    // TEST PRINCIPAL
    // ======================================================
    @Test
    public void givenCuentaConSaldoInicial_whenRegistrarGasto_thenActualizaSaldoYCreaMovimiento() {
        double montoGasto = 300.0;
        String descripcion = "Pago de servicios básicos";
        CategoriaGasto categoria = CategoriaGasto.SERVICIOS;

        configurarMocksCuenta(cuentaCorriente);
        double montoInicial = cuentaCorriente.getMonto();

        servicioMovimiento.registrarGasto(
                cuentaCorriente.getId(),
                montoGasto,
                descripcion,
                categoria
        );

        Movimiento movimientoCreado = verificarMovimientoCreado(
                Gasto.class,
                montoGasto,
                descripcion,
                categoria,
                cuentaCorriente
        );
        verificarCuentaActualizada(cuentaCorriente, montoInicial - montoGasto);
        verificarRecalculoCartera(cartera);
    }

    @Test
    public void givenSaldoIgualAlMontoGasto_whenRegistrarGasto_thenSaldoQuedaEnCeroYSeRegistraMovimiento() {
        // GIVEN: saldo de la cuenta igual al monto del gasto
        double montoGasto = 500.0;
        String descripcion = "Gasto igual al saldo";
        CategoriaGasto categoria = CategoriaGasto.SERVICIOS;
        cuentaCorriente.setMonto(montoGasto);
        configurarMocksCuenta(cuentaCorriente);

        servicioMovimiento.registrarGasto(
                cuentaCorriente.getId(),
                montoGasto,
                descripcion,
                categoria
        );

        // THEN: capturar y validar el movimiento
        Movimiento movimientoCreado = verificarMovimientoCreado(Gasto.class, montoGasto, descripcion, categoria, cuentaCorriente);
        verificarCuentaActualizada(cuentaCorriente, 0.0);
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
        when(servicioCuenta.obtenerMonto(cuenta.getId())).thenReturn(cuenta.getMonto());
    }

    private Movimiento verificarMovimientoCreado(
            Class<? extends Movimiento> tipoMovimiento,
            double monto, String descripcion, Enum<?> categoria, Cuenta cuenta) {

        ArgumentCaptor<Movimiento> movimientoCaptor = ArgumentCaptor.forClass(Movimiento.class);
        verify(daoMovimiento).crear(movimientoCaptor.capture());
        Movimiento movimiento = movimientoCaptor.getValue();

        assertTrue("Debe ser del tipo esperado", tipoMovimiento.isInstance(movimiento));
        assertEquals("Monto incorrecto", monto, movimiento.getMonto(), 0.01);
        assertEquals("Descripción incorrecta", descripcion, movimiento.getDescripcion());
        assertEquals("Cuenta incorrecta", cuenta, movimiento.getCuenta());

        if (movimiento instanceof Gasto)
            assertEquals("Categoría incorrecta", categoria, ((Gasto) movimiento).getCategoria());

        return movimiento;
    }

    private void verificarCuentaActualizada(Cuenta cuentaOriginal, double saldoEsperado) {
        ArgumentCaptor<Cuenta> cuentaCaptor = ArgumentCaptor.forClass(Cuenta.class);
        verify(daoCuenta).actualizar(cuentaCaptor.capture());
        Cuenta cuentaActualizada = cuentaCaptor.getValue();

        assertEquals("El saldo debe disminuir correctamente", saldoEsperado, cuentaActualizada.getMonto(), 0.01);
    }

    private void verificarRecalculoCartera(Cartera cartera) {
        verify(daoCartera).recalcularSaldoDesdeDB(cartera.getId());
    }
}
