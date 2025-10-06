package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.modelos.Movimiento;
import com.sistema_financiero_personal.servicios.ServicioMovimientos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/movimientos")
public class ServletMovimientos extends HttpServlet {

    private final ServicioMovimientos servicio;

    public ServletMovimientos() {
        this.servicio = new ServicioMovimientos();
    }

    // Constructor para inyección en tests
    public ServletMovimientos(ServicioMovimientos servicio) {
        this.servicio = servicio;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Totales globales (según los movimientos y carteras registradas)
        double ingresosTotales = servicio.obtenerIngresosTotales();
        double gastosTotales = servicio.obtenerGastosTotales();
        double saldoActual = servicio.obtenerSaldoActualTotal();

        request.setAttribute("ingresosTotales", ingresosTotales);
        request.setAttribute("gastosTotales", gastosTotales);
        request.setAttribute("saldoActual", saldoActual);

        request.getRequestDispatcher("/VistaMovimientos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tipo = request.getParameter("tipo"); // INGRESO | GASTO
        String montoStr = request.getParameter("monto");
        String descripcion = request.getParameter("descripcion");
        String categoria = request.getParameter("categoria");
        String nombreCartera = request.getParameter("cartera");

        try {
            double monto = Double.parseDouble(montoStr);
            if ("INGRESO".equals(tipo)) {
                servicio.registrarIngreso(monto, descripcion, categoria, nombreCartera);
            } else {
                servicio.registrarGasto(monto, descripcion, categoria, nombreCartera);
            }
            request.setAttribute("mensajeExito", "¡Registrado exitosamente!");
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al registrar el movimiento: " + e.getMessage());
        }
        doGet(request, response);
    }

    // Métodos de fachada hacia el servicio
    public Movimiento registrarIngreso(double monto, String descripcion, String categoria, String nombreCartera) {
        return servicio.registrarIngreso(monto, descripcion, categoria, nombreCartera);
    }

    public Movimiento registrarGasto(double monto, String descripcion, String categoria, String nombreCartera) {
        return servicio.registrarGasto(monto, descripcion, categoria, nombreCartera);
    }
}
