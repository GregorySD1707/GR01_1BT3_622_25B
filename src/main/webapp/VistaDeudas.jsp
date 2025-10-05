<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="comun/VistaHeader.jsp" %>
<html>
<head>
    <title>Gestión de Deudas y Préstamos</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
<div class="container">
    <h2>Deudas y Préstamos Pendientes</h2>
    <table class="tabla">
        <thead>
        <tr>
            <th>ID</th>
            <th>Persona</th>
            <th>Monto Total</th>
            <th>Monto Pagado</th>
            <th>Saldo Pendiente</th>
            <th>Fecha de Pago</th>
            <th>Tipo</th>
            <th>Estado</th>
            <th>Abonar</th>
        </tr>
        </thead>
        <tbody>
        <% 
            java.util.List<com.sistema_financiero_personal.modelos.DeudaPrestamo> deudas = (java.util.List<com.sistema_financiero_personal.modelos.DeudaPrestamo>) request.getAttribute("deudas");
            if (deudas != null) {
                for (com.sistema_financiero_personal.modelos.DeudaPrestamo deuda : deudas) {
        %>
        <tr>
            <td><%= deuda.getId() %></td>
            <td><%= deuda.getNombrePersona() %></td>
            <td><%= deuda.getMontoTotal() %></td>
            <td><%= deuda.getMontoPagado() %></td>
            <td><%= deuda.calcularSaldoPendiente() %></td>
            <td><%= deuda.getFechaPago() %></td>
            <td><%= deuda.getTipo() %></td>
            <td><%= deuda.getEstado() %></td>
            <td>
                <form method="post" action="deudas">
                    <input type="hidden" name="accion" value="abonar">
                    <input type="hidden" name="idDeuda" value="<%= deuda.getId() %>">
                    <input type="number" name="monto" min="1" max="<%= deuda.calcularSaldoPendiente() %>" step="0.01" required>
                    <button type="submit">Abonar</button>
                </form>
            </td>
        </tr>
        <% 
                }
            } else {
        %>
        <tr><td colspan="9">No hay deudas ni préstamos pendientes.</td></tr>
        <% } %>
        </tbody>
    </table>

    <h3>Registrar nueva deuda/préstamo</h3>
    <form method="post" action="deudas" class="formulario">
        <input type="hidden" name="accion" value="registrar">
        <label>Persona:</label>
        <input type="text" name="nombrePersona" required>
        <label>Monto total:</label>
        <input type="number" name="montoTotal" min="1" step="0.01" required>
        <label>Fecha de pago:</label>
        <input type="date" name="fechaPago" required>
        <label>Tipo:</label>
        <select name="tipo" required>
            <option value="DEUDA">Deuda</option>
            <option value="PRESTAMO">Préstamo</option>
        </select>
        <button type="submit">Registrar</button>
    </form>
</div>
<%@ include file="comun/VistaFooter.jsp" %>
</body>
</html>
