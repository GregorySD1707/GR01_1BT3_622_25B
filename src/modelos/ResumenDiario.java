public class ResumenDiario {
    private double ingresosTotales;
    private double gastosTotales;
    private double ahorroNeto;

    public ResumenDiario() {
        this.ingresosTotales = 0.0;
        this.gastosTotales = 0.0;
        this.ahorroNeto = 0.0;
    }

    public ResumenDiario(double ingresosTotales, double gastosTotales, double ahorroNeto) {
        this.ingresosTotales = ingresosTotales;
        this.gastosTotales = gastosTotales;
        this.ahorroNeto = ahorroNeto;
    }

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    public double getGastosTotales() {
        return gastosTotales;
    }

    public double getAhorroNeto() {
        return ahorroNeto;
    }

    public void setIngresosTotales(double ingresosTotales) {
        this.ingresosTotales = ingresosTotales;
    }

    public void setGastosTotales(double gastosTotales) {
        this.gastosTotales = gastosTotales;
    }

    public void setAhorroNeto(double ahorroNeto) {
        this.ahorroNeto = ahorroNeto;
    }

}
