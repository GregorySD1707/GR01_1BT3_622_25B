package com.sistema_financiero_personal.movimiento.controladores;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso;
import com.sistema_financiero_personal.movimiento.servicios.ServicioCartera;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

        // Cargar y limpiar mensajes para mostrarlos una sola vez (patrón de recordatorios)
        MensajeUtil.obtenerYLimpiarMensajes(request);

        double ingresosTotales = servicioMovimiento.obtenerIngresosTotales();
        double gastosTotales = servicioMovimiento.obtenerGastosTotales();

        double saldoActual = servicioCartera.obtenerSaldo(1L);

        request.setAttribute("ingresosTotales", ingresosTotales);
        request.setAttribute("gastosTotales", gastosTotales);
        request.setAttribute("saldoActual", saldoActual);

        request.getRequestDispatcher("/movimiento/VistaMovimientos.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        String tipo = request.getParameter("tipo"); // INGRESO | GASTO
        String montoStr = request.getParameter("monto");
        String descripcion = request.getParameter("descripcion");
        String categoriaStr = request.getParameter("categoria");
        String carteraIdStr = request.getParameter("carteraId");

        // Validación de campos obligatorios (mensaje unificado)
        if (isBlank(tipo) || isBlank(montoStr) || isBlank(descripcion) || isBlank(categoriaStr) || isBlank(carteraIdStr)) {
            MensajeUtil.agregarError(session, "Todos los campos deben ser llenados");
            response.sendRedirect(request.getContextPath() + "/movimientos");
            return;
        }

        try {
            double monto = Double.parseDouble(montoStr);
            Long carteraId = Long.parseLong(carteraIdStr);

            // Validación de monto positivo
            if (monto <= 0) {
                MensajeUtil.agregarError(session, "Monto inválido. Debe ser mayor a cero");
                response.sendRedirect(request.getContextPath() + "/movimientos");
                return;
            }

            if ("INGRESO".equalsIgnoreCase(tipo)) {
                CategoriaIngreso categoriaIngreso = CategoriaIngreso.valueOf(categoriaStr.toUpperCase());
                servicioMovimiento.registrarIngreso(carteraId, monto, descripcion, categoriaIngreso);
            } else if ("GASTO".equalsIgnoreCase(tipo)) {
                CategoriaGasto categoriaGasto = CategoriaGasto.valueOf(categoriaStr.toUpperCase());
                servicioMovimiento.registrarGasto(carteraId, monto, descripcion, categoriaGasto);
            } else {
                MensajeUtil.agregarError(session, "Tipo de movimiento no válido");
                response.sendRedirect(request.getContextPath() + "/movimientos");
                return;
            }

            // Éxito
            MensajeUtil.agregarExito(session, "Movimiento registrado exitosamente");
            response.sendRedirect(request.getContextPath() + "/movimientos");

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: El monto o ID de cartera no es un número válido");
            response.sendRedirect(request.getContextPath() + "/movimientos");
        } catch (IllegalArgumentException e) {
            // Por ejemplo, categoría inválida
            MensajeUtil.agregarError(session, "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/movimientos");
        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al registrar el movimiento: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/movimientos");
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}