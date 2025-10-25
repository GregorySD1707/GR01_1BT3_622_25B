package com.sistema_financiero_personal.comun.utilidades;

import org.mindrot.jbcrypt.BCrypt;

public class EncriptadorContrasena {
    public static String encriptarContrasena(String contrasena){
        return new BCrypt().hashpw(contrasena, BCrypt.gensalt(12));

    }
    public static boolean verificarContrasena(String contrasenaPlana, String contrasenaEncriptada){
        return BCrypt.checkpw(contrasenaPlana, contrasenaEncriptada);
    }
}
