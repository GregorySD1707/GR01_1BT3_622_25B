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

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletObligacionFinanciera", urlPatterns = {"/obligacion_financiera/deudas"})
public class ServletObligacionFinanciera extends HttpServlet {

    private ServicioObligacionFinanciera servicioDeudas;
    private DAOObligacionFinanciera daoObligacionFinanciera;

    @Override
    public void init() throws ServletException {
        super.init();
        servicioDeudas = new ServicioObligacionFinanciera();
        daoObligacionFinanciera = new DAOObligacionFinanciera();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");

        if (accion == null || "listar".equals(accion)) {
            listarDeudas(req, resp);
        }
    }

    private void listarDeudas(HttpServletRequest req, HttpServletResponse resp) throws  ServletException, IOException{

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

        // Pasar datos a la vista
        prepararVistaObligacionFinanciera(req, deudas, personas, nombreFiltro, fechaInicioStr, fechaFinStr);

        req.getRequestDispatcher("/obligacion_financiera/VistaObligacionFinanciera.jsp").forward(req, resp);
    }

    private static void prepararVistaObligacionFinanciera(HttpServletRequest req, List<ObligacionFinanciera> deudas, List<String> personas, String nombreFiltro, LocalDate fechaInicioStr, LocalDate fechaFinStr) {
        req.setAttribute("deudas", deudas);
        req.setAttribute("personas", personas);
        req.setAttribute("filtroNombre", nombreFiltro);
        req.setAttribute("filtroFechaInicio", fechaInicioStr);
        req.setAttribute("filtroFechaFin", fechaFinStr);
    }

    private LocalDate parseFecha(String fecha) {
        if(fecha == null || fecha.trim().isEmpty()){
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
        String accion = req.getParameter("accion");

        if ("registrar".equals(accion)) {
            registrarObligacionFinanciera(req, resp);
        } else if ("abonar".equals(accion)) {
            abonarObligacionFinanciera(req, resp);
        }
    }

    private void abonarObligacionFinanciera(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long idDeuda = Long.parseLong(req.getParameter("idDeuda"));
        double monto = Double.parseDouble(req.getParameter("monto"));

        servicioDeudas.abonarADeuda(idDeuda, monto);

        resp.sendRedirect("deudas?accion=listar");
    }

    private void registrarObligacionFinanciera(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String nombrePersona = req.getParameter("nombrePersona");
        double montoTotal = Double.parseDouble(req.getParameter("montoTotal"));
        LocalDate fechaPago = LocalDate.parse(req.getParameter("fechaPago"));
        String tipo = req.getParameter("tipo");

        ObligacionFinanciera obligacionFinanciera = "PRESTAMO".equalsIgnoreCase(tipo) // TODO: PATRON FACTORY
                ? new Prestamo(nombrePersona, montoTotal, fechaPago)
                : new Deuda(nombrePersona, montoTotal, fechaPago);

        daoObligacionFinanciera.crear(obligacionFinanciera);

        resp.sendRedirect("deudas?accion=listar");
    }
}