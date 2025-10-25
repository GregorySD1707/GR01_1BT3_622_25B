package com.sistema_financiero_personal.obligaciones_financieras.modelos;

import com.sistema_financiero_personal.movimiento.modelos.CategoriaGasto;
import com.sistema_financiero_personal.movimiento.servicios.ServicioMovimiento;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import org.eclipse.tags.shaded.org.apache.bcel.generic.CASTORE;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("DEUDA")
public class Deuda extends ObligacionFinanciera{
    @Transient
    ServicioMovimiento servicioMovimiento;

    public Deuda(){
        this.servicioMovimiento = new ServicioMovimiento();
    } // Constructor sin parámetros requerido por JPA

    public Deuda(String nombrePersona, double montoTotal, LocalDate fechaPago) {
        super(nombrePersona, montoTotal, fechaPago);
    }

    @Override
    public void registrarMovimientoAsociado(Long idCartera, double monto) {
        servicioMovimiento.registrarGasto(idCartera, monto, "Abono deuda a " + getNombrePersona(), CategoriaGasto.ABONO_DEUDA);
    }

}
