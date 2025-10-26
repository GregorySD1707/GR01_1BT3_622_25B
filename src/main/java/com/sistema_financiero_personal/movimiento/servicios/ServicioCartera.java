package com.sistema_financiero_personal.movimiento.servicios;

import com.sistema_financiero_personal.movimiento.daos.DAOCartera;
import com.sistema_financiero_personal.movimiento.modelos.Cartera;
import com.sistema_financiero_personal.usuario.modelos.Usuario;

public class ServicioCartera {
    private final DAOCartera daoCartera;

    public ServicioCartera() {
        this.daoCartera = new DAOCartera();
    }

    public ServicioCartera(DAOCartera daoCartera) {
        this.daoCartera = daoCartera;
    }

    /**
     * Obtiene el saldo de una cartera específica
     */
    public double obtenerSaldo(Long carteraId) {
        return daoCartera.obtenerSaldo(carteraId);
    }

    /**
     * Actualiza una cartera completa
     */
    public void actualizarCartera(Cartera cartera) {
        daoCartera.actualizar(cartera);
    }

    /**
     * Busca una cartera por ID
     */
    public Cartera buscarPorId(Long id) {
        return daoCartera.buscarPorId(id);
    }

    public boolean existe(Long id) {
        return daoCartera.existe(id);
    }

    public void asignarCartera(Usuario usuario) {
        Cartera nuevaCartera = new Cartera();
        usuario.setCartera(nuevaCartera);
    }

    public void recalcularSaldo(Long carteraId) {
        if (!existe(carteraId)) {
            throw new IllegalArgumentException("La cartera con ID " + carteraId + " no existe");
        }
        daoCartera.recalcularSaldoDesdeDB(carteraId);
    }
}