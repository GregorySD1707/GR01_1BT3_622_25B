package com.sistema_financiero_personal.usuarios;

import com.sistema_financiero_personal.usuario.servicios.ServicioUsuarioTest;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class AtributoNullTest {
    private ServicioUsuarioTest servicioUsuarioTest;
    private static final String NOMBRE = "Mateo";
    private static final String APELLIDO = "Zambrano";
    private static final String CORREO = "mateito@epn.edu.ec";
    private static final String NOMBRE_USUARIO = "xxXMateoXxx";
    private static final String CONTRASENA = "Abc123$%^";
    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(2004, 4, 11);
    @Before
    public void setUp(){
        servicioUsuarioTest = new ServicioUsuarioTest();
    }
    @Test(expected = IllegalArgumentException.class)
    public void givenNullUserName_whenRegistresUser_thenThrowsException(){
        servicioUsuarioTest.registrarUsuario(NOMBRE, APELLIDO, CORREO, null,
                CONTRASENA, FECHA_NACIMIENTO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void givenNullEmail_whenRegistresUser_thenThrowsException(){
        servicioUsuarioTest.registrarUsuario(NOMBRE, APELLIDO, null, NOMBRE_USUARIO, CONTRASENA, FECHA_NACIMIENTO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void givenNullPassword_whenRegistresUser_thenThrowsException(){
        servicioUsuarioTest.registrarUsuario(NOMBRE, APELLIDO, CORREO, NOMBRE_USUARIO, null, FECHA_NACIMIENTO);
    }
    @Test(expected = IllegalArgumentException.class)
    public void givenNullBirthDate_whenRegistresUser_thenThrowsException(){
        servicioUsuarioTest.registrarUsuario(NOMBRE, APELLIDO, CORREO, NOMBRE_USUARIO, CONTRASENA, null);
    }

}



