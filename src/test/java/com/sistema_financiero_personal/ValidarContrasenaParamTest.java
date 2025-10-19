package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ValidarContrasenaParamTest {

    private ServicioUsuario servicioUsuario;

    @Parameterized.Parameter(0)
    public String password;

    @Parameterized.Parameter(1)
    public boolean expectedResult;

    @Before
    public void setUp() {
        servicioUsuario = new ServicioUsuario();
    }

    @Parameterized.Parameters(name = "{index}: password=\"{0}\" => esperado={1}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                // contraseña, resultado esperado
                {"123", false},             // menos de 8 caracteres
                {"123abc$%", false},        // sin mayúsculas
                {"Abc123$%", true},         // válida
                {"123ABC$%", false},        // sin minúsculas
                {"Abcdef$%", false},        // sin números
                {"Abc12345", false}         // sin carácter especial
        });
    }

    @Test
    public void given_password_when_validate_then_expected_result() {
        boolean result = servicioUsuario.validarContrasena(password);
        assertEquals(
                "Fallo al validar la contraseña: \"" + password + "\"",
                expectedResult,
                result
        );
    }
}
