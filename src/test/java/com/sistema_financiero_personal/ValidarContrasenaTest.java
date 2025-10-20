package com.sistema_financiero_personal;

import com.sistema_financiero_personal.usuario.servicios.ServicioUsuarioTest;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidarContrasenaTest {
    private ServicioUsuarioTest servicioUsuarioTest;
    @Before
    public void setUp(){
        servicioUsuarioTest = new ServicioUsuarioTest();
    }
    @Test
    public void given_passwordWithLessThan8Characters_whenValidatePassword_thenReturnFalse(){
        String shortPassword = "123";
        boolean result = servicioUsuarioTest.validarContrasena(shortPassword);
        assertFalse(result);
    }
    @Test
    public void given_passwordWithoutUppercase_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutUppercase = "123abc$%";
        boolean result = servicioUsuarioTest.validarContrasena(passwordWithoutUppercase);
        assertFalse(result);
    }
    @Test
    public void given_validPassword_whenValidatePassword_thenReturnTrue(){
        String validPassword = "Abc123$%";
        boolean result = servicioUsuarioTest.validarContrasena(validPassword);
        assertTrue(result);
    }
    @Test
    public void given_passwordWithoutLowercase_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutLowercase = "123ABC$%";
        boolean result = servicioUsuarioTest.validarContrasena(passwordWithoutLowercase);
        assertFalse(result);
    }
    @Test
    public void given_passwordWithoutNumber_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutNumber = "Abcdef$%";
        boolean result = servicioUsuarioTest.validarContrasena(passwordWithoutNumber);
        assertFalse(result);
    }
    @Test
    public void given_passwordWithoutSpecialCharacter_whenValidatePassword_thenReturnFalse(){
        String passwordWithoutSpecialCharacter = "Abc12345";
        boolean result = servicioUsuarioTest.validarContrasena(passwordWithoutSpecialCharacter);
        assertFalse(result);
    }
}





