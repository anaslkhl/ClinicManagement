package clinicmanagement.repository;

import clinicmanagement.model.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final EntityManagerFactory entityManagerFactory;

    public DepartmentRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Department save(Department department) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            em.persist(department);

            transaction.commit();

            return department;

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
    public Optional<Department> findById(UUID id) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            Department department = em.find(Department.class, id);

            return Optional.ofNullable(department);

        } finally {
            em.close();
        }
    }

    @Override
    public Optional<Department> findByName(String name) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            Department department = em.createQuery(
                            "SELECT d FROM Department d WHERE d.name = :name",
                            Department.class
                    )
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst();

            return Optional.ofNullable(department);

        } finally {
            em.close();
        }
    }

    @Override
    public List<Department> findAll() {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            return em.createQuery(
                    "SELECT d FROM Department d ORDER BY d.name",
                    Department.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    @Override
    public void update(Department department) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            em.merge(department);

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
    public void delete(UUID id) {

        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {

            transaction.begin();

            Department department = em.find(Department.class, id);

            if (department == null) {
                throw new IllegalArgumentException(
                        "Department not found with id: " + id
                );
            }

            em.remove(department);

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
    public boolean existsByName(String name) {

        EntityManager em = entityManagerFactory.createEntityManager();

        try {

            Long count = em.createQuery(
                            "SELECT COUNT(d) FROM Department d WHERE d.name = :name",
                            Long.class
                    )
                    .setParameter("name", name)
                    .getSingleResult();

            return count > 0;

        } finally {
            em.close();
        }
    }
}