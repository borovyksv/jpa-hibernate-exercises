package com.bobocode.dao;

import com.bobocode.model.Photo;
import com.bobocode.model.PhotoComment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Please note that you should not use auto-commit mode for your implementation.
 */
public class PhotoDaoImpl implements PhotoDao {
    private EntityManagerFactory entityManagerFactory;

    public PhotoDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void save(Photo photo) {
        transaction(em -> em.persist(photo));
    }

    @Override
    public Photo findById(long id) {
        return transactionReturning(em -> em.find(Photo.class, id));
    }

    @Override
    public List<Photo> findAll() {
        return transactionReturning(em -> em.createQuery("from Photo", Photo.class).getResultList());
    }

    @Override
    public void remove(Photo photo) {
        transaction(em -> {
            Photo merge = em.merge(photo);
            em.remove(merge);
        });
    }

    @Override
    public void addComment(long photoId, String comment) {
        transaction(em -> {
            Photo photoReference = em.getReference(Photo.class, photoId);
            PhotoComment photoComment = new PhotoComment(comment, photoReference);
            em.persist(photoComment);
        });
    }

    @SuppressWarnings("all")
    private void transaction(Consumer<EntityManager> entityConsumer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            entityConsumer.accept(entityManager);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
    @SuppressWarnings("all")
    private <T> T transactionReturning(Function<EntityManager, T> entityConsumer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        try {
            T apply = entityConsumer.apply(entityManager);
            entityManager.getTransaction().commit();
            return apply;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }
}
