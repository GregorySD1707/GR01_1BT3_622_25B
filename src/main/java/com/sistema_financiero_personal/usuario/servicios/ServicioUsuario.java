package com.sistema_financiero_personal.usuario.servicios;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.modelos.UsuarioGoogle;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicioUsuario {
    private DAOUsuario daoUsuario;
    // private ServicioCartera servicioCartera;
    private static final String CARACTERES_ESPECIALES = "!@$%^&*_+-=[]{}|;:,.<>?";
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;

    public ServicioUsuario(DAOUsuario daoUsuario) {
        this.daoUsuario = daoUsuario;
    }

    public ServicioUsuario() {

    }

    public void registrarUsuario(String nombre, String apellido, String correo, String nombreUsuario,
                                    String contrasena, LocalDate fechaNacimiento) {
        validarDatosUsuario(nombreUsuario, correo, contrasena, fechaNacimiento);

        Usuario usuario = new Usuario(nombre, apellido, correo, nombreUsuario, contrasena, fechaNacimiento);
        // asignarCartera(usuario);
        //daoUsuario.crear(usuario);
    }

    private void validarDatosUsuario(String nombreUsuario, String correo, String contrasena, LocalDate fechaNacimiento) {
        validarCampoTexto(nombreUsuario, "Nombre de usuario");
        validarCampoTexto(correo, "Correo");
        validarCampoTexto(contrasena, "Contraseña");
        validarFechaNacimiento(fechaNacimiento);
    }

    private void validarCampoTexto(String campo, String nombreCampo) {
        if (campo == null || campo.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacío");
        }
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("Fecha de nacimiento no puede ser nula");
        }
    }

//    private void asignarCartera(Usuario usuario) {
//        Cartera cartera = new Cartera();
//        usuario.setCartera(cartera);
//    }

    public boolean validarContrasena(String contrasena) {
        if (!esContrasenaValida(contrasena)) {return false;}
        if (!tieneLongitudMinima(contrasena)) {return false;}
        return cumpleRequisitosDeComplejidad(contrasena);
    }
    private boolean esContrasenaValida(String contrasena) {
        return contrasena != null && !contrasena.isEmpty();
    }
    private boolean tieneLongitudMinima(String contrasena) {
        return contrasena.length() >= LONGITUD_MINIMA_CONTRASENA;
    }
    private boolean cumpleRequisitosDeComplejidad(String contrasena) {
        return tieneMayuscula(contrasena)
                && tieneMinuscula(contrasena)
                && tieneNumero(contrasena)
                && tieneCaracterEspecial(contrasena);
    }
    private boolean tieneMayuscula(String contrasena) {return contrasena.chars().anyMatch(Character::isUpperCase);}
    private boolean tieneMinuscula(String contrasena) { return contrasena.chars().anyMatch(Character::isLowerCase);}
    private boolean tieneNumero(String contrasena) {return contrasena.chars().anyMatch(Character::isDigit);}
    private boolean tieneCaracterEspecial(String contrasena) {
        return contrasena.chars()
                .anyMatch(c -> CARACTERES_ESPECIALES.indexOf(c) >= 0);
    }

    //public Cartera obtenerCartera(Long carteraId) {return servicioCartera.buscarPorId(carteraId);}

    public List<Usuario> listar() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public boolean existeUsuario(String correo) {
        return daoUsuario.existe(correo);
    }

    public boolean loginUsuario(String correo, String contrasena) {
        boolean credencialesValidas = daoUsuario.credencialesCorrectas(correo, contrasena);
        if (!credencialesValidas) {
            throw new RuntimeException("Credenciales incorrectas");
        }
        return true; // login exitoso
    }

}