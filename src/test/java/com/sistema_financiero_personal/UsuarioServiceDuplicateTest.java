package com.sistema_financiero_personal;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario.UserAlreadyExistsException;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
public class UsuarioServiceDuplicateTest {

    @Test
    public void register_fails_if_username_is_duplicate() {
        ServicioUsuario servicio = Mockito.spy(new ServicioUsuario());

        String usernameDuplicado = "vicente";

        Usuario creado = servicio.registrarUsuario(
                "Vicente", "Crespo", "vicente@gmail.com",
                usernameDuplicado, "Abc123!@", LocalDate.of(2000, 1, 1));

        assertNotNull(creado);

        assertThrows(UserAlreadyExistsException.class, () -> servicio.registrarUsuario(
                "Pedro", "Lopez", "pedro@gmail.com",
                usernameDuplicado, "Abc123!@", LocalDate.of(2000, 1, 1)));

        Mockito.verify(servicio, Mockito.atLeastOnce())
                .existeNombreUsuario(ArgumentMatchers.argThat(u ->
                        u != null && u.equalsIgnoreCase(usernameDuplicado)));
    }
}
