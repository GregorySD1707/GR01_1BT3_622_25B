<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="VistaHeader.jsp" %>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        const pageContent = document.querySelector('.page-content');
        if (pageContent) {
            pageContent.classList.add('landing');
        }
    });
</script>

<section class="hero-section">
    <div class="hero-content">
        <div class="hero-text">
            <h1 class="hero-title">Controla tus finanzas con <span class="text-gradient">EconoMe</span></h1>
            <p class="hero-subtitle">La plataforma inteligente que te ayuda a gestionar tu dinero, planificar tu futuro y alcanzar tus metas financieras de manera sencilla y efectiva.</p>
            <div class="hero-actions">
                <a href="${pageContext.request.contextPath}/resumen_financiero/componentes_resumen_financiero/FormularioSubirPDF.jsp" class="btn btn-primary btn-large">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M3 3v18h18"/>
                        <path d="M18.7 8l-5.1 5.2-2.8-2.7L7 14.3"/>
                    </svg>
                    Mi Resumen
                </a>
                <a href="${pageContext.request.contextPath}/recordatorios" class="btn btn-outline btn-large">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"/>
                        <path d="M13.73 21a2 2 0 0 1-3.46 0"/>
                    </svg>
                    Recordatorios
                </a>
                <a href="${pageContext.request.contextPath}/obligacion_financiera/deudas?accion=listar" class="btn btn-outline btn-large">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <rect x="3" y="11" width="18" height="8" rx="2"/>
                        <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                    </svg>
                    Deudas y préstamos
                </a>
                <a href="${pageContext.request.contextPath}/movimientos" class="btn btn-outline btn-large">
                    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <path d="M12 2v20"/>
                        <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"/>
                    </svg>
                    Ingresos y gastos
                </a>
            </div>
        </div>
        <div class="hero-visual">
            <div class="dashboard-mockup">
                <div style="width:100%;display:flex;justify-content:center;margin-bottom:12px;">
                    <div style="background:#222e3c;color:#fff;padding:8px 18px;border-radius:10px;font-size:1.2rem;font-weight:bold;box-shadow:0 2px 8px #0002;">
                        Saldo actual: $
                        <fmt:formatNumber value="${saldoActual}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                    </div>
                </div>
                <div class="mockup-header">
                    <div class="mockup-dots">
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                </div>
                <div class="mockup-content">
                    <div class="chart-placeholder">
                        <div class="chart-bars">
                            <div class="bar" style="height: 60%"></div>
                            <div class="bar" style="height: 80%"></div>
                            <div class="bar" style="height: 45%"></div>
                            <div class="bar" style="height: 90%"></div>
                            <div class="bar" style="height: 70%"></div>
                        </div>
                    </div>
                    <div class="stats-row">
                        <div class="stat-item">
                            <div class="stat-label">Ingresos</div>
                            <div class="stat-value">$
                                <fmt:formatNumber value="${ingresosTotales}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                            </div>
                        </div>
                        <div class="stat-item">
                            <div class="stat-label">Gastos</div>
                            <div class="stat-value">$
                                <fmt:formatNumber value="${gastosTotales}" type="number" minFractionDigits="2" maxFractionDigits="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<%@ include file="VistaFooter.jsp" %>