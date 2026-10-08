package clinicmanagement.Validation;

import clinicmanagement.model.Specialty;

public class SpecialtyValidation {

    public static void validateSpecialty(Specialty specialty) {

        if (specialty == null) {
            throw new IllegalArgumentException("Specialty is required !");
        }
    }
}