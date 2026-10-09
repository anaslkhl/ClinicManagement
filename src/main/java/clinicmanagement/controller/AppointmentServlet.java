
package clinicmanagement.controller;

import clinicmanagement.model.Appointment;
import clinicmanagement.model.AppointmentStatus;
import clinicmanagement.service.AppointmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@WebServlet("/appointments/*")
public class AppointmentServlet extends HttpServlet {

    private AppointmentService appointmentService;

    @Override
    public void init() throws ServletException {

        appointmentService = (AppointmentService)
                getServletContext().getAttribute("appointmentService");

        if (appointmentService == null) {
            throw new ServletException(
                    "AppointmentService is not configured in ServletContext."
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        try {
            if (path == null || path.equals("/") || path.equals("/list")) {

                request.setAttribute(
                        "appointments",
                        appointmentService.getAllAppointments()
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/appointment-list.jsp"
                ).forward(request, response);

            } else if (path.equals("/create")) {

                request.getRequestDispatcher(
                        "/WEB-INF/views/appointment-form.jsp"
                ).forward(request, response);

            } else if (path.equals("/edit")) {

                UUID id = UUID.fromString(request.getParameter("id"));

                Appointment appointment =
                        appointmentService.getAppointmentById(id);

                request.setAttribute("appointment", appointment);

                request.getRequestDispatcher(
                        "/WEB-INF/views/appointment-form.jsp"
                ).forward(request, response);

            } else {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Appointment page not found."
                );
            }

        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String path = request.getPathInfo();

        try {
            if ("/create".equals(path)) {

                UUID patientId = UUID.fromString(
                        request.getParameter("patientId")
                );

                UUID doctorId = UUID.fromString(
                        request.getParameter("doctorId")
                );

                LocalDateTime dateHeure = LocalDateTime.parse(
                        request.getParameter("dateHeure")
                );

                String motif = request.getParameter("motif");

                AppointmentStatus status = AppointmentStatus.valueOf(
                        request.getParameter("status")
                );

                appointmentService.createAppointment(
                        patientId,
                        doctorId,
                        dateHeure,
                        motif,
                        status
                );

                response.sendRedirect(
                        request.getContextPath() + "/appointments/list"
                );

            } else if ("/update".equals(path)) {

                UUID id = UUID.fromString(
                        request.getParameter("id")
                );

                UUID patientId = UUID.fromString(
                        request.getParameter("patientId")
                );

                UUID doctorId = UUID.fromString(
                        request.getParameter("doctorId")
                );

                LocalDateTime dateHeure = LocalDateTime.parse(
                        request.getParameter("dateHeure")
                );

                String motif = request.getParameter("motif");

                AppointmentStatus status = AppointmentStatus.valueOf(
                        request.getParameter("status")
                );

                appointmentService.updateAppointment(
                        id,
                        patientId,
                        doctorId,
                        dateHeure,
                        motif,
                        status
                );

                response.sendRedirect(
                        request.getContextPath() + "/appointments/list"
                );

            } else if ("/delete".equals(path)) {

                UUID id = UUID.fromString(
                        request.getParameter("id")
                );

                appointmentService.deleteAppointment(id);

                response.sendRedirect(
                        request.getContextPath() + "/appointments/list"
                );

            } else {
                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Appointment action not found."
                );
            }

        } catch (IllegalArgumentException e) {
            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }
}