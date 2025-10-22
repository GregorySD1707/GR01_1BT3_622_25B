<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>EconoMe - Iniciar Sesión</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/auth.css">
</head>
<body class = "login-page">
<!-- Logo y nombre FUERA del formulario -->
<div class="brand-header">
  <img src="${pageContext.request.contextPath}/resources/images/Logo.png" alt="EconoMe Logo" class="brand-logo">
  <h1 class="brand-name">Econo<span class="accent">Me</span></h1>
</div>

<!-- Contenedor del formulario -->
<div class="login-container">
  <h2 class="form-title">Iniciar Sesión</h2>

  <% if (request.getAttribute("error") != null) { %>
  <div class="error-message show">
    <%= request.getAttribute("error") %>
  </div>
  <% } %>

  <form action="${pageContext.request.contextPath}/ingreso" method="post">
    <div class="form-group">
      <input
              type="text"
              id="identificadorUsuario"
              name="identificadorUsuario"
              placeholder="Usuario o correo electrónico"
              required
              autocomplete="username"
      >
    </div>

    <div class="form-group">
      <input
              type="password"
              id="contrasena"
              name="contrasena"
              placeholder="Contraseña"
              required
              autocomplete="contrasena"
      >
    </div>

    <button type="submit" class="btn-login">Iniciar Sesión</button>
  </form>

  <!-- Links debajo del botón -->
  <div class="links">
    <a href="${pageContext.request.contextPath}/registro">¿Eres nuevo? Regístrate</a>
    <a href="${pageContext.request.contextPath}/forgot-password.jsp">¿Olvidaste tu contraseña?</a>
  </div>
</div>
</body>
</html>