package com.sistema_financiero_personal.deuda_prestamo.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.sistema_financiero_personal.deuda_prestamo.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.deuda_prestamo.servicios.ServicioDeudas;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletDeudas", urlPatterns = {"/deudas"})
public class ServletDeudas extends HttpServlet {

    private final ServicioDeudas servicioDeudas = new ServicioDeudas();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");

        if (accion == null || "listar".equals(accion)) {
            List<DeudaPrestamo> deudas = servicioDeudas.obtenerDeudasPendientes();

            // Parámetros de filtro
            String nombreFiltro = req.getParameter("nombrePersona");
            String fechaInicioStr = req.getParameter("fechaInicio");
            String fechaFinStr = req.getParameter("fechaFin");
            // Aplicar filtros
            deudas = filtrarPorNombre(deudas, nombreFiltro);
            deudas = filtrarPorFechaInicio(deudas, fechaInicioStr);
            deudas = filtrarPorFechaFin(deudas, fechaFinStr);

            // Lista de personas
            List<String> personas = deudas.stream()
                    .map(DeudaPrestamo::getNombrePersona)
                    .filter(p -> p != null && !p.trim().isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            // Pasar datos a la vista
            req.setAttribute("deudas", deudas);
            req.setAttribute("personas", personas);
            req.setAttribute("filtroNombre", nombreFiltro);
            req.setAttribute("filtroFechaInicio", fechaInicioStr);
            req.setAttribute("filtroFechaFin", fechaFinStr);
            req.getRequestDispatcher("/deuda_prestamo/VistaDeudas.jsp").forward(req, resp);
        }
    }

    private List<DeudaPrestamo> filtrarPorNombre(List<DeudaPrestamo> deudas, String nombreFiltro) {
        if (nombreFiltro == null || nombreFiltro.trim().isEmpty()) {
            return deudas;
        }
        String lower = nombreFiltro.trim().toLowerCase();
        return deudas.stream()
                .filter(d -> d.getNombrePersona() != null && d.getNombrePersona().toLowerCase().contains(lower))
                .collect(Collectors.toList());
    }
    private List<DeudaPrestamo> filtrarPorFechaInicio(List<DeudaPrestamo> deudas, String fechaInicioStr) {
        if (fechaInicioStr == null || fechaInicioStr.trim().isEmpty()) {
            return deudas;
        }
        try {
            LocalDate inicio = LocalDate.parse(fechaInicioStr);
            return deudas.stream()
                    .filter(d -> d.getFechaPago() != null && !d.getFechaPago().isBefore(inicio))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return deudas;
        }
    }
    private List<DeudaPrestamo> filtrarPorFechaFin(List<DeudaPrestamo> deudas, String fechaFinStr) {
        if (fechaFinStr == null || fechaFinStr.trim().isEmpty()) {
            return deudas;
        }
        try {
            LocalDate fin = LocalDate.parse(fechaFinStr);
            return deudas.stream()
                    .filter(d -> d.getFechaPago() != null && !d.getFechaPago().isAfter(fin))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return deudas;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");

        if ("registrar".equals(accion)) {
            String nombrePersona = req.getParameter("nombrePersona");
            double montoTotal = Double.parseDouble(req.getParameter("montoTotal"));
            LocalDate fechaPago = LocalDate.parse(req.getParameter("fechaPago"));
            String tipo = req.getParameter("tipo");

            if ("PRESTAMO".equalsIgnoreCase(tipo)) {
                servicioDeudas.registrarPrestamo(nombrePersona, montoTotal, fechaPago);
            } else {
                servicioDeudas.registrarDeuda(nombrePersona, montoTotal, fechaPago);
            }

            resp.sendRedirect("deudas?accion=listar");

        } else if ("abonar".equals(accion)) {
            Long idDeuda = Long.parseLong(req.getParameter("idDeuda"));
            double monto = Double.parseDouble(req.getParameter("monto"));
            Long idCartera = Long.parseLong(req.getParameter("idCartera"));
            servicioDeudas.abonarADeuda(idCartera, idDeuda, monto);
            resp.sendRedirect("deudas?accion=listar");
        }
    }
}