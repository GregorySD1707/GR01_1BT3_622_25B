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
        this.servicioRecordatorio = new ServicioRecordatorio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LocalDate hoy = LocalDate.now();
            List<Recordatorio> recordatorios = servicioRecordatorio.listarRecordatoriosActivos();

            List<Map<String, String>> notificaciones = new ArrayList<>();

            if (recordatorios != null && !recordatorios.isEmpty()) {
                for (Recordatorio r : recordatorios) {
                    r.obtenerFechaVencimiento(hoy).ifPresent(fechaVencimiento -> {
                        Map<String, String> notificacion = new HashMap<>();
                        notificacion.put("descripcion", r.getDescripcion());
                        notificacion.put("fecha", fechaVencimiento.toString());
                        notificaciones.add(notificacion);
                    });
                }
            }

            String jsonResponse = this.gson.toJson(notificaciones);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(jsonResponse);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }
}