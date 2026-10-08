package clinicmanagement.Validation;

import clinicmanagement.model.Gender;

public class GenderValidation {

    public static void ValidateGender(Gender gender){

        if(gender == null){
            throw new IllegalArgumentException("Gender is required !");
        }
    }
}
