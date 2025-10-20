package com.sistema_financiero_personal.usuario.servicios;

import com.sistema_financiero_personal.comun.utilidades.EncriptadorContrasena;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServicioRegistroUsuario {
    private DAOUsuario daoUsuario;
    private ServicioCartera servicioCartera;
    private static final String CARACTERES_ESPECIALES = "!@$%^&*_+-=[]{}|;:,.<>?";
    private static final int LONGITUD_MINIMA_CONTRASENA = 8;
    public ServicioRegistroUsuario() {
        this.daoUsuario = new DAOUsuario();
        this.servicioCartera = new ServicioCartera();
    }

    public void registrarUsuario(String nombre, String apellido, String correo, String nombreUsuario,
                                    String contrasena, LocalDate fechaNacimiento) {

        validarCampos(nombre, apellido, correo, nombreUsuario, contrasena, fechaNacimiento);
        verificarCorreoElectronico(correo);
        validarContrasena(contrasena);
        // verificarDuplicados(nombreUsuario, correo);
        String contrasenaEncripada = EncriptadorContrasena.encriptarContrasena(contrasena);

        Usuario usuario = new Usuario(
                nombre,
                apellido,
                correo,
                nombreUsuario,
                contrasenaEncripada,
                fechaNacimiento
        );
        servicioCartera.asignarCartera(usuario);
        daoUsuario.crear(usuario);
    }

    private void validarCampos(String nombre, String apellido, String correo, String nombreUsuario, String contrasena, LocalDate fechaNacimiento) {
        if(nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if(apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }
        if(correo == null || correo.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        if(nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if(contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }
        validarFechaNacimiento(fechaNacimiento);
    }

    private void validarFechaNacimiento(LocalDate fechaNacimiento) {
        LocalDate hoy = LocalDate.now();
        if(fechaNacimiento.isAfter(hoy)) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser en el futuro");
        }
        int edadMinima = Period.between(fechaNacimiento, hoy).getYears();
        if(edadMinima < 13) {
            throw new IllegalArgumentException("Debes tener al menos 13 años para registrarte");
        }
        if (edadMinima > 120) {
            throw new IllegalArgumentException("La fecha de nacimiento no es válida");
        }
    }
    public boolean verificarCorreoElectronico(String correo) {
        String usuario = "[a-zA-Z0-9_+&*-]+";
        String puntosOpcionales = "(?:\\.[a-zA-Z0-9_+&*-]+)*"; // puntos múltiples, evitando puntos consecutivos
        String dominio = "(?:[a-zA-Z0-9-]+\\.)+"; // sub.dominio.com ó dominio.com
        String extension = "[a-zA-Z]{2,7}"; // [TLD]{entre 2 a 7 letras}

        Pattern pattern = Pattern.compile("^" + usuario + puntosOpcionales + "@" + dominio + extension + "$");
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }
    private void validarContrasena(String contrasena) {
        if(contrasena.length() < 8){
            throw new IllegalArgumentException("La contraseña debe tener al menos " + LONGITUD_MINIMA_CONTRASENA + " caracteres");
        }
        if(!tieneMayuscula(contrasena)){
            throw new IllegalArgumentException("La contraseña debe tener al menos una letra mayúscula");
        }
        if (!tieneMinuscula(contrasena)){
            throw new IllegalArgumentException("La contraseña debe tener al menos una letra minúscula");
        }
        if (!tieneNumero(contrasena)){
            throw new IllegalArgumentException("La contraseña debe tener al menos un número");
        }
        if (!tieneCaracterEspecial(contrasena)){
            throw new IllegalArgumentException("La contraseña debe tener al menos un carácter especial: " + CARACTERES_ESPECIALES);
        }
    }

    private boolean tieneMayuscula(String contrasena) {return contrasena.chars().anyMatch(Character::isUpperCase);}
    private boolean tieneMinuscula(String contrasena) { return contrasena.chars().anyMatch(Character::isLowerCase);}
    private boolean tieneNumero(String contrasena) {return contrasena.chars().anyMatch(Character::isDigit);}
    private boolean tieneCaracterEspecial(String contrasena) {
        return contrasena.chars()
                .anyMatch(c -> CARACTERES_ESPECIALES.indexOf(c) >= 0);
    }

    public Usuario autenticarUsuario(String identificadorUsuario, String contrasena) {
        if (identificadorUsuario == null || identificadorUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía");
        }

        Usuario usuario = daoUsuario.buscarPorNombreUsuario(identificadorUsuario);
        if (usuario == null) {
           usuario = daoUsuario.buscarPorCorreo(identificadorUsuario);
        }

        if (usuario == null) {
            throw new IllegalArgumentException("Usuario o contraseña incorrectos");
        }

        boolean contrasenaValida = EncriptadorContrasena.verificarContrasena(contrasena, usuario.getContrasena());
        if (contrasenaValida) {
            return usuario;
        }
        return null;
    }
}