package com.sistema_financiero_personal.recordatorio.servicios;

import com.sistema_financiero_personal.recordatorio.daos.DAORecordatorio;
import com.sistema_financiero_personal.recordatorio.modelos.Recordatorio;

import java.util.List;

public class ServicioRecordatorio {
    private DAORecordatorio recordatorioDAO;

    public ServicioRecordatorio() {
        this.recordatorioDAO = new DAORecordatorio();
    }

    /**
     * Crea un nuevo recordatorio para un usuario
     */
    public void crearRecordatorio(Recordatorio recordatorio){
        recordatorioDAO.crear(recordatorio);
    }


    /**
     * Actualiza un recordatorio existente
     */
    public void actualizarRecordatorio(Recordatorio recordatorio){
        recordatorioDAO.actualizar(recordatorio);
    }

    /**
     * Elimina un recordatorio
     */
    public void eliminarRecordatorio(Long recordatorioId) {
        if (recordatorioId == null) {
            throw new IllegalArgumentException("El ID del recordatorio es obligatorio");
        }

        // Verificar que existe
       // obtenerRecordatorioPorId(recordatorioId);

        // Eliminar
        recordatorioDAO.borrar(recordatorioId);
    }

    public List<Recordatorio> listarRecordatoriosPorUsuario(Long id) {
        return recordatorioDAO.listarPorCampo(id);
    }

    public Recordatorio buscarPorId(Long id) {
        return recordatorioDAO.buscarPorId(id);
    }
}