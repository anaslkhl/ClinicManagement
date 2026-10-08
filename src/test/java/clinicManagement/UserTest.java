package clinicManagement;

import clinicmanagement.config.DatabaseConnection;
import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUser() {

        EntityManager em = DatabaseConnection.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            User user = new User(
                    "Ali",
                    "Amrani",
                    "usertest-" + UUID.randomUUID() + "@test.com",
                    "0612345678",
                    "password123",
                    Role.ADMIN
            );

            em.persist(user);

            transaction.commit();

            System.out.println("User created successfully!");
            System.out.println("Generated UUID: " + user.getId());

            assertNotNull(user.getId());

            cleanUp(em, user);

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }

    /**
     * The user is committed to the real database, so it is removed again to keep
     * the suite repeatable.
     */
    private void cleanUp(EntityManager em, User user) {

        EntityTransaction cleanupTransaction = em.getTransaction();

        try {
            cleanupTransaction.begin();
            em.remove(em.contains(user) ? user : em.merge(user));
            cleanupTransaction.commit();

        } catch (Exception e) {

            if (cleanupTransaction.isActive()) {
                cleanupTransaction.rollback();
            }

            throw e;
        }
    }
}