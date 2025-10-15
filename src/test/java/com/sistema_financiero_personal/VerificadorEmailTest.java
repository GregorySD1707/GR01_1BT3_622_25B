package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.modelos.Usuario;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(value = Parameterized.class)
public class VerificadorEmailTest {

    private String email;

    @Parameterized.Parameters
    public static Iterable<Object[]> parameters(){
        List<Object[]> objects = new ArrayList<>();
        objects.add(new Object[]{"gregory.sd.17@gmail.com"});
        objects.add(new Object[]{"gregory.sd.17@hotmail.com"});
        return objects;
    }

    public void UsuarioParameters(String email){
        this.email = email;
    }

    @Test
    public void given_email_when_verify_email_then_ok(){
        Usuario usuario = new Usuario();
        //usuario.setEmail(email);
        //VerificadorEmail verificadorEmail = new VerficadorEmail();
        //bool valido = verificadorEmail.verificarEmail(usuario.getEmail());
        //assertTrue(true, valido);
    }

}
