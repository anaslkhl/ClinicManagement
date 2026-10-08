package clinicmanagement.Validation;

public class EmailValidation {

    public static void validateEmail(String email){

        if(email == null || email.isBlank()){

            throw new IllegalArgumentException("Email is required !");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}
