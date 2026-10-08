package clinicmanagement.service;

import clinicmanagement.model.Department;
import clinicmanagement.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department) {

        if (department == null) {
            throw new IllegalArgumentException("Department is required.");
        }

        validateName(department.getName());

        String name = department.getName().trim();

        if (departmentRepository.existsByName(name)) {
            throw new IllegalArgumentException(
                    "A department with this name already exists."
            );
        }

        department.setName(name);

        return departmentRepository.save(department);
    }

    public Optional<Department> findById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("Department id is required.");
        }

        return departmentRepository.findById(id);
    }

    public List<Department> findAll() {

        return departmentRepository.findAll();
    }

    public void updateDepartment(Department department) {

        if (department == null) {
            throw new IllegalArgumentException("Department is required.");
        }

        if (department.getId() == null) {
            throw new IllegalArgumentException(
                    "Department id is required."
            );
        }

        validateName(department.getName());

        String name = department.getName().trim();

        Optional<Department> existingDepartment =
                departmentRepository.findByName(name);

        if (existingDepartment.isPresent()
                && !existingDepartment.get().getId().equals(department.getId())) {

            throw new IllegalArgumentException(
                    "A department with this name already exists."
            );
        }

        department.setName(name);

        departmentRepository.update(department);
    }

    public void deleteDepartment(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException("Department id is required.");
        }

        departmentRepository.delete(id);
    }

    private void validateName(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Department name is required."
            );
        }

        if (name.trim().length() < 2) {
            throw new IllegalArgumentException(
                    "Department name must contain at least 2 characters."
            );
        }
    }
}