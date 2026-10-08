package clinicmanagement.Validation;


public class NameValidation {

    public static void validateName(String name){

        if(name == null || name.isEmpty()){
            throw new IllegalArgumentException("Name is required !! ");
        }
    }
}
