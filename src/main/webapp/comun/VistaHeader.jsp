<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page isELIgnored="false" %>

<%-- RUTA: /comun/VistaHeader.jsp --%>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>${not empty param.pageTitle ? param.pageTitle : 'EconoMe'}</title>

  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
</head>
<body>

<%-- ========================================================= --%>
<%-- NUEVA BARRA LATERAL (SIDEBAR) --%>
<%-- ========================================================= --%>
<aside id="sidebar">
  <div class="sidebar-header">
    <h3>Menú</h3>
    <button id="close-sidebar-btn" class="icon-btn">&times;</button>
  </div>
  <nav class="sidebar-nav">
    <a href="${pageContext.request.contextPath}/movimientos">Ingresos y gastos</a>
    <a href="${pageContext.request.contextPath}/deudas?accion=listar">Deudas y prestamos</a>
    <a href="${pageContext.request.contextPath}/resumen_financiero/VistaResumenFinanciero.jsp">Resumen Financiero</a>
    <a href="${pageContext.request.contextPath}/recordatorios">Recordatorios</a>
    
  </nav>
</aside>
<div id="overlay"></div>
<%-- ========================================================= --%>


<header class="topbar">
  <div class="container">
    <div class="header-left">
      <%-- NUEVO BOTÓN PARA ABRIR LA SIDEBAR --%>
      <button id="sidebar-toggle-btn" class="icon-btn">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="3" y1="12" x2="21" y2="12"></line><line x1="3" y1="6" x2="21" y2="6"></line><line x1="3" y1="18" x2="21" y2="18"></line></svg>
      </button>
      <a href="${pageContext.request.contextPath}/inicio" class="brand">
        <img src="${pageContext.request.contextPath}/resources/images/Logo.png" alt="Logo de EconoMe" class="brand-logo">
        <span>EconoMe</span>
      </a>
    </div>

    <div class="user-actions">
      <div class="notification-container">
        <button class="icon-btn notification-btn">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9"></path><path d="M13.73 21a2 2 0 0 1-3.46 0"></path></svg>
          <span id="notification-count" class="badge" style="display: none;"></span>
        </button>
        <div id="notification-list" class="dropdown-menu">
        </div>
     <!-- </div>
        <div class="user-menu">
        <button class="user-avatar"><span>EU</span></button>
        <div class="dropdown-menu">
          <div class="dropdown-header">
            <p class="username">Nombre de Usuario</p>
            <p class="email">usuario@email.com</p>
          </div>
          <a href="${pageContext.request.contextPath}/perfil" class="dropdown-item">Mi Perfil</a>
          <a href="#" class="dropdown-item">Configuración</a>
          <div class="dropdown-divider"></div>
          <a href="#" class="dropdown-item logout">Cerrar Sesión</a>
        </div>  
      </div> -->
    </div>
  </div>
</header>

<main class="container page-content">
<%-- La etiqueta <main> y <body> se cerrarán en VistaFooter.jsp --%>