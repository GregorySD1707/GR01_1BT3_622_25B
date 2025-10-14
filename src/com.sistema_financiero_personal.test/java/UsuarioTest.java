import com.sistema_financiero_personal.usuarios.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    public void given_data_when_login_then_ok(){
        Usuario usuario = new Usuario("Pepe", "Zambrano", "pepe.zambrano@gmail.com","xXXpepeXxx", "abc123");
        assertEquals("Pepe", usuario.getName());
        assertEquals("Luchin", usuario.getApellido());
    }

}
