package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.servicios.ServicioUsuario;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidarContrasenaTest {
    private ServicioUsuario servicioUsuario;
    @Before
    public void setUp(){
        servicioUsuario = new ServicioUsuario();
    }
    @Test
    public void given_passwordWithLessThan8Characters_whenValidatePassword_thenReturnFalse(){
        String shortPassword = "123";
        boolean result = servicioUsuario.validarContrasena(shortPassword);
        assertFalse(result);
    }
    @Test
    public void given_passwordWithoutUppercase_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutUppercase = "123abc$%";
        boolean result = servicioUsuario.validarContrasena(passwordWithoutUppercase);
        assertFalse(result);
    }
    @Test
    public void given_validPassword_whenValidatePassword_thenReturnTrue(){
        String validPassword = "Abc123$%";
        boolean result = servicioUsuario.validarContrasena(validPassword);
        assertTrue(result);
    }
    @Test
    public void given_passwordWithoutLowercase_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutLowercase = "123ABC$%";
        boolean result = servicioUsuario.validarContrasena(passwordWithoutLowercase);
        assertFalse(result);
    }
    @Test
    public void given_passwordWithoutNumber_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutNumber = "Abcdef$%";
        boolean result = servicioUsuario.validarContrasena(passwordWithoutNumber);
        assertFalse(result);
    }
    @Test
    public void given_passwordWithoutSpecialCharacter_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutSpecialCharacter = "Abc12345";
        boolean result = servicioUsuario.validarContrasena(passwordWithoutSpecialCharacter);
        assertFalse(result);
    }
}





