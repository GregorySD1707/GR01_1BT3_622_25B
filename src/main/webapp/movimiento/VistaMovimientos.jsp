<%-- Vista de Movimientos --%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ page import="com.sistema_financiero_personal.movimiento.modelos.CategoriaIngreso" %>
<%@ page import="com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<jsp:include page="/comun/VistaHeader.jsp">
    <jsp:param name="pageTitle" value="Gestor de Movimientos" />
</jsp:include>

<div class="page-header">
    <h1>Gestor de Movimientos</h1>
</div>

<jsp:include page="/comun/Mensajes.jsp" />

<div class="form-container">
    <form class="movimientos-form" method="post" action="${pageContext.request.contextPath}/movimientos">

        <div class="form-grid">
            <%-- Campo: Cuenta --%>
            <div class="form-group">
                <label for="cuentaId">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                        <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
                    </svg>
                    Cuenta *
                </label>
                <select id="cuentaId" name="cuentaId" required>
                    <option value="">Seleccione una cuenta</option>
                    <c:forEach var="cuenta" items="${cuentas}">
                        <option value="${cuenta.id}"
                                data-saldo="${cuenta.monto}"
                                <c:if test="${cuenta.id == param.cuentaId}">selected</c:if>>
                                ${cuenta.nombre} (${cuenta.tipo}) - Saldo: $<fmt:formatNumber value="${cuenta.monto}" pattern="#,##0.00"/>
                        </option>
                    </c:forEach>
                </select>
                <c:if test="${empty cuentas}">
                    <small class="form-hint" style="color: #e74c3c;">
                        No hay cuentas disponibles.
                        <a href="${pageContext.request.contextPath}/cuentas/nuevo">Crear una cuenta</a>
                    </small>
                </c:if>
            </div>

            <%-- Campo: Tipo de Movimiento --%>
            <div class="form-group">
                <label for="tipo">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="23 6 13.5 15.5 8.5 10.5 1 18"></polyline>
                        <polyline points="17 6 23 6 23 12"></polyline>
                    </svg>
                    Tipo de Movimiento *
                </label>
                <select id="tipo" name="tipo" required>
                    <option value="INGRESO" <c:if test="${param.tipo == 'INGRESO'}">selected</c:if>>Ingreso</option>
                    <option value="GASTO" <c:if test="${param.tipo == 'GASTO'}">selected</c:if>>Gasto</option>
                </select>
            </div>

            <%-- Campo: Monto --%>
            <div class="form-group">
                <label for="monto">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="12" y1="1" x2="12" y2="23"></line>
                        <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                    </svg>
                    Monto *
                </label>
                <input
                        type="number"
                        id="monto"
                        name="monto"
                        value="${param.monto}"
                        placeholder="0.00"
                        step="0.01"
                        min="0.01"
                        onblur="if(this.value) this.value = parseFloat(this.value).toFixed(2)"
                        required
                />
                <small class="form-hint">El monto debe ser mayor a cero</small>
                <small id="saldoInsuficiente" class="form-hint" style="display: none; color: #e74c3c;">
                    ⚠️ El monto excede el saldo disponible
                </small>
            </div>

            <%-- Campo: Descripción --%>
            <div class="form-group">
                <label for="descripcion">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                        <polyline points="14 2 14 8 20 8"></polyline>
                        <line x1="16" y1="13" x2="8" y2="13"></line>
                        <line x1="16" y1="17" x2="8" y2="17"></line>
                        <polyline points="10 9 9 9 8 9"></polyline>
                    </svg>
                    Descripción *
                </label>
                <input
                        type="text"
                        id="descripcion"
                        name="descripcion"
                        value="${param.descripcion}"
                        placeholder="Ej: Pago de servicios básicos"
                        required
                        maxlength="200"
                />
            </div>

            <%-- Campo: Categoría (Dinámico) --%>
            <div class="form-group">
                <label for="categoria">
                    <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="3" y="3" width="7" height="7"></rect>
                        <rect x="14" y="3" width="7" height="7"></rect>
                        <rect x="14" y="14" width="7" height="7"></rect>
                        <rect x="3" y="14" width="7" height="7"></rect>
                    </svg>
                    Categoría *
                </label>

                <%-- Dropdown para Ingresos --%>
                <select id="categoriaIngreso" name="categoria" required>
                    <option value="">Seleccione una categoría</option>
                    <c:forEach var="categoria" items="<%= CategoriaIngreso.values() %>">
                        <option value="${categoria.name()}"
                                <c:if test="${categoria.name() == param.categoria}">selected</c:if>>
                                ${categoria.name()}
                        </option>
                    </c:forEach>
                </select>

                <%-- Dropdown para Gastos (oculto por defecto) --%>
                <select id="categoriaGasto" style="display: none;" disabled required>
                    <option value="">Seleccione una categoría</option>
                    <c:forEach var="categoria" items="<%= CategoriaGasto.values() %>">
                        <option value="${categoria.name()}"
                                <c:if test="${categoria.name() == param.categoria}">selected</c:if>>
                                ${categoria.name()}
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <%-- Botones de Acción --%>
        <div class="form-actions" style="display: flex; gap: 12px;">
            <button type="submit" class="btn btn-primary" id="btnRegistrar">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="20 6 9 17 4 12"></polyline>
                </svg>
                <span>Registrar</span>
            </button>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</div>

<script>
    document.addEventListener('DOMContentLoaded', function() {
        const form = document.querySelector('form.movimientos-form');
        const tipoSelect = document.getElementById('tipo');
        const categoriaIngresoSelect = document.getElementById('categoriaIngreso');
        const categoriaGastoSelect = document.getElementById('categoriaGasto');
        const montoInput = document.getElementById('monto');
        const cuentaSelect = document.getElementById('cuentaId');
        const saldoInsuficienteHint = document.getElementById('saldoInsuficiente');
        const btnRegistrar = document.getElementById('btnRegistrar');

        // Toggle entre categorías de ingreso y gasto
        function toggleCategorias() {
            if (tipoSelect.value === 'INGRESO') {
                categoriaIngresoSelect.style.display = 'block';
                categoriaIngresoSelect.disabled = false;
                categoriaIngresoSelect.setAttribute('name', 'categoria');

                categoriaGastoSelect.style.display = 'none';
                categoriaGastoSelect.disabled = true;
                categoriaGastoSelect.removeAttribute('name');
            } else {
                categoriaIngresoSelect.style.display = 'none';
                categoriaIngresoSelect.disabled = true;
                categoriaIngresoSelect.removeAttribute('name');

                categoriaGastoSelect.style.display = 'block';
                categoriaGastoSelect.disabled = false;
                categoriaGastoSelect.setAttribute('name', 'categoria');
            }
        }

        // Validar saldo suficiente para gastos
        function validarSaldo() {
            if (tipoSelect.value === 'GASTO' && cuentaSelect.value && montoInput.value) {
                const selectedOption = cuentaSelect.options[cuentaSelect.selectedIndex];
                const saldoDisponible = parseFloat(selectedOption.dataset.saldo || 0);
                const montoGasto = parseFloat(montoInput.value);

                if (!isNaN(montoGasto) && montoGasto > saldoDisponible) {
                    saldoInsuficienteHint.style.display = 'block';
                    btnRegistrar.disabled = true;
                    btnRegistrar.style.opacity = '0.5';
                    return false;
                } else {
                    saldoInsuficienteHint.style.display = 'none';
                    btnRegistrar.disabled = false;
                    btnRegistrar.style.opacity = '1';
                    return true;
                }
            } else {
                saldoInsuficienteHint.style.display = 'none';
                btnRegistrar.disabled = false;
                btnRegistrar.style.opacity = '1';
                return true;
            }
        }

        // Validación de monto personalizada
        montoInput.addEventListener('invalid', function() {
            if (montoInput.validity.valueMissing) {
                montoInput.setCustomValidity('');
            } else if (montoInput.validity.rangeUnderflow) {
                montoInput.setCustomValidity('Monto inválido. Debe ser mayor a cero');
            } else {
                montoInput.setCustomValidity('');
            }
        });

        montoInput.addEventListener('input', function() {
            montoInput.setCustomValidity('');
            validarSaldo();
        });

        // Formatear monto al perder el foco
        montoInput.addEventListener('blur', function() {
            const value = parseFloat(this.value);
            if (!isNaN(value)) {
                this.value = value.toFixed(2);
            }
        });

        // Validación antes de enviar el formulario
        form.addEventListener('submit', function(e) {
            toggleCategorias();

            // Validar monto mínimo
            const min = parseFloat(montoInput.min || '0.01');
            const val = parseFloat(montoInput.value);
            if (montoInput.value && (isNaN(val) || val < min)) {
                e.preventDefault();
                montoInput.setCustomValidity('Monto inválido. Debe ser mayor a cero');
                montoInput.reportValidity();
                return false;
            } else {
                montoInput.setCustomValidity('');
            }

            // Validar saldo suficiente
            if (!validarSaldo()) {
                e.preventDefault();
                alert('El monto del gasto excede el saldo disponible en la cuenta seleccionada');
                return false;
            }
        });

        // Event listeners
        toggleCategorias();
        tipoSelect.addEventListener('change', function() {
            toggleCategorias();
            validarSaldo();
        });
        cuentaSelect.addEventListener('change', validarSaldo);
    });
</script>

<jsp:include page="/comun/VistaFooter.jsp" />