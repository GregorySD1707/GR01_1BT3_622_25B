package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.modelos.DeudaPrestamo;
import com.sistema_financiero_personal.servicios.ServicioDeudas;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(name = "ServletDeudas", urlPatterns = {"/deudas"})
public class ServletDeudas extends HttpServlet {
    private final ServicioDeudas servicioDeudas = new ServicioDeudas();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String accion = req.getParameter("accion");
        if ("listar".equals(accion)) {
            List<DeudaPrestamo> deudas = servicioDeudas.obtenerDeudasPendientes();
            req.setAttribute("deudas", deudas);
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
            servicioDeudas.registrarDeuda(nombrePersona, montoTotal, fechaPago);
            resp.sendRedirect("deudas?accion=listar");
        } else if ("abonar".equals(accion)) {
            Long idDeuda = Long.parseLong(req.getParameter("idDeuda"));
            double monto = Double.parseDouble(req.getParameter("monto"));
            servicioDeudas.abonarADeuda(idDeuda, monto);
            resp.sendRedirect("deudas?accion=listar");
        }
    }
}
