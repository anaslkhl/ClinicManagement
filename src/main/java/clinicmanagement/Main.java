package clinicmanagement;

import clinicmanagement.config.DatabaseConnection;
import jakarta.persistence.EntityManager;

public class Main {

    public static void main(String[] args) {

        EntityManager em = null;

        try {
            em = DatabaseConnection.createEntityManager();

            Object result = em.createNativeQuery("SELECT 1").getSingleResult();

            System.out.println("Database connection successful!");
            System.out.println("PostgreSQL response: " + result);

        } catch (Exception e) {
            System.err.println("Database connection failed!");
            e.printStackTrace();

        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }

            DatabaseConnection.close();
        }
    }
}