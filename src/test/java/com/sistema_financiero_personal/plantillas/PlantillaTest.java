package com.sistema_financiero_personal.plantillas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.plantillas.daos.DAOPlantilla;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.plantillas.servicios.ServicioPlantilla;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    @Test

    public void given_existing_template_when_duplicate_then_creates_copy_with_suffix_1_and_same_data() {
        ServicioPlantilla servicio = new ServicioPlantilla();

        Plantilla original = new Plantilla("Netflix Mensual", 15.99);
        original.setTipo("GASTO");
        original.setCategoria("ENTRETENIMIENTO");
        Cuenta cuenta = new Cuenta();
        original.setCuenta(cuenta);

        Plantilla copia = servicio.duplicarPlantilla(original, 0);

        assertNotSame(original, copia);
        assertEquals("Netflix Mensual (1)", copia.getNombre());
        assertEquals(15.99, copia.getMonto(), 0.0001);
        assertEquals("GASTO", copia.getTipo());
        assertEquals("ENTRETENIMIENTO", copia.getCategoria());
        assertSame(cuenta, copia.getCuenta());
        assertTrue(copia.isActivo());
        assertNotNull(copia.getFechaCreacion());
    }
}


