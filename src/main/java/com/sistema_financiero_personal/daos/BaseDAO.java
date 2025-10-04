package com.sistema_financiero_personal.daos;

import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class BaseDAO<T> {
    private static final SessionFactory factory = new Configuration().configure().buildSessionFactory();
    private final Class<T> entityClass;

    protected BaseDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected void executeInTransaction(Consumer<Session> operation) {
        try (Session session = factory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                operation.accept(session);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    protected <R> R executeQuery(Function<Session, R> query) {
        try (Session session = factory.openSession()) {
            return query.apply(session);
        }
    }

    // Métodos CRUD
    public void crear(T entity) {
        executeInTransaction(session -> session.persist(entity));
    }

    public void borrar(Long id) {
        executeInTransaction(session -> {
            T entity = session.get(entityClass, id);
            if (entity != null) {
                session.remove(entity);
            }
        });
    }

    public void actualizar(T entity) {
        executeInTransaction(session -> session.merge(entity));
    }

    public T buscarPorId(Long id) {
        return executeQuery(session -> session.get(entityClass, id));
    }

    public static void cerrarFactory() {
        if (factory != null && !factory.isClosed()) {
            factory.close();
        }
    }

    /**
     * Buscar todas las entidades sin límite
     */
    public List<T> listar() {
        return listar(true, -1, -1);
    }

    /**
     * Buscar entidades con paginación
     * @param maxResults Número máximo de resultados
     * @param firstResult Índice del primer resultado (0-based)
     */
    public List<T> listar(int maxResults, int firstResult) {
        return listar(false, maxResults, firstResult);
    }

    /**
     * Método privado que implementa la lógica de búsqueda
     * @param all Si es true, ignora maxResults y firstResult
     * @param maxResults Límite de resultados
     * @param firstResult Offset (desde qué registro)
     */
    private List<T> listar(boolean all, int maxResults, int firstResult) {
        return executeQuery(session -> {
            // Crear CriteriaBuilder y CriteriaQuery
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(entityClass);
            Root<T> root = cq.from(entityClass);
            cq.select(root);

            // Crear Query desde CriteriaQuery
            Query query = session.createQuery(cq);

            // Si no se quieren todos los resultados, aplicar paginación
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }

            return (List<T>) query.getResultList();
        });
    };
}

