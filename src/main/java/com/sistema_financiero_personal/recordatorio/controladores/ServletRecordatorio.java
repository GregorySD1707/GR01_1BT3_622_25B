package com.sistema_financiero_personal.recordatorio.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.sistema_financiero_personal.recordatorio.modelos.Recordatorio;
import com.sistema_financiero_personal.recordatorio.modelos.Recurrencia;
import com.sistema_financiero_personal.recordatorio.servicios.ServicioRecordatorio;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Verificar sesión
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ingreso");
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        // Verificar sesión
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ingreso");
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

        MensajeUtil.obtenerYLimpiarMensajes(request);

        List<Recordatorio> listaRecordatorios = servicioRecordatorio.listarRecordatoriosPorUsuario(usuario.getId());
        request.setAttribute("recordatorios", listaRecordatorios);
        request.getRequestDispatcher("/recordatorio/VistaRecordatorios.jsp").forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Recordatorio recordatorio)
            throws ServletException, IOException {

        MensajeUtil.obtenerYLimpiarMensajes(request);

        request.setAttribute("recordatorio", recordatorio);
        request.setAttribute("recurrencias", Recurrencia.values());
        request.getRequestDispatcher("/recordatorio/VistaFormularioRecordatorio.jsp").forward(request, response);
    }

    private void mostrarFormularioEditar(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        try {
            Long id = Long.parseLong(request.getParameter("id"));
            Recordatorio recordatorioExistente = servicioRecordatorio.buscarPorId(id);

            if (recordatorioExistente != null) {
                if (!recordatorioExistente.getUsuario().getId().equals(usuario.getId())) {
                    MensajeUtil.agregarError(session, "No tienes autorización para editar este recordatorio");
                    response.sendRedirect(request.getContextPath() + "/recordatorios");
                    return;
                }
                mostrarFormulario(request, response, recordatorioExistente);
            } else {
                MensajeUtil.agregarError(session, "Recordatorio no encontrado");
                response.sendRedirect(request.getContextPath() + "/recordatorios");
            }
        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: ID de recordatorio inválido");
            response.sendRedirect(request.getContextPath() + "/recordatorios");
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

        HttpSession session = request.getSession();

        try {
            // Parsear las fechas ANTES de construir el recordatorio
            LocalDate fechaInicio = LocalDate.parse(request.getParameter("fechaInicio"));
            String fechaFinStr = request.getParameter("fechaFin");
            LocalDate fechaFin = (fechaFinStr != null && !fechaFinStr.isEmpty())
                    ? LocalDate.parse(fechaFinStr)
                    : null;

            if (!validarFechas(request, session, fechaInicio, fechaFin)) {
                // Si la validación falla, recargar el formulario con los datos ingresados
                Recordatorio datosIngresados = new Recordatorio();
                datosIngresados.setDescripcion(request.getParameter("descripcion"));
                datosIngresados.setFechaInicio(fechaInicio);
                datosIngresados.setFechaFin(fechaFin);
                try {
                    datosIngresados.setMonto(Double.parseDouble(request.getParameter("monto")));
                } catch (NumberFormatException e) {
                    // Ignorar si el monto es inválido
                }
                mostrarFormulario(request, response, datosIngresados);
                return;
            }

            // Si las fechas son válidas, continuar con la creación
            Recordatorio nuevoRecordatorio = construirRecordatorioDesdeRequest(request, usuario);
            servicioRecordatorio.crearRecordatorio(nuevoRecordatorio);

            MensajeUtil.agregarExito(session, "Recordatorio creado exitosamente");
            response.sendRedirect(request.getContextPath() + "/recordatorios");

        } catch (DateTimeParseException e) {
            MensajeUtil.agregarError(session, "Error: Formato de fecha inválido");

            Recordatorio datosIngresados = new Recordatorio();
            datosIngresados.setDescripcion(request.getParameter("descripcion"));
            mostrarFormulario(request, response, datosIngresados);

        } catch (IllegalArgumentException e) {
            MensajeUtil.agregarError(session, "Datos inválidos. Por favor, revisa los campos");

            Recordatorio datosIngresados = new Recordatorio();
            datosIngresados.setDescripcion(request.getParameter("descripcion"));
            mostrarFormulario(request, response, datosIngresados);

        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al crear el recordatorio: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recordatorios");
        }
    }

    private boolean validarFechas(HttpServletRequest request, HttpSession session,
                                  LocalDate fechaInicio, LocalDate fechaFin) {

        LocalDate hoy = LocalDate.now();

        // Validar que fechaInicio no sea anterior a hoy
        if (fechaInicio.isBefore(hoy)) {
            MensajeUtil.agregarError(session, "La fecha de inicio no puede ser anterior a la fecha actual");
            return false;
        }

        // Validar que fechaFin no sea anterior a fechaInicio (si existe fechaFin)
        if (fechaFin != null && fechaFin.isBefore(fechaInicio)) {
            MensajeUtil.agregarError(session, "La fecha de inicio no puede ser posterior a la fecha final");
            return false;
        }

        return true;
    }
    private void actualizarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Recordatorio recordatorioExistente = servicioRecordatorio.buscarPorId(id);
            if (recordatorioExistente == null) {
                MensajeUtil.agregarError(session, "Recordatorio no encontrado");
                response.sendRedirect(request.getContextPath() + "/recordatorios");
                return;
            }

            if (!recordatorioExistente.getUsuario().getId().equals(usuario.getId())) {
                MensajeUtil.agregarError(session, "No tienes autorización para modificar este recordatorio");
                response.sendRedirect(request.getContextPath() + "/recordatorios");
                return;
            }

            LocalDate fechaInicio = LocalDate.parse(request.getParameter("fechaInicio"));
            String fechaFinStr = request.getParameter("fechaFin");
            LocalDate fechaFin = (fechaFinStr != null && !fechaFinStr.isEmpty())
                    ? LocalDate.parse(fechaFinStr)
                    : null;

            if (!validarFechas(request, session, fechaInicio, fechaFin)) {
                // Si la validación falla, recargar el formulario con los datos ingresados
                recordatorioExistente.setDescripcion(request.getParameter("descripcion"));
                recordatorioExistente.setFechaInicio(fechaInicio);
                recordatorioExistente.setFechaFin(fechaFin);
                try {
                    recordatorioExistente.setMonto(Double.parseDouble(request.getParameter("monto")));
                } catch (NumberFormatException e) {
                    // Ignorar
                }
                mostrarFormulario(request, response, recordatorioExistente);
                return;
            }

            Recordatorio recordatorio = construirRecordatorioDesdeRequest(request, usuario);
            recordatorio.setId(id);

            servicioRecordatorio.actualizarRecordatorio(recordatorio);

            MensajeUtil.agregarExito(session, "Recordatorio actualizado exitosamente");
            response.sendRedirect(request.getContextPath() + "/recordatorios");

        } catch (DateTimeParseException e) {
            MensajeUtil.agregarError(session, "Error: Formato de fecha inválido");
            request.setAttribute("recordatorio", request.getParameterMap());
            request.getRequestDispatcher("/recordatorio/VistaFormularioRecordatorio.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            MensajeUtil.agregarError(session, "Datos inválidos. Por favor, revisa los campos");
            request.setAttribute("recordatorio", request.getParameterMap());
            request.getRequestDispatcher("/recordatorio/VistaFormularioRecordatorio.jsp").forward(request, response);

        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al actualizar el recordatorio: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recordatorios");
        }
    }

    private void borrarRecordatorio(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException {

        HttpSession session = request.getSession();

        try {
            Long id = Long.parseLong(request.getParameter("id"));

            Recordatorio recordatorio = servicioRecordatorio.buscarPorId(id);

            if (recordatorio == null) {
                MensajeUtil.agregarError(session, "Recordatorio no encontrado");
                response.sendRedirect(request.getContextPath() + "/recordatorios");
                return;
            }

            if (!recordatorio.getUsuario().getId().equals(usuario.getId())) {
                MensajeUtil.agregarError(session, "No tienes autorización para eliminar este recordatorio");
                response.sendRedirect(request.getContextPath() + "/recordatorios");
                return;
            }

            servicioRecordatorio.eliminarRecordatorio(id);

            MensajeUtil.agregarExito(session, "Recordatorio eliminado exitosamente");
            response.sendRedirect(request.getContextPath() + "/recordatorios");

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: ID de recordatorio inválido");
            response.sendRedirect(request.getContextPath() + "/recordatorios");

        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al eliminar el recordatorio: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/recordatorios");
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