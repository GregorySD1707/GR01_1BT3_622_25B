package com.sistema_financiero_personal.obligaciones_financieras.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.sistema_financiero_personal.obligaciones_financieras.daos.DAOObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.Deuda;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.ObligacionFinanciera;
import com.sistema_financiero_personal.obligaciones_financieras.modelos.Prestamo;
import com.sistema_financiero_personal.obligaciones_financieras.servicios.ServicioObligacionFinanciera;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet para gestionar obligaciones financieras (Deudas y Préstamos)
 * Maneja el listado, creación y abonos a obligaciones financieras
 */
@WebServlet(name = "ServletObligacionFinanciera", urlPatterns = {
        "/obligacion_financiera/deudas",
        "/obligacion_financiera/nuevo"
})
public class ServletObligacionFinanciera extends HttpServlet {

    private ServicioObligacionFinanciera servicioDeudas;
    private DAOObligacionFinanciera daoObligacionFinanciera;
    private ServicioCuenta servicioCuenta;

    @Override
    public void init() throws ServletException {
        super.init();
        servicioDeudas = new ServicioObligacionFinanciera();
        daoObligacionFinanciera = new DAOObligacionFinanciera();
        servicioCuenta = new ServicioCuenta();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();

        // Validar sesión
        Usuario usuario = obtenerUsuarioSesion(req);
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/ingreso");
            return;
        }

        if (path.endsWith("/nuevo")) {
            mostrarFormulario(req, resp);
        } else {
            String accion = req.getParameter("accion");
            if (accion == null || "listar".equals(accion)) {
                listarDeudas(req, resp);
            }
        }
    }

    /**
     * Muestra el formulario de nueva obligación financiera
     */
    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MensajeUtil.obtenerYLimpiarMensajes(req);
        req.getRequestDispatcher("/obligacion_financiera/VistaObligacionFinancieraFormulario.jsp")
                .forward(req, resp);
    }

    /**
     * Lista todas las obligaciones financieras con filtros
     */
    private void listarDeudas(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Usuario usuario = obtenerUsuarioSesion(req);
        Long carteraId = usuario.getCartera().getId();

        // Obtener y limpiar mensajes
        MensajeUtil.obtenerYLimpiarMensajes(req);

        // Parámetros de filtro
        String nombreFiltro = req.getParameter("nombrePersona");
        LocalDate fechaInicioStr = parseFecha(req.getParameter("fechaInicio"));
        LocalDate fechaFinStr = parseFecha(req.getParameter("fechaFin"));

        // Aplicar filtros
        // TODO: PATRON CRITERIA
        List<ObligacionFinanciera> deudas = daoObligacionFinanciera.buscarConFiltros(
                nombreFiltro, fechaInicioStr, fechaFinStr
        );

        // Lista de personas
        List<String> personas = deudas.stream()
                .map(ObligacionFinanciera::getNombrePersona)
                .filter(p -> p != null && !p.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());

        // Cargar cuentas disponibles para abonar del usuario logueado
        List<Cuenta> cuentas = servicioCuenta.listarCuentasPorCartera(carteraId);

        // Pasar datos a la vista
        prepararVistaObligacionFinanciera(req, deudas, personas, cuentas,
                nombreFiltro, fechaInicioStr, fechaFinStr);

        req.getRequestDispatcher("/obligacion_financiera/VistaObligacionFinanciera.jsp")
                .forward(req, resp);
    }

    /**
     * Prepara los atributos para la vista
     */
    private static void prepararVistaObligacionFinanciera(
            HttpServletRequest req,
            List<ObligacionFinanciera> deudas,
            List<String> personas,
            List<Cuenta> cuentas,
            String nombreFiltro,
            LocalDate fechaInicioStr,
            LocalDate fechaFinStr) {
        req.setAttribute("deudas", deudas);
        req.setAttribute("personas", personas);
        req.setAttribute("cuentas", cuentas);
        req.setAttribute("filtroNombre", nombreFiltro);
        req.setAttribute("filtroFechaInicio", fechaInicioStr);
        req.setAttribute("filtroFechaFin", fechaFinStr);
    }

    /**
     * Parsea una fecha desde String a LocalDate
     */
    private LocalDate parseFecha(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(fecha);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Validar sesión en POST también
        Usuario usuario = obtenerUsuarioSesion(req);
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/ingreso");
            return;
        }

        String accion = req.getParameter("accion");

        if ("registrar".equals(accion)) {
            registrarObligacionFinanciera(req, resp);
        } else if ("abonar".equals(accion)) {
            abonarObligacionFinanciera(req, resp);
        }
    }

    /**
     * Registra una nueva obligación financiera (Deuda o Préstamo)
     */
    private void registrarObligacionFinanciera(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();

        try {
            String nombrePersona = req.getParameter("nombrePersona");
            double montoTotal = Double.parseDouble(req.getParameter("montoTotal"));
            LocalDate fechaPago = LocalDate.parse(req.getParameter("fechaPago"));
            String tipo = req.getParameter("tipo");

            // Validaciones
            if (nombrePersona == null || nombrePersona.trim().isEmpty()) {
                MensajeUtil.agregarError(session, "El nombre de la persona es requerido");
                resp.sendRedirect("nuevo");
                return;
            }

            if (montoTotal <= 0) {
                MensajeUtil.agregarError(session, "El monto debe ser mayor a cero");
                resp.sendRedirect("nuevo");
                return;
            }

            if (fechaPago.isBefore(LocalDate.now())) {
                MensajeUtil.agregarAdvertencia(session,
                        "La fecha de pago es anterior a hoy. ¿Estás seguro?");
            }

            // TODO: PATRON FACTORY
            ObligacionFinanciera obligacionFinanciera = "PRESTAMO".equalsIgnoreCase(tipo)
                    ? new Prestamo(nombrePersona, montoTotal, fechaPago)
                    : new Deuda(nombrePersona, montoTotal, fechaPago);

            daoObligacionFinanciera.crear(obligacionFinanciera);

            String tipoTexto = "PRESTAMO".equalsIgnoreCase(tipo) ? "Préstamo" : "Deuda";
            MensajeUtil.agregarExito(session,
                    tipoTexto + " registrado exitosamente para " + nombrePersona);

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: El monto debe ser un número válido");
            resp.sendRedirect("nuevo");
            return;
        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al registrar: " + e.getMessage());
            resp.sendRedirect("nuevo");
            return;
        }

        resp.sendRedirect("deudas?accion=listar");
    }

    /**
     * Realiza un abono a una obligación financiera existente
     */
    private void abonarObligacionFinanciera(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        HttpSession session = req.getSession();

        try {
            Long idDeuda = Long.parseLong(req.getParameter("idDeuda"));
            double monto = Double.parseDouble(req.getParameter("monto"));
            Long idCartera = Long.parseLong(req.getParameter("idCartera")); // en realidad es idCuenta

            // Validación básica
            if (monto <= 0) {
                MensajeUtil.agregarError(session, "El monto debe ser mayor a cero");
                resp.sendRedirect("deudas?accion=listar");
                return;
            }

            // Obtener la obligación para validar
            ObligacionFinanciera obligacion = daoObligacionFinanciera.buscarPorId(idDeuda);
            if (obligacion == null) {
                MensajeUtil.agregarError(session, "Obligación financiera no encontrada");
                resp.sendRedirect("deudas?accion=listar");
                return;
            }

            double saldoPendiente = obligacion.calcularSaldoPendiente();
            if (monto > saldoPendiente) {
                MensajeUtil.agregarError(session,
                        "El monto a abonar ($" + String.format("%.2f", monto) +
                                ") es mayor al saldo pendiente ($" + String.format("%.2f", saldoPendiente) + ")");
                resp.sendRedirect("deudas?accion=listar");
                return;
            }

            servicioDeudas.abonarADeuda(idCartera, idDeuda, monto);

            String mensaje = String.format("Abono de $%.2f realizado exitosamente", monto);
            if (Math.abs(saldoPendiente - monto) < 0.01) {
                mensaje += ". ¡Obligación completamente pagada!";
            }
            MensajeUtil.agregarExito(session, mensaje);

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: Los datos ingresados no son válidos");
        } catch (IllegalArgumentException e) {
            MensajeUtil.agregarError(session, e.getMessage());
        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al procesar el abono: " + e.getMessage());
        }

        resp.sendRedirect("deudas?accion=listar");
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