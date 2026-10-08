package clinicmanagement.Validation;


public class PasswordValidation {


    public static void validatePassword(String password){

        if(password == null || password.isEmpty() || password.length() < 6){

            throw new IllegalArgumentException("Password must be over 6 characters ");
        }
    }
}
