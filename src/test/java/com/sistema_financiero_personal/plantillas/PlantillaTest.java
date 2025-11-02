package com.sistema_financiero_personal.plantillas;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.plantillas.servicios.ServicioPlantilla;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

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


    @Before
    public void setUp() {
        servicioPlantilla = new ServicioPlantilla();
        cuenta = new Cuenta();
        usuario = new Usuario();
        categoriaIngreso = CategoriaIngreso.SALARIO;
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

        // Verificar Mensaje de Error
        assertTrue("El mensaje debe indicar que el nombre no puede estar vacío. Mensaje actual: " + msg,
                msg.contains("nombre") || msg.contains("no") || msg.contains("vacío"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_invalid_type_when_create_template_then_throw_exception() {
        servicioPlantilla.validarTipo(TIPO_INVALIDO);
    }
}
