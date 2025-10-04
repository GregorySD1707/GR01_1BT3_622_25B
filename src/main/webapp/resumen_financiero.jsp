<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Resumen Financiero</title>
</head>

<body>
    <div class="container">
        <h2>Sube tu estado de cuenta en formato PDF para extraer la información financiera</h2>

        <form action="subirPDF" method="post" enctype="multipart/form-data" class="upload-form">
            <p>Selecciona tu archivo PDF</p>
            <input type="file" name="archivoPDF" accept="application/pdf" required>
            <br><br>
            <button type="submit">Subir y procesar</button>
        </form>

        <% if(request.getAttribute("error") != null) { %>
            <div class="error">
                <strong>Error:</strong> <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if(request.getAttribute("Ingresos") != null) { %>
            <div>
                <h3>Resultado</h3>
                <p><strong>Ingresos:</strong> $<%= request.getAttribute("Ingresos") %></p>
            </div>
        <% } %>

        <% if(request.getAttribute("Gastos") != null) { %>
                    <div>
                        <p><strong>Gastos:</strong> $<%= request.getAttribute("Gastos") %></p>
                    </div>
        <% } %>

        <% if(request.getAttribute("Ahorro Neto") != null) { %>
                            <div>
                                <p><strong>Ahorro Neto:</strong> $<%= request.getAttribute("Ahorro Neto") %></p>
                            </div>
        <% } %>

    </div>
</body>
</html>