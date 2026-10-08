package clinicmanagement.config;

import clinicmanagement.repository.UserRepository;
import clinicmanagement.repository.UserRepositoryImpl;
import clinicmanagement.service.AuthService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        EntityManagerFactory emf = DatabaseConnection.getEntityManagerFactory();

        UserRepository userRepository = new UserRepositoryImpl(emf);

        AuthService authService = new AuthService(userRepository);

        event.getServletContext().setAttribute("authService", authService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        DatabaseConnection.close();
    }
}