package com.bobocode.dao;

import com.bobocode.model.Company;

import javax.persistence.EntityManagerFactory;

public class CompanyDaoImpl implements CompanyDao {
    private EntityManagerFactory entityManagerFactory;

    public CompanyDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Company findByIdFetchProducts(Long id) {
        return null;
    }
}

//    @Override
//    public Company findByIdFetchProducts(Long id) {
//        return readWithinTransaction(entityManager -> entityManager
//                .createQuery("select c from Company c join fetch c.products where c.id = :id", Company.class)
//                .setParameter("id", id)
//                .getSingleResult());
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
