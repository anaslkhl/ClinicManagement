package clinicmanagement.repository;

import clinicmanagement.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {

    private final EntityManagerFactory entityManagerFactory;

    public UserRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public User save(User user) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (user.getId() == null) {
                em.persist(user);
            } else {
                user = em.merge(user);
            }

            transaction.commit();
            return user;

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findById(UUID id) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return Optional.ofNullable(
                    em.find(User.class, id)
            );
        } finally {
            em.close();
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            try {
                User user = em.createQuery(
                                "SELECT u FROM User u WHERE u.email = :email",
                                User.class
                        ).setParameter("email", email)
                        .getSingleResult();

                return Optional.of(user);

            } catch (NoResultException e) {
                return Optional.empty();
            }

        } finally {
            em.close();
        }
    }

    @Override
    public List<User> findAll() {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            return em.createQuery(
                    "SELECT u FROM User u",
                    User.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public void delete(User user) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User managedUser = em.contains(user)
                    ? user
                    : em.merge(user);

            em.remove(managedUser);

            transaction.commit();

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    @Override
        public boolean existsByEmail(String email) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {
            Long count = em.createQuery(
                            "SELECT COUNT(u) FROM User u WHERE u.email = :email",
                            Long.class
                    ).setParameter("email", email)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }
}