package clinicmanagement.repository;

import clinicmanagement.model.Patient;

import java.util.List;
import java.util.Optional;
import java.util.UUID;



    public interface PatientRepository {

        Patient save(Patient patient);

        Optional<Patient> findById(UUID id);

        Optional<Patient> findByEmail(String email);

        List<Patient> findAll();

        void delete(Patient patient);

        boolean existsByEmail(String email);

        public boolean existsByCin(String cin);
    }
