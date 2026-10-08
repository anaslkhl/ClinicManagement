package clinicmanagement.controller;

import clinicmanagement.model.Role;
import clinicmanagement.model.User;
import clinicmanagement.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends HttpServlet {

    private AuthService authService;

    @Override
    public void init() {
        authService = (AuthService) getServletContext()
                .getAttribute("authService");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        try {

            if ("/register".equals(path)) {

                request.getRequestDispatcher(
                        "/WEB-INF/views/user-form.jsp"
                ).forward(request, response);

            } else if ("/login".equals(path)) {

                request.getRequestDispatcher(
                        "/WEB-INF/views/login-form.jsp"
                ).forward(request, response);

            } else if ("/logout".equals(path)) {

                logout(request, response);

            } else {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        try {

            if ("/register".equals(path)) {

                register(request, response);

            } else if ("/login".equals(path)) {

                login(request, response);

            } else {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND
                );
            }

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher(
                    "/WEB-INF/views/user-form.jsp"
            ).forward(request, response);

        } catch (Exception e) {

            e.printStackTrace();

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }

    private void register(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String email = request.getParameter("email");
        String telephone = request.getParameter("telephone");
        String password = request.getParameter("password");

        User user = new User(
                nom,
                prenom,
                email,
                telephone,
                password,
                Role.PATIENT
        );

        authService.createUser(user);

        response.sendRedirect(
                request.getContextPath() + "/users/login"
        );
    }

    private void login(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Optional<User> user = authService.login(email, password);

        if (user.isEmpty()) {
            throw new IllegalArgumentException(
                    "Invalid email or password"
            );
        }

        HttpSession session = request.getSession();

        session.setAttribute("user", user.get());

        response.sendRedirect(
                request.getContextPath() + "/"
        );
    }

    private void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        response.sendRedirect(
                request.getContextPath() + "/users/login"
        );
    }
}