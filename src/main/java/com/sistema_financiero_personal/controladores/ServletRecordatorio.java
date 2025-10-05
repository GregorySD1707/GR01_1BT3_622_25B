package com.sistema_financiero_personal.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.sistema_financiero_personal.daos.DAORecordatorio;
import com.sistema_financiero_personal.modelos.Recordatorio;
import com.sistema_financiero_personal.modelos.Recurrencia;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/recordatorios/*")
public class ServletRecordatorio extends HttpServlet {

    private DAORecordatorio recordatorioDAO;

    @Override
    public void init() {
        this.recordatorioDAO = new DAORecordatorio();
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
            default:
                listarRecordatorios(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/";
        }

        String method = request.getParameter("_method");

        if ("/borrar".equals(action)) {
            borrarRecordatorio(request, response);
        } else if ("PUT".equalsIgnoreCase(method)) {
            actualizarRecordatorio(request, response);
        } else {
            crearRecordatorio(request, response);
        }
    }

    // --- MÉTODOS DE ACCIÓN ---

    private void listarRecordatorios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Recordatorio> listaRecordatorios = recordatorioDAO.listar();
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
            Recordatorio recordatorioExistente = recordatorioDAO.buscarPorId(id);
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

    private void crearRecordatorio(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            Recordatorio nuevoRecordatorio = construirRecordatorioDesdeRequest(request);
            recordatorioDAO.crear(nuevoRecordatorio);

            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=creado");

        } catch ( DateTimeParseException | IllegalArgumentException e) {
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
            recordatorio.setId(id);

            recordatorioDAO.actualizar(recordatorio);
            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=actualizado");
        } catch (DateTimeParseException | IllegalArgumentException e) {
            request.setAttribute("error", "Datos inválidos. Por favor, revisa los campos.");
            request.setAttribute("recordatorio", request.getParameterMap());
            request.getRequestDispatcher("/VistaFormularioRecordatorio.jsp").forward(request, response);
        }
    }

    private void borrarRecordatorio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            recordatorioDAO.borrar(id);
            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=eliminado");
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recordatorios?error=idInvalido");
        }
    }


}