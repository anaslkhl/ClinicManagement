package clinicmanagement.repository;

import clinicmanagement.model.Availability;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AvailabilityRepositoryImpl implements AvailabilityRepository {

    private final EntityManagerFactory entityManagerFactory;

    public AvailabilityRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Availability save(Availability availability) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            entityManager.persist(availability);

            entityManager.getTransaction().commit();

            return availability;

        } catch (RuntimeException e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Optional<Availability> findById(UUID id) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            Availability availability =
                    entityManager.find(Availability.class, id);

            return Optional.ofNullable(availability);

        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Availability> findAll() {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT a FROM Availability a",
                            Availability.class
                    )
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Availability> findByDoctorId(UUID doctorId) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            return entityManager
                    .createQuery(
                            "SELECT a FROM Availability a " +
                                    "WHERE a.doctor.id = :doctorId",
                            Availability.class
                    )
                    .setParameter("doctorId", doctorId)
                    .getResultList();

        } finally {
            entityManager.close();
        }
    }

    @Override
    public Availability update(Availability availability) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Availability updatedAvailability =
                    entityManager.merge(availability);

            entityManager.getTransaction().commit();

            return updatedAvailability;

        } catch (RuntimeException e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    @Override
    public void delete(UUID id) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            entityManager.getTransaction().begin();

            Availability availability =
                    entityManager.find(Availability.class, id);

            if (availability != null) {
                entityManager.remove(availability);
            }

            entityManager.getTransaction().commit();

        } catch (RuntimeException e) {

            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }

            throw e;

        } finally {
            entityManager.close();
        }
    }

    @Override
    public boolean existsById(UUID id) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        try {
            Long count = entityManager
                    .createQuery(
                            "SELECT COUNT(a) FROM Availability a " +
                                    "WHERE a.id = :id",
                            Long.class
                    )
                    .setParameter("id", id)
                    .getSingleResult();

            return count > 0;

        } finally {
            entityManager.close();
        }
    }
}