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

@WebServlet(name = "ServletObligacionFinanciera", urlPatterns = {"/obligacion_financiera/deudas"})
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
        String accion = req.getParameter("accion");

        if (accion == null || "listar".equals(accion)) {
            listarDeudas(req, resp);
        }
    }

    private void listarDeudas(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener usuario de sesión
        Usuario usuario = obtenerUsuarioSesion(req);
        if (usuario == null) {
            resp.sendRedirect(req.getContextPath() + "/ingreso");
            return;
        }

        Long carteraId = usuario.getCartera().getId();

        // Obtener y limpiar mensajes
        MensajeUtil.obtenerYLimpiarMensajes(req);

        // Parámetros de filtro
        String nombreFiltro = req.getParameter("nombrePersona");
        LocalDate fechaInicioStr = parseFecha(req.getParameter("fechaInicio"));
        LocalDate fechaFinStr = parseFecha(req.getParameter("fechaFin"));

        // Aplicar filtros
        // TODO: PATRON CRITERIA
        List<ObligacionFinanciera> deudas = daoObligacionFinanciera.buscarConFiltros(nombreFiltro, fechaInicioStr, fechaFinStr);

        // Lista de personas
        List<String> personas = deudas.stream()
                .map(ObligacionFinanciera::getNombrePersona)
                .filter(p -> p != null && !p.trim().isEmpty())
                .distinct()
                .collect(Collectors.toList());

        // Cargar cuentas disponibles para abonar del usuario logueado
        List<Cuenta> cuentas = servicioCuenta.listarCuentasPorCartera(carteraId);

        // Pasar datos a la vista
        prepararVistaObligacionFinanciera(req, deudas, personas, cuentas, nombreFiltro, fechaInicioStr, fechaFinStr);

        req.getRequestDispatcher("/obligacion_financiera/VistaObligacionFinanciera.jsp").forward(req, resp);
    }

    private static void prepararVistaObligacionFinanciera(HttpServletRequest req, List<ObligacionFinanciera> deudas,
                                                          List<String> personas, List<Cuenta> cuentas,
                                                          String nombreFiltro, LocalDate fechaInicioStr, LocalDate fechaFinStr) {
        req.setAttribute("deudas", deudas);
        req.setAttribute("personas", personas);
        req.setAttribute("cuentas", cuentas);
        req.setAttribute("filtroNombre", nombreFiltro);
        req.setAttribute("filtroFechaInicio", fechaInicioStr);
        req.setAttribute("filtroFechaFin", fechaFinStr);
    }

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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    private void abonarObligacionFinanciera(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

            servicioDeudas.abonarADeuda(idCartera, idDeuda, monto);
            MensajeUtil.agregarExito(session, "Abono realizado exitosamente");

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: Los datos ingresados no son válidos");
        } catch (IllegalArgumentException e) {
            MensajeUtil.agregarError(session, e.getMessage());
        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al procesar el abono: " + e.getMessage());
        }

        resp.sendRedirect("deudas?accion=listar");
    }

    private void registrarObligacionFinanciera(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();

        try {
            String nombrePersona = req.getParameter("nombrePersona");
            double montoTotal = Double.parseDouble(req.getParameter("montoTotal"));
            LocalDate fechaPago = LocalDate.parse(req.getParameter("fechaPago"));
            String tipo = req.getParameter("tipo");

            ObligacionFinanciera obligacionFinanciera = "PRESTAMO".equalsIgnoreCase(tipo) // TODO: PATRON FACTORY
                    ? new Prestamo(nombrePersona, montoTotal, fechaPago)
                    : new Deuda(nombrePersona, montoTotal, fechaPago);

            daoObligacionFinanciera.crear(obligacionFinanciera);
            MensajeUtil.agregarExito(session, "Obligación financiera registrada exitosamente");

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: El monto debe ser un número válido");
        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al registrar: " + e.getMessage());
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