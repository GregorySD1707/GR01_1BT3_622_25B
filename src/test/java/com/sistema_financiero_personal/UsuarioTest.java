package com.sistema_financiero_personal;

import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.modelos.TipoDeudaPrestamo;
import com.sistema_financiero_personal.usuario.Usuario;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class UsuarioTest {

    @Test
    public void given_data_when_login_then_ok(){
        Usuario usuario = new Usuario("Pepe", "Zambrano", "pepe.zambrano@gmail.com","xXXpepeXxx", "abc123");
        assertEquals("Pepe", usuario.getName());
        assertEquals("Luchin", usuario.getApellido());
    }

}
