<%--formulario--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<%-- 1. Determinar si estamos en modo de edición o creación --%>
<c:set var="modoEdicion" value="${not empty cuenta and not empty cuenta.id}" />

<%-- 2. Incluimos el header --%>
<jsp:include page="/comun/VistaHeader.jsp">
    <jsp:param name="pageTitle" value="${modoEdicion ? 'Editar Cuenta' : 'Nueva Cuenta'}"/>
</jsp:include>

<%-- 3. Contenido del Formulario --%>
<div class="form-container">

    <div class="page-header">
        <h1>${modoEdicion ? 'Editar Cuenta' : 'Nueva Cuenta'}</h1>
        <a href="${pageContext.request.contextPath}/cuentas" class="btn btn-secondary">Cancelar</a>
    </div>

    <jsp:include page="/comun/Mensajes.jsp" />

    <form method="POST" action="${pageContext.request.contextPath}/cuentas">

        <%-- Campos ocultos para el modo de edición (comentado por ahora) --%>
        <%--
        <c:if test="${modoEdicion}">
            <input type="hidden" name="id" value="${cuenta.id}">
            <input type="hidden" name="_method" value="PUT">
        </c:if>
        --%>

        <div class="form-grid">
            <%-- Campo: Nombre de la Cuenta --%>
            <div class="form-group">
                <label for="nombre">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="12" y1="18" x2="12" y2="12"></line>
                        <line x1="9" y1="15" x2="15" y2="15"></line>
                    </svg>
                    Nombre de la Cuenta *
                </label>
                <input
                        type="text"
                        id="nombre"
                        name="nombre"
                        value="${not empty cuenta ? cuenta.nombre : param.nombre}"
                        placeholder="Ej: Cuenta de Ahorros Principal"
                        required
                        maxlength="100"
                />
            </div>

            <%-- Campo: Tipo de Cuenta --%>
            <div class="form-group">
                <label for="tipo">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                        <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
                    </svg>
                    Tipo de Cuenta *
                </label>
                <select id="tipo" name="tipo" required>
                    <option value="">Seleccione un tipo</option>
                    <c:forEach var="tipoCuenta" items="${tipos}">
                        <option value="${tipoCuenta.name()}"
                                <c:if test="${tipoCuenta.name() == (not empty cuenta ? cuenta.tipo.name() : param.tipo)}">selected</c:if>>
                                ${tipoCuenta}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <%-- Campo: Saldo Inicial --%>
            <div class="form-group">
                <label for="monto">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="12" y1="1" x2="12" y2="23"></line>
                        <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                    </svg>
                    Saldo Inicial *
                </label>
                <input
                        type="number"
                        id="monto"
                        name="monto"
                        value="${not empty cuenta ? cuenta.monto : param.monto}"
                        placeholder="0.00"
                        step="0.01"
                        min="0.00"
                        onblur="if(this.value) this.value = parseFloat(this.value).toFixed(2)"
                        required
                />
                <small class="form-hint">El saldo inicial debe ser mayor a cero</small>
            </div>
        </div>

        <%-- Botones de Acción --%>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                </svg>
                <span>${modoEdicion ? 'Actualizar' : 'Crear'} Cuenta</span>
            </button>
        </div>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('form');
        const montoInput = document.getElementById('monto');

        // Validación del monto antes de enviar
        form.addEventListener('submit', function(e) {
            const monto = parseFloat(montoInput.value);

            if (isNaN(monto) || monto <= 0) {
                e.preventDefault();
                alert('El saldo inicial debe ser un número mayor a cero');
                montoInput.focus();
                return false;
            }
        });

        // Formatear el input de monto mientras escribe
        montoInput.addEventListener('blur', function() {
            const value = parseFloat(this.value);
            if (!isNaN(value)) {
                this.value = value.toFixed(2);
            }
        });
    });
</script>

<%-- 4. Finalmente, incluimos el footer --%>
<jsp:include page="/comun/VistaFooter.jsp" />