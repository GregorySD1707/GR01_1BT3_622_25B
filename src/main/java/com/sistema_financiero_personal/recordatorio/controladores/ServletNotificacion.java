package com.sistema_financiero_personal.recordatorio.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.sistema_financiero_personal.recordatorio.daos.DAORecordatorio;
import com.sistema_financiero_personal.recordatorio.modelos.Recordatorio;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/notificaciones")
public class ServletNotificacion extends HttpServlet {

    private DAORecordatorio recordatorioDAO;
    private Gson gson = new Gson();

    @Override
    public void init() {
        this.recordatorioDAO = new DAORecordatorio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Usuario usuario = obtenerUsuarioSesion(request);
            if (usuario == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Usuario no autenticado");
                return;
            }

            LocalDate hoy = LocalDate.now();

            List<Recordatorio> recordatorios = recordatorioDAO.listarActivos(usuario.getId());
            List<Map<String, String>> notificaciones = new ArrayList<>();

            if (recordatorios != null && !recordatorios.isEmpty()) {
                for (Recordatorio r : recordatorios) {
                    r.obtenerFechaNotificable(hoy).ifPresent(fechaVencimiento -> {
                        Map<String, String> notificacion = new HashMap<>();
                        notificacion.put("descripcion", r.getDescripcion());
                        notificacion.put("fecha", fechaVencimiento.toString());
                        notificacion.put("monto", String.valueOf(r.getMonto()));
                        notificaciones.add(notificacion);
                    });
                }
            }

            procesarNotificaciones(response, notificaciones);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing request");
        }
    }

    /**
     * Procesa las notificaciones generadas y las envía como respuesta JSON
     */
    private void procesarNotificaciones(HttpServletResponse response, List<Map<String, String>> notificaciones)
            throws IOException {
        String jsonResponse = this.gson.toJson(notificaciones);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    /**
     * Obtiene el usuario de la sesión actual
     */
    private Usuario obtenerUsuarioSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (Usuario) session.getAttribute("usuario");
        }
        return null;
    }
}