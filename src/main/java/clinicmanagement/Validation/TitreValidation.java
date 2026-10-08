package clinicmanagement.Validation;

public class TitreValidation {

    public static void validateTitre(String titre) {

        if (titre == null || titre.isBlank()) {
            throw new IllegalArgumentException("Title is required !");
        }
    }
}