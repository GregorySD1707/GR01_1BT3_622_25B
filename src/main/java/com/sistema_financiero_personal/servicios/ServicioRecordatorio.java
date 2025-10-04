package com.sistema_financiero_personal.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.sistema_financiero_personal.daos.DAORecordatorio;
import com.sistema_financiero_personal.modelos.Recordatorio;
import com.sistema_financiero_personal.modelos.Recurrencia;
public class ServicioRecordatorio {
    private DAORecordatorio recordatorioDAO;

    public ServicioRecordatorio(){
        this.recordatorioDAO = new DAORecordatorio();
    }
    public void crearRecordatorio(LocalDate fechaInicio, LocalDate fechaFin, String descripcion, Recurrencia recurrencia, double monto, int diasDeAnticipacion){
        Recordatorio recordatorio = new Recordatorio(fechaInicio, fechaFin, descripcion, recurrencia, monto, diasDeAnticipacion);
        recordatorioDAO.crear(recordatorio);
    }

    public void borrarRecordatorio(Long idRecordatorio){
        recordatorioDAO.borrar(idRecordatorio);
    }

    public List<Recordatorio> listarRecordatorios(){
        return recordatorioDAO.listar();
    }

    public List<Recordatorio> listarRecordatoriosActivos(){
        return recordatorioDAO.listarActivos();
    }

    public void actualizarRecordatorio(Recordatorio recordatorio){
        recordatorioDAO.actualizar(recordatorio);
    }
   public Recordatorio buscarRecordatorio(Long id){
        return (Recordatorio) recordatorioDAO.buscarPorId(id);
   }
}
