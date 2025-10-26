<%@ page import="com.sistema_financiero_personal.obligaciones_financieras.modelos.ObligacionFinanciera" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ include file="../comun/VistaHeader.jsp" %>

<html>
<head>
    <title>Gestión de Deudas y Préstamos</title>
    <link rel="stylesheet" href="../resources/css/style.css">
</head>
<body>
<div class="container page-content">
    <div class="page-header">
        <h2>Deudas y Préstamos Pendientes</h2>
        <div>
            <form method="get" action="deudas" style="display:flex;gap:8px;align-items:center;">
                <input type="hidden" name="accion" value="listar">
                <input class="search" type="text" name="nombrePersona" placeholder="Filtrar por persona" value="${filtroNombre != null ? filtroNombre : ''}">
                <input class="search" type="date" name="fechaInicio" value="${filtroFechaInicio != null ? filtroFechaInicio : ''}">
                <input class="search" type="date" name="fechaFin" value="${filtroFechaFin != null ? filtroFechaFin : ''}">
                <button class="btn btn-primary" type="submit">Aplicar</button>
                <a class="btn btn-secondary" href="deudas?accion=listar">Limpiar</a>
            </form>
        </div>
    </div>

    <jsp:include page="/comun/Mensajes.jsp" />

    <%-- Mensajes de éxito o error --%>
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
                ${sessionScope.success}
            <c:remove var="success" scope="session"/>
        </div>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-error">
                ${sessionScope.error}
            <c:remove var="error" scope="session"/>
        </div>
    </c:if>

    <div class="grid">
        <c:choose>
            <c:when test="${not empty deudas}">
                <c:forEach var="deuda" items="${deudas}">
                    <div class="card">
                        <div class="card-header">
                            <div class="card-title">ID: <strong>${deuda.id}</strong></div>
                            <div style="margin-left:auto;color:var(--text-secondary);">
                                Tipo: ${deuda.getClass().simpleName}
                            </div>
                        </div>
                        <div class="card-body">
                            <p><strong>Persona:</strong> ${deuda.nombrePersona}</p>
                            <p><strong>Monto total:</strong> S/ ${String.format("%.2f", deuda.montoTotal)}</p>
                            <p><strong>Monto pagado:</strong> S/ ${String.format("%.2f", deuda.montoPagado)}</p>
                            <p><strong>Saldo pendiente:</strong> S/ ${String.format("%.2f", deuda.calcularSaldoPendiente())}</p>
                            <p><strong>Fecha de pago:</strong> ${deuda.fechaPago}</p>
                            <p><strong>Estado:</strong> ${deuda.estado}</p>
                        </div>
                        <div class="card-footer">
                            <form method="post" action="deudas" style="display:flex;gap:8px;align-items:center;flex:1;">
                                <input type="hidden" name="accion" value="abonar">
                                <input type="hidden" name="idDeuda" value="${deuda.id}">

                                    <%-- Select para elegir la cuenta desde donde abonar --%>
                                <select name="idCartera" required style="flex:1;padding:8px;border:1px solid #ddd;border-radius:4px;">
                                    <option value="">Seleccionar cuenta</option>
                                    <c:forEach var="cuenta" items="${cuentas}">
                                        <option value="${cuenta.id}">
                                                ${cuenta.nombre} - S/ ${String.format("%.2f", cuenta.monto)}
                                        </option>
                                    </c:forEach>
                                </select>

                                <input style="flex:1;padding:8px;border:1px solid #ddd;border-radius:4px;"
                                       type="number"
                                       name="monto"
                                       min="0.01"
                                       max="${deuda.calcularSaldoPendiente()}"
                                       step="0.01"
                                       placeholder="Monto a abonar"
                                       required>
                                <button class="btn btn-primary" type="submit">Abonar</button>
                            </form>
                            <form method="get" action="deudas" style="margin-left:auto;">
                                <input type="hidden" name="accion" value="listar">
                                <input type="hidden" name="nombrePersona" value="${deuda.nombrePersona}">
                                <button class="btn btn-outline" type="submit">Ver por persona</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h2>No hay deudas ni préstamos pendientes</h2>
                    <p>Registra una nueva deuda o préstamo usando el formulario. Usa los filtros para ver por persona o por rango de fechas.</p>
                </div>
            </c:otherwise>
        </c:choose>
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

<style>
    .alert {
        padding: 12px 16px;
        border-radius: 6px;
        margin-bottom: 16px;
    }

    .alert-success {
        background-color: #d4edda;
        color: #155724;
        border: 1px solid #c3e6cb;
    }

    .alert-error {
        background-color: #f8d7da;
        color: #721c24;
        border: 1px solid #f5c6cb;
    }
</style>

<%@ include file="../comun/VistaFooter.jsp" %>
</body>
</html>