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

<jsp:include page="/comun/Mensajes.jsp" />

<div class="page-header">
    <h1>Gestor de Movimientos</h1>
</div>

<form class="movimientos-form" method="post" action="${pageContext.request.contextPath}/movimientos" style="max-width: 520px; margin: 0 auto; display: grid; gap: 16px;">
    <label for="tipoSelect">Tipo:</label>
    <select name="tipo" id="tipoSelect" required>
        <option value="INGRESO">Ingreso</option>
        <option value="GASTO">Gasto</option>
    </select>

    <label for="monto">Monto:</label>
    <!-- agregar min para que el navegador considere inválidos valores < 0.01 -->
    <input type="number" id="monto" step="0.01" name="monto" min="0.01" required>

    <label for="descripcion">Descripción:</label>
    <input type="text" id="descripcion" name="descripcion" required>

    <label for="categoria">Categoría:</label>
    <%-- Dropdown para Ingresos (visible por defecto) --%>
    <select id="categoriaIngreso" name="categoria" required>
        <c:forEach var="categoria" items="<%= CategoriaIngreso.values() %>">
            <option value="${categoria.name()}">${categoria.name()}</option>
        </c:forEach>
    </select>

    <%-- Dropdown para Gastos (oculto por defecto). Importante: sin name al inicio --%>
    <select id="categoriaGasto" style="display: none;" disabled required>
        <c:forEach var="categoria" items="<%= CategoriaGasto.values() %>">
            <option value="${categoria.name()}">${categoria.name()}</option>
        </c:forEach>
    </select>

    <%-- Requerido por el servlet --%>
    <input type="hidden" name="carteraId" value="1" />

    <div>
        <button type="submit" class="btn btn-primary">Registrar</button>
    </div>
</form>

<script>
    document.addEventListener('DOMContentLoaded', function () {
        const form = document.querySelector('form.movimientos-form');
        const tipoSelect = document.getElementById('tipoSelect');
        const categoriaIngresoSelect = document.getElementById('categoriaIngreso');
        const categoriaGastoSelect = document.getElementById('categoriaGasto');
        const montoInput = document.getElementById('monto');

        function toggleCategorias() {
            if (tipoSelect.value === 'INGRESO') {
                categoriaIngresoSelect.style.display = 'block';
                categoriaIngresoSelect.disabled = false;
                categoriaIngresoSelect.setAttribute('name', 'categoria');

                categoriaGastoSelect.style.display = 'none';
                categoriaGastoSelect.disabled = true;
                categoriaGastoSelect.removeAttribute('name');
            } else { // GASTO
                categoriaIngresoSelect.style.display = 'none';
                categoriaIngresoSelect.disabled = true;
                categoriaIngresoSelect.removeAttribute('name');

                categoriaGastoSelect.style.display = 'block';
                categoriaGastoSelect.disabled = false;
                categoriaGastoSelect.setAttribute('name', 'categoria');
            }
        }

        // Mantener el mensaje nativo junto al input (igual que "Completa este campo")
        montoInput.addEventListener('invalid', function () {
            if (montoInput.validity.valueMissing) {
                // dejar que el navegador muestre su mensaje nativo
                montoInput.setCustomValidity('');
            } else if (montoInput.validity.rangeUnderflow) {
                // personalizar mensaje pero usando la UI nativa (globo junto al input)
                montoInput.setCustomValidity('Monto inválido. Debe ser mayor a cero');
            } else {
                montoInput.setCustomValidity('');
            }
        });
        montoInput.addEventListener('input', function () {
            // limpiar cualquier mensaje personalizado al escribir
            montoInput.setCustomValidity('');
        });

        form.addEventListener('submit', function (e) {
            // Asegurar que el name esté correcto según el tipo antes de validar/enviar
            toggleCategorias();
            // Si el monto está por debajo del mínimo, forzar el globo nativo y evitar el submit
            const min = parseFloat(montoInput.min || '0.01');
            const val = parseFloat(montoInput.value);
            if (montoInput.value && (isNaN(val) || val < min)) {
                montoInput.setCustomValidity('Monto inválido. Debe ser mayor a cero');
                montoInput.reportValidity();
                e.preventDefault();
            } else {
                montoInput.setCustomValidity('');
            }
        });

        toggleCategorias();
        tipoSelect.addEventListener('change', toggleCategorias);
    });
</script>

<jsp:include page="/comun/VistaFooter.jsp" />
