package com.sistema_financiero_personal.usuario.servicios;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificadorCorreo {
    public boolean verificarEmail(String correo) {
        // [usuario] + (puntos múltiples, evitando puntos consecutivos) + @ + [sub.dominio.com ó dominio.com] + [TLD]{entre 2 a 7 letras}
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }
}