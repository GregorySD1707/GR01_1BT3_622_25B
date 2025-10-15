package com.sistema_financiero_personal.movimiento.controladores;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/movimientos")
public class ServletMovimientos extends HttpServlet {

    private final ServicioMovimiento servicioMovimiento;
    private final ServicioCartera servicioCartera;

    public ServletMovimientos() {
        this.servicioMovimiento = new ServicioMovimiento();
        this.servicioCartera = new ServicioCartera();
    }

    // Constructor para inyección en tests
    public ServletMovimientos(ServicioMovimiento servicioMovimiento, ServicioCartera servicioCartera) {
        this.servicioMovimiento = servicioMovimiento;
        this.servicioCartera = servicioCartera;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        double ingresosTotales = servicioMovimiento.obtenerIngresosTotales();
        double gastosTotales = servicioMovimiento.obtenerGastosTotales();

        double saldoActual = servicioCartera.obtenerSaldo(1L); // Cambia esto según tu lógica

        request.setAttribute("ingresosTotales", ingresosTotales);
        request.setAttribute("gastosTotales", gastosTotales);
        request.setAttribute("saldoActual", saldoActual);

        request.getRequestDispatcher("/movimiento/VistaMovimientos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String tipo = request.getParameter("tipo"); // INGRESO | GASTO
        String montoStr = request.getParameter("monto");
        String descripcion = request.getParameter("descripcion");
        String categoriaStr = request.getParameter("categoria");
        String carteraIdStr = request.getParameter("carteraId");

        try {
            // Validar parámetros
            if (montoStr == null || montoStr.trim().isEmpty()) {
                throw new IllegalArgumentException("El monto es obligatorio");
            }
            if (carteraIdStr == null || carteraIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("El ID de cartera es obligatorio");
            }
            if (tipo == null || tipo.trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo de movimiento es obligatorio");
            }
            if (categoriaStr == null || categoriaStr.trim().isEmpty()) {
                throw new IllegalArgumentException("La categoría es obligatoria");
            }

            double monto = Double.parseDouble(montoStr);
            Long carteraId = Long.parseLong(carteraIdStr);

            if ("INGRESO".equalsIgnoreCase(tipo)) {
                CategoriaIngreso categoriaIngreso = CategoriaIngreso.valueOf(categoriaStr.toUpperCase());
                servicioMovimiento.registrarIngreso(carteraId, monto, descripcion, categoriaIngreso);
                request.setAttribute("mensajeExito", "¡Ingreso registrado exitosamente!");

            } else if ("GASTO".equalsIgnoreCase(tipo)) {
                CategoriaGasto categoriaGasto = CategoriaGasto.valueOf(categoriaStr.toUpperCase());
                servicioMovimiento.registrarGasto(carteraId, monto, descripcion, categoriaGasto);
                request.setAttribute("mensajeExito", "¡Gasto registrado exitosamente!");

            } else {
                throw new IllegalArgumentException("Tipo de movimiento no válido: " + tipo);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("mensajeError", "Error: El monto o ID de cartera no es un número válido");
        } catch (IllegalArgumentException e) {
            request.setAttribute("mensajeError", "Error: " + e.getMessage());
        } catch (Exception e) {
            request.setAttribute("mensajeError", "Error al registrar el movimiento: " + e.getMessage());
        }

        // Recargar la página con los datos actualizados
        doGet(request, response);
    }
}