<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Gestor de Movimientos</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <style>
        .movimientos-container {
            position: relative;
            max-width: 420px;
            margin: 48px auto 0 auto;
        }
        .movimientos-wrapper {
            background: var(--bg-med);
            border-radius: 18px;
            box-shadow: 0 2px 16px var(--shadow-color);
            padding: 32px 24px;
            width: 100%;
            margin: 0;
            /* position ya no necesaria para la flecha */
        }
        .movimientos-title {
            text-align: center;
            font-size: 2rem;
            font-weight: 600;
            margin-bottom: 24px;
            color: var(--text-primary);
        }
        .movimientos-form { display: flex; flex-direction: column; gap: 18px; }
        .movimientos-form label { color: var(--text-secondary); font-weight: 500; }
        .movimientos-form input,
        .movimientos-form select {
            background: var(--bg-light);
            color: var(--text-primary);
            border: 1px solid var(--border-color);
            border-radius: 8px;
            padding: 10px;
            font-size: 1rem;
        }
        .movimientos-form button {
            background: var(--accent-primary);
            color: #fff;
            border: none;
            border-radius: 8px;
            padding: 12px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: background 0.2s;
        }
        .movimientos-form button:hover { background: var(--accent-hover); }
        /* Flecha fuera del recuadro */
        .back-arrow { position: absolute; top: -16px; left: -84px; width: 36px; height: 36px; display: flex; align-items: center; justify-content: center; border-radius: 8px; background: var(--bg-light); border: 1px solid var(--border-color); color: var(--text-primary); text-decoration: none; box-shadow: 0 2px 6px var(--shadow-color); transition: background 0.2s ease; z-index: 10; }
        .back-arrow:hover { background: #2f3a52; }
        @media (max-width: 480px) {
            .back-arrow { left: -16px; top: -12px; }
        }
    </style>
</head>
<body>
<div class="movimientos-container">
    <a class="back-arrow" href="${pageContext.request.contextPath}/inicio" title="Volver al inicio" aria-label="Volver al inicio">
        <svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="15 18 9 12 15 6"></polyline></svg>
    </a>
    <div class="movimientos-wrapper">
        <div class="movimientos-title">Gestor de Movimientos</div>
        <c:if test="${not empty mensajeExito}">
            <div style="background:#2563eb;color:#fff;padding:10px 0;border-radius:8px;text-align:center;margin-bottom:18px;font-weight:600;">
                    ${mensajeExito}
            </div>
        </c:if>
        <c:if test="${not empty mensajeError}">
            <div style="background:#ef4444;color:#fff;padding:10px 0;border-radius:8px;text-align:center;margin-bottom:18px;font-weight:600;">
                    ${mensajeError}
            </div>
        </c:if>
        <form class="movimientos-form" method="post" action="movimientos">
            <label>Tipo:</label>
            <select name="tipo">
                <option value="INGRESO">Ingreso</option>
                <option value="GASTO">Gasto</option>
            </select>
            <label>Monto:</label>
            <input type="number" step="0.01" name="monto" required>
            <label>Descripción:</label>
            <input type="text" name="descripcion">
            <label>Categoría:</label>
            <input type="text" name="categoria">
            <label>Cartera:</label>
            <input type="text" name="cartera" required>
            <button type="submit">Registrar</button>
        </form>
    </div>
</div>
</body>
</html>