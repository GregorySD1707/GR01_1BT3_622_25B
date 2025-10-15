package com.sistema_financiero_personal.comun;

import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/inicio")
public class ServletInicio extends HttpServlet {
    private final ServicioMovimiento servicio;

    public ServletInicio() {
        this.servicio = new ServicioMovimiento();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        double ingresosTotales = servicio.obtenerIngresosTotales();
        double gastosTotales = servicio.obtenerGastosTotales();
        request.setAttribute("ingresosTotales", ingresosTotales);
        request.setAttribute("gastosTotales", gastosTotales);
        request.getRequestDispatcher("comun/VistaInicio.jsp").forward(request, response);
    }
}

