package clinicmanagement.repository;

import clinicmanagement.model.Availability;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AvailabilityRepository {

    Availability save(Availability availability);

    Optional<Availability> findById(UUID id);

    List<Availability> findAll();

    List<Availability> findByDoctorId(UUID doctorId);

    Availability update(Availability availability);

    void delete(UUID id);

    boolean existsById(UUID id);
}