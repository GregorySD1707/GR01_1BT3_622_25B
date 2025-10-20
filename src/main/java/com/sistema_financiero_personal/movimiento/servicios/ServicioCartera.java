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

    /**
     * Verifica si una cartera existe
     */
    public boolean existe(Long id) {
        return daoCartera.existe(id);
    }

    /**
     * Ajusta el saldo de una cartera (suma o resta)
     * Usa el método directo del DAO para mejor rendimiento
     */
    public void actualizarSaldo(Long carteraId, double cambio) {
        if (!existe(carteraId)) {
            throw new IllegalArgumentException("La cartera con ID " + carteraId + " no existe");
        }
        daoCartera.actualizarSaldo(carteraId, cambio);
    }

    /**
     * Obtiene una cartera con todos sus movimientos cargados
     */
    public Cartera buscarConMovimientos(Long id) {
        return daoCartera.buscarConMovimientos(id);
    }
    public void asignarCartera(Usuario usuario) {
        Cartera nuevaCartera = new Cartera();
        usuario.setCartera(nuevaCartera);
    }
}