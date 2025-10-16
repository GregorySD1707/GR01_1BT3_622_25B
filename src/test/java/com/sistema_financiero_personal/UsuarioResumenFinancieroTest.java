package com.sistema_financiero_personal;

import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UsuarioResumenFinancieroTest {
    @Test
    public void given_user_when_create_resumenFinanciero_then_associate_user() {
        Usuario usuario = new Usuario("mateo", "calvache", "matecavache123",
                "mateo", "123", LocalDate.now());
        ResumenFinanciero resumen = new ResumenFinanciero();
        resumen.setUsuario(usuario);
        assertNotNull(resumen.getUsuario());
        assertEquals(usuario, resumen.getUsuario());

    }

}