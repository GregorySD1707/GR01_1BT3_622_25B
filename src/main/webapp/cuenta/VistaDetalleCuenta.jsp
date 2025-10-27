    <%@ page contentType="text/html; charset=UTF-8" language="java" %>
    <%@ page isELIgnored="false" %>
    <%@ taglib uri="jakarta.tags.core" prefix="c" %>
    <%@ taglib uri="jakarta.tags.fmt" prefix="fmt" %>

    <jsp:include page="/comun/VistaHeader.jsp">
        <jsp:param name="pageTitle" value="Detalle de Cuenta"/>
    </jsp:include>

    <div class="page-header">
        <h1>
            <svg xmlns="http://www.w3.org/2000/svg" width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" style="vertical-align: middle; margin-right: 8px;">
                <c:choose>
                    <c:when test="${cuenta.tipo == 'EFECTIVO'}">
                        <line x1="12" y1="1" x2="12" y2="23"></line>
                        <path d="M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6"></path>
                    </c:when>
                    <c:when test="${cuenta.tipo == 'CORRIENTE'}">
                        <rect x="2" y="5" width="20" height="14" rx="2"></rect>
                        <line x1="2" y1="10" x2="22" y2="10"></line>
                    </c:when>
                    <c:when test="${cuenta.tipo == 'AHORROS'}">
                        <path d="M19 5c-1.5 0-2.8 1.4-3 2-3.5-1.5-11-.3-11 5 0 1.8 0 3 2 4.5V20h4v-2h3v2h4v-4c1-.5 1.7-1 2-2h2v-4h-2c0-1-.5-1.5-1-2h0V5z"></path>
                        <path d="M2 9v1c0 1.1.9 2 2 2h1"></path>
                        <path d="M16 11h0"></path>
                    </c:when>
                    <c:otherwise>
                        <rect x="2" y="7" width="20" height="14" rx="2" ry="2"></rect>
                        <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16"></path>
                    </c:otherwise>
                </c:choose>
            </svg>
            <c:out value="${cuenta.nombre}"/>
        </h1>
        <a href="${pageContext.request.contextPath}/cuentas" class="btn btn-secondary">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="19" y1="12" x2="5" y2="12"></line>
                <polyline points="12 19 5 12 12 5"></polyline>
            </svg>
            <span>Volver</span>
        </a>
    </div>

    <jsp:include page="/comun/Mensajes.jsp" />

    <%-- Resumen compacto en una sola fila --%>
    <div class="resumen-compacto">
        <div class="resumen-item saldo-principal">
            <span class="resumen-label">Saldo Actual</span>
            <span class="resumen-valor"><fmt:formatNumber value="${cuenta.monto}" type="currency" currencySymbol="$" /></span>
        </div>
        <div class="resumen-divisor"></div>
        <div class="resumen-item">
            <span class="resumen-label">Tipo</span>
            <span class="resumen-valor tipo-badge">${cuenta.tipo}</span>
        </div>
        <div class="resumen-divisor"></div>
        <div class="resumen-item ingreso">
            <span class="resumen-label">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="12" y1="19" x2="12" y2="5"></line>
                    <polyline points="5 12 12 5 19 12"></polyline>
                </svg>
                Ingresos
            </span>
            <span class="resumen-valor"><fmt:formatNumber value="${totalIngresos}" type="currency" currencySymbol="$" /></span>
        </div>
        <div class="resumen-divisor"></div>
        <div class="resumen-item gasto">
            <span class="resumen-label">
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="12" y1="5" x2="12" y2="19"></line>
                    <polyline points="19 12 12 19 5 12"></polyline>
                </svg>
                Gastos
            </span>
            <span class="resumen-valor"><fmt:formatNumber value="${totalGastos}" type="currency" currencySymbol="$" /></span>
        </div>
        <div class="resumen-divisor"></div>
        <div class="resumen-item">
            <span class="resumen-label">Movimientos</span>
            <span class="resumen-valor">${cantidadMovimientos}</span>
        </div>
    </div>

    <%-- Título de la sección de movimientos --%>
    <div class="seccion-header">
        <h2>Movimientos Recientes</h2>
        <a href="${pageContext.request.contextPath}/movimientos" class="btn btn-primary">
            <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <line x1="12" y1="5" x2="12" y2="19"></line>
                <line x1="5" y1="12" x2="19" y2="12"></line>
            </svg>
            <span>Nuevo</span>
        </a>
    </div>

    <%-- Estado vacío --%>
    <c:if test="${empty movimientos}">
        <div class="empty-state-modern">
            <div class="empty-icon">
                <svg xmlns="http://www.w3.org/2000/svg" width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round">
                    <polyline points="22 12 18 12 15 21 9 3 6 12 2 12"></polyline>
                </svg>
            </div>
            <h3>Sin movimientos</h3>
            <p>No hay transacciones registradas en esta cuenta</p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/movimientos">
                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                    <line x1="12" y1="5" x2="12" y2="19"></line>
                    <line x1="5" y1="12" x2="19" y2="12"></line>
                </svg>
                Crear movimiento
            </a>
        </div>
    </c:if>

    <%-- Lista de movimientos moderna --%>
    <c:if test="${not empty movimientos}">
        <div class="movimientos-lista">
            <c:forEach var="movimiento" items="${movimientos}">
                <div class="mov-item ${movimiento.tipo == 'INGRESO' ? 'mov-ingreso' : 'mov-gasto'}">
                    <div class="mov-icon">
                        <c:choose>
                            <c:when test="${movimiento.tipo == 'INGRESO'}">
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                                    <line x1="12" y1="19" x2="12" y2="5"></line>
                                    <polyline points="5 12 12 5 19 12"></polyline>
                                </svg>
                            </c:when>
                            <c:otherwise>
                                <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round">
                                    <line x1="12" y1="5" x2="12" y2="19"></line>
                                    <polyline points="19 12 12 19 5 12"></polyline>
                                </svg>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="mov-info">
                        <div class="mov-descripcion"><c:out value="${movimiento.descripcion}"/></div>
                        <div class="mov-meta">
                            <span class="mov-categoria">${movimiento.categoria}</span>
                            <span class="mov-separador">•</span>
                            <span class="mov-fecha">${movimiento.fechaFormateada}</span>
                        </div>
                    </div>

                    <div class="mov-monto">
                        <c:choose>
                            <c:when test="${movimiento.tipo == 'INGRESO'}">
                                <span class="monto-valor positivo">+<fmt:formatNumber value="${movimiento.monto}" type="currency" currencySymbol="$" /></span>
                            </c:when>
                            <c:otherwise>
                                <span class="monto-valor negativo">-<fmt:formatNumber value="${movimiento.monto}" type="currency" currencySymbol="$" /></span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <style>
        /* Resumen compacto */
        .resumen-compacto {
            display: flex;
            align-items: center;
            background: rgba(255, 255, 255, 0.05);
            backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.1);
            border-radius: 16px;
            padding: 1.5rem;
            margin-bottom: 2rem;
            gap: 1.5rem;
            overflow-x: auto;
        }

        .resumen-item {
            display: flex;
            flex-direction: column;
            gap: 0.4rem;
            min-width: fit-content;
        }

        .resumen-item.saldo-principal {
            flex: 1;
        }

        .resumen-label {
            font-size: 0.75rem;
            color: rgba(255, 255, 255, 0.5);
            text-transform: uppercase;
            letter-spacing: 0.5px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 0.3rem;
        }

        .resumen-valor {
            font-size: 1.25rem;
            font-weight: 700;
            color: #fff;
        }

        .resumen-item.saldo-principal .resumen-valor {
            font-size: 1.75rem;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            background-clip: text;
        }

        .resumen-item.ingreso .resumen-valor {
            color: #10b981;
        }

        .resumen-item.gasto .resumen-valor {
            color: #f43f5e;
        }

        .tipo-badge {
            background: rgba(102, 126, 234, 0.15);
            color: #667eea;
            padding: 0.35rem 0.75rem;
            border-radius: 8px;
            font-size: 0.85rem;
            font-weight: 600;
            width: fit-content;
        }

        .resumen-divisor {
            width: 1px;
            height: 40px;
            background: rgba(255, 255, 255, 0.1);
        }

        /* Header de sección */
        .seccion-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
        }

        .seccion-header h2 {
            font-size: 1.5rem;
            font-weight: 700;
            margin: 0;
            color: #fff;
        }

        /* Estado vacío moderno */
        .empty-state-modern {
            text-align: center;
            padding: 4rem 2rem;
            background: rgba(255, 255, 255, 0.03);
            border: 1px dashed rgba(255, 255, 255, 0.1);
            border-radius: 16px;
        }

        .empty-icon {
            width: 80px;
            height: 80px;
            margin: 0 auto 1.5rem;
            background: rgba(102, 126, 234, 0.1);
            border-radius: 20px;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #667eea;
        }

        .empty-state-modern h3 {
            font-size: 1.5rem;
            font-weight: 700;
            margin-bottom: 0.5rem;
            color: #fff;
        }

        .empty-state-modern p {
            color: rgba(255, 255, 255, 0.5);
            margin-bottom: 2rem;
        }

        /* Lista de movimientos */
        .movimientos-lista {
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
        }

        .mov-item {
            display: flex;
            align-items: center;
            gap: 1rem;
            padding: 1.25rem;
            background: rgba(255, 255, 255, 0.05);
            border: 1px solid rgba(255, 255, 255, 0.08);
            border-radius: 12px;
            transition: all 0.2s ease;
            position: relative;
            overflow: hidden;
        }

        .mov-item::before {
            content: '';
            position: absolute;
            left: 0;
            top: 0;
            bottom: 0;
            width: 3px;
            transition: all 0.2s ease;
        }

        .mov-item.mov-ingreso::before {
            background: #10b981;
        }

        .mov-item.mov-gasto::before {
            background: #f43f5e;
        }

        .mov-item:hover {
            background: rgba(255, 255, 255, 0.08);
            border-color: rgba(255, 255, 255, 0.15);
            transform: translateX(4px);
        }

        .mov-icon {
            width: 42px;
            height: 42px;
            border-radius: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
        }

        .mov-item.mov-ingreso .mov-icon {
            background: rgba(16, 185, 129, 0.15);
            color: #10b981;
        }

        .mov-item.mov-gasto .mov-icon {
            background: rgba(244, 63, 94, 0.15);
            color: #f43f5e;
        }

        .mov-info {
            flex: 1;
            min-width: 0;
        }

        .mov-descripcion {
            font-size: 0.95rem;
            font-weight: 600;
            color: #fff;
            margin-bottom: 0.3rem;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .mov-meta {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            font-size: 0.8rem;
            color: rgba(255, 255, 255, 0.4);
        }

        .mov-categoria {
            background: rgba(255, 255, 255, 0.08);
            padding: 0.2rem 0.6rem;
            border-radius: 6px;
            font-weight: 500;
        }

        .mov-separador {
            opacity: 0.3;
        }

        .mov-monto {
            text-align: right;
            flex-shrink: 0;
        }

        .monto-valor {
            font-size: 1.1rem;
            font-weight: 700;
        }

        .monto-valor.positivo {
            color: #10b981;
        }

        .monto-valor.negativo {
            color: #f43f5e;
        }

        /* Responsive */
        @media (max-width: 768px) {
            .resumen-compacto {
                flex-wrap: nowrap;
                overflow-x: auto;
                -webkit-overflow-scrolling: touch;
            }

            .resumen-divisor {
                display: none;
            }

            .resumen-item.saldo-principal .resumen-valor {
                font-size: 1.5rem;
            }

            .resumen-valor {
                font-size: 1rem;
            }

            .mov-item {
                padding: 1rem;
            }

            .mov-descripcion {
                font-size: 0.9rem;
            }

            .monto-valor {
                font-size: 1rem;
            }
        }
    </style>

    <jsp:include page="/comun/VistaFooter.jsp" />