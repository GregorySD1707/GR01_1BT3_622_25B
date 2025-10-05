import com.sistema_financiero_personal.daos.CarteraDAO;
import com.sistema_financiero_personal.daos.DAOMovimiento;
import com.sistema_financiero_personal.modelos.Cartera;
import com.sistema_financiero_personal.modelos.Movimiento;
import com.sistema_financiero_personal.servicios.ServicioMovimientos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestServicioMovimientos {

    private ServicioMovimientos servicio;
    private CarteraDAO carteraDAO;
    private DAOMovimiento movimientoDAO;

    @BeforeEach
    void setup() {
        servicio = new ServicioMovimientos();
        carteraDAO = new CarteraDAO();
        movimientoDAO = new DAOMovimiento();
    }

    @Test
    void testRegistrarIngresoActualizaCartera() {
        String nombreCartera = "CARTERA_TEST_ING_" + UUID.randomUUID();
        double monto = 123.45;
        String descripcion = "test-ingreso-" + UUID.randomUUID();

        Movimiento mov = servicio.registrarIngreso(monto, descripcion, "SUELDO", nombreCartera);
        assertNotNull(mov.getId());

        Cartera cartera = carteraDAO.buscarPorNombre(nombreCartera);
        assertNotNull(cartera);
        assertEquals(monto, cartera.getSaldoActual(), 0.0001);

        List<Movimiento> movimientos = movimientoDAO.listar();
        assertTrue(movimientos.stream().anyMatch(m -> descripcion.equals(m.getDescripcion()) && Math.abs(m.getMonto() - monto) < 0.0001));
    }

    @Test
    void testRegistrarGastoDisminuyeCartera() {
        String nombreCartera = "CARTERA_TEST_GAS_" + UUID.randomUUID();
        double ingreso = 200.0;
        double gasto = 50.0;
        String descIng = "test-ingreso2-" + UUID.randomUUID();
        String descGas = "test-gasto-" + UUID.randomUUID();

        servicio.registrarIngreso(ingreso, descIng, "OTROS", nombreCartera);
        servicio.registrarGasto(gasto, descGas, "COMIDA", nombreCartera);

        Cartera cartera = carteraDAO.buscarPorNombre(nombreCartera);
        assertNotNull(cartera);
        assertEquals(ingreso - gasto, cartera.getSaldoActual(), 0.0001);
    }

    @Test
    void testIngresoYGastoActualizanTablaCartera() {
        String nombreCartera = "CARTERA_TEST_MIX_" + UUID.randomUUID();
        double ingreso = 275.0;
        double gasto = 125.0;
        String descIng = "mix-ingreso-" + UUID.randomUUID();
        String descGas = "mix-gasto-" + UUID.randomUUID();

        servicio.registrarIngreso(ingreso, descIng, "VARIOS", nombreCartera);
        servicio.registrarGasto(gasto, descGas, "VARIOS", nombreCartera);

        Cartera cartera = carteraDAO.buscarPorNombre(nombreCartera);
        assertNotNull(cartera);
        assertEquals(ingreso - gasto, cartera.getSaldoActual(), 0.0001);

        List<Movimiento> movimientos = movimientoDAO.listar();
        assertTrue(movimientos.stream().anyMatch(m -> descIng.equals(m.getDescripcion()) && Math.abs(m.getMonto() - ingreso) < 0.0001));
        assertTrue(movimientos.stream().anyMatch(m -> descGas.equals(m.getDescripcion()) && Math.abs(m.getMonto() - gasto) < 0.0001));
    }


    @Test
    void testRegistrarIngreso666ActualizaCartera() {
        String nombreCartera = "CARTERA_TEST_666_" + UUID.randomUUID();
        double monto = 666.0;
        String descripcion = "test-ingreso-666-" + UUID.randomUUID();

        Movimiento mov = servicio.registrarIngreso(monto, descripcion, "SUELDO", nombreCartera);
        assertNotNull(mov.getId());

        Cartera cartera = carteraDAO.buscarPorNombre(nombreCartera);
        assertNotNull(cartera);
        assertEquals(monto, cartera.getSaldoActual(), 0.0001);

        List<Movimiento> movimientos = movimientoDAO.listar();
        assertTrue(movimientos.stream().anyMatch(m -> descripcion.equals(m.getDescripcion()) && Math.abs(m.getMonto() - monto) < 0.0001));
    }


}
