<%-- RUTA: /recordatorios.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<%-- 1. Incluimos el header que abre <html>, <body> y <main> --%>
<jsp:include page="/comun/VistaHeader.jsp">
    <jsp:param name="pageTitle" value="Mis Recordatorios"/>
</jsp:include>

<%-- 2. Aquí va el contenido específico de ESTA página --%>
<div class="page-header">
    <h1>Gestión de Recordatorios</h1>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/recordatorios/nuevo">
        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"></line><line x1="5" y1="12" x2="19" y2="12"></line></svg>
        <span>Nuevo Recordatorio</span>
    </a>
</div>

<section class="controls">
    <div class="search-wrapper">
        <input id="search-input" class="search" type="text" placeholder="Buscar por descripción..." />
        <svg class="search-icon" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"></circle><line x1="21" y1="21" x2="16.65" y2="16.65"></line></svg>
    </div>
</section>

<c:if test="${empty recordatorios}">
    <div class="empty-state">
        <div class="empty-state-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"><path d="M8 2v4"></path><path d="M16 2v4"></path><path d="M21 13V6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h8"></path><path d="M3 10h18"></path><path d="M16 19h6"></path><path d="M19 16v6"></path></svg>
        </div>
        <h2>No hay recordatorios</h2>
        <p>¡Parece que no tienes ningún recordatorio registrado todavía. Anímate y crea el primero!</p>
        <a class="btn btn-primary" href="${pageContext.request.contextPath}/recordatorios/nuevo">Crear mi primer recordatorio</a>
    </div>
</c:if>

<section id="reminders-grid" class="grid">
    <c:forEach var="r" items="${recordatorios}">
        <article class="card" data-description="<c:out value='${r.descripcion}'/>">
            <div class="card-header">
                <div class="card-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"></rect><line x1="16" y1="2" x2="16" y2="6"></line><line x1="8" y1="2" x2="8" y2="6"></line><line x1="3" y1="10" x2="21" y2="10"></line></svg>
                </div>
                <h3 class="card-title">${r.descripcion}</h3>
            </div>
            <div class="card-body">
                <p><strong>Monto:</strong> <fmt:formatNumber value="${r.monto}" type="currency" currencySymbol="$" /></p>
                <p><strong>Recurrencia:</strong> ${r.recurrencia}</p>
                <p><strong>Período:</strong>
                    <fmt:parseDate value="${r.fechaInicio}" pattern="yyyy-MM-dd" var="parsedFechaInicio" />
                    <fmt:parseDate value="${r.fechaFin}" pattern="yyyy-MM-dd" var="parsedFechaFin" />
                    <fmt:formatDate value="${parsedFechaInicio}" pattern="dd/MM/yyyy" /> -
                    <fmt:formatDate value="${parsedFechaFin}" pattern="dd/MM/yyyy" />
                </p>
                <p><strong>Anticipación:</strong> ${r.diasDeAnticipacion} día(s)</p>
            </div>
            <div class="card-footer">
                <a href="${pageContext.request.contextPath}/recordatorios/editar?id=${r.id}" class="btn btn-secondary">Editar</a>
                <form action="${pageContext.request.contextPath}/recordatorios/eliminar?id=${r.id}" method="POST" style="display: inline;" onsubmit="return confirm('¿Estás seguro de que deseas eliminar este recordatorio?');">
                    <button type="submit" class="btn btn-danger">Eliminar</button>
                </form>
            </div>
        </article>
    </c:forEach>
</section>

<%-- 3. Finalmente, incluimos el footer que cierra <main>, <body> y <html> --%>
<jsp:include page="/comun/VistaFooter.jsp" />