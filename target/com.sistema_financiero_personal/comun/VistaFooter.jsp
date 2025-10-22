<%@ page isELIgnored="false" %>

<%-- RUTA: /comun/VistaFooter.jsp --%>
</main> <%-- Cierra la etiqueta <main> abierta en VistaHeader.jsp --%>

<footer class="footer">
    <div class="container">
        <p>&copy; 2025 EconoMe. Todos los derechos reservados.</p>
        <div class="footer-links">
            <a href="#">Terminos de Servicio</a>
            <a href="#">Politica de Privacidad</a>
            <a href="#">Contacto</a>
        </div>
    </div>
</footer>

<script>
    const CONTEXT_PATH = "${pageContext.request.contextPath}";
</script>
<script src="${pageContext.request.contextPath}/resources/js/app.js"></script>

</body>
</html>