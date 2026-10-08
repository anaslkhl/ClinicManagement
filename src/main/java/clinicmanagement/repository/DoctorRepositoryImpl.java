package clinicmanagement.repository;

import clinicmanagement.model.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DoctorRepositoryImpl implements DoctorRepository {

    private final EntityManagerFactory entityManagerFactory;

    public DoctorRepositoryImpl(
            EntityManagerFactory entityManagerFactory
    ) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Doctor save(Doctor doctor) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            if (doctor.getId() == null) {
                em.persist(doctor);
            } else {
                doctor = em.merge(doctor);
            }

            transaction.commit();

            return doctor;

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
    public Optional<Doctor> findById(UUID id) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            return Optional.ofNullable(em.find(Doctor.class, id));

        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            return em.createQuery(
                            "SELECT d FROM Doctor d WHERE d.email = :email", Doctor.class)
                    .setParameter("email", email)
                    .getResultStream()
                    .findFirst();

        } finally {
            em.close();
        }
    }

    @Override
    public Doctor update(Doctor doctor) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            em.merge(doctor);

            transaction.commit();

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
        return doctor;
    }

    @Override
    public Optional<Doctor> findByMatricule(String matricule) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            return em.createQuery(
                            "SELECT d FROM Doctor d WHERE d.matricule = :matricule", Doctor.class)
                    .setParameter("matricule", matricule)
                    .getResultStream()
                    .findFirst();

        } finally {
            em.close();
        }
    }

    @Override
    public List<Doctor> findAll() {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            return em.createQuery(
                    "SELECT d FROM Doctor d", Doctor.class).getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public void delete(UUID id) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            Doctor doctor = em.find(Doctor.class, id);

            if (doctor == null) {
                throw new IllegalArgumentException("Doctor not found with id: " + id);
            }

            em.remove(doctor);

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
    public boolean existsByMatricule(String matricule) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            Long count = em.createQuery(
                            "SELECT COUNT(d) FROM Doctor d WHERE d.matricule = :matricule", Long.class)
                    .setParameter("matricule", matricule)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }

    @Override
    public boolean existsByEmail(String email) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            Long count = em.createQuery(
                            "SELECT COUNT(d) FROM Doctor d WHERE d.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }
}