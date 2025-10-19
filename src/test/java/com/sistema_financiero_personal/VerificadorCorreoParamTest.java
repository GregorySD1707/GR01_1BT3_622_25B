package com.sistema_financiero_personal;
import com.sistema_financiero_personal.usuario.servicios.VerificadorCorreoElectronico;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class VerificadorCorreoParamTest {
    @Parameterized.Parameters(name = "{index} : email={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"mateo.calvache@gmail.com"}, {"mateo.calvache54@hotmail.com"}, {"mateo.calvache@hotmail.com"}
        });
    }
    private String email;

    public VerificadorCorreoParamTest(String email) {
        this.email = email;
    }

    @Test
    public void given_email_when_verify_email_then_ok() {
        VerificadorCorreoElectronico verificadorCorreoElectronico = new VerificadorCorreoElectronico();
        boolean correoValido = verificadorCorreoElectronico.verificarCorreoElectronico(email);
        assertTrue("correo con formato válido debe ser aceptado: " + email, correoValido);
    }
}