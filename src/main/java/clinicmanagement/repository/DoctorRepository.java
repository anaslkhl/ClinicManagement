package clinicmanagement.repository;

import clinicmanagement.model.Doctor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DoctorRepository {

    Doctor save(Doctor doctor);

    Optional<Doctor> findById(UUID id);

    Optional<Doctor> findByEmail(String email);

    Optional<Doctor> findByMatricule(String matricule);

    List<Doctor> findAll();

    void delete(UUID id);

    boolean existsByMatricule(String matricule);

    Doctor update(Doctor doctor);

    boolean existsByEmail(String email);
}