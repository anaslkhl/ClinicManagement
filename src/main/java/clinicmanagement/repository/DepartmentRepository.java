package clinicmanagement.repository;

import clinicmanagement.model.Department;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository {

    Department save(Department department);

    Optional<Department> findById(UUID id);

    Optional<Department> findByName(String name);

    List<Department> findAll();

    void update(Department department);

    void delete(UUID id);

    boolean existsByName(String name);
}