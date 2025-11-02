package com.sistema_financiero_personal.plantillas.servicios;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
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

    public void validarMonto(double monto) {
        if (Double.isNaN(monto) || monto <= 0.0 || monto > 999_999.99) {
            throw new IllegalArgumentException("Monto no válido");
        }
    }


    public void validarTipo(String tipo) {
        if(tipo == null || tipo.isBlank()){
            throw new IllegalArgumentException("El tipo no puede estar vacío o en blanco");
        }
        if(!tipo.equals("GASTO") && !tipo.equals("INGRESO")){
            throw new IllegalArgumentException("Tipo inválido");
        }
    }

    public void validarCategoria(String tipo, String categoria) {
        if (categoria == null || categoria.isBlank()) {
            throw new IllegalArgumentException("La categoría no puede estar vacía o en blanco");
        }

        try {
            if ("GASTO".equalsIgnoreCase(tipo)) {
                CategoriaGasto.valueOf(categoria.toUpperCase());
            } else if ("INGRESO".equalsIgnoreCase(tipo)) {
                CategoriaIngreso.valueOf(categoria.toUpperCase());
            } else {
                throw new IllegalArgumentException("Tipo inválido para validación de categoría");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoría inválida");
        }
    }

}
