package com.sistema_financiero_personal.plantillas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.plantillas.servicios.ServicioPlantilla;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlantillaTest {
    private ServicioPlantilla servicioPlantilla;
    private Usuario usuario;
    private Cuenta cuenta;
    private CategoriaIngreso categoriaIngreso;

    // validos
    private static final String NOMBRE_VALIDO = "Pago de Servicios";
    private static final double MONTO_VALIDO = 100.0;
    // invalidos
    private static final String NOMBRE_INVALIDO = "   ";
    private static final String TIPO_INVALIDO = "Ingreso";

    private static final String TIPO_GASTO = "GASTO";
    private static final String TIPO_INGRESO = "INGRESO";
    private static final String CATEGORIA_VALIDA_GASTO = "TRANSPORTE";
    private static final String CATEGORIA_VALIDA_INGRESO = "SUELDO";
    private static final String CATEGORIA_INVALIDA = "CATEGORIA_QUE_NO_EXISTE";

    private static Plantilla plantilla;

    @Before
    public void setUp() {
        servicioPlantilla = new ServicioPlantilla();
        cuenta = new Cuenta();
        usuario = new Usuario();
        categoriaIngreso = CategoriaIngreso.SALARIO;

        plantilla = new Plantilla("Netflix Mensual", 15.99);
        plantilla.setCategoriaGasto(CategoriaGasto.ENTRETENIMIENTO);
        plantilla.setCuenta(cuenta);
        plantilla.setActivo(true);

    }

    @Test
    public void given_name_using_spaces_when_save_template_then_fail(){
        Plantilla plantilla = new Plantilla(NOMBRE_INVALIDO, MONTO_VALIDO);
        plantilla.setCategoriaIngreso(categoriaIngreso);
        plantilla.setCuenta(cuenta);

        ServicioPlantilla servicioPlantilla = new ServicioPlantilla();
        // Act y Assert
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicioPlantilla.crearPlantilla(plantilla, usuario.getId())
        );

        String msg = ex.getMessage() != null ? ex.getMessage().toLowerCase() : "";

        assertTrue("El mensaje debe indicar que el nombre no puede estar vacío. Mensaje actual: " + msg,
                msg.contains("nombre") || msg.contains("no") || msg.contains("vacío"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_invalid_type_when_create_template_then_throw_exception() {
        servicioPlantilla.validarTipo(TIPO_INVALIDO);
    }

    @Test
    public void given_invalid_amount_when_create_template_then_throw_exception() {
        assertMontoInvalido(Double.NaN);
        assertMontoInvalido(0.0);
        assertMontoInvalido(-10.0);
        assertMontoInvalido(1_000_000.00);
    }

    private void assertMontoInvalido(double monto) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> servicioPlantilla.validarMonto(monto)
        );
        assertTrue("El mensaje debe ser: 'Monto no válido'",
                ex.getMessage().contains("Monto no válido"));
    }

    @Test
    public void given_invalid_category_when_validate_then_throw_exception() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> servicioPlantilla.validarCategoria(TIPO_INGRESO, CATEGORIA_INVALIDA)
        );

        assertTrue("El mensaje debe contener 'Categoría inválida'",
                exception.getMessage().contains("Categoría inválida"));
    }

    @Test
    public void given_expense_template_when_apply_then_creates_gasto_for_form_autofill() {

        Movimiento movimiento = servicioPlantilla.aplicarPlantilla(plantilla);
        assertNotNull(movimiento);
        assertTrue("Debe ser un Gasto", movimiento instanceof Gasto);
        assertEquals(15.99, movimiento.getMonto(), 0.01);
        assertEquals(cuenta.getId(), movimiento.getCuenta().getId());
        assertEquals("Netflix Mensual", movimiento.getDescripcion());

        Gasto gasto = (Gasto) movimiento;
        assertEquals(CategoriaGasto.ENTRETENIMIENTO, gasto.getCategoria());
    }

    @Test
    public void given_three_decimal_numbers_when_redondearMonto_then_ok() {

        assertEquals(23.12, servicioPlantilla.redondearMonto(23.117), 0.001);
        assertEquals(17.00, servicioPlantilla.redondearMonto(17.002), 0.001);
        assertEquals(115.56, servicioPlantilla.redondearMonto(115.555), 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_a_template_when_create_another_template_with_the_same_name_then_fail(){
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setNombre("test bus");

        servicioPlantilla.verificarNombreUnico(plantilla1);

        Plantilla plantilla2 = new Plantilla();
        plantilla2.setNombre("test bus");

        servicioPlantilla.verificarNombreUnico(plantilla2);
    }

    @Test
    public void given_name_to_search_when_search_the_name_then_ok(){
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setNombre("test bus 1");

        Plantilla plantilla2 = new Plantilla();
        plantilla2.setNombre("test bus");

        Plantilla plantilla3 = new Plantilla();
        plantilla3.setNombre("test metro");

        List<Plantilla> plantillasABuscar = new ArrayList<>();
        plantillasABuscar.add(plantilla1);
        plantillasABuscar.add(plantilla2);
        plantillasABuscar.add(plantilla3);

        List<Plantilla> plantillasEncontradas = servicioPlantilla.buscarPorNombre(plantillasABuscar, "bus");

        assertEquals(2, plantillasEncontradas.size());
        assertTrue(plantillasEncontradas.stream()
                .allMatch(p -> p.getNombre().contains("bus")));
    }

    @Test
    public void given_category_to_search_when_search_it_then_ok(){
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setCategoriaGasto(CategoriaGasto.SERVICIOS);

        Plantilla plantilla2 = new Plantilla();
        plantilla2.setCategoriaGasto(CategoriaGasto.SERVICIOS);

        Plantilla plantilla3 = new Plantilla();
        plantilla3.setCategoriaIngreso(CategoriaIngreso.ABONO_PRESTAMO);

        List<Plantilla> plantillasABuscar = new ArrayList<>();
        plantillasABuscar.add(plantilla1);
        plantillasABuscar.add(plantilla2);
        plantillasABuscar.add(plantilla3);

        List<Plantilla> plantillasEncontradas = servicioPlantilla.buscarPorCategoriaGasto(plantillasABuscar, CategoriaGasto.SERVICIOS);

        assertEquals(2, plantillasEncontradas.size());
        assertTrue(plantillasEncontradas.stream()
                .allMatch(p -> p.getCategoria().contains(CategoriaGasto.SERVICIOS.toString())));
    }

    @Test
    public void given_type_to_search_when_search_it_then_ok(){
        Plantilla plantilla1 = new Plantilla();
        plantilla1.setTipo("INGRESO");

        Plantilla plantilla2 = new Plantilla();
        plantilla2.setTipo("INGRESO");

        Plantilla plantilla3 = new Plantilla();
        plantilla3.setTipo("GASTO");

        List<Plantilla> plantillasABuscar = new ArrayList<>();
        plantillasABuscar.add(plantilla1);
        plantillasABuscar.add(plantilla2);
        plantillasABuscar.add(plantilla3);

        List<Plantilla> plantillasEncontradas = servicioPlantilla.buscarPorTipo(plantillasABuscar, "GASTO");

        assertEquals(1, plantillasEncontradas.size());
        assertTrue(plantillasEncontradas.stream()
                .allMatch(p -> p.getTipo().contains("GASTO")));
    }
}