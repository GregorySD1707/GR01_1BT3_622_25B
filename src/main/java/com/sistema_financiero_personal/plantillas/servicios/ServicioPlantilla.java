package com.sistema_financiero_personal.plantillas.servicios;

import com.sistema_financiero_personal.movimiento.modelos.*;
import com.sistema_financiero_personal.plantillas.daos.DAOPlantilla;
import com.sistema_financiero_personal.plantillas.modelos.Plantilla;
import com.sistema_financiero_personal.usuario.daos.DAOUsuario;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServicioPlantilla {

    private final DAOPlantilla dao;
    private DAOUsuario daoUsuario;
    private List<Plantilla> plantillas = new ArrayList<>();


    public ServicioPlantilla() {
        this.dao = new DAOPlantilla();
        this.daoUsuario = new DAOUsuario();
    }

    public ServicioPlantilla(DAOPlantilla dao, DAOUsuario daoUsuario) {
        this.dao = dao;
        this.daoUsuario = daoUsuario;
    }
/*
utilice Extract Method para mover las validaciones de crearPlantilla a un nuevo método llamado validarPlantilla.
*   Antes :
* public void crearPlantilla(Plantilla plantilla, Long usuario_id) {
    if (plantilla == null) {
        throw new IllegalArgumentException("Plantilla no puede ser nula");
    }

    boolean estaVacio = plantilla.getNombre() == null || plantilla.getNombre().isEmpty();
    boolean estaEnBlanco = plantilla.getNombre() != null && plantilla.getNombre().isBlank();
    if(estaVacio || estaEnBlanco){
        throw new IllegalArgumentException("El nombre no puede estar vacío");
    }

    validarMonto(plantilla.getMonto());
    String tipo = plantilla.getTipo();
    validarTipo(tipo);
    Object categoriaEnum = plantilla.getCategoriaEnum();
    String categoriaStr = (categoriaEnum != null) ? categoriaEnum.toString() : null;
    validarCategoria(tipo, categoriaStr);

    Usuario usuario = daoUsuario.buscarPorId(usuario_id);
    plantilla.setUsuario(usuario);
    plantilla.setFechaCreacion(LocalDateTime.now());
    dao.crear(plantilla);
}
Después :
* */
    public void crearPlantilla(Plantilla plantilla, Long usuarioId) {
        validarPlantilla(plantilla);

        Usuario usuario = daoUsuario.buscarPorId(usuarioId);
        plantilla.setUsuario(usuario);
        plantilla.setFechaCreacion(LocalDateTime.now());
        dao.crear(plantilla);
    }

    private void validarPlantilla(Plantilla plantilla) {
        if (plantilla == null) {
            throw new IllegalArgumentException("Plantilla no puede ser nula");
        }

        String nombre = plantilla.getNombre();
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        validarMonto(plantilla.getMonto());
        validarTipo(plantilla.getTipo());

        Object categoriaEnum = plantilla.getCategoriaEnum();
        String categoriaStr = (categoriaEnum != null) ? categoriaEnum.toString() : null;
        validarCategoria(plantilla.getTipo(), categoriaStr);
    }

//
    public void validarMonto(double monto) {
        if (Double.isNaN(monto) || monto <= 0.0 || monto > 999_999.99) {
            throw new IllegalArgumentException("Monto no válido");
        }
        redondearMonto(monto);
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

    public void eliminarPlantilla(Long plantilla_Id) {
        dao.borrar(plantilla_Id);
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
                    (CategoriaIngreso) plantilla.getCategoriaEnum()
            );
        } else if ("GASTO".equals(plantilla.getTipo())) {
            movimiento = new Gasto(
                    plantilla.getMonto(),
                    LocalDateTime.now(),
                    descripcion,
                    (CategoriaGasto) plantilla.getCategoriaEnum()
            );
        } else {
            throw new IllegalArgumentException("Tipo de plantilla inválido: " + plantilla.getTipo());
        }

        movimiento.setCuenta(plantilla.getCuenta());
        return movimiento;
    }

    public Plantilla duplicarPlantilla(Plantilla original) {

        if (original == null) {
            throw new IllegalArgumentException("Plantilla original requerida");
        }

        Plantilla copia = new Plantilla();

        copia.setNombre(generarNombreUnico(original));

        copia.setMonto(original.getMonto());
        copia.setTipo(original.getTipo());
        copia.setCategoria(original.getCategoria());
        copia.setCuenta(original.getCuenta());
        copia.setActivo(true);
        copia.setFechaCreacion(LocalDateTime.now());

        return copia;
    }

    private String generarNombreUnico(Plantilla original) {
        String nombreBase = extraerNombreBase(original.getNombre());

        int maxNumero = 0;

        for (Plantilla p : plantillas) {
            String nombreActual = p.getNombre();

            if (nombreActual.equals(nombreBase)) { // Caso: nombre exacto sin número
                maxNumero = Math.max(maxNumero, 1);
            } else if (nombreActual.startsWith(nombreBase + " (")) {
                // Extraer el número de "nombreBase (n)"
                String numeroStr = nombreActual.substring(nombreBase.length() + 2, nombreActual.length() - 1);
                int numero = Integer.parseInt(numeroStr);
                maxNumero = Math.max(maxNumero, numero + 1);
            }
        }

        // Si maxNumero es 0, significa que no hay copias, usar nombre base
        // Si maxNumero es 1 o más, usar el número correspondiente
        return maxNumero == 0 ? nombreBase : nombreBase + " (" + maxNumero + ")";
    }

    private String extraerNombreBase(String nombreCompleto) {
        // Si el nombre ya tiene formato "nombre (n)", extraer solo la parte base
        if (nombreCompleto.matches(".+ \\(\\d+\\)")) {
            return nombreCompleto.substring(0, nombreCompleto.lastIndexOf(" ("));
        }
        return nombreCompleto;
    }

    public double redondearMonto(double monto) {
        return Math.round(monto * 100.0) / 100.0;
    }
    public void actualizarPlantilla(Plantilla plantilla){
        dao.actualizar(plantilla);
    }

    public Plantilla buscarPorId(Long id) {
        return dao.buscarPorId(id);
    }

    public List<Plantilla> listarPlantillasPorUsuario(Long usuarioId) {
        return dao.buscarPorCampo("usuario.id", usuarioId);
    }

    public void verificarNombreUnico(Plantilla plantilla1) {
        plantillas.forEach(plantilla -> {
            if(plantilla.getNombre().equals(plantilla1.getNombre())){
                throw new IllegalArgumentException("Los nombres de las plantillas deben ser unicos");
            }
        });
        plantillas.add(plantilla1);
    }


    public List<Plantilla> buscarPorNombre(String nombreABuscar) {
        List<Plantilla> plantillasEncontradas = new ArrayList<>();
        plantillas.forEach(plantilla -> {
            if(plantilla.getNombre().contains(nombreABuscar)){
                plantillasEncontradas.add(plantilla);
            }
        });
        return plantillasEncontradas;
    }

    public List<Plantilla> buscarPorCategoriaGasto(CategoriaGasto categoriaGasto) {
        List<Plantilla> plantillasEncontradas = new ArrayList<>();
        plantillas.forEach(plantilla -> {
            if(plantilla.getCategoria().contains(categoriaGasto.toString())){
                plantillasEncontradas.add(plantilla);
            }
        });
        return plantillasEncontradas;
    }

    public List<Plantilla> buscarPorTipo(String tipo) {
        List<Plantilla> plantillasEncontradas = new ArrayList<>();
        plantillas.forEach(plantilla -> {
            if(plantilla.getTipo().contains(tipo)){
                plantillasEncontradas.add(plantilla);
            }
        });
        return plantillasEncontradas;
    }

    public void guardarEnLista(Plantilla plantilla) {
        plantillas.add(plantilla);
    }
}
