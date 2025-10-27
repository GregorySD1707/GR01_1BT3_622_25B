<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<%-- 1. Incluimos el header --%>
<jsp:include page="/comun/VistaHeader.jsp">
    <jsp:param name="pageTitle" value="Deudas y Préstamos"/>
</jsp:include>

<%-- 2. Contenido específico --%>
<div class="page-header">
    <h1>Deudas y Préstamos</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/obligacion_financiera/nuevo">
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19"></line>
            <line x1="5" y1="12" x2="19" y2="12"></line>
        </svg>
        <span>Nueva Obligación</span>
    </a>
</div>

<%-- Filtros --%>
<div class="filters-container">
    <form method="get" action="deudas" class="filters-form">
        <input type="hidden" name="accion" value="listar">

        <div class="filter-group">
            <label for="nombrePersona">Persona</label>
            <input
                    type="text"
                    id="nombrePersona"
                    name="nombrePersona"
                    placeholder="Filtrar por persona"
                    value="${filtroNombre != null ? filtroNombre : ''}"
            />
        </div>

        <div class="filter-group">
            <label for="fechaInicio">Fecha inicio</label>
            <input
                    type="date"
                    id="fechaInicio"
                    name="fechaInicio"
                    value="${filtroFechaInicio != null ? filtroFechaInicio : ''}"
            />
        </div>

        <div class="filter-group">
            <label for="fechaFin">Fecha fin</label>
            <input
                    type="date"
                    id="fechaFin"
                    name="fechaFin"
                    value="${filtroFechaFin != null ? filtroFechaFin : ''}"
            />
        </div>

        <div class="filter-actions">
            <button class="btn btn-primary" type="submit">Aplicar</button>
            <a class="btn btn-secondary" href="deudas?accion=listar">Limpiar</a>
        </div>
    </form>
</div>

<jsp:include page="/comun/Mensajes.jsp" />

<%-- Estado vacío --%>
<c:if test="${empty deudas}">
    <div class="empty-state">
        <div class="empty-state-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <path d="M16 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8"></path>
                <path d="M12 18V6"></path>
            </svg>
        </div>
        <h2>No hay obligaciones financieras</h2>
        <p>No tienes deudas ni préstamos registrados. ¡Crea tu primera obligación financiera!</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/obligacion_financiera/nuevo">Registrar obligación</a>
    </div>
</c:if>

<%-- Grid de obligaciones --%>
<c:if test="${not empty deudas}">
    <section class="grid">
        <c:forEach var="deuda" items="${deudas}">
            <article class="card">
                <div class="card-header">
                    <div class="card-icon ${deuda.getClass().simpleName == 'Deuda' ? 'icon-deuda' : 'icon-prestamo'}">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <circle cx="12" cy="12" r="10"></circle>
                            <path d="M16 8h-6a2 2 0 1 0 0 4h4a2 2 0 1 1 0 4H8"></path>
                            <path d="M12 18V6"></path>
                        </svg>
                    </div>
                    <h3 class="card-title"><c:out value="${deuda.nombrePersona}"/></h3>
                </div>

                <div class="card-body">
                    <p><strong>Tipo:</strong> ${deuda.getClass().simpleName}</p>
                    <p><strong>Estado:</strong> ${deuda.estado}</p>

                    <div class="amounts-section">
                        <p><strong>Monto Total:</strong> <fmt:formatNumber value="${deuda.montoTotal}" type="currency" currencySymbol="$" /></p>
                        <p><strong>Monto Pagado:</strong> <fmt:formatNumber value="${deuda.montoPagado}" type="currency" currencySymbol="$" /></p>
                        <p class="amount"><strong>Saldo Pendiente:</strong> <span class="balance"><fmt:formatNumber value="${deuda.calcularSaldoPendiente()}" type="currency" currencySymbol="$" /></span></p>
                        <p><strong>Fecha de Pago:</strong> ${deuda.fechaPago}</p>
                    </div>

                        <%-- Progreso del pago --%>
                    <c:set var="porcentajePagado" value="${(deuda.montoPagado / deuda.montoTotal) * 100}" />
                    <div class="progress-container">
                        <div class="progress-bar">
                            <div class="progress-fill" style="width: ${porcentajePagado}%"></div>
                        </div>
                        <span class="progress-text">
                            <fmt:formatNumber value="${porcentajePagado}" pattern="#0" />% pagado
                        </span>
                    </div>
                </div>

                <div class="card-footer">
                    <form method="post" action="deudas" class="abono-form">
                        <input type="hidden" name="accion" value="abonar">
                        <input type="hidden" name="idDeuda" value="${deuda.id}">

                        <div class="abono-inputs">
                            <select name="idCartera" required class="select-cuenta">
                                <option value="">Seleccionar cuenta</option>
                                <c:forEach var="cuenta" items="${cuentas}">
                                    <option value="${cuenta.id}">
                                            ${cuenta.nombre} - <fmt:formatNumber value="${cuenta.monto}" type="currency" currencySymbol="$" />
                                    </option>
                                </c:forEach>
                            </select>

                            <input
                                    type="number"
                                    name="monto"
                                    min="0.01"
                                    max="${deuda.calcularSaldoPendiente()}"
                                    step="0.01"
                                    placeholder="Monto a abonar"
                                    required
                                    class="input-monto"
                            />
                        </div>

                        <button class="btn btn-primary btn-full" type="submit">
                            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <polyline points="20 6 9 17 4 12"></polyline>
                            </svg>
                            Abonar
                        </button>
                    </form>
                </div>
            </article>
        </c:forEach>
    </section>
</c:if>

<style>
    /* Filtros */
    .filters-container {
        background: var(--surface-color);
        padding: 1.5rem;
        border-radius: 12px;
        margin-bottom: 2rem;
        box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }

    .filters-form {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        gap: 1rem;
        align-items: end;
    }

    .filter-group {
        display: flex;
        flex-direction: column;
        gap: 0.5rem;
    }

    .filter-group label {
        font-size: 0.875rem;
        font-weight: 500;
        color: var(--text-secondary);
    }

    .filter-group input {
        padding: 0.625rem;
        border: 1px solid var(--border-color);
        border-radius: 8px;
        font-size: 0.875rem;
    }

    .filter-actions {
        display: flex;
        gap: 0.5rem;
    }

    /* Iconos de tipo */
    .icon-deuda {
        background-color: #fee;
        color: #c33;
    }

    .icon-prestamo {
        background-color: #efe;
        color: #3c3;
    }

    /* Sección de montos */
    .amounts-section {
        margin: 1rem 0;
    }

    .amounts-section p {
        margin: 0.5rem 0;
        font-size: 0.9375rem;
    }

    /* Progress bar */
    .progress-container {
        margin-top: 1.25rem;
        padding-top: 1rem;
        border-top: 1px solid var(--border-color);
    }

    .progress-bar {
        height: 10px;
        background-color: #e9ecef;
        border-radius: 5px;
        overflow: hidden;
        margin-bottom: 0.5rem;
    }

    .progress-fill {
        height: 100%;
        background: linear-gradient(90deg, #4CAF50, #45a049);
        transition: width 0.3s ease;
        border-radius: 5px;
    }

    .progress-text {
        font-size: 0.875rem;
        color: var(--text-secondary);
        font-weight: 500;
    }

    /* Abono form */
    .abono-form {
        display: flex;
        flex-direction: column;
        gap: 0.75rem;
    }

    .abono-inputs {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 0.5rem;
    }

    .select-cuenta,
    .input-monto {
        padding: 0.625rem;
        border: 1px solid var(--border-color);
        border-radius: 8px;
        font-size: 0.875rem;
    }

    .btn-full {
        width: 100%;
        justify-content: center;
    }

    @media (max-width: 768px) {
        .abono-inputs {
            grid-template-columns: 1fr;
        }

        .filter-actions {
            grid-column: 1 / -1;
        }
    }
</style>

<%-- 3. Incluimos el footer --%>
<jsp:include page="/comun/VistaFooter.jsp" />