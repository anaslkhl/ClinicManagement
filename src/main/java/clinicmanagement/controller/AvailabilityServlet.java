package clinicmanagement.controller;

import clinicmanagement.model.Availability;
import clinicmanagement.model.AvailabilityStatus;
import clinicmanagement.model.Doctor;
import clinicmanagement.service.AvailabilityService;
import clinicmanagement.service.DoctorService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/availabilities/*")
public class AvailabilityServlet extends HttpServlet {

    private AvailabilityService availabilityService;
    private DoctorService doctorService;

    @Override
    public void init() throws ServletException {

        availabilityService = (AvailabilityService) getServletContext().getAttribute("availabilityService");

        doctorService = (DoctorService) getServletContext().getAttribute("doctorService");

        if (availabilityService == null) {
            throw new ServletException(
                    "AvailabilityService is not initialized."
            );
        }

        if (doctorService == null) {
            throw new ServletException(
                    "DoctorService is not initialized."
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null || path.equals("/")) {

            request.setAttribute(
                    "availabilities",
                    availabilityService.findAll()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/availability/availability-list.jsp"
            ).forward(request, response);

            return;
        }

        if (path.equals("/create")) {

            request.getRequestDispatcher(
                    "/WEB-INF/views/availability/availability-form.jsp"
            ).forward(request, response);

            return;
        }

        try {

            UUID id = UUID.fromString(
                    path.substring(1)
            );

            Optional<Availability> availability =
                    availabilityService.findById(id);

            if (availability.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Availability not found."
                );

                return;
            }

            request.setAttribute(
                    "availability",
                    availability.get()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/availability/availability-details.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid availability ID."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            UUID doctorId = UUID.fromString(
                    request.getParameter("doctorId")
            );

            Optional<Doctor> doctor =
                    doctorService.findById(doctorId);

            if (doctor.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Doctor not found."
                );

                return;
            }

            DayOfWeek dayOfWeek =
                    DayOfWeek.valueOf(
                            request.getParameter("dayOfWeek")
                    );

            LocalTime startTime =
                    LocalTime.parse(
                            request.getParameter("startTime")
                    );

            LocalTime endTime =
                    LocalTime.parse(
                            request.getParameter("endTime")
                    );

            AvailabilityStatus status =
                    AvailabilityStatus.valueOf(
                            request.getParameter("status")
                    );

            LocalDate dateDebut =
                    LocalDate.parse(
                            request.getParameter("dateDebut")
                    );

            LocalDate dateFin =
                    LocalDate.parse(
                            request.getParameter("dateFin")
                    );

            Availability availability =
                    new Availability(
                            doctor.get(),
                            dayOfWeek,
                            startTime,
                            endTime,
                            status,
                            dateDebut,
                            dateFin
                    );

            availabilityService.createAvailability(
                    availability
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/availabilities"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/availability/availability-form.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doDelete(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        try {

            String path = request.getPathInfo();

            if (path == null || path.equals("/")) {

                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Availability ID is required."
                );

                return;
            }

            UUID id = UUID.fromString(
                    path.substring(1)
            );

            availabilityService.deleteAvailability(id);

            response.setStatus(
                    HttpServletResponse.SC_NO_CONTENT
            );

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }
}