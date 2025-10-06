<%@ include file="comun/VistaHeader.jsp" %>

<html>
<head>
    <title>Gestión de Deudas y Préstamos</title>
    <link rel="stylesheet" href="resources/css/style.css">
</head>
<body>
<div class="container page-content">
    <div class="page-header">
        <h2>Deudas y Prestamos Pendientes</h2>
        <div>
            <form method="get" action="deudas" style="display:flex;gap:8px;align-items:center;">
                <input type="hidden" name="accion" value="listar">
                <input class="search" type="text" name="nombrePersona" placeholder="Filtrar por persona" value="<%= request.getAttribute("filtroNombre") != null ? request.getAttribute("filtroNombre") : "" %>">
                <input class="search" type="date" name="fechaInicio" value="<%= request.getAttribute("filtroFechaInicio") != null ? request.getAttribute("filtroFechaInicio") : "" %>">
                <input class="search" type="date" name="fechaFin" value="<%= request.getAttribute("filtroFechaFin") != null ? request.getAttribute("filtroFechaFin") : "" %>">
                <button class="btn btn-primary" type="submit">Aplicar</button>
                <a class="btn btn-secondary" href="deudas?accion=listar">Limpiar</a>
            </form>
        </div>
    </div>

    <div class="grid">
        <% java.util.List<com.sistema_financiero_personal.modelos.DeudaPrestamo> deudas = (java.util.List<com.sistema_financiero_personal.modelos.DeudaPrestamo>) request.getAttribute("deudas");
            if (deudas != null && !deudas.isEmpty()) {
                for (com.sistema_financiero_personal.modelos.DeudaPrestamo deuda : deudas) {
        %>
        <div class="card">
            <div class="card-header">
                <div class="card-title">ID: <strong><%= deuda.getId() %></strong></div>
                <div style="margin-left:auto;color:var(--text-secondary);">Tipo: <%= deuda.getTipo() %></div>
            </div>
            <div class="card-body">
                <p><strong>Persona:</strong> <%= deuda.getNombrePersona() %></p>
                <p><strong>Monto total:</strong> S/ <%= String.format("%.2f", deuda.getMontoTotal()) %></p>
                <p><strong>Monto pagado:</strong> S/ <%= String.format("%.2f", deuda.getMontoPagado()) %></p>
                <p><strong>Saldo pendiente:</strong> S/ <%= String.format("%.2f", deuda.calcularSaldoPendiente()) %></p>
                <p><strong>Fecha de pago:</strong> <%= deuda.getFechaPago() %></p>
                <p><strong>Estado:</strong> <%= deuda.getEstado() %></p>
            </div>
            <div class="card-footer">
                <form method="post" action="deudas" style="display:flex;gap:8px;align-items:center;flex:1;">
                    <input type="hidden" name="accion" value="abonar">
                    <input type="hidden" name="idDeuda" value="<%= deuda.getId() %>">
                    <input class="form-group" type="number" name="monto" min="0.01" max="<%= deuda.calcularSaldoPendiente() %>" step="0.01" placeholder="Monto" required>
                    <button class="btn btn-primary" type="submit">Abonar</button>
                </form>
                <form method="get" action="deudas" style="margin-left:auto;">
                    <input type="hidden" name="accion" value="listar">
                    <input type="hidden" name="nombrePersona" value="<%= deuda.getNombrePersona() %>">
                    <button class="btn btn-outline" type="submit">Ver por persona</button>
                </form>
            </div>
        </div>
        <%     }
        } else { %>
        <div class="empty-state">
            <div class="empty-state-icon">📭</div>
            <h2>No hay deudas ni préstamos pendientes</h2>
            <p>Registra una nueva deuda o prestamo usando el formulario. Usa los filtros para ver por persona o por rango de fechas.</p>
        </div>
        <% } %>
    </div>

    <div class="form-container">
        <h3>Registrar nueva deuda/préstamo</h3>
        <form method="post" action="deudas">
            <input type="hidden" name="accion" value="registrar">
            <div class="form-grid">
                <div class="form-group">
                    <label>Persona:</label>
                    <input type="text" name="nombrePersona" required>
                </div>
                <div class="form-group">
                    <label>Monto total (S/):</label>
                    <input type="number" name="montoTotal" min="0.01" step="0.01" required>
                </div>
                <div class="form-group">
                    <label>Fecha de pago:</label>
                    <input type="date" name="fechaPago" required>
                </div>
                <div class="form-group">
                    <label>Tipo:</label>
                    <select name="tipo">
                        <option value="DEUDA">Deuda</option>
                        <option value="PRESTAMO">Préstamo</option>
                    </select>
                </div>
            </div>
            <div class="form-actions">
                <button class="btn btn-primary" type="submit">Registrar</button>
            </div>
        </form>
    </div>

</div>
<%@ include file="comun/VistaFooter.jsp" %>
</body>
</html>
