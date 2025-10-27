package com.sistema_financiero_personal.cuentas.controladores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import com.sistema_financiero_personal.cuentas.servicios.ServicioCuenta;
import com.sistema_financiero_personal.movimiento.modelos.Movimiento;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import com.sistema_financiero_personal.usuario.modelos.Usuario;
import com.sistema_financiero_personal.comun.utilidades.mensajes.MensajeUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cuentas/*")
public class ServletCuenta extends HttpServlet {

    private ServicioCuenta servicioCuenta;

    @Override
    public void init() {
        this.servicioCuenta = new ServicioCuenta();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Verificar sesión
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ingreso");
            return;
        }

        String action = request.getPathInfo();
        if (action == null) {
            action = "/";
        }

        switch (action) {
            case "/nuevo":
                mostrarFormularioNuevo(request, response);
                break;
            // case "/editar":
            //     mostrarFormularioEditar(request, response, usuario);
            //     break;
            case "/detalle":
                mostrarDetalleCuenta(request, response, usuario);
                break;
            default:
                listarCuentas(request, response, usuario);
                break;
        }
    }

    private void mostrarDetalleCuenta(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        String cuentaIdStr = request.getParameter("id");

        if (cuentaIdStr == null || cuentaIdStr.trim().isEmpty()) {
            HttpSession session = request.getSession();
            MensajeUtil.agregarError(session, "ID de cuenta no válido");
            response.sendRedirect(request.getContextPath() + "/cuentas");
            return;
        }

        try {
            Long cuentaId = Long.parseLong(cuentaIdStr);

            // Buscar la cuenta
            Cuenta cuenta = servicioCuenta.buscarCuenta(cuentaId);

            if (cuenta == null) {
                HttpSession session = request.getSession();
                MensajeUtil.agregarError(session, "La cuenta no existe");
                response.sendRedirect(request.getContextPath() + "/cuentas");
                return;
            }

            // Verificar que la cuenta pertenece al usuario
            if (!cuenta.getCartera().getId().equals(usuario.getCartera().getId())) {
                HttpSession session = request.getSession();
                MensajeUtil.agregarError(session, "No tienes permiso para ver esta cuenta");
                response.sendRedirect(request.getContextPath() + "/cuentas");
                return;
            }

            // Necesitamos instanciar ServicioMovimiento
            ServicioMovimiento servicioMovimiento = new ServicioMovimiento();

            // Obtener los movimientos de la cuenta
            List<Movimiento> movimientos = servicioMovimiento.obtenerMovimientosPorCuenta(cuentaId);

            // Calcular estadísticas
            double totalIngresos = servicioMovimiento.sumarIngresosPorCuenta(cuentaId);
            double totalGastos = servicioMovimiento.sumarGastosPorCuenta(cuentaId);
            long cantidadMovimientos = servicioMovimiento.contarMovimientos(cuentaId);

            // Limpiar mensajes
            MensajeUtil.obtenerYLimpiarMensajes(request);

            // Pasar datos a la vista
            request.setAttribute("cuenta", cuenta);
            request.setAttribute("movimientos", movimientos);
            request.setAttribute("totalIngresos", totalIngresos);
            request.setAttribute("totalGastos", totalGastos);
            request.setAttribute("cantidadMovimientos", cantidadMovimientos);

            request.getRequestDispatcher("/cuenta/VistaDetalleCuenta.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            HttpSession session = request.getSession();
            MensajeUtil.agregarError(session, "ID de cuenta no válido");
            response.sendRedirect(request.getContextPath() + "/cuentas");
        } catch (Exception e) {
            HttpSession session = request.getSession();
            MensajeUtil.agregarError(session, "Error al cargar los detalles de la cuenta: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cuentas");
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Verificar sesión
        Usuario usuario = obtenerUsuarioSesion(request);
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ingreso");
            return;
        }

        String action = request.getPathInfo();
        if (action == null) {
            action = "/";
        }

        // String method = request.getParameter("_method");

        // if ("/borrar".equals(action)) {
        //     borrarCuenta(request, response, usuario);
        // } else if ("PUT".equalsIgnoreCase(method)) {
        //     actualizarCuenta(request, response, usuario);
        // } else {
        crearCuenta(request, response, usuario);
        // }
    }

    private void listarCuentas(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws ServletException, IOException {

        MensajeUtil.obtenerYLimpiarMensajes(request);

        // Obtener las cuentas del usuario desde su cartera
        List<Cuenta> listaCuentas = servicioCuenta.listarCuentasPorCartera(usuario.getCartera().getId());

        request.setAttribute("cuentas", listaCuentas);
        request.getRequestDispatcher("/cuenta/VistaCuentas.jsp").forward(request, response);
    }

    private void mostrarFormularioNuevo(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MensajeUtil.obtenerYLimpiarMensajes(request);

        request.setAttribute("cuenta", null);
        request.setAttribute("tipos", TipoCuenta.values());
        request.getRequestDispatcher("/cuenta/VistaFormularioCuenta.jsp").forward(request, response);
    }

    private void crearCuenta(HttpServletRequest request, HttpServletResponse response, Usuario usuario)
            throws IOException, ServletException {

        HttpSession session = request.getSession();

        try {
            // Obtener datos del formulario
            String nombre = request.getParameter("nombre");
            TipoCuenta tipo = TipoCuenta.valueOf(request.getParameter("tipo"));
            double monto = Double.parseDouble(request.getParameter("monto"));

            // Validar que no exista una cuenta con el mismo nombre y tipo
            if (servicioCuenta.existeCuentaDuplicada(nombre, tipo, usuario.getCartera().getId())) {
                MensajeUtil.agregarError(session, "Ya existe una cuenta del mismo nombre y tipo");

                // Crear objeto temporal con los datos ingresados para no perderlos
                Cuenta datosIngresados = new Cuenta();
                datosIngresados.setNombre(nombre);
                datosIngresados.setTipo(tipo);
                datosIngresados.setMonto(monto);

                request.setAttribute("cuenta", datosIngresados);
                request.setAttribute("tipos", TipoCuenta.values());
                request.getRequestDispatcher("/cuenta/VistaFormularioCuenta.jsp").forward(request, response);
                return;
            }

            // Validar monto
            if (!servicioCuenta.validarSaldoInicial(monto)) {
                MensajeUtil.agregarError(session, "Error: El saldo inicial debe ser mayor a cero");

                // Crear objeto temporal con los datos ingresados para no perderlos
                Cuenta datosIngresados = new Cuenta();
                datosIngresados.setNombre(nombre);
                datosIngresados.setTipo(tipo);
                datosIngresados.setMonto(monto);

                request.setAttribute("cuenta", datosIngresados);
                request.setAttribute("tipos", TipoCuenta.values());
                request.getRequestDispatcher("/cuenta/VistaFormularioCuenta.jsp").forward(request, response);
                return;
            }

            // Crear la cuenta usando la cartera del usuario de la sesión
            Cuenta nuevaCuenta = new Cuenta(nombre, tipo, monto, usuario.getCartera());
            servicioCuenta.crearCuenta(nuevaCuenta);

            MensajeUtil.agregarExito(session, "Cuenta creada exitosamente");
            response.sendRedirect(request.getContextPath() + "/cuentas");

        } catch (NumberFormatException e) {
            MensajeUtil.agregarError(session, "Error: El monto debe ser un número válido");

            Cuenta datosIngresados = new Cuenta();
            datosIngresados.setNombre(request.getParameter("nombre"));

            request.setAttribute("cuenta", datosIngresados);
            request.setAttribute("tipos", TipoCuenta.values());
            request.getRequestDispatcher("/cuenta/VistaFormularioCuenta.jsp").forward(request, response);

        } catch (IllegalArgumentException e) {
            MensajeUtil.agregarError(session, "Datos inválidos. Por favor, revisa los campos");

            Cuenta datosIngresados = new Cuenta();
            datosIngresados.setNombre(request.getParameter("nombre"));

            request.setAttribute("cuenta", datosIngresados);
            request.setAttribute("tipos", TipoCuenta.values());
            request.getRequestDispatcher("/cuenta/VistaFormularioCuenta.jsp").forward(request, response);

        } catch (Exception e) {
            MensajeUtil.agregarError(session, "Error al crear la cuenta: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/cuentas");
        }
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