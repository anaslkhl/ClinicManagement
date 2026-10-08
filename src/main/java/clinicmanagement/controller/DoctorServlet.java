package clinicmanagement.controller;

import clinicmanagement.model.Doctor;
import clinicmanagement.model.Specialty;
import clinicmanagement.service.DoctorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/doctors/*")
public class DoctorServlet extends HttpServlet {

    private DoctorService doctorService;

    @Override
    public void init() throws ServletException {

        doctorService = (DoctorService) getServletContext().getAttribute("doctorService");

        if (doctorService == null) {
            throw new ServletException("DoctorService is not initialized.");
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        switch (path) {

            case "/":
            case "/list":
                listDoctors(request, response);
                break;

            case "/register":
                showRegisterForm(request, response);
                break;

            case "/edit":
                showEditForm(request, response);
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Page not found");
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,     HttpServletResponse response
    ) throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {
            path = "/";
        }

        switch (path) {

            case "/create":
                createDoctor(request, response);
                break;

            case "/update":
                updateDoctor(request, response);
                break;

            case "/delete":
                deleteDoctor(request, response);
                break;

            default:
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Action not found"
                );
        }
    }

    private void listDoctors(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        request.setAttribute("doctors", doctorService.findAll());

        request.getRequestDispatcher("/WEB-INF/views/doctor/list.jsp").forward(request, response);
    }

    private void showRegisterForm(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        request.getRequestDispatcher("/WEB-INF/views/doctor/form.jsp").forward(request, response);
    }

    private void showEditForm(
            HttpServletRequest request, HttpServletResponse response
    ) throws ServletException, IOException {

        String id = request.getParameter("id");

        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Doctor ID is required.");
            return;
        }

        UUID doctorId;

        try {
            doctorId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid doctor ID.");
            return;
        }

        Doctor doctor = doctorService.findById(doctorId)
                .orElseThrow(() -> new ServletException("Doctor not found."));

        request.setAttribute("doctor", doctor);

        request.getRequestDispatcher("/WEB-INF/views/doctor/form.jsp").forward(request, response);
    }

    private void createDoctor(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        Doctor doctor = new Doctor();

        doctor.setNom(request.getParameter("nom"));
        doctor.setPrenom(request.getParameter("prenom"));
        doctor.setEmail(request.getParameter("email"));
        doctor.setTelephone(request.getParameter("telephone"));
        doctor.setPassword(request.getParameter("password"));

        doctor.setMatricule(request.getParameter("matricule"));
        doctor.setTitre(request.getParameter("titre"));

        doctorService.createDoctor(doctor);

        response.sendRedirect(request.getContextPath() + "/doctors/list");
    }

    private void updateDoctor(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        String id = request.getParameter("id");

        if (id == null || id.isBlank()) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST, "Doctor ID is required.");
            return;
        }

        UUID doctorId;

        try {
            doctorId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid doctor ID.");
            return;
        }

        Doctor doctor = new Doctor();

        doctor.setNom(request.getParameter("nom"));
        doctor.setPrenom(request.getParameter("prenom"));
        doctor.setEmail(request.getParameter("email"));
        doctor.setTelephone(request.getParameter("telephone"));
        doctor.setPassword(request.getParameter("password"));

        doctor.setMatricule(request.getParameter("matricule"));
        doctor.setTitre(request.getParameter("titre"));

        doctorService.updateDoctor(doctor);

        response.sendRedirect(request.getContextPath() + "/doctors/list");
    }

    private void deleteDoctor(
            HttpServletRequest request, HttpServletResponse response
    ) throws IOException {

        String id = request.getParameter("id");

        if (id == null || id.isBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Doctor ID is required.");
            return;
        }

        UUID doctorId;

        try {
            doctorId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid doctor ID.");
            return;
        }

        doctorService.deleteDoctor(doctorId);

        response.sendRedirect(request.getContextPath() + "/doctors/list");
    }
}