package clinicmanagement.Validation;

import clinicmanagement.model.Availability;
import clinicmanagement.model.Doctor;

public class AvailabilityValidation {

    public static void validate(Availability availability) {

        if (availability == null) {
            throw new IllegalArgumentException(
                    "Availability cannot be null."
            );
        }

        Doctor doctor = availability.getDoctor();

        if (doctor == null) {
            throw new IllegalArgumentException(
                    "Doctor is required."
            );
        }

        if (availability.getDayOfWeek() == null) {
            throw new IllegalArgumentException(
                    "Day of week is required."
            );
        }

        if (availability.getStartTime() == null) {
            throw new IllegalArgumentException(
                    "Start time is required."
            );
        }

        if (availability.getEndTime() == null) {
            throw new IllegalArgumentException(
                    "End time is required."
            );
        }

        if (!availability.getStartTime()
                .isBefore(availability.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time."
            );
        }

        if (availability.getStatus() == null) {
            throw new IllegalArgumentException(
                    "Availability status is required."
            );
        }

        if (availability.getDateDebut() == null) {
            throw new IllegalArgumentException(
                    "Start date is required."
            );
        }

        if (availability.getDateFin() == null) {
            throw new IllegalArgumentException(
                    "End date is required."
            );
        }

        if (availability.getDateDebut()
                .isAfter(availability.getDateFin())) {

            throw new IllegalArgumentException(
                    "Start date must be before or equal to end date."
            );
        }
    }
}