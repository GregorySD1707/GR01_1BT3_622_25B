package com.sistema_financiero_personal.usuarios;

import com.sistema_financiero_personal.usuario.servicios.VerificadorCorreoElectronico;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//@RunWith(value = Parameterized.class)

public class VerificadorCorreoElectronicoTest {

    @Test
    public void given_email_when_verify_email_then_ok(){

        VerificadorCorreoElectronico verificadorCorreoElectronico = new VerificadorCorreoElectronico();
        boolean correoValido = verificadorCorreoElectronico.verificarCorreoElectronico("gregory.sd.17@gmail.com");
        assertTrue("correo con formato válido debe ser aceptado", correoValido);
    }

    /*private String email;

    @Parameterized.Parameters
    public static Iterable<Object[]> parameters(){
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{"gregory.sd.17@gmail.com"});
        objects.add(new Object[]{"gregory.sd.17@hotmail.com"});
        return objects;
    }

    public void UsuarioParameters(String email){
        this.email = email;
    }*/
}
