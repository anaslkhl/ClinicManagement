package clinicmanagement.controller;

import clinicmanagement.model.Gender;
import clinicmanagement.model.Patient;


import clinicmanagement.service.PatientService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.UUID;


@WebServlet("/patients/*")
public class PatientServlet extends HttpServlet{


    private PatientService patientService;

    @Override
    public void init(){
        patientService = (PatientService) getServletContext().getAttribute("patientService");
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
                        "/WEB-INF/views/patient-form.jsp"
                ).forward(request, response);

            }else if ("/profile".equals(path)) {

                String idParam = request.getParameter("id");

                if (idParam == null || idParam.isBlank()) {
                    throw new IllegalArgumentException(
                            "Patient ID is required !"
                    );
                }

                UUID id = UUID.fromString(idParam);

                Patient patient = patientService.getPatientById(id);

                request.setAttribute("patient", patient);

                request.getRequestDispatcher(
                        "/WEB-INF/views/patient-profile.jsp"
                ).forward(request, response);

            }
            else {

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
                            throws ServletException, IOException
    {

        String path = request.getPathInfo();

        if("/register".equals(path)){

            try {

                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                String telephone = request.getParameter("telephone");
                String cin = request.getParameter("cin");
                String dateNaissanceParam = request.getParameter("dateNaissance");
                String adresse = request.getParameter("adresse");
                String genderParam = request.getParameter("gender");

                LocalDate dateNaissance = LocalDate.parse(dateNaissanceParam);

                Gender gender = Gender.valueOf(genderParam);

                Patient patient = new Patient(nom, prenom, email, telephone,
                        password, cin, dateNaissance, gender, adresse);

                patientService.registerPatient(patient);

                response.sendRedirect(request.getContextPath() + "/patients/register");
            } catch (Exception e) {
                request.setAttribute("error" , e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/patient-form.jsp").forward(request,response);
            }

        }
        else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}
