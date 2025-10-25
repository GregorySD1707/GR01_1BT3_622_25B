package com.sistema_financiero_personal.usuario.servicios;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificadorCorreoElectronico {
    public boolean verificarCorreoElectronico(String correo) {
        String usuario = "[a-zA-Z0-9_+&*-]+";
        String puntosOpcionales = "(?:\\.[a-zA-Z0-9_+&*-]+)*"; // puntos múltiples, evitando puntos consecutivos
        String dominio = "(?:[a-zA-Z0-9-]+\\.)+"; // sub.dominio.com ó dominio.com
        String extension = "[a-zA-Z]{2,7}"; // [TLD]{entre 2 a 7 letras}

        Pattern pattern = Pattern.compile("^" + usuario + puntosOpcionales + "@" + dominio + extension + "$");
        Matcher matcher = pattern.matcher(correo);

        return matcher.matches();
    }
}