package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.modelos.Recordatorio;
import com.sistema_financiero_personal.modelos.Recurrencia;
import com.sistema_financiero_personal.servicios.ServicioRecordatorio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/recordatorios/*")
public class ServletRecordatorio extends HttpServlet {

    private ServicioRecordatorio servicioRecordatorio;

    @Override
    public void init() {
        this.servicioRecordatorio = new ServicioRecordatorio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/";
        }

        switch (action) {
            case "/nuevo":
                mostrarFormulario(request, response, new Recordatorio()); // Enviar objeto vacío para evitar nulls en el JSP
                break;
            case "/editar":
                mostrarFormularioEditar(request, response);
                break;
            // CAMBIO 1: Se eliminó el caso "/eliminar" de doGet. Es una operación insegura aquí.
            default:
                listarRecordatorios(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/"; // Por si se envía a /recordatorios
        }

        // CAMBIO 2: Se mejora la lógica de enrutamiento para incluir la eliminación.
        String method = request.getParameter("_method");

        if ("/eliminar".equals(action)) {
            eliminarRecordatorio(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            actualizarRecordatorio(request, response);
        } else {
            insertarRecordatorio(request, response);
        }
    }

    // --- MÉTODOS DE ACCIÓN ---

    private void listarRecordatorios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Recordatorio> listaRecordatorios = servicioRecordatorio.listarRecordatorios();
        request.setAttribute("recordatorios", listaRecordatorios);
        request.getRequestDispatcher("/VistaRecordatorios.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Recordatorio recordatorio) throws ServletException, IOException {
        request.setAttribute("recordatorio", recordatorio);
        // También enviamos los valores del enum para poblar el <select> en el JSP
        request.setAttribute("recurrencias", Recurrencia.values());
        request.getRequestDispatcher("/VistaFormularioRecordatorio.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Recordatorio recordatorioExistente = servicioRecordatorio.buscarRecordatorio(id);
            if (recordatorioExistente != null) {
                mostrarFormulario(request, response, recordatorioExistente);
            } else {
                // Manejar el caso en que el recordatorio no se encuentre
                response.sendRedirect(request.getContextPath() + "/recordatorios?error=noEncontrado");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recordatorios?error=idInvalido");
        }
    }

    private Recordatorio construirRecordatorioDesdeRequest(HttpServletRequest request) {
        String descripcion = request.getParameter("descripcion");
        LocalDate fechaInicio = LocalDate.parse(request.getParameter("fechaInicio"));
        LocalDate fechaFin = LocalDate.parse(request.getParameter("fechaFin"));
        Recurrencia recurrencia = Recurrencia.valueOf(request.getParameter("recurrencia"));
        int diasAnticipacion = Integer.parseInt(request.getParameter("diasDeAnticipacion"));
        double monto = Double.parseDouble(request.getParameter("monto"));

        return new Recordatorio(fechaInicio, fechaFin, descripcion, recurrencia, monto, diasAnticipacion);
    }

    private void insertarRecordatorio(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // 1. Usamos el método auxiliar para crear el objeto. Esto sigue siendo una buena práctica.
            Recordatorio nuevoRecordatorio = construirRecordatorioDesdeRequest(request);

            // 2. AHORA, pasamos cada valor individualmente al servicio, tal como lo espera.
            servicioRecordatorio.registrarRecordatorio(
                    nuevoRecordatorio.getFechaInicio(),
                    nuevoRecordatorio.getFechaFin(),
                    nuevoRecordatorio.getDescripcion(),
                    nuevoRecordatorio.getRecurrencia(),
                    nuevoRecordatorio.getMonto(),
                    nuevoRecordatorio.getDiasDeAnticipacion()
            );

            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=creado");

        } catch ( DateTimeParseException | IllegalArgumentException e) {
            // El manejo de errores que ya teníamos sigue siendo válido y funciona bien.
            request.setAttribute("error", "Datos inválidos. Por favor, revisa los campos.");
            Recordatorio datosIngresados = new Recordatorio();
            datosIngresados.setDescripcion(request.getParameter("descripcion"));
            mostrarFormulario(request, response, datosIngresados);
        }
    }

    private void actualizarRecordatorio(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Recordatorio recordatorio = construirRecordatorioDesdeRequest(request);
            recordatorio.setId(id); // Asignamos el ID para la actualización

            servicioRecordatorio.actualizarRecordatorio(recordatorio);
            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=actualizado");
        } catch (DateTimeParseException | IllegalArgumentException e) {
            request.setAttribute("error", "Datos inválidos. Por favor, revisa los campos.");
            request.setAttribute("recordatorio", request.getParameterMap());
            request.getRequestDispatcher("/VistaFormularioRecordatorio.jsp").forward(request, response);
        }
    }

    private void eliminarRecordatorio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            servicioRecordatorio.borrarRecordatorio(id);
            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=eliminado");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recordatorios?error=idInvalido");
        }
    }


}