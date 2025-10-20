<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EconoMe - Registro</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/auth.css">
</head>
<body class = "signup-page">
<div class="signup-wrapper">
    <!-- Lado izquierdo: Branding minimalista -->
    <div class="brand-side">
        <div class="brand-content">
            <img src="${pageContext.request.contextPath}/resources/images/Logo.png" alt="EconoMe Logo" class="brand-logo-large">
            <h1 class="brand-title">Econo<span class="accent">Me</span></h1>
            <p class="tagline">Estás a punto de empezar a ahorrar como nunca antes</p>
        </div>

        <div class="brand-footer">
            <p>¿Ya tienes una cuenta? <a href="${pageContext.request.contextPath}/LogInUsuario">Iniciar sesión</a></p>
        </div>
    </div>

    <!-- Lado derecho: Formulario -->
    <div class="form-side">
        <div class="form-container">
            <h2 class="form-title">Crear Cuenta</h2>

            <% if (request.getAttribute("error") != null) { %>
            <div class="error-message show">
                <%= request.getAttribute("error") %>
            </div>
            <% } %>

            <% if (request.getAttribute("success") != null) { %>
            <div class="success-message show">
                <%= request.getAttribute("success") %>
            </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/RegistroUsuario" method="post">
                <div class="form-row">
                    <div class="form-group">
                        <input
                                type="text"
                                id="nombre"
                                name="nombre"
                                placeholder="Nombre"
                                required
                                maxlength="50"
                        >
                    </div>

                    <div class="form-group">
                        <input
                                type="text"
                                id="apellido"
                                name="apellido"
                                placeholder="Apellido"
                                required
                                maxlength="50"
                        >
                    </div>
                </div>

                <div class="form-group">
                    <input
                            type="email"
                            id="correo"
                            name="correo"
                            placeholder="Correo electrónico"
                            required
                            maxlength="100"
                            autocomplete="email"
                    >
                </div>

                <div class="form-group">
                    <input
                            type="text"
                            id="nombreUsuario"
                            name="nombreUsuario"
                            placeholder="Nombre de usuario"
                            required
                            maxlength="50"
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
                            autocomplete="new-password"
                    >
                </div>

                <div class="form-group">
                    <input
                            type="password"
                            id="confirmarContrasena"
                            name="confirmarContrasena"
                            placeholder="Confirmar contraseña"
                            required
                            autocomplete="new-password"
                    >
                </div>

                <div class="form-group">
                    <input
                            type="date"
                            id="fechaNacimiento"
                            name="fechaNacimiento"
                            max="<%= java.time.LocalDate.now().minusYears(13) %>"
                            required
                    >
                    <small class="input-hint">Fecha de nacimiento</small>
                </div>

                <button type="submit" class="btn-login">Crear mi cuenta</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>