package com.bobocode.dao;

import com.bobocode.exception.CompanyDaoException;
import com.bobocode.model.Company;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class CompanyDaoImpl implements CompanyDao {
    private EntityManagerFactory entityManagerFactory;

    public CompanyDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Company findByIdFetchProducts(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.unwrap(Session.class).setDefaultReadOnly(true);
        entityManager.getTransaction().begin();
        try {
            Company company = entityManager
                    .createQuery(" select c from Company c join fetch c.products where c.id = :id", Company.class)
                    .setParameter("id", id)
                    .getSingleResult();
            entityManager.getTransaction().commit();
            return company;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw new CompanyDaoException(e.getMessage(), e.getCause());
        } finally {
            entityManager.close();
        }
    }
}
