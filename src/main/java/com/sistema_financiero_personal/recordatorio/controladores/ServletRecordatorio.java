package com.sistema_financiero_personal.recordatorio.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.sistema_financiero_personal.recordatorio.modelos.Recordatorio;
import com.sistema_financiero_personal.recordatorio.modelos.Recurrencia;
import com.sistema_financiero_personal.recordatorio.servicios.ServicioRecordatorio;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/recordatorios/*")
public class ServletRecordatorio extends HttpServlet {

    private ServicioRecordatorio servicioRecordatorio;
    @Override
    public void init() {
        this.servicioRecordatorio = new ServicioRecordatorio();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Verificar sesión
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getPathInfo();
        if (action == null) {
            action = "/";
        }

        switch (action) {
            case "/nuevo":
                mostrarFormulario(request, response, new Recordatorio());
                break;
            case "/editar":
                mostrarFormularioEditar(request, response, usuario);
                break;
            default:
                listarRecordatorios(request, response, usuario);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // Verificar sesión
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String action = request.getPathInfo();
        if (action == null) {
            action = "/";
        }

        String method = request.getParameter("_method");

        if ("/borrar".equals(action)) {
            borrarRecordatorio(request, response, usuario);
        } else if ("PUT".equalsIgnoreCase(method)) {
            actualizarRecordatorio(request, response, usuario);
        } else {
            crearRecordatorio(request, response, usuario);
        }
    }

    private void listarRecordatorios(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        List<Recordatorio> listaRecordatorios = servicioRecordatorio.listarRecordatoriosPorUsuario(usuario.getId());
        request.setAttribute("recordatorios", listaRecordatorios);
        request.getRequestDispatcher("/recordatorio/VistaRecordatorios.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Recordatorio recordatorio)
            throws ServletException, IOException {
        request.setAttribute("recordatorio", recordatorio);
        request.setAttribute("recurrencias", Recurrencia.values());
        request.getRequestDispatcher("/recordatorio/VistaFormularioRecordatorio.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Recordatorio recordatorioExistente = servicioRecordatorio.buscarPorId(id);

            if (recordatorioExistente != null) {
                if (!recordatorioExistente.getUsuario().getId().equals(usuario.getId())) {
                    response.sendRedirect(request.getContextPath() + "/recordatorios?error=noAutorizado");
                    return;
                }
                mostrarFormulario(request, response, recordatorioExistente);
            } else {
                response.sendRedirect(request.getContextPath() + "/recordatorios?error=noEncontrado");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recordatorios?error=idInvalido");
        }
    }

    private Recordatorio construirRecordatorioDesdeRequest(HttpServletRequest request, Usuario usuario) {
        String descripcion = request.getParameter("descripcion");
        LocalDate fechaInicio = LocalDate.parse(request.getParameter("fechaInicio"));

        // Manejar fecha fin opcional
        String fechaFinStr = request.getParameter("fechaFin");
        LocalDate fechaFin = (fechaFinStr != null && !fechaFinStr.isEmpty())
                ? LocalDate.parse(fechaFinStr)
                : null;

        Recurrencia recurrencia = Recurrencia.valueOf(request.getParameter("recurrencia"));
        int diasAnticipacion = Integer.parseInt(request.getParameter("diasDeAnticipacion"));
        double monto = Double.parseDouble(request.getParameter("monto"));

        Recordatorio recordatorio = new Recordatorio(fechaInicio, fechaFin, descripcion, recurrencia, monto, diasAnticipacion);

        recordatorio.setUsuario(usuario);

        return recordatorio;
    }

    private void crearRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            Recordatorio nuevoRecordatorio = construirRecordatorioDesdeRequest(request, usuario);
            servicioRecordatorio.crearRecordatorio(nuevoRecordatorio);

            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=creado");

        } catch (DateTimeParseException | IllegalArgumentException e) {
            request.setAttribute("error", "Datos inválidos. Por favor, revisa los campos.");
            Recordatorio datosIngresados = new Recordatorio();
            datosIngresados.setDescripcion(request.getParameter("descripcion"));
            mostrarFormulario(request, response, datosIngresados);
        }
    }

    private void actualizarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Recordatorio recordatorioExistente = servicioRecordatorio.buscarPorId(id);
            if (recordatorioExistente == null ||
                    !recordatorioExistente.getUsuario().getId().equals(usuario.getId())) {
                response.sendRedirect(request.getContextPath() + "/recordatorios?error=noAutorizado");
                return;
            }

            Recordatorio recordatorio = construirRecordatorioDesdeRequest(request, usuario);
            recordatorio.setId(id);

            servicioRecordatorio.actualizarRecordatorio(recordatorio);
            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=actualizado");

        } catch (DateTimeParseException | IllegalArgumentException e) {
            request.setAttribute("error", "Datos inválidos. Por favor, revisa los campos.");
            request.setAttribute("recordatorio", request.getParameterMap());
            request.getRequestDispatcher("/recordatorio/VistaFormularioRecordatorio.jsp").forward(request, response);
        }
    }

    private void borrarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {
        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Recordatorio recordatorio = servicioRecordatorio.buscarPorId(id);
            if (recordatorio == null ||
                    !recordatorio.getUsuario().getId().equals(usuario.getId())) {
                response.sendRedirect(request.getContextPath() + "/recordatorios?error=noAutorizado");
                return;
            }

            servicioRecordatorio.eliminarRecordatorio(id);
            response.sendRedirect(request.getContextPath() + "/recordatorios?exito=eliminado");

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/recordatorios?error=idInvalido");
        }
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