package dao;

import play.db.jpa.JPA;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;


/**
 * Created by stark on 03/06/2017.
 */
public abstract class BaseDAO<T, ID extends Serializable> {
    private Class<T> clazz;

    protected void setClazz(Class<T> clazzToSet) {
        this.clazz = clazzToSet;
    }

    public T findOne(ID id) {
        return JPA.em().find(clazz, id);
    }

    public List<T> findAll() {
        return JPA.em().createQuery("from " + clazz.getName())
                .getResultList();
    }


    public T update(T entity) {
        try {
            return JPA.em().merge(entity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean delete(T entity) {
        try {
            JPA.em().remove(entity);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public boolean deleteById(ID entityId) {

        T entity = findOne(entityId);
        if (entity != null) {
            delete(entity);
            return true;
        } else {
            return false;
        }
    }

    public T save(T entity) {
        try {
            return JPA.em().merge(entity);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    public Set<T> get() {
        List<T> resultList = run(entityManager -> {
            final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            final CriteriaQuery<T> criteria = criteriaBuilder.createQuery(clazz);

            final Root<T> root = criteria.from(clazz);
            criteria.select(root);

            final TypedQuery<T> query = entityManager.createQuery(criteria);
            return query.getResultList();
        });

        return new HashSet<>(resultList);
    }

    public Optional<T> get(String id) {
        return Optional.ofNullable(run(entityManager -> {
            return entityManager.find(clazz, id);
        }));
    }


    public void persist(T entity) {
        runInTransaction(entityManager -> {
            entityManager.merge(entity);
        });
    }

    public boolean persist(Collection<T> entities) {

        for (T entity : entities){
            JPA.em().merge(entity);
        }

        return true;
    }

    private <R> R run(Function<EntityManager, R> function) {
        final EntityManager entityManager = JPA.em();
        try {
            return function.apply(entityManager);
        } finally {
            entityManager.close();
        }
    }

    private void run(Consumer<EntityManager> function) {
        run(entityManager -> {
            function.accept(entityManager);
            return null;
        });
    }

    private <R> R runInTransaction(Function<EntityManager, R> function) {
        return run(entityManager -> {
            entityManager.getTransaction().begin();

            final R result = function.apply(entityManager);

            entityManager.getTransaction().commit();

            return result;
        });
    }

    private void runInTransaction(Consumer<EntityManager> function) {
        runInTransaction(entityManager -> {
            function.accept(entityManager);
            return null;
        });
    }



}
