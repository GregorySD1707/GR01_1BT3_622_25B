package com.sistema_financiero_personal.controladores;

import com.sistema_financiero_personal.modelos.Cartera;
import com.sistema_financiero_personal.modelos.Movimiento;
import com.sistema_financiero_personal.servicios.ServicioMovimientos;

public class ControladorMovimientos {

    private final ServicioMovimientos servicio;

    public ControladorMovimientos() {
        this.servicio = new ServicioMovimientos();
    }

    public ControladorMovimientos(ServicioMovimientos servicio) {
        this.servicio = servicio;
    }

    public Movimiento registrarIngreso(double monto, String descripcion, String categoria, String nombreCartera) {
        return servicio.registrarIngreso(monto, descripcion, categoria, nombreCartera);
    }

    public Movimiento registrarGasto(double monto, String descripcion, String categoria, String nombreCartera) {
        return servicio.registrarGasto(monto, descripcion, categoria, nombreCartera);
    }

    public Cartera actualizarCartera(double delta, String nombreCartera) {
        return servicio.actualizarCartera(delta, nombreCartera, null);
    }
}

