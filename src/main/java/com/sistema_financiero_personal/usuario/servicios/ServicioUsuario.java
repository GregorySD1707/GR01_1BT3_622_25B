package com.sistema_financiero_personal.usuario.servicios;

import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.usuario.modelos.UsuarioGoogle;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ServicioUsuario {
    private DAOUsuario daoUsuario;
    private ServicioCartera servicioCartera;
    private static final String CARACTERES_ESPECIALES = "!@$%^&*_+-=[]{}|;:,.<>?";
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;

    // Sets en memoria para manejo de duplicados cuando no hay DAO
    private final Set<String> nombresUsuarioUsados = new HashSet<>();
    private final Set<String> correosUsados = new HashSet<>();

    public ServicioUsuario(DAOUsuario daoUsuario) {
        this.servicioCartera = new ServicioCartera();
        this.daoUsuario = daoUsuario;
    }
    public ServicioUsuario() {
       this(null);
    }


    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException(String message) {
            super(message);
        }
    }

    public Usuario registrarUsuario(String nombre, String apellido, String correo, String nombreUsuario,
                                    String contrasena, LocalDate fechaNacimiento) {
        validarDatosUsuario(nombreUsuario, correo, contrasena, fechaNacimiento);
        verificarDuplicados(nombreUsuario, correo);

        Usuario usuario = new Usuario(nombre, apellido, correo, nombreUsuario, contrasena, fechaNacimiento);
        if (usuario.getCartera() == null) {
            asignarCartera(usuario);
        }
        if(daoUsuario != null) {
            daoUsuario.crear(usuario);
        }

        // Registrar en memoria (case-insensitive) para futuras comprobaciones
        if (daoUsuario == null) {
            if (correo != null) {
                correosUsados.add(correo.toLowerCase());
            }
            if (nombreUsuario != null) {
                nombresUsuarioUsados.add(nombreUsuario.toLowerCase());
            }
        }

        return usuario;
    }

    private void verificarDuplicados(String nombreUsuario, String correo) {
        if (existeNombreUsuario(nombreUsuario)) {
            throw new UserAlreadyExistsException("El nombre de usuario ya existe " + nombreUsuario);
        }
        if (existeCorreo(correo)) {
            throw new UserAlreadyExistsException("El correo ya está registrado " + correo);
        }
    }

    public boolean existeCorreo(String correo) {
        if (correo == null) return false;
        String key = correo.toLowerCase();
        boolean enMemoria = correosUsados.contains(key);
        boolean enDao = daoUsuario != null && daoUsuario.existe(correo);
        return enMemoria || enDao;
    }


    public boolean existeNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null) return false;
        String key = nombreUsuario.toLowerCase();
        return nombresUsuarioUsados.contains(key);
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
        LocalDate hoy = LocalDate.now();
        if (fechaNacimiento.isAfter(hoy)) {
            throw new IllegalArgumentException("Fecha de nacimiento no puede ser en el futuro");
        }
        int edad = Period.between(fechaNacimiento, hoy).getYears();
        if (edad < 13) {
            throw new IllegalArgumentException("El usuario debe tener al menos 13 años");
        }
    }

    private void asignarCartera(Usuario usuario) {
        Cartera cartera = new Cartera();
        usuario.setCartera(cartera);
    }

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
        return daoUsuario.listar();
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
