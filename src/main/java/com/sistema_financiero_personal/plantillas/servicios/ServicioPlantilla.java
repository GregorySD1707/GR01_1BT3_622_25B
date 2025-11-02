package com.sistema_financiero_personal.plantillas.servicios;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

public class ServicioPlantilla {
    public void crearPlantilla(Plantilla plantilla, Long usuario_id) {

        boolean estaVacio = plantilla.getNombre().isEmpty();
        boolean estaEnBlanco = plantilla.getNombre().isBlank();
        if(estaVacio ||estaEnBlanco){
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
    }
}
