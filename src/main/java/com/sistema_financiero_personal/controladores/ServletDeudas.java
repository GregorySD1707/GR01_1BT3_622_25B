package com.sistema_financiero_personal.controladores;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.servicios.ServicioDeudas;

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
        // Soporte por defecto: listar cuando no se especifica acción o cuando es "listar"
        if (accion == null || "listar".equals(accion)) {
            List<DeudaPrestamo> deudas = servicioDeudas.obtenerDeudasPendientes();

            // Filtros opcionales por query params: nombrePersona, fechaInicio, fechaFin
            String nombreFiltro = req.getParameter("nombrePersona");
            String fechaInicioStr = req.getParameter("fechaInicio");
            String fechaFinStr = req.getParameter("fechaFin");

            if (nombreFiltro != null && !nombreFiltro.trim().isEmpty()) {
                String lower = nombreFiltro.trim().toLowerCase();
                deudas = deudas.stream()
                        .filter(d -> d.getNombrePersona() != null && d.getNombrePersona().toLowerCase().contains(lower))
                        .collect(Collectors.toList());
            }

            if (fechaInicioStr != null && !fechaInicioStr.trim().isEmpty()) {
                try {
                    LocalDate inicio = LocalDate.parse(fechaInicioStr);
                    deudas = deudas.stream()
                            .filter(d -> d.getFechaPago() != null && !d.getFechaPago().isBefore(inicio))
                            .collect(Collectors.toList());
                } catch (Exception ignored) {
                    // Si el formato no es válido, ignoramos el filtro
                }
            }

            if (fechaFinStr != null && !fechaFinStr.trim().isEmpty()) {
                try {
                    LocalDate fin = LocalDate.parse(fechaFinStr);
                    deudas = deudas.stream()
                            .filter(d -> d.getFechaPago() != null && !d.getFechaPago().isAfter(fin))
                            .collect(Collectors.toList());
                } catch (Exception ignored) {
                    // Ignorar si no es válido
                }
            }

            // Lista de personas (para autocompletar/select en la vista)
            List<String> personas = deudas.stream()
                    .map(DeudaPrestamo::getNombrePersona)
                    .filter(p -> p != null && !p.trim().isEmpty())
                    .distinct()
                    .collect(Collectors.toList());

            req.setAttribute("deudas", deudas);
            req.setAttribute("personas", personas);
            req.setAttribute("filtroNombre", nombreFiltro);
            req.setAttribute("filtroFechaInicio", fechaInicioStr);
            req.setAttribute("filtroFechaFin", fechaFinStr);
            req.getRequestDispatcher("/VistaDeudas.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("registrar".equals(accion)) {
            String nombrePersona = req.getParameter("nombrePersona");
            double montoTotal = Double.parseDouble(req.getParameter("montoTotal"));
            LocalDate fechaPago = LocalDate.parse(req.getParameter("fechaPago"));
            // Respetar el tipo enviado desde la vista: DEUDA o PRESTAMO
            String tipo = req.getParameter("tipo");
            if (tipo != null && tipo.equalsIgnoreCase("PRESTAMO")) {
                servicioDeudas.registrarPrestamo(nombrePersona, montoTotal, fechaPago);
            } else {
                servicioDeudas.registrarDeuda(nombrePersona, montoTotal, fechaPago);
            }
            resp.sendRedirect("deudas?accion=listar");
        } else if ("abonar".equals(accion)) {
            Long idDeuda = Long.parseLong(req.getParameter("idDeuda"));
            double monto = Double.parseDouble(req.getParameter("monto"));
            servicioDeudas.abonarADeuda(idDeuda, monto);
            resp.sendRedirect("deudas?accion=listar");
        }
    }
}
