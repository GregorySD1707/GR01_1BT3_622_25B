package com.sistema_financiero_personal.resumen_financiero.daos;

import com.sistema_financiero_personal.comun.DAOBase;
import com.sistema_financiero_personal.resumen_financiero.modelos.ResumenFinanciero;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class DAOResumenFinanciero extends DAOBase<ResumenFinanciero> {

    public DAOResumenFinanciero(){
        super(ResumenFinanciero.class);
    }

    // Método de conveniencia requerido por el código existente (Servlet y Tests)
    public void guardar(ResumenFinanciero resumenFinanciero) {
        // delega al método genérico de creación del BaseDAO
        crear(resumenFinanciero);
    }

    public List<ResumenFinanciero> listarConDocumentosPDF(Long usuarioId){
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ResumenFinanciero> cq = cb.createQuery(ResumenFinanciero.class);
            Root<ResumenFinanciero> root = cq.from(ResumenFinanciero.class);

            root.fetch("documentoPDF", JoinType.INNER);

            cq.select(root).where(cb.equal(root.get("usuario").get("id"), usuarioId));

            cq.orderBy(cb.desc(root.get("fechaCreacion")));
            return  session.createQuery(cq).getResultList();
        });
    }

    public ResumenFinanciero buscarPorIdYUsuario(Long resumenId, Long usuarioId) {
        return executeQuery(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<ResumenFinanciero> cq = cb.createQuery(ResumenFinanciero.class);
            Root<ResumenFinanciero> root = cq.from(ResumenFinanciero.class);

            // Fetch del documento PDF para evitar lazy loading
            root.fetch("documentoPDF", JoinType.LEFT);

            // Filtrar por ID del resumen Y por usuario
            cq.select(root).where(
                    cb.and(
                            cb.equal(root.get("id"), resumenId),
                            cb.equal(root.get("usuario").get("id"), usuarioId)
                    )
            );

            List<ResumenFinanciero> resultados = session.createQuery(cq).getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        });
    }
}
