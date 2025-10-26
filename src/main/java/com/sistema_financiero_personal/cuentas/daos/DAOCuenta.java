package com.sistema_financiero_personal.cuentas.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.cuentas.modelos.Cuenta;
import com.sistema_financiero_personal.cuentas.modelos.TipoCuenta;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DAOCuenta extends DAOBase<Cuenta> {

    public DAOCuenta() {super(Cuenta.class);}

    public List<Cuenta> listarPorCartera(Long idCartera) {
        return buscarPorCampo("cartera.id", idCartera);
    }
    public boolean existeCuentaPorNombreYTipo(String nombre, TipoCuenta tipo, Long idCartera) {
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Long> cq = cb.createQuery(Long.class);
            Root<Cuenta> root = cq.from(Cuenta.class);

            cq.select(cb.count(root))
                    .where(
                            cb.and(
                                    cb.equal(root.get("nombre"), nombre),
                                    cb.equal(root.get("tipo"), tipo),  // ← Pasa el enum directamente
                                    cb.equal(root.get("cartera").get("id"), idCartera)
                            )
                    );

            Long count = session.createQuery(cq).getSingleResult();
            return count > 0;
        });
    }
}
