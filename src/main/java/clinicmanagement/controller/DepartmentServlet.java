package clinicmanagement.controller;

import clinicmanagement.model.Department;
import clinicmanagement.service.DepartmentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/departments/*")
public class DepartmentServlet extends HttpServlet {

    private DepartmentService departmentService;

    @Override
    public void init() throws ServletException {

        departmentService =
                (DepartmentService) getServletContext()
                        .getAttribute("departmentService");

        if (departmentService == null) {
            throw new ServletException(
                    "DepartmentService is not available in ServletContext."
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

            listDepartments(request, response);

        } else if (path.equals("/new")) {

            showCreateForm(request, response);

        } else if (path.equals("/edit")) {

            showEditForm(request, response);

        } else {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Department page not found."
            );
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();

        if (path == null) {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND
            );

        } else if (path.equals("/create")) {

            createDepartment(request, response);

        } else if (path.equals("/update")) {

            updateDepartment(request, response);

        } else if (path.equals("/delete")) {

            deleteDepartment(request, response);

        } else {

            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Department action not found."
            );
        }
    }

    private void listDepartments(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        List<Department> departments =
                departmentService.findAll();

        request.setAttribute("departments", departments);

        request.getRequestDispatcher(
                "/WEB-INF/views/department/department-list.jsp"
        ).forward(request, response);
    }

    private void showCreateForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/views/department/department-form.jsp"
        ).forward(request, response);
    }

    private void showEditForm(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String idParameter = request.getParameter("id");

        try {

            UUID id = UUID.fromString(idParameter);

            Optional<Department> department =
                    departmentService.findById(id);

            if (department.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Department not found."
                );

                return;
            }

            request.setAttribute(
                    "department",
                    department.get()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/department/department-form.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid department id."
            );
        }
    }

    private void createDepartment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String name = request.getParameter("name");

        Department department = new Department(name);

        try {

            departmentService.createDepartment(department);

            response.sendRedirect(
                    request.getContextPath()
                            + "/departments"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());
            request.setAttribute("department", department);

            request.getRequestDispatcher(
                    "/WEB-INF/views/department/department-form.jsp"
            ).forward(request, response);
        }
    }

    private void updateDepartment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException, ServletException {

        String idParameter = request.getParameter("id");
        String name = request.getParameter("name");

        try {

            UUID id = UUID.fromString(idParameter);

            Department department = new Department(name);

            // The ID is generated by JPA and normally has no setter.
            // We retrieve the existing entity first.
            Optional<Department> existingDepartment =
                    departmentService.findById(id);

            if (existingDepartment.isEmpty()) {

                response.sendError(
                        HttpServletResponse.SC_NOT_FOUND,
                        "Department not found."
                );

                return;
            }

            Department existing = existingDepartment.get();
            existing.setName(name);

            departmentService.updateDepartment(existing);

            response.sendRedirect(
                    request.getContextPath()
                            + "/departments"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher(
                    "/WEB-INF/views/department/department-form.jsp"
            ).forward(request, response);
        }
    }

    private void deleteDepartment(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        String idParameter = request.getParameter("id");

        try {

            UUID id = UUID.fromString(idParameter);

            departmentService.deleteDepartment(id);

            response.sendRedirect(request.getContextPath() + "/departments");

        } catch (IllegalArgumentException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    e.getMessage()
            );
        }
    }
}