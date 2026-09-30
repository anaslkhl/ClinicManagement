package clinicManagement;

import clinicmanagement.config.DatabaseConnection;
import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
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
                    "ali.amrani@test.com",
                    "0612345678",
                    "password123",
                    Role.ADMIN
            );

            em.persist(user);

            transaction.commit();

            System.out.println("User created successfully!");
            System.out.println("Generated UUID: " + user.getId());

            assertNotNull(user.getId());

        } catch (Exception e) {

            if (transaction.isActive()) {
                transaction.rollback();
            }

            throw e;

        } finally {
            em.close();
        }
    }
}