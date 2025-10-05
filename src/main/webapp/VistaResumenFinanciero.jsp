<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

<%-- 1. Incluimos el header que abre <html>, <body> y <main> --%>
<jsp:include page="/comun/VistaHeader.jsp">
    <jsp:param name="paitle" value="Resumen Financiero"/>
</jsp:include>

<%-- 2. Contenido específico de esta página --%>
<div class="page-header">
    <h1>Gestión de Resumen Financiero</h1>
    <a href="${pageContext.request.contextPath}/consultarResumenes" class="btn btn-secondary">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"></path>
            </svg>
            <span>Consultar Resúmenes</span>
    </a>

</div>

<%-- Mostrar lista de resúmenes si existen --%>
<c:if test="${not empty ResumenesFinancieros}">
    <div class="page-header" style="margin-top: 2rem;">
        <h2>Historial de Resúmenes Financieros</h2>
            <a href="${pageContext.request.contextPath}/VistaResumenFinanciero.jsp" class="btn btn-primary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="7 10 12 15 17 10"></polyline>
                        <line x1="12" y1="15" x2="12" y2="3"></line>
                    </svg>
                    <span>Subir PDF</span>
            </a>

    </div>

    <c:forEach var="resumen" items="${ResumenesFinancieros}" varStatus="status">
        <div style="margin-bottom: 3rem; padding-bottom: 2rem; border-bottom: 2px solid var(--border-color);">
            <h3 style="margin-bottom: 1rem; color: var(--accent-primary);">Resumen #${resumen.id}</h3>
            <span>Período: ${resumen.fechaPeriodoAnterior} / ${resumen.fechaPeriodoActual}</span>
            <section style="display: grid; grid-template-columns: repeat(auto-fit, minmax(280px, 1fr)); gap: 1rem;">
                <%-- Tarjeta de Ingresos --%>
                <article class="card">
                    <div class="card-header">
                        <div class="card-icon" style="color: #10b981;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <line x1="12" y1="1" x2="12" y2="23"></line>
                                <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                            </svg>
                        </div>
                        <h3 class="card-title">Ingresos</h3>
                    </div>
                    <div class="card-body">
                        <p style="font-size: 1.75rem; font-weight: 600; color: #10b981; margin: 0;">
                            <fmt:formatNumber value="${resumen.ingresosTotales}" type="currency" currencySymbol="$" />
                        </p>
                        <p style="margin-top: 0.5rem; color: var(--text-secondary); font-size: 0.9rem;">
                            Depósitos y créditos
                        </p>
                    </div>
                </article>

                <%-- Tarjeta de Gastos --%>
                <article class="card">
                    <div class="card-header">
                        <div class="card-icon" style="color: #ef4444;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <circle cx="12" cy="12" r="10"></circle>
                                <line x1="15" y1="9" x2="9" y2="15"></line>
                                <line x1="9" y1="9" x2="15" y2="15"></line>
                            </svg>
                        </div>
                        <h3 class="card-title">Gastos</h3>
                    </div>
                    <div class="card-body">
                        <p style="font-size: 1.75rem; font-weight: 600; color: #ef4444; margin: 0;">
                            <fmt:formatNumber value="${resumen.gastosTotales}" type="currency" currencySymbol="$" />
                        </p>
                        <p style="margin-top: 0.5rem; color: var(--text-secondary); font-size: 0.9rem;">
                            Cheques y débitos
                        </p>
                    </div>
                </article>

                <%-- Tarjeta de Ahorro Neto --%>
                <article class="card">
                    <div class="card-header">
                        <div class="card-icon" style="color: ${resumen.ahorroNeto >= 0 ? '#10b981' : '#ef4444'};">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
                            </svg>
                        </div>
                        <h3 class="card-title">Ahorro Neto</h3>
                    </div>
                    <div class="card-body">
                        <p style="font-size: 1.75rem; font-weight: 600; color: ${resumen.ahorroNeto >= 0 ? '#10b981' : '#ef4444'}; margin: 0;">
                            <fmt:formatNumber value="${resumen.ahorroNeto}" type="currency" currencySymbol="$" />
                        </p>
                        <p style="margin-top: 0.5rem; color: var(--text-secondary); font-size: 0.9rem;">
                            ${resumen.ahorroNeto >= 0 ? 'Balance positivo' : 'Balance negativo'}
                        </p>
                    </div>
                </article>

                <%-- Nueva card del PDF --%>
                            <c:if test="${not empty DocumentosPDF[status.index]}">
                                <c:set var="documento" value="${DocumentosPDF[status.index]}" />
                <%-- Tarjeta del PDF con opción de descarga --%>
                <article class="card">
                    <div class="card-header">
                        <div class="card-icon" style="color: #3b82f6;">
                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                                <polyline points="14 2 14 8 20 8"></polyline>
                                <line x1="16" y1="13" x2="8" y2="13"></line>
                                <line x1="16" y1="17" x2="8" y2="17"></line>
                                <polyline points="10 9 9 9 8 9"></polyline>
                            </svg>
                        </div>
                        <h3 class="card-title">Documento PDF</h3>
                    </div>
                    <div class="card-body">
                        <div style="text-align: center;">

                            <p style="font-weight: 600; color: var(--text-primary); margin: 0.25rem 0;">
                                ${documento.nombre}
                            </p>
                            <p style="color: var(--text-secondary); font-size: 0.9rem; margin: 0.25rem 0;">
                                <fmt:formatNumber value="${documento.tamanio / 1024}" maxFractionDigits="0" /> KB
                            </p>

                        </div>

                        <div style="margin-top: 1rem; display: flex; gap: 0.5rem; justify-content: center;">
                            <a href="${pageContext.request.contextPath}/descargarPDF?id=${documento.id}"
                               class="btn btn-primary" style="padding: 0.5rem 1rem; font-size: 0.9rem;">
                                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                                    <polyline points="7 10 12 15 17 10"></polyline>
                                    <line x1="12" y1="15" x2="12" y2="3"></line>
                                </svg>
                                Descargar
                            </a>


                        </div>
                    </div>
                </article>
                </c:if>
            </section>
        </div>
    </c:forEach>
</c:if>

<%-- Mostrar error si existe --%>
<c:if test="${not empty error}">
    <div class="page-header" style="margin-top: 2rem;">
                <h2>Resultados del Análisis</h2>
                <a href="${pageContext.request.contextPath}/VistaResumenFinanciero.jsp" class="btn btn-primary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="7 10 12 15 17 10"></polyline>
                        <line x1="12" y1="15" x2="12" y2="3"></line>
                    </svg>
                    <span>Subir Otro PDF</span>
                </a>
        </div>
    <div>

    </div>

    <div class="alert alert-danger" style="margin-top: 1.5rem;">
        <strong>Error:</strong> ${error}
    </div>
</c:if>

<%-- Mostrar resultados si existen (FUERA del formulario) --%>
<c:if test="${not empty Ingresos}">

    <div class="page-header" style="margin-top: 2rem;">
            <h2>Resultados del Análisis</h2>
            <a href="${pageContext.request.contextPath}/VistaResumenFinanciero.jsp" class="btn btn-primary">
                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                    <polyline points="7 10 12 15 17 10"></polyline>
                    <line x1="12" y1="15" x2="12" y2="3"></line>
                </svg>

                <span>Subir Otro PDF</span>
            </a>

    </div>

    <span>    <p style="color: var(--text-secondary); margin: 0.5rem 0;">
                                                  Fecha de Creación: ${fechaCreacionFormateada}
                                              </p></span>
    <span><p style="margin: 1.rem;"> Período: ${fechaPeriodoAnterior} / ${fechaPeriodoActual} </p></span>


    <section class="grid" style="margin-top: 1rem;">
        <%-- Tarjeta de Ingresos --%>
        <article class="card">
            <div class="card-header">
                <div class="card-icon" style="color: #10b981;">
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <line x1="12" y1="1" x2="12" y2="23"></line>
                        <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                    </svg>
                </div>
                <h3 class="card-title">Ingresos Totales</h3>
            </div>
            <div class="card-body">
                <p style="font-size: 2rem; font-weight: 600; color: #10b981; margin: 0;">
                    <fmt:formatNumber value="${Ingresos}" type="currency" currencySymbol="$" />
                </p>
                <p style="margin-top: 0.5rem; color: var(--text-secondary);">
                    Depósitos y créditos del período
                </p>
            </div>
        </article>

        <%-- Tarjeta de Gastos --%>
        <c:if test="${not empty Gastos}">
            <article class="card">
                <div class="card-header">
                    <div class="card-icon" style="color: #ef4444;">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <circle cx="12" cy="12" r="10"></circle>
                            <line x1="15" y1="9" x2="9" y2="15"></line>
                            <line x1="9" y1="9" x2="15" y2="15"></line>
                        </svg>
                    </div>
                    <h3 class="card-title">Gastos Totales</h3>
                </div>
                <div class="card-body">
                    <p style="font-size: 2rem; font-weight: 600; color: #ef4444; margin: 0;">
                        <fmt:formatNumber value="${Gastos}" type="currency" currencySymbol="$" />
                    </p>
                    <p style="margin-top: 0.5rem; color: var(--text-secondary);">
                        Cheques y débitos del período
                    </p>
                </div>
            </article>
        </c:if>

        <%-- Tarjeta de Ahorro Neto con color condicional --%>
        <c:if test="${not empty AhorroNeto}">
            <article class="card">
                <div class="card-header">
                    <div class="card-icon" style="color: ${AhorroNeto  >= 0 ? '#10b981' : '#ef4444'};">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                            <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
                        </svg>
                    </div>
                    <h3 class="card-title">AhorroNeto</h3>
                </div>
                <div class="card-body">
                    <p style="font-size: 2rem; font-weight: 600; color: ${AhorroNeto >= 0 ? '#10b981' : '#ef4444'}; margin: 0;">
                        <fmt:formatNumber value="${AhorroNeto}" type="currency" currencySymbol="$" />
                    </p>
                    <p style="margin-top: 0.5rem; color: var(--text-secondary);">
                        ${AhorroNeto >= 0 ? 'Balance positivo del período' : 'Balance negativo del período'}
                    </p>
                </div>
            </article>
        </c:if>
                </div>
            </div>
        </article>
    </section>
</c:if>

<%-- Estado vacío cuando no hay resultados --%>
<c:if test="${empty Ingresos and empty error and empty ResumenesFinancieros}">
    <div class="empty-state">
        <div class="empty-state-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
            </svg>
        </div>
        <h2>Sube tu estado de cuenta</h2>
        <p>Envía tu archivo PDF del estado de cuenta bancario</p>

        <form action="subirPDF" method="post" enctype="multipart/form-data" style="max-width: 500px; margin: 1rem auto;">
            <div class="form-group">
                <label for="archivoPDF" class="btn btn-secondary" style="cursor: pointer; display: inline-block; margin-bottom: 1rem;">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="vertical-align: middle; margin-right: 0.5rem;">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="7 10 12 15 17 10"></polyline>
                        <line x1="12" y1="15" x2="12" y2="3"></line>
                    </svg>
                    Seleccionar Archivo PDF
                </label>
                <input type="file" id="archivoPDF" name="archivoPDF" accept="application/pdf" required style="display: none;" onchange="updateFileName(this)">
                <p id="file-name" style="color: var(--text-secondary); margin-top: 0.5rem; font-size: 0.9rem;">Ningún archivo seleccionado</p>
            </div>

            <div style="text-align: center; margin-top: 1. rem;">
                <button type="submit" class="btn btn-primary">
                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"></path>
                        <polyline points="17 8 12 3 7 8"></polyline>
                        <line x1="12" y1="3" x2="12" y2="15"></line>
                    </svg>
                    <span>Procesar</span>
                </button>
            </div>
        </form>
    </div>
</c:if>

<script>
function updateFileName(input) {
    const fileName = input.files[0]?.name || 'Ningún archivo seleccionado';
    document.ElementById('file-name').textContent = fileName;
}
// Limpiar el input file cuando la página carga (evita problema de caché)
 window.addEventListener('load', function() {
     const fileInput = document.ElementById('archivoPDF');
     if (fileInput) {
         fileInput.value = '';
         const fileNameElement = document.ElementById('file-name');
         if (fileNameElement) {
             fileNameElement.textContent = 'Ningún archivo seleccionado';
         }
     }
 });
</script>

<%-- 3. Footer que cierra <main>, <body> y <html> --%>
<jsp:include page="/comun/VistaFooter.jsp" />