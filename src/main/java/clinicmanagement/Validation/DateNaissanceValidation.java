package clinicmanagement.Validation;

import java.time.LocalDate;

public class DateNaissanceValidation {


    public static void ValidateDateNaissance(LocalDate dateNaissance){

        if (dateNaissance == null) {
            throw new IllegalArgumentException("Date de naissance is required !");
        }

        if (dateNaissance.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date de naissance cannot be in the future !"
            );
        }
    }
}
