package com.bobocode.dao;

import com.bobocode.exception.CompanyDaoException;
import com.bobocode.model.Company;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

public class CompanyDaoImpl implements CompanyDao {

    private EntityManagerFactory entityManagerFactory;

    public CompanyDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Company findByIdFetchProducts(Long id) {
        Objects.requireNonNull(id);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("select c from Company c join fetch c.products where c.id = :id", Company.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new CompanyDaoException("error, rollback!", e);
        } finally {
            entityManager.close();
        }
    }
}

//    @Override
//    public Company findByIdFetchProducts(Long id) {
//        return readWithinTx(entityManager ->
//                entityManager
//                        .createQuery("select c from Company c join fetch c.products where c.id = :id", Company.class)
//                        .setParameter("id", id)
//                        .getSingleResult()
//        );
//    }
//
//    public <T> T readWithinTransaction(Function<EntityManager, T> readFunction) {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.unwrap(Session.class).setDefaultReadOnly(true);
//        entityManager.getTransaction().begin();
//        try {
//            T queryResult = readFunction.apply(entityManager);
//            entityManager.getTransaction().commit();
//            return queryResult;
//        } catch (Exception e){
//            entityManager.getTransaction().rollback();
//            throw new CompanyDaoException("Unable to select records. Rolling back.", e);
//        } finally {
//            entityManager.close();
//        }
//    }
//}
