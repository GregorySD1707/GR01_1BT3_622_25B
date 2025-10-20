package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.servicios.ServicioUsuarioTest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertThrows;

public class UsuarioLimiteEdadTest {

    private ServicioUsuarioTest servicio;
    @Before
    public void setUp() {
        servicio = new ServicioUsuarioTest();
    }
    @Test
    public void registrar_debeLanzarIllegalArgumentException_si_edadEsMenorA13() {
        assertThrows(IllegalArgumentException.class, () -> servicio.registrarUsuario(
                "Juan", "Pérez", "juan@example.com", "juanp",
                "Aa1!aaaa", LocalDate.now().minusYears(12)
        ));
    }
    @Test
    public void registrar_noLanzaExcepcion_si_edadEsExactamente13() {
        servicio.registrarUsuario(
                "Ana", "López", "ana@example.com", "anita",
                "Aa1!aaaa", LocalDate.now().minusYears(13)
        );
    }
}
