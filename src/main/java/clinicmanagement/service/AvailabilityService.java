package clinicmanagement.service;

import clinicmanagement.Validation.AvailabilityValidation;
import clinicmanagement.model.Availability;
import clinicmanagement.repository.AvailabilityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;

    public AvailabilityService(AvailabilityRepository availabilityRepository) {
        this.availabilityRepository = availabilityRepository;
    }

    public Availability createAvailability(Availability availability) {

        AvailabilityValidation.validate(availability);

        checkForOverlap(availability);

        return availabilityRepository.save(availability);
    }

    public Optional<Availability> findById(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Availability ID cannot be null."
            );
        }

        return availabilityRepository.findById(id);
    }

    public List<Availability> findAll() {

        return availabilityRepository.findAll();
    }

    public List<Availability> findByDoctorId(UUID doctorId) {

        if (doctorId == null) {
            throw new IllegalArgumentException(
                    "Doctor ID cannot be null."
            );
        }

        return availabilityRepository.findByDoctorId(doctorId);
    }

    public Availability updateAvailability(Availability availability) {

        AvailabilityValidation.validate(availability);

        if (availability.getId() == null) {
            throw new IllegalArgumentException(
                    "Availability ID cannot be null."
            );
        }

        checkForOverlap(availability);

        return availabilityRepository.update(availability);
    }

    public void deleteAvailability(UUID id) {

        if (id == null) {
            throw new IllegalArgumentException(
                    "Availability ID cannot be null."
            );
        }

        if (!availabilityRepository.existsById(id)) {
            throw new IllegalArgumentException(
                    "Availability with ID " + id + " does not exist."
            );
        }

        availabilityRepository.delete(id);
    }

    private void checkForOverlap(Availability availability) {

        List<Availability> existingAvailabilities =
                availabilityRepository.findByDoctorId(
                        availability.getDoctor().getId()
                );

        for (Availability existing : existingAvailabilities) {

            if (availability.getId() != null &&
                    availability.getId().equals(existing.getId())) {
                continue;
            }

            boolean sameDay =
                    availability.getDayOfWeek()
                            .equals(existing.getDayOfWeek());

            boolean datesOverlap =
                    !availability.getDateDebut()
                            .isAfter(existing.getDateFin())
                            &&
                            !availability.getDateFin()
                                    .isBefore(existing.getDateDebut());

            boolean timesOverlap =
                    availability.getStartTime()
                            .isBefore(existing.getEndTime())
                            &&
                            availability.getEndTime()
                                    .isAfter(existing.getStartTime());

            if (sameDay && datesOverlap && timesOverlap) {

                throw new IllegalArgumentException(
                        "The availability overlaps with an existing "
                                + "availability for this doctor."
                );
            }
        }
    }
}