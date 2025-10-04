// File: com/sistema_financiero_personal/controladores/ServletNotificacion.java

package com.sistema_financiero_personal.controladores;

import com.google.gson.Gson;
import com.sistema_financiero_personal.modelos.Recordatorio;
import com.sistema_financiero_personal.servicios.ServicioRecordatorio; // Assuming you have this service
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/notificaciones") // An API endpoint URL
public class ServletNotificacion extends HttpServlet {

    private ServicioRecordatorio servicioRecordatorio;
    private Gson gson = new Gson();

    @Override
    public void init() {
        // You can get the service instance here, maybe from a context or new up
        this.servicioRecordatorio = new ServicioRecordatorio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // This is where your logic goes
        try {
            LocalDate hoy = LocalDate.now();
            List<Recordatorio> recordatorios = servicioRecordatorio.listarRecordatoriosActivos();

            System.out.println("RECORDATORIOS ENCONTRADOS POR EL SERVICIO: " + recordatorios.size()); // Esta línea ya la tienes

            List<Map<String, String>> notificaciones = new ArrayList<>();

            if (recordatorios != null && !recordatorios.isEmpty()) {
                // Este bucle se está ejecutando 3 veces
                for (Recordatorio r : recordatorios) {

                    // =================================================================
                    //  AÑADE ESTAS LÍNEAS DE DEBUG
                    // =================================================================
                    System.out.println("--- Revisando Recordatorio ID: " + r.getId() + ", Descripción: " + r.getDescripcion());
                    System.out.println("    Fecha Fin: " + r.getFechaFin() + ", Días Anticipación: " + r.getDiasDeAnticipacion());
                    // =================================================================

                    r.obtenerFechaVencimiento(hoy).ifPresent(fechaVencimiento -> {
                        // Si este mensaje NUNCA aparece en la consola, confirmamos la teoría
                        System.out.println("    ¡ÉXITO! Este recordatorio SÍ genera notificación para la fecha: " + fechaVencimiento);

                        Map<String, String> notificacion = new HashMap<>();
                        notificacion.put("descripcion", r.getDescripcion());
                        notificacion.put("fecha", fechaVencimiento.toString());
                        notificaciones.add(notificacion);
                    });
                }
            }
        // Convert the list of notifications to a JSON string
        String jsonResponse = this.gson.toJson(notificaciones);

        // Set the response type to JSON and send it back to the browser
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }
}