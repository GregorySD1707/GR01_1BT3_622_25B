<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<%-- 1. Incluimos el header que abre <html>, <body> y <main> --%>
<jsp:include page="/comun/VistaHeader.jsp">
    <jsp:param name="pageTitle" value="Mis Cuentas"/>
</jsp:include>

<%-- 2. Contenido específico de ESTA página --%>
<div class="page-header">
    <h1>Gestión de Cuentas</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/cuentas/nuevo">
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <line x1="12" y1="5" x2="12" y2="19"></line>
            <line x1="5" y1="12" x2="19" y2="12"></line>
        </svg>
        <span>Nueva Cuenta</span>
    </a>
</div>

<jsp:include page="/comun/Mensajes.jsp" />

<%-- Estado vacío: cuando no hay cuentas --%>
<c:if test="${empty cuentas}">
    <div class="empty-state">
        <div class="empty-state-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
            </svg>
        </div>
        <h2>No hay cuentas</h2>
        <p>¡Parece que no tienes ninguna cuenta registrada todavía. Anímate y crea la primera!</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/cuentas/nuevo">Crear mi primera cuenta</a>
    </div>
</c:if>

<%-- Grid de cuentas: se mostrará cuando haya cuentas --%>
<c:if test="${not empty cuentas}">
    <section id="accounts-grid" class="grid">
        <c:forEach var="cuenta" items="${cuentas}">
            <article class="card card-clickable"
                     data-nombre="<c:out value='${cuenta.nombre}'/>"
                     onclick="window.location.href='${pageContext.request.contextPath}/cuentas/detalle?id=${cuenta.id}'"
                     style="cursor: pointer;">
                <div class="card-header">
                    <div class="card-icon">
                        <c:choose>
                            <c:when test="${cuenta.tipo == 'EFECTIVO'}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <line x1="12" y1="1" x2="12" y2="23"></line>
                                    <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                                </svg>
                            </c:when>
                            <c:when test="${cuenta.tipo == 'CORRIENTE'}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <rect x="2" y="5" width="20" height="14" rx="2"></rect>
                                    <line x1="2" y1="10" x2="22" y2="10"></line>
                                </svg>
                            </c:when>
                            <c:when test="${cuenta.tipo == 'AHORROS'}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M19 5c-1.5 0-2.8 1.4-3 2-3.5-1.5-11-.3-11 5 0 1.8 0 3 2 4.5V20h4v-2h3v2h4v-4c1-.5 1.7-1 2-2h2v-4h-2c0-1-.5-1.5-1-2h0V5z"></path>
                                    <path d="M2 9v1c0 1.1.9 2 2 2h1"></path>
                                    <path d="M16 11h0"></path>
                                </svg>
                            </c:when>
                            <c:otherwise>
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                                    <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
                                </svg>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <h3 class="card-title"><c:out value="${cuenta.nombre}"/></h3>
                </div>
                <div class="card-body">
                    <p><strong>Tipo:</strong> ${cuenta.tipo}</p>
                    <p class="amount"><strong>Saldo:</strong> <span class="balance"><fmt:formatNumber value="${cuenta.monto}" type="currency" currencySymbol="$" /></span></p>
                </div>
                    <%--
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/cuentas/editar?id=${cuenta.id}" class="btn btn-secondary">Editar</a>
                        <form class="delete-form"
                              action="${pageContext.request.contextPath}/cuentas/borrar?id=${cuenta.id}"
                              method="POST"
                              style="display: inline;">
                            <button type="button" class="btn btn-danger delete-btn">Eliminar</button>
                        </form>
                    </div>
                    --%>
            </article>
        </c:forEach>
    </section>
</c:if>

<style>
    .card-clickable {
        transition: all 0.3s ease;
    }

    .card-clickable:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 16px rgba(0,0,0,0.12);
    }
</style>

<%-- 3. Finalmente, incluimos el footer que cierra <main>, <body> y <html> --%>
<jsp:include page="/comun/VistaFooter.jsp" />