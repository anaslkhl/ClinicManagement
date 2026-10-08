package clinicmanagement.Validation;

public class MatriculeValidation {

    public static void validateMatricule(String matricule) {

        if (matricule == null || matricule.isBlank()) {
            throw new IllegalArgumentException("Matricule is required !");
        }
    }
}