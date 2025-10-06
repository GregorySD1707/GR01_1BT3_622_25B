package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.servicios.ServicioMovimientos;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/inicio")
public class ServletInicio extends HttpServlet {
    private final ServicioMovimientos servicio;

    public ServletInicio() {
        this.servicio = new ServicioMovimientos();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        double saldoActual = servicio.obtenerSaldoActualTotal();
        double ingresosTotales = servicio.obtenerIngresosTotales();
        double gastosTotales = servicio.obtenerGastosTotales();
        request.setAttribute("saldoActual", saldoActual);
        request.setAttribute("ingresosTotales", ingresosTotales);
        request.setAttribute("gastosTotales", gastosTotales);
        request.getRequestDispatcher("/VistaInicio.jsp").forward(request, response);
    }
}

