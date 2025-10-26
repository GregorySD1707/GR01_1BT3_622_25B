package com.sistema_financiero_personal.cuentas;

import com.sistema_financiero_personal.cuentas.daos.DAOCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.daos.DAOCartera;
import com.sistema_financiero_personal.movimiento.daos.DAOMovimiento;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.Gasto;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import static org.junit.Assert.*;

import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CuentaMovimientoGastoTest {
    @Mock
    private DAOCuenta daoCuenta;
    @Mock
    private DAOMovimiento daoMovimiento;
    @Mock
    private ServicioCartera servicioCartera;
    @Mock
    private DAOCartera daoCartera;
    private ServicioMovimiento servicioMovimiento;
    private ServicioCuenta servicioCuenta;
    private Cartera cartera;
    private Cuenta cuentaAhorro;
    private Cuenta cuentaCorriente;

    @Before
    public void setUp() {
        servicioCartera = new ServicioCartera(daoCartera);
        servicioCuenta = new ServicioCuenta(daoCuenta, servicioCartera);
        servicioMovimiento = new ServicioMovimiento(daoMovimiento, servicioCuenta);

        cartera = new Cartera();
        cartera.setId(1L);
        cartera.setSaldo(5000.0);

        cuentaAhorro = new Cuenta();
        cuentaAhorro.setId(1L);
        cuentaAhorro.setNombre("Ahorro Principal");
        cuentaAhorro.setTipo(TipoCuenta.AHORROS);
        cuentaAhorro.setMonto(1000.0);
        cuentaAhorro.setCartera(cartera);

        cuentaCorriente = new Cuenta();
        cuentaCorriente.setId(2L);
        cuentaCorriente.setNombre("Cuenta Corriente");
        cuentaCorriente.setTipo(TipoCuenta.CORRIENTE);
        cuentaCorriente.setMonto(500.0);
        cuentaCorriente.setCartera(cartera);
    }

    @Test
    public void givenCuentaConSaldoInicial_whenRegistrarGasto_thenActualizaSaldoYCreaMovimiento() {
        double montoGasto = 300.0;
        String descripcion = "Pago de servicios básicos";
        CategoriaGasto categoria = CategoriaGasto.SERVICIOS;

        when(daoCuenta.existe(cuentaCorriente.getId())).thenReturn(true);
        when(daoCuenta.buscarPorId(cuentaCorriente.getId())).thenReturn(cuentaCorriente);
        when(daoCartera.existe(cartera.getId())).thenReturn(true);
        when(servicioCuenta.obtenerMonto(cuentaCorriente.getId()))
                .thenReturn(cuentaCorriente.getMonto());

        servicioMovimiento.registrarGasto(
                cuentaCorriente.getId(),
                montoGasto,
                descripcion,
                categoria
        );

        ArgumentCaptor<Movimiento> movimientoCaptor = ArgumentCaptor.forClass(Movimiento.class);
        verify(daoMovimiento).crear(movimientoCaptor.capture());

        Movimiento movimientoCreado = movimientoCaptor.getValue();

        assertTrue("Debe ser un Gasto", movimientoCreado instanceof Gasto);

        assertEquals("El monto debe coincidir",
                montoGasto,
                movimientoCreado.getMonto(),
                0.01);

        assertEquals("La descripción debe coincidir",
                descripcion,
                movimientoCreado.getDescripcion());

        assertEquals("La categoría debe coincidir",
                categoria,
                ((Gasto) movimientoCreado).getCategoria());

        assertEquals("La cuenta debe coincidir",
                cuentaCorriente,
                movimientoCreado.getCuenta());

        ArgumentCaptor<Cuenta> cuentaCaptor = ArgumentCaptor.forClass(Cuenta.class);
        verify(daoCuenta).actualizar(cuentaCaptor.capture());

        Cuenta cuentaActualizada = cuentaCaptor.getValue();
        double saldoEsperado = 500.0 - 300.0;

        assertEquals("El saldo debe disminuir en el monto del gasto",
                saldoEsperado,
                cuentaActualizada.getMonto(),
                0.01);

        verify(daoCartera).recalcularSaldoDesdeDB(cartera.getId());
    }

}