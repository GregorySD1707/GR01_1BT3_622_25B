package com.sistema_financiero_personal.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.sistema_financiero_personal.daos.RecordatorioDAO;
import com.sistema_financiero_personal.modelos.Recordatorio;
import com.sistema_financiero_personal.modelos.Recurrencia;
public class ServicioRecordatorio {
    private RecordatorioDAO recordatorioDAO;

    public ServicioRecordatorio(){
        this.recordatorioDAO = new RecordatorioDAO();
    }
    public void registrarRecordatorio(LocalDate fechaInicio, LocalDate fechaFin, String descripcion, Recurrencia recurrencia, double monto, int diasDeAnticipacion){
        Recordatorio recordatorio = new Recordatorio(fechaInicio, fechaFin, descripcion, recurrencia, monto, diasDeAnticipacion);
        recordatorioDAO.crear(recordatorio);
    }

    public void borrarRecordatorio(Long idRecordatorio){
        recordatorioDAO.borrar(idRecordatorio);
    }

    public List<Recordatorio> listarRecordatorios(){
        return recordatorioDAO.listar();
    }

    public void notificarRecordatorio(){
        LocalDate hoy = LocalDate.now();
        List<Recordatorio> recordatorios = recordatorioDAO.listarActivos();
        if(recordatorios.isEmpty()){
            System.out.println("No hay recordatorios activos");
        } else {
            for(Recordatorio r : recordatorios) {
                Optional <LocalDate> op = r.obtenerFechaVencimiento(hoy);
                op.ifPresent(fechaVencimiento -> {
                    System.out.println("Notificación: De recordatorio " + r.getDescripcion() + " con vencimiento el " + fechaVencimiento);
                });
            }
        }
    }
    public void actualizarRecordatorio(Recordatorio recordatorio){
        recordatorioDAO.actualizar(recordatorio);
    }
   public Recordatorio buscarRecordatorio(Long id){
        return (Recordatorio) recordatorioDAO.buscarPorId(id);
   }
}
