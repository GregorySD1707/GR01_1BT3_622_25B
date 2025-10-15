package com.sistema_financiero_personal;

import com.sistema_financiero_personal.movimiento.daos.DAOCartera;
import com.sistema_financiero_personal.movimiento.daos.DAOMovimiento;
import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class MovimientoTest {

    private static DAOCartera daoCartera;
    private static DAOMovimiento daoMovimiento;
    private static ServicioCartera servicioCartera;
    private static ServicioMovimiento servicioMovimiento;
    private static Long carteraId;

    @BeforeClass
    public static void inicializar() {
        daoCartera = new DAOCartera();
        daoMovimiento = new DAOMovimiento();
        servicioCartera = new ServicioCartera(daoCartera);
        servicioMovimiento = new ServicioMovimiento(daoMovimiento, servicioCartera);

        // Crear una cartera de prueba
        Cartera cartera = new Cartera();
        daoCartera.crear(cartera);
        carteraId = cartera.getId();

        System.out.println("✅ Cartera de prueba creada con ID: " + carteraId);
    }

    @Test
    public void test01_CarteraExiste() {
        System.out.println("\n--- Test 1: Verificar que la cartera existe ---");

        boolean existe = servicioCartera.existe(carteraId);
        assertTrue("La cartera debería existir", existe);

        Cartera cartera = servicioCartera.buscarPorId(carteraId);
        assertNotNull("La cartera no debería ser null", cartera);
        assertEquals("El saldo inicial debería ser 0", 0.0, cartera.getSaldo(), 0.01);

        System.out.println("✅ Cartera existe y tiene saldo 0");
    }

    @Test
    public void test02_RegistrarIngreso() {
        System.out.println("\n--- Test 2: Registrar un ingreso ---");

        double montoInicial = servicioCartera.obtenerSaldo(carteraId);
        double montoIngreso = 1000.0;

        servicioMovimiento.registrarIngreso(
                carteraId,
                montoIngreso,
                "Salario mensual",
                CategoriaIngreso.SALARIO
        );

        double saldoFinal = servicioCartera.obtenerSaldo(carteraId);
        assertEquals("El saldo debería incrementarse en " + montoIngreso,
                montoInicial + montoIngreso, saldoFinal, 0.01);

        System.out.println("✅ Ingreso registrado. Saldo: " + saldoFinal);
    }

    @Test
    public void test03_RegistrarMultiplesIngresos() {
        System.out.println("\n--- Test 3: Registrar múltiples ingresos ---");

        double saldoInicial = servicioCartera.obtenerSaldo(carteraId);

        servicioMovimiento.registrarIngreso(carteraId, 500.0, "Freelance", CategoriaIngreso.INTERESES);
        servicioMovimiento.registrarIngreso(carteraId, 200.0, "Venta", CategoriaIngreso.VENTAS);
        servicioMovimiento.registrarIngreso(carteraId, 150.0, "Inversión", CategoriaIngreso.VENTAS);

        double saldoFinal = servicioCartera.obtenerSaldo(carteraId);
        double totalIngresos = 500.0 + 200.0 + 150.0;

        assertEquals("El saldo debería incrementarse en " + totalIngresos,
                saldoInicial + totalIngresos, saldoFinal, 0.01);

        System.out.println("✅ Múltiples ingresos registrados. Saldo: " + saldoFinal);
    }

    @Test
    public void test04_RegistrarGasto() {
        System.out.println("\n--- Test 4: Registrar un gasto ---");

        double saldoInicial = servicioCartera.obtenerSaldo(carteraId);
        double montoGasto = 100.0;

        servicioMovimiento.registrarGasto(
                carteraId,
                montoGasto,
                "Compra supermercado",
                CategoriaGasto.ALIMENTACION
        );

        double saldoFinal = servicioCartera.obtenerSaldo(carteraId);
        assertEquals("El saldo debería decrementarse en " + montoGasto,
                saldoInicial - montoGasto, saldoFinal, 0.01);

        System.out.println("✅ Gasto registrado. Saldo: " + saldoFinal);
    }

    @Test
    public void test05_RegistrarMultiplesGastos() {
        System.out.println("\n--- Test 5: Registrar múltiples gastos ---");

        double saldoInicial = servicioCartera.obtenerSaldo(carteraId);

        servicioMovimiento.registrarGasto(carteraId, 50.0, "Netflix", CategoriaGasto.ENTRETENIMIENTO);
        servicioMovimiento.registrarGasto(carteraId, 30.0, "Gasolina", CategoriaGasto.TRANSPORTE);
        servicioMovimiento.registrarGasto(carteraId, 75.0, "Luz", CategoriaGasto.SERVICIOS);

        double saldoFinal = servicioCartera.obtenerSaldo(carteraId);
        double totalGastos = 50.0 + 30.0 + 75.0;

        assertEquals("El saldo debería decrementarse en " + totalGastos,
                saldoInicial - totalGastos, saldoFinal, 0.01);

        System.out.println("✅ Múltiples gastos registrados. Saldo: " + saldoFinal);
    }

    @Test
    public void test06_ListarMovimientosPorCartera() {
        System.out.println("\n--- Test 6: Listar movimientos por cartera ---");

        List<Movimiento> movimientos = daoMovimiento.buscarPorCartera(carteraId);

        assertNotNull("La lista de movimientos no debería ser null", movimientos);
        assertTrue("Debería haber al menos un movimiento", movimientos.size() > 0);

        System.out.println("✅ Movimientos encontrados: " + movimientos.size());
        System.out.println("--- HISTORIAL DE MOVIMIENTOS ---");
        for (Movimiento m : movimientos) {
            String tipo = (m instanceof Ingreso) ? "INGRESO" : "GASTO";
            String signo = (m instanceof Ingreso) ? "+" : "-";
            System.out.printf("%s | %s$%.2f | %s | %s\n",
                    m.getFecha(),
                    signo,
                    m.getMonto(),
                    tipo,
                    m.getDescripcion()
            );
        }
        System.out.println("--------------------------------");
    }

    @Test
    public void test07_SumaIngresosPorCartera() {
        System.out.println("\n--- Test 7: Verificar suma de ingresos por cartera ---");

        double ingresos = daoMovimiento.sumIngresosPorCartera(carteraId);
        assertTrue("Debería haber ingresos registrados", ingresos > 0);

        // Esperamos: 1000 + 500 + 200 + 150 = 1850
        assertEquals("Los ingresos deberían sumar 1850", 1850.0, ingresos, 0.01);

        System.out.println("✅ Ingresos totales de la cartera: $" + ingresos);
    }

    @Test
    public void test08_SumaGastosPorCartera() {
        System.out.println("\n--- Test 8: Verificar suma de gastos por cartera ---");

        double gastos = daoMovimiento.sumGastosPorCartera(carteraId);
        assertTrue("Debería haber gastos registrados", gastos > 0);

        // Esperamos: 100 + 50 + 30 + 75 = 255
        assertEquals("Los gastos deberían sumar 255", 255.0, gastos, 0.01);

        System.out.println("✅ Gastos totales de la cartera: $" + gastos);
    }

    @Test
    public void test09_SaldoFinalCorrecto() {
        System.out.println("\n--- Test 9: Verificar saldo final correcto ---");

        double ingresos = daoMovimiento.sumIngresosPorCartera(carteraId);
        double gastos = daoMovimiento.sumGastosPorCartera(carteraId);
        double saldoEsperado = ingresos - gastos;
        double saldoReal = servicioCartera.obtenerSaldo(carteraId);

        assertEquals("El saldo debería ser igual a ingresos - gastos",
                saldoEsperado, saldoReal, 0.01);

        System.out.println("✅ Saldo verificado correctamente");
        System.out.println("--- RESUMEN FINANCIERO ---");
        System.out.printf("Ingresos:  +$%.2f\n", ingresos);
        System.out.printf("Gastos:    -$%.2f\n", gastos);
        System.out.printf("Saldo:     $%.2f\n", saldoReal);
        System.out.println("--------------------------");
    }

    @Test
    public void test10_ContarMovimientos() {
        System.out.println("\n--- Test 10: Contar movimientos ---");

        long cantidad = daoMovimiento.contarMovimientos(carteraId);

        // Deberíamos tener: 4 ingresos + 4 gastos = 8 movimientos
        assertEquals("Deberían existir 8 movimientos en total", 8L, cantidad);

        System.out.println("✅ Total de movimientos: " + cantidad);
    }

    @Test
    public void test11_ListarUltimosMovimientos() {
        System.out.println("\n--- Test 11: Listar últimos 3 movimientos ---");

        List<Movimiento> ultimos = daoMovimiento.buscarUltimosMovimientos(carteraId, 3);

        assertNotNull("La lista no debería ser null", ultimos);
        assertEquals("Deberían retornar exactamente 3 movimientos", 3, ultimos.size());

        System.out.println("✅ Últimos 3 movimientos:");
        for (Movimiento m : ultimos) {
            String tipo = (m instanceof Ingreso) ? "INGRESO" : "GASTO";
            System.out.printf("- %s: $%.2f (%s)\n", tipo, m.getMonto(), m.getDescripcion());
        }
    }

    @Test
    public void test12_FiltrarSoloIngresos() {
        System.out.println("\n--- Test 12: Filtrar solo ingresos ---");

        List<Ingreso> ingresos = daoMovimiento.buscarIngresosPorCartera(carteraId);

        assertNotNull("La lista de ingresos no debería ser null", ingresos);
        assertEquals("Deberían haber 4 ingresos", 4, ingresos.size());

        // Verificar que todos son ingresos
        for (Ingreso i : ingresos) {
            assertTrue("Todos deberían ser de tipo Ingreso", i instanceof Ingreso);
        }

        System.out.println("✅ Filtrados " + ingresos.size() + " ingresos");
    }

    @Test
    public void test13_FiltrarSoloGastos() {
        System.out.println("\n--- Test 13: Filtrar solo gastos ---");

        List<Gasto> gastos = daoMovimiento.buscarGastosPorCartera(carteraId);

        assertNotNull("La lista de gastos no debería ser null", gastos);
        assertEquals("Deberían haber 4 gastos", 4, gastos.size());

        // Verificar que todos son gastos
        for (Gasto g : gastos) {
            assertTrue("Todos deberían ser de tipo Gasto", g instanceof Gasto);
        }

        System.out.println("✅ Filtrados " + gastos.size() + " gastos");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test14_MontoNegativoDebeFallar() {
        System.out.println("\n--- Test 14: Validar monto negativo (debe fallar) ---");

        servicioMovimiento.registrarIngreso(carteraId, -100.0, "Monto negativo", CategoriaIngreso.OTROS);

        // Si llega aquí, el test falla
        fail("Debería lanzar IllegalArgumentException para montos negativos");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test15_CarteraInexistenteDebeFallar() {
        System.out.println("\n--- Test 15: Validar cartera inexistente (debe fallar) ---");

        Long idInexistente = 99999L;
        servicioMovimiento.registrarIngreso(idInexistente, 100.0, "Test", CategoriaIngreso.OTROS);

        // Si llega aquí, el test falla
        fail("Debería lanzar IllegalArgumentException para cartera inexistente");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test16_SaldoInsuficienteDebeFallar() {
        System.out.println("\n--- Test 16: Validar saldo insuficiente (debe fallar) ---");

        double saldoActual = servicioCartera.obtenerSaldo(carteraId);
        double montoExcesivo = saldoActual + 1000.0;

        servicioMovimiento.registrarGasto(carteraId, montoExcesivo, "Gasto excesivo", CategoriaGasto.OTROS);

        // Si llega aquí, el test falla
        fail("Debería lanzar IllegalArgumentException para saldo insuficiente");
    }

    @Test
    public void test17_EliminarMovimiento() {
        System.out.println("\n--- Test 17: Eliminar movimiento y verificar ajuste de saldo ---");

        // Obtener un movimiento para eliminar
        List<Movimiento> movimientos = daoMovimiento.buscarPorCartera(carteraId);
        Movimiento movimientoAEliminar = movimientos.get(0);
        Long movimientoId = movimientoAEliminar.getId();

        double saldoAntes = servicioCartera.obtenerSaldo(carteraId);
        double montoMovimiento = movimientoAEliminar.getMonto();
        boolean esIngreso = movimientoAEliminar instanceof Ingreso;

        // Eliminar el movimiento
        servicioMovimiento.eliminarMovimiento(movimientoId);

        double saldoDespues = servicioCartera.obtenerSaldo(carteraId);
        double saldoEsperado = esIngreso ? saldoAntes - montoMovimiento : saldoAntes + montoMovimiento;

        assertEquals("El saldo debería ajustarse correctamente al eliminar el movimiento",
                saldoEsperado, saldoDespues, 0.01);

        System.out.println("✅ Movimiento eliminado y saldo ajustado correctamente");
    }

    @AfterClass
    public static void limpiar() {
        // Opcional: Eliminar la cartera de prueba
        if (carteraId != null) {
            try {
                daoCartera.borrar(carteraId);
                System.out.println("\n🧹 Cartera de prueba eliminada (ID: " + carteraId + ")");
            } catch (Exception e) {
                System.out.println("⚠️ No se pudo eliminar la cartera de prueba: " + e.getMessage());
            }
        }
    }
}