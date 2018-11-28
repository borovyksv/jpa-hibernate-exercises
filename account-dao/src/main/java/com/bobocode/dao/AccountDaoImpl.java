package com.bobocode.dao;

import com.bobocode.exception.AccountDaoException;
import com.bobocode.model.Account;
import com.bobocode.util.EntityManagerUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AccountDaoImpl implements AccountDao {
    private final EntityManagerUtil entityManagerUtil;

    public AccountDaoImpl(EntityManagerFactory emf) {
        entityManagerUtil = new EntityManagerUtil(emf);
    }

    @Override
    public void save(Account account) {
        transaction(em -> em.persist(account));
    }

    @Override
    public Account findById(Long id) {
        return transactionReturning(em -> em.find(Account.class, id));
    }

    @Override
    public Account findByEmail(String email) {
        return transactionReturning(em ->
                em.createQuery("select a from Account a where a.email = :email", Account.class)
                        .setParameter("email", email)
                        .getSingleResult());
    }

    @Override
    public List<Account> findAll() {
        return transactionReturning(em ->
                em.createQuery("from Account", Account.class)
                        .getResultList());
    }

    @Override
    public void update(Account account) {
        transaction(em -> em.merge(account));
    }

    @Override
    public void remove(Account account) {
        transaction(em -> {
            Account merged = em.merge(account);
            em.remove(merged);
        });
    }

    private void transaction(Consumer<EntityManager> entityManagerConsumer) {
        try {
            entityManagerUtil.performWithinTx(entityManagerConsumer);
        } catch (Exception e) {
            throw new AccountDaoException(e.getMessage(), e.getCause());
        }
    }

    private <T> T transactionReturning(Function<EntityManager, T> entityManagerFunction) {
        try {
            return entityManagerUtil.performReturningWithinTx(entityManagerFunction);
        } catch (Exception e) {
            throw new AccountDaoException(e.getMessage(), e.getCause());
        }
    }
}

