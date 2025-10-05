<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gestión de Movimientos</title>
</head>
<body>
    <h1>Gestión de Movimientos</h1>
    <h2>Saldo actual: $${saldoActual}</h2>
    <h3>Ingresos totales: $${ingresosTotales}</h3>
    <h3>Gastos totales: $${gastosTotales}</h3>
    <hr>
    <h2>Registrar Movimiento</h2>
    <form method="post" action="movimientos">
        <label>Tipo:</label>
        <select name="tipo">
            <option value="INGRESO">Ingreso</option>
            <option value="GASTO">Gasto</option>
        </select><br>
        <label>Monto:</label>
        <input type="number" step="0.01" name="monto" required><br>
        <label>Descripción:</label>
        <input type="text" name="descripcion"><br>
        <label>Categoría:</label>
        <input type="text" name="categoria"><br>
        <label>Cartera:</label>
        <input type="text" name="cartera" required><br>
        <button type="submit">Registrar</button>
    </form>
</body>
</html>

