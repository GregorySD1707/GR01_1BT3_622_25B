package com.sistema_financiero_personal.plantillas.servicios;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.plantillas.daos.DAOPlantilla;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.plantillas.daos.DAOPlantilla;

import java.time.LocalDateTime;

public class ServicioPlantilla {

    private final DAOPlantilla dao;

    public ServicioPlantilla() {
        this.dao = new DAOPlantilla();
    }

    public ServicioPlantilla(DAOPlantilla dao) {
        this.dao = dao;
    }

    public boolean crearPlantilla(Plantilla plantilla, Long usuario_id) {

        if (plantilla == null) {
            throw new IllegalArgumentException("Plantilla no puede ser nula");
        }

        boolean estaVacio = plantilla.getNombre() == null || plantilla.getNombre().isEmpty();
        boolean estaEnBlanco = plantilla.getNombre() != null && plantilla.getNombre().isBlank();
        if(estaVacio || estaEnBlanco){
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        validarMonto(plantilla.getMonto());

        String tipo = plantilla.getTipo(); // en la entidad Plantilla es String "GASTO"/"INGRESO"
        validarTipo(tipo);

        Object categoriaEnum = plantilla.getCategoria();
        String categoriaStr = (categoriaEnum != null) ? categoriaEnum.toString() : null;
        validarCategoria(tipo, categoriaStr);

        return dao.crearPlantilla(plantilla, usuario_id);
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

    public boolean eliminarPlantilla(Long plantilla_Id) {
        return dao.eliminarPlantilla(plantilla_Id);
    }
    public Movimiento aplicarPlantilla(Plantilla plantilla) {
        if (plantilla == null) {
            throw new IllegalArgumentException("La plantilla no puede ser nula");
        }

        if (!plantilla.isActivo()) {
            throw new IllegalStateException("La plantilla debe estar activa");
        }

        String descripcion = plantilla.getNombre();

        Movimiento movimiento;

        if ("INGRESO".equals(plantilla.getTipo())) {
            movimiento = new Ingreso(
                    plantilla.getMonto(),
                    LocalDateTime.now(),
                    descripcion,
                    (CategoriaIngreso) plantilla.getCategoria()
            );
        } else if ("GASTO".equals(plantilla.getTipo())) {
            movimiento = new Gasto(
                    plantilla.getMonto(),
                    LocalDateTime.now(),
                    descripcion,
                    (CategoriaGasto) plantilla.getCategoria()
            );
        } else {
            throw new IllegalArgumentException("Tipo de plantilla inválido: " + plantilla.getTipo());
        }

        movimiento.setCuenta(plantilla.getCuenta());
        return movimiento;
    }

}
